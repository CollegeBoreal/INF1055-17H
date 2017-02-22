
### la commande pour faire marcher le script 

```
spark-shell --jars /usr/local/spark/lib/mysql-connector-java-5.1.38.jar -i /data/scripts/Initial_data_dump_gary.scala
```


```
docker exec -it 1spark_spark_1 bash
```

docker exec -it 1spark_olddb_1 \
 mysql -u root -p -e "GRANT ALL PRIVILEGES on *.* TO 'etudiants'@'%' 
 IDENTIFIED BY 'etudiants_1' WITH GRANT OPTION;"
