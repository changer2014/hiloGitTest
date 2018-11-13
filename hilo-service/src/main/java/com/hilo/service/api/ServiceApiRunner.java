package com.hilo.service.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.hilo.service.api.cache.SystemCache;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ServiceApiRunner implements CommandLineRunner {

    @Autowired
    private SystemCache systemCache;

    /**
     * 加载静态数据，字典表数据
     *
     * @param args
     */
    @Override
    public void run(String... args) {
    	log.info("load cache...");
        systemCache.loadCache();
    }
}
