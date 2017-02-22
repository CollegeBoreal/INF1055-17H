# Spark

## Launch the Spark Docker Container

### Get onto the container
```
$ docker exec -it 1spark_spark_1 bash
```

### Run the spark shell with MySQL    
`opts: ( --master yarn-client  --driver-memory 1g --executor-memory 1g )`

```
$ spark-shell --jars /usr/local/spark/lib/mysql-connector-java-5.1.38.jar
```

### execute the the following command which should return 1000
```
scala> sc.parallelize(1 to 1000).count()
```
### Run Spark script from scala file as followed:
```
$ spark-shell --jars /usr/local/spark/lib/mysql-connector-java-5.1.38.jar -i /data/scripts/Initial_data_dump.scala
```
