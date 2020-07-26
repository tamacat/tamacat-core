/*
 * Copyright (c) 2016 tamacat.org
 * All rights reserved.
 */
package org.tamacat.util;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.tamacat.log.Log;
import org.tamacat.log.LogFactory;

//copy from tamacat-auth-1.4 OneTimePassword/TimeBasedOneTimePassword
public class TimeBasedToken {

	static Log LOG = LogFactory.getLog(TimeBasedToken.class);
	
	protected String algorithm = "HmacSHA256";
	protected String timeFormat = "yyyyMMddHHmm";
	protected int oneTimePasswordPeriod = 15; //min
	protected int timeUnit = Calendar.MINUTE;
	protected boolean caseSensitive = true;
	
	public static TimeBasedToken getDefault() {
		return new TimeBasedToken();
	}
	
	private TimeBasedToken() {}
	
	public TimeBasedToken format(String timeFormat) {
		this.timeFormat = timeFormat;
		return this;
	}
	
	public TimeBasedToken algorithm(String algorithm) {
		this.algorithm = algorithm;
		return this;
	}
	
	/**
	 * Calendar.MINUTE | Calendar.HOUR | Calendar.DATE;
	 * Default: Calendar.MINUTE
	 */
	public TimeBasedToken timeUnit(int timeUnit) {
		this.timeUnit = timeUnit;
		if (timeUnit == Calendar.HOUR) {
			this.format("yyyyMMddHH");
		} else if (timeUnit == Calendar.DATE) {
			this.format("yyyyMMdd");
		}
		return this;
	}
	
	public String generate(String secret, Date date) {
		String time = DateUtils.getTime(date, timeFormat);
		String pw = EncryptionUtils.getMac(time, secret, algorithm);
		//LOG.debug(time+"="+pw);
		if (caseSensitive) {
			return pw;
		} else {
			return pw.toLowerCase();
		}
	}
	
	public String generate(String secret) {
		String time = DateUtils.getTimestamp(timeFormat);
		String pw = EncryptionUtils.getMac(time, secret, algorithm);
		//LOG.debug(time+"="+pw);
		if (caseSensitive) {
			return pw;
		} else {
			return pw.toLowerCase();
		}
	}
	
	public boolean validate(String secret, String token) {
		if (token == null) return false;
		Set<String> hash = new HashSet<>();
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int period = oneTimePasswordPeriod;
		cal.add(timeUnit, -period);
		for (int i=0; i<=period*2; i++) {
			String time = DateUtils.getTime(cal.getTime(),timeFormat);
			String pw = EncryptionUtils.getMac(time, secret, algorithm);
			if (LOG.isTraceEnabled()) {
				LOG.trace(time+" ["+token+"]=["+pw +"] result="+pw.equals(token));
			}
			if (caseSensitive) {
				hash.add(pw);
			} else {
				hash.add(pw.toLowerCase());
			}
			cal.add(timeUnit, 1);
		}
		if (caseSensitive) {
			return hash.contains(token);
		} else {
			return hash.contains(token.toLowerCase());
		}
	}
	
	/**
	 * Minutes of time period.
	 * @param oneTimePasswordPeriod
	 */
	public TimeBasedToken period(int oneTimePasswordPeriod) {
		this.oneTimePasswordPeriod = oneTimePasswordPeriod;
		return this;
	}

	public TimeBasedToken caseSensitive(boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
		return this;
	}
}
