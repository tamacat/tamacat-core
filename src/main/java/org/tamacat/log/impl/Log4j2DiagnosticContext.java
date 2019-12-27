/*
 * Copyright (c) 2018 tamacat.org
 * All rights reserved.
 */
package org.tamacat.log.impl;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.ThreadContext;

import org.tamacat.log.DiagnosticContext;

public class Log4j2DiagnosticContext implements DiagnosticContext {

	@Override
	public void setMappedContext(String key, String data) {
		ThreadContext.put(key, data);
	}

	@Override
	public void setNestedContext(String data) {
		ThreadContext.push(data);
	}

	@Override
	public void remove() {
		ThreadContext.clearAll();
	}
	
	@Override
	public void remove(String key) {
		ThreadContext.remove(key);
	}

	public String get(String key) {
		return ThreadContext.get(key);
	}
	
	protected Set<?> keySet() {
		Map<?, ?> context = ThreadContext.getContext();
		return context != null ? context.keySet() : new HashSet<>();
	}
}
