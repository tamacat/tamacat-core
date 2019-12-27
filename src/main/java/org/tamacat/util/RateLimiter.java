/*
 * Copyright (c) 2018 tamacat.org
 * All rights reserved.
 */
package org.tamacat.util;

import java.util.HashMap;
import java.util.Map;

public class RateLimiter {

	public static RateLimiter create(int maxLimit) {
		return new RateLimiter(maxLimit);
	}
	
	Map<String, TimeLimitedMap<Long, String>> counter = new HashMap<>();
	int maxLimit;
	
	RateLimiter(int maxLimit) {
		this.maxLimit = maxLimit;
	}

	public synchronized boolean addAndCheck(String key, long maxLifeTimeMillis) {
		TimeLimitedMap<Long, String> log = counter.get(key);
		if (log == null) {
			log = new TimeLimitedMap<>(maxLifeTimeMillis);
			counter.put(key, log);
		}
		if (log.size() < maxLimit) {
			log.put(System.nanoTime(), key, maxLifeTimeMillis);
			return true;
		} else {
			return false;
		}
	}

	public synchronized int count(String key) {
		TimeLimitedMap<Long, String> log = counter.get(key);
		if (log != null) {
			return log.size();
		}
		return 0;
	}
	
	public synchronized void remove(String key) {
		counter.remove(key);
	}
}
