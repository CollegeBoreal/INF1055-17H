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

// Importing citys

val df_citys_oldDB = sqlContext.read.format("jdbc").option("url", urlSource).option("driver", driver).option("dbtable", "city").option("user", userSrcDB).option("password", passSrcDB).option("verifyServerCertificate", "false").load()
val df_citys_newDB = df_citys_oldDB.select($"city" as "City")


df_citys_newDB.write.mode("append").jdbc(urlDest,"city",prop) // Overwrite existing citys



System.exit(0)
