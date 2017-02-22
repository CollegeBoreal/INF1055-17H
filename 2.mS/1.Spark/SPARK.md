# Spark

## Launch the Spark Docker Container

### Get onto the container
```
$ docker exec -it 1spark_spark_1 bash
```

### Run the spark shell with MySQL    
( --master yarn-client  --driver-memory 1g --executor-memory 1g )

```
$ spark-shell --jars /usr/local/spark/lib/mysql-connector-java-5.1.38.jar
```

### execute the the following command which should return 1000
```
scala> sc.parallelize(1 to 1000).count()
```
### Run Spark script from scala file as followed:
```
$ spark-shell --jars /usr/local/spark/lib/mysql-connector-java-5.1.38.jar -i /data/scripts/Countries_dump.scala
```
## Details about the Spark scripts for importation

### Good to know
To minimize duplication data or constraint issues, it could be good to empty by truncating the followed tables: COUNTRIES, GENRES, ARTISTS, LOGIN_INFO, ARTIST_LOGIN_INFO, PASSWORD_INFO, SONGS

### Files
Files are placed in data/scripts directory. 
The directory contains more files used to compose the importation but finally, scripts was combined into 3 files which are:

Initial_data_dump.scala <BR>
Artists_details_dump.scala <BR>
Songs_dump.scala <BR>

The order should be followed as it is listed above because of foreign keys constraints. 
