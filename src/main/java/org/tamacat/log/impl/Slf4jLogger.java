/*
 * Copyright (c) 2018 tamacat.org
 * All rights reserved.
 */
package org.tamacat.log.impl;

import java.io.IOException;
import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

public class Slf4jLogger extends AbstractLogger<Level> implements Serializable {

	private static final long serialVersionUID = 1L;
	private transient Logger delegate;
	static final String callerFQCN = Slf4jLogger.class.getName();

	private String name;

	public Slf4jLogger(String name) {
		this.name = name;
		delegate = createSlf4jLogger(name);
	}

	static Logger createSlf4jLogger(String name) {
		return LoggerFactory.getLogger(name);
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		delegate = createSlf4jLogger(name);
	}

	@Override
	protected void log(Level level, Object message) {
		if (message != null) {
			switch (level) {
				case TRACE:
					delegate.trace(message.toString());
					break;
				case DEBUG:
					delegate.debug(message.toString());
					break;
				case INFO:
					delegate.info(message.toString());
					break;
				case WARN:
					delegate.warn(message.toString());
					break;
				case ERROR:
					delegate.error(message.toString());
					break;
				default:
					delegate.debug(message.toString());
			}
		}
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
		return Level.ERROR;
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
		switch (level) {
		case TRACE:
			return delegate.isTraceEnabled();
		case DEBUG:
			return delegate.isDebugEnabled();
		case INFO:
			return delegate.isInfoEnabled();
		case WARN:
			return delegate.isWarnEnabled();
		case ERROR:
			return delegate.isErrorEnabled();
		default:
			return false;
		}
	}

	public Object getOriginalLogger() {
		return delegate;
	}
}
