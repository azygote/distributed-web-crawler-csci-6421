package org.gty.crawler.slave.repository;

import org.gty.crawler.slave.model.Record;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RecordRepository extends MongoRepository<Record, String> {

    List<Record> findByUrl(String url);
}
