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
      --crawler.rabbit-uri="amqp://tianyuge:tianyuge@localhost:5672" \
      --crawler.rabbit-queue-name="crawler" \
      --crawler.mongo-db-uri="mongodb://tianyuge:tianyuge@localhost:27017/admin" \
      --crawler.mongo-db-collection-name="crawler" \
      --crawler.master-address="localhost" \
      --crawler.master-port="8080" \
```

### To start a slave node
```
$ java -jar ./crawler-slave/build/libs/crawler-slave-1.0.0.RELEASE.jar \
      --spring.profiles.active=default \
      --crawler.rabbit-uri="amqp://tianyuge:tianyuge@localhost:5672"
      --crawler.rabbit-queue-name="crawler"
      --crawler.mongo-db-uri="mongodb://tianyuge:tianyuge@localhost:27017/admin"
      --crawler.mongo-db-collection-name="crawler"
      --crawler.master-address="localhost"
      --crawler.master-port="8080"
      --crawler.slave-name="john"
      --crawler.slave-address="localhost"
      --crawler.slave-port="27651"
```

## Example of a Chord network of size 128 and containing 4 nodes
### Node: John on 127.0.0.1:18001 as a bootstrapping node
```
$ java -jar ./chord-node/build/libs/chord-node-1.0.0.RELEASE.jar \
      --spring.profiles.active=default \
      --chord.node-name="john" \
      --chord.node-address="127.0.0.1" \
      --chord.node-port="18001" \
      --chord.finger-ring-size-bits="7" \
      --chord.bootstrapping-node="true"
```

### Node: Austin on 127.0.0.1:18652 as a normal node
```
$ java -jar ./chord-node/build/libs/chord-node-1.0.0.RELEASE.jar \
      --spring.profiles.active=default \
      --chord.node-name="austin" \
      --chord.node-address="127.0.0.1" \
      --chord.node-port="18652" \
      --chord.finger-ring-size-bits="7" \
      --chord.bootstrapping-node="false" \
      --chord.joining-to-address="127.0.0.1" \
      --chord.joining-to-port="18001"
```

### Node: Taylor on 127.0.0.1:18162 as a normal node
```
$ java -jar ./chord-node/build/libs/chord-node-1.0.0.RELEASE.jar \
      --spring.profiles.active=default \
      --chord.node-name="taylor" \
      --chord.node-address="127.0.0.1" \
      --chord.node-port="18162" \
      --chord.finger-ring-size-bits="7" \
      --chord.bootstrapping-node="false" \
      --chord.joining-to-address="127.0.0.1" \
      --chord.joining-to-port="18652"
```

### Node: Matthew on 127.0.0.1:18777 as a normal node
```
$ java -jar ./chord-node/build/libs/chord-node-1.0.0.RELEASE.jar \
      --spring.profiles.active=default \
      --chord.node-name="matthew" \
      --chord.node-address="127.0.0.1" \
      --chord.node-port="18777" \
      --chord.finger-ring-size-bits="7" \
      --chord.bootstrapping-node="false" \
      --chord.joining-to-address="127.0.0.1" \
      --chord.joining-to-port="18001"
```
