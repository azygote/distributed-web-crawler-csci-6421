package org.gty.crawler.master.controller;

import org.gty.crawler.master.model.Master;
import org.gty.crawler.master.model.SeedUrl;
import org.gty.crawler.master.model.Slave;
import org.gty.crawler.master.service.CrawlerMasterService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class CrawlerMasterController {

    private final CrawlerMasterService service;

    public CrawlerMasterController(CrawlerMasterService service) {
        this.service = service;
    }

    @PostMapping(value = "/api/register-node", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Master registerNode(@RequestBody Slave slave) {
        return service.registerNode(slave);
    }

    @PostMapping(value = "/api/assign-seed", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void assignSeed(@RequestBody SeedUrl seedUrl) {
        service.assignSeed(seedUrl);
    }

    @GetMapping(value = "/api/start")
    public void start() {
        service.start();
    }

    @GetMapping(value = "/api/stop")
    public void stop() {
        service.stop();
    }

    @GetMapping(value = "/api/get-slaves")
    @ResponseBody
    public Map<String, Slave> getSlaves() {
        return service.getSlaves();
    }
}
