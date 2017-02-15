#!/bin/sh
  exec scala "$0" "$@"
!#

import org.apache.spark.sql.SQLContext

val sqlContext = new SQLContext(sc)
val driver = "com.mysql.jdbc.Driver"

def convertColumn(df: org.apache.spark.sql.DataFrame, name:String, newType:String) = {
  val df_1 = df.withColumnRenamed(name, "swap")
  df_1.withColumn(name, df_1.col("swap").cast(newType)).drop("swap")
}

// OldDB
val userSrcDB = "indie_user"
val passSrcDB = "kOdp58vjER"
val urlSource = "jdbc:mysql://olddb:3306/theindie?useSSL=false"

// Prepare destination parameters
val userDestDB = "artists"
val passDestDB = "artists_1"
val prop = new java.util.Properties
prop.setProperty("user", userDestDB)
prop.setProperty("password", passDestDB)
val urlDest = s"jdbc:mysql://db:3306/artists?useSSL=false"

// Build Songs dataframe
val df_artists_newDB = sqlContext.read.format("jdbc").option("url", urlDest).option("driver", driver).option("dbtable", "ARTISTS").option("user", userDestDB).option("password", passDestDB).option("verifyServerCertificate", "false").load()
val df_songs_oldDB = sqlContext.read.format("jdbc").option("url", urlSource).option("driver", driver).option("dbtable", "songs").option("user", userSrcDB).option("password", passSrcDB).option("verifyServerCertificate", "false").load()
df_songs_oldDB.registerTempTable("songs")
val songsNotLengthNull = sqlContext.sql("SELECT * FROM songs WHERE song_length is not null")

val df_newartists_songs = df_artists_newDB.as("artists").join(songsNotLengthNull.as("songs"),$"songs.user_id"===$"artists.number")
//df_songs_oldDB.write.mode("overwrite").parquet("data/temp/old_songs")
val orig_songs_newDB = df_newartists_songs.select($"songs.id" as "id", $"user_id" as "artistNumber", $"song_name" as "songName",$"original_file_name" as "fileName",$"song_size" as "sizeMgbs",$"bit_rate" as "kbps",$"song_length" as "length",$"genre_id" as "genreID",$"status_id" as "isActive")
val conv_songs_newDB = convertColumn(orig_songs_newDB,"artistNumber","String")
val add_status_songs_newDB = conv_songs_newDB.withColumn("inserted", lit("1"))
val songs_newDB = add_status_songs_newDB.select($"id", $"songName", $"fileName", $"sizeMgbs", $"length", $"kbps", $"artistNumber", $"genreID", $"isActive", $"inserted")
songs_newDB.write.mode("append").jdbc(urlDest,"SONGS",prop)

val df_songs_newDB = sqlContext.read.format("jdbc").option("url", urlDest).option("driver", driver).option("dbtable", "SONGS").option("user", userDestDB).option("password", passDestDB).option("verifyServerCertificate", "false").load()
val df_songs_oldsong = df_songs_newDB.as("songs").join(songsNotLengthNull.as("oldsongs"),$"songs.fileName"===$"oldsongs.original_file_name")
val df_song_paths_messed_newDB = df_songs_oldsong.select($"songs.id" as "songID", $"oldsongs.song_file" as "bitrate128").withColumn("id", lit(0))
val df_song_paths_newDB = df_song_paths_messed_newDB.select($"id", $"songID", $"bitrate128")
df_song_paths_newDB.write.mode("append").jdbc(urlDest,"SONG_FILES",prop)

// End Songs dataframe

