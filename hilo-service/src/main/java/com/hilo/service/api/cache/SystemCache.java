package com.hilo.service.api.cache;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SystemCache {

	private CacheEntry cacheEntry;

	public void refresh() {
		this.loadCache();
	}

	public void loadCache() {
		//log.info("start to load system static data from database");
		cacheEntry = new CacheEntry();
	}

	public String get(@NonNull String key) {
		return cacheEntry.dic.get(key);
	}

	private class CacheEntry {

		Map<String, String> dic = new HashMap<>();
	}
}
