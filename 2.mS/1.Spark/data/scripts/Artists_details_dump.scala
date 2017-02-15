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

val df_users_oldDB = sqlContext.read.format("jdbc").option("url", urlSource).option("driver", driver).option("dbtable", "users").option("user", userSrcDB).option("password", passSrcDB).option("verifyServerCertificate", "false").load()
//df_users_oldDB.write.mode("overwrite").parquet("oldDB_users.parquet")

val df_artists_oldDB = sqlContext.read.format("jdbc").option("url", urlSource).option("driver", driver).option("dbtable", "artists").option("user", userSrcDB).option("password", passSrcDB).option("verifyServerCertificate", "false").load()
//df_artists_oldDB.write.mode("overwrite").parquet("oldDB_artists.parquet")

def importPhotos = {
  val df_photos_oldDB = sqlContext.read.format("jdbc").option("url", urlSource).option("driver", driver).option("dbtable", "photos").option("user", userSrcDB).option("password", passSrcDB).option("verifyServerCertificate", "false").load()
  //df_photos_oldDB.write.mode("overwrite").parquet("oldDB_photos.parquet")
  //val df_join_photo_users = df_photos_oldDB.as("photos").join(df_users_oldDB.as("users"),$"photos.user_id"===$"users.id")
  //val df_photos_newDB = df_photos_oldDB.select("id","filename","filename_thumb","file_type",$"photos.user_id" as "number",$"photos.date_created" as "createdDate")
  df_photos_oldDB.write.jdbc(urlDest,"PHOTOS",prop)

}


// Build artists dataframe
//val df_artists_oldDB = sqlContext.read.parquet("oldDB_artists.parquet")
//val df_users_oldDB = sqlContext.read.parquet("oldDB_users.parquet")

def importArtists = {
  val df_artists_newDB = df_artists_oldDB.as("ao").join(df_users_oldDB.as("uo"),$"ao.user_id"===$"uo.id")
  val orig_artists_newDB = df_artists_newDB.select($"ao.user_id" as "number", $"ao.group_name" as "bandName",$"uo.email",$"ao.genre_id" as "genre",$"ao.status_id" as "active",$"ao.date_created" as "createdDate")//.save("artists_new","parquet")
  val conv_artists_newDB = convertColumn(orig_artists_newDB,"number","String").withColumn("id", lit(0))
  val artists_newDB = conv_artists_newDB.select($"id",$"number",$"bandName",$"email",$"genre",$"active",$"createdDate")
  artists_newDB.write.mode("overwrite").parquet("newDB_artists.parquet")
  artists_newDB.write.mode("append").jdbc(urlDest,"ARTISTS",prop)
}

def importLoginInfo = {
  val df_logininfo_newDB = df_users_oldDB.select($"email" as "providerKey").withColumn("id", lit(0)).withColumn("providerId", lit("credentials"))
  val logininfo_newDB = df_logininfo_newDB.select($"id",$"providerId",$"providerKey")
  logininfo_newDB.write.mode("overwrite").parquet("newDB_LOGIN_INFO.parquet")
  logininfo_newDB.write.mode("append").jdbc(urlDest,"LOGIN_INFO",prop)
}

def importPasswordInfo = {
  val df_passwordinfo_newDB = df_users_oldDB.select($"passwd" as "password").withColumn("hasher", lit("bcrypt")).withColumn("salt", lit("")).withColumn("loginInfoId", lit(0))

  //The below script calls the currentDB/login_info table to get the ID for the foreign Key reference
  val df_logininfo_newDB = sqlContext.read.jdbc(urlDest,"LOGIN_INFO",prop)
  val df_users_logininfo = df_users_oldDB.as("users").join(df_logininfo_newDB.as("logininfo"),$"users.email"===$"logininfo.providerKey")
  val df_orig_passwordinfo_newDB = df_users_logininfo.select($"users.passwd" as "password",$"logininfo.id" as "loginInfoId").withColumn("hasher", lit("bcrypt")).withColumn("salt", lit(""))
  val df_passwordinfo_newDB = df_orig_passwordinfo_newDB.select("hasher","password","salt","loginInfoId");
  df_passwordinfo_newDB.write.mode("overwrite").parquet("newDB_PASSWORD_INFO.parquet")
  df_passwordinfo_newDB.write.mode("append").jdbc(urlDest,"PASSWORD_INFO",prop)
}


def importArtistLoginInfo = {
  val df_artists_newDB = sqlContext.read.jdbc(urlDest,"ARTISTS",prop)
  val df_join_artist_logininfo = df_artists_newDB.as("artists").join(df_logininfo_newDB.as("logininfo"),$"artists.email"===$"logininfo.providerKey")
  val df_artists_logininfo_table = df_join_artist_logininfo.select($"artists.number",$"logininfo.id" as "loginInfoId")
  df_artists_logininfo_table.write.mode("overwrite").parquet("newDB_ARTISTS_LOGININFO.parquet")
  df_artists_logininfo_table.write.mode("append").jdbc(urlDest,"ARTIST_LOGIN_INFO",prop)
}

def importProfiles = {
  // Build profiles dataframe
  val df_profiles_newDB = df_artists_oldDB.as("ao").join(df_users_oldDB.as("uo"),$"ao.user_id"===$"uo.id")
  val orig_profiles_newDB = df_profiles_newDB.select($"ao.user_id" as "artistNumber", $"uo.name" as "name",$"uo.gender",$"uo.country_id" as "cid",$"uo.state_id" as "sid",$"uo.city",$"uo.phone" as "mobno")//.save("artists_new","parquet")
  val conv_profiles_newDB = convertColumn(orig_profiles_newDB,"artistNumber","String")
  val add_email_profiles_newDB = conv_profiles_newDB.withColumn("email", lit(""))
  val add_pass_profiles_newDB = ((add_email_profiles_newDB.withColumn("password", lit(""))).withColumn("id", lit(0))).withColumn("age", lit(""))
  val profiles_newDB = add_pass_profiles_newDB.select($"id",$"name",$"gender",$"age",$"cid",$"sid",$"city",$"email",$"password",$"mobno",$"artistNumber")
  profiles_newDB.write.mode("append").jdbc(urlDest,"PROFILES",prop)
  // End build profiles
}

def importBiographies = {
  // Build biographies dataframe
  val df_biographies_newDB = df_artists_oldDB.as("ao").join(df_users_oldDB.as("uo"),$"ao.user_id"===$"uo.id")
  val orig_biographies_newDB = df_biographies_newDB.select($"ao.user_id" as "artistNumber", $"ao.record_label" as "recordLabelName",$"ao.genre_id" as "genre",$"ao.biography" as "description")
  val conv_biographies_newDB = convertColumn(orig_biographies_newDB,"artistNumber","String")
  val add_status_biographies_newDB = (conv_biographies_newDB.withColumn("status", lit("Signed"))).withColumn("id", lit(0))
  val biographies_newDB = add_status_biographies_newDB.select($"id",$"status",$"recordLabelName",$"genre",$"description",$"artistNumber")
  biographies_newDB.write.mode("append").jdbc(urlDest,"BIOGRAPHIES",prop)
  // End build biographies
}

importArtists
importPhotos
importLoginInfo
importPasswordInfo
importArtistLoginInfo
importProfiles
importBiographies

System.exit(0)