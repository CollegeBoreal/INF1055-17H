
/**
  * This file contains scripts that initiates required data for the website
  * Because of the Spark resource, there are an intermediate step that records data to parquet format
  */

import org.apache.spark.sql.SQLContext

val sqlContext = new SQLContext(sc)
val driver = "com.mysql.jdbc.Driver"

// OldDB
val userSrcDB = "etudiants"
val passSrcDB = "etudiants_1"
val urlSource = "jdbc:mysql://olddb:3306/sakila?useSSL=false"

// Prepare destination parameters
val userDestDB = "etudiants"
val passDestDB = "etudiants_1"
val nameDestDB = "sakila"
val prop = new java.util.Properties
prop.setProperty("user", userDestDB)
prop.setProperty("password", passDestDB)
val urlDest = s"jdbc:mysql://db:3306/$nameDestDB?useSSL=false"

// Importing countries

val df_country_oldDB = sqlContext.read.format("jdbc").option("url", urlSource).option("driver", driver).option("dbtable", "countries").option("user", userSrcDB).option("password", passSrcDB).option("verifyServerCertificate", "false").load()
val df_country_newDB = df_country_oldDB.select($"country_id", $"country", $"last_update")
df_country_newDB.write.mode("append").jdbc(urlDest,"COUNTRIES",prop) // Overwrite existing countries


System.exit(0)
