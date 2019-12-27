/*
 * Copyright (c) 2018 tamacat.org
 * All rights reserved.
 */
package org.tamacat.log.impl;

import java.io.IOException;
import java.io.Serializable;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log4j2Logger extends AbstractLogger<Level> implements Serializable {

	private static final long serialVersionUID = 1L;
	private transient Logger delegate;
	static final String callerFQCN = Log4j2Logger.class.getName();

	private String name;

	public Log4j2Logger(String name) {
		this.name = name;
		delegate = createLog4j2Logger(name);
	}

	static Logger createLog4j2Logger(String name) {
		return LogManager.getLogger(name);
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		delegate = createLog4j2Logger(name);
	}

	@Override
	protected void log(Level level, Object message) {
		delegate.log(level, message, null);
	}

	@Override
	protected Level getDebugLevel() {
		return Level.DEBUG;
	}

	@Override
	protected Level getErrorLevel() {
		return Level.ERROR;
	}

	@Override
	protected Level getFatalLevel() {
		return Level.FATAL;
	}

	@Override
	protected Level getInfoLevel() {
		return Level.INFO;
	}

	@Override
	protected Level getTraceLevel() {
		return Level.TRACE;
	}

	@Override
	protected Level getWarnLevel() {
		return Level.WARN;
	}

	@Override
	protected boolean isEnabled(Level level) {
		return delegate.isEnabled(level);
	}

	public Object getOriginalLogger() {
		return delegate;
	}
}
