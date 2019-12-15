# Final Project for CSCI 6421 GWU

A Java implementation of a distributed crawler

## How to start the cluster

### Build
```
$ ./gradlew clean bootJar
```

### To start a master node
```
$ java -jar ./crawler-master/build/libs/crawler-master-1.0.0.RELEASE.jar \
      --spring.profiles.active=default \ 
      --crawler.rabbit-uri="" \
      --crawler.rabbit-queue-name="" \
      --crawler.mongo-db-uri="" \
      --crawler.mongo-db-collection-name="" \
      --crawler.master-address="" \
      --crawler.master-port="" \
```

### To start a slave node
```
$ java -jar ./crawler-slave/build/libs/crawler-slave-1.0.0.RELEASE.jar \
      --spring.profiles.active=default \
      --crawler.rabbit-uri="" \
      --crawler.rabbit-queue-name="" \
      --crawler.mongo-db-uri="" \
      --crawler.mongo-db-collection-name="" \
      --crawler.master-address="" \
      --crawler.master-port="" \
      --crawler.slave-name="" \
      --crawler.slave-address="" \
      --crawler.slave-port="" \
```

## Example of a cluster of 1 master node and 3 slave nodes
### Node: master node on 127.0.0.1:8080
```
$ java -jar ./crawler-master/build/libs/crawler-master-1.0.0.RELEASE.jar \
      --spring.profiles.active=default \ 
      --crawler.rabbit-uri="amqp://tianyuge:tianyuge@localhost:5672" \
      --crawler.rabbit-queue-name="crawler" \
      --crawler.mongo-db-uri="mongodb://tianyuge:tianyuge@localhost:27017/admin" \
      --crawler.mongo-db-collection-name="crawler" \
      --crawler.master-address="localhost" \
      --crawler.master-port="8080" \
```

### Node: john on 127.0.0.1:27651 as a slave node
```
$ java -jar ./crawler-slave/build/libs/crawler-slave-1.0.0.RELEASE.jar \
      --spring.profiles.active=default \
      --crawler.rabbit-uri="amqp://tianyuge:tianyuge@localhost:5672" \
      --crawler.rabbit-queue-name="crawler" \
      --crawler.mongo-db-uri="mongodb://tianyuge:tianyuge@localhost:27017/admin" \
      --crawler.mongo-db-collection-name="crawler" \
      --crawler.master-address="localhost" \
      --crawler.master-port="8080" \
      --crawler.slave-name="john" \
      --crawler.slave-address="localhost" \
      --crawler.slave-port="27651" \
```

### Node: jane on 127.0.0.1:57812 as a slave node
```
$ java -jar ./crawler-slave/build/libs/crawler-slave-1.0.0.RELEASE.jar \
      --spring.profiles.active=default \
      --crawler.rabbit-uri="amqp://tianyuge:tianyuge@localhost:5672" \
      --crawler.rabbit-queue-name="crawler" \
      --crawler.mongo-db-uri="mongodb://tianyuge:tianyuge@localhost:27017/admin" \
      --crawler.mongo-db-collection-name="crawler" \
      --crawler.master-address="localhost" \
      --crawler.master-port="8080" \
      --crawler.slave-name="jane" \
      --crawler.slave-address="localhost" \
      --crawler.slave-port="57812" \
```

### Node: alex on 127.0.0.1:37771 as a normal node
```
$ java -jar ./crawler-slave/build/libs/crawler-slave-1.0.0.RELEASE.jar \
      --spring.profiles.active=default \
      --crawler.rabbit-uri="amqp://tianyuge:tianyuge@localhost:5672" \
      --crawler.rabbit-queue-name="crawler" \
      --crawler.mongo-db-uri="mongodb://tianyuge:tianyuge@localhost:27017/admin" \
      --crawler.mongo-db-collection-name="crawler" \
      --crawler.master-address="localhost" \
      --crawler.master-port="8080" \
      --crawler.slave-name="alex" \
      --crawler.slave-address="localhost" \
      --crawler.slave-port="37771" \
```
