/*
 * Copyright (c) 2007, tamacat.org
 * All rights reserved.
 */
package org.tamacat.util;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

public class DateUtilsTest {

	@Before
	public void setUp() throws Exception {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}
	
	@Test
	public void testGetTime() {
		assertEquals("2011-01-01 00:00:00,000", DateUtils.getTime(new Date(1293840000000L), "yyyy-MM-dd HH:mm:ss,SSS"));
		assertTrue(true);
	}

	@Test
	public void testGetTimeLocale() {
		assertEquals("2011-01-01 00:00:00,000", DateUtils.getTime(new Date(1293840000000L), "yyyy-MM-dd HH:mm:ss,SSS", Locale.US));
		assertTrue(true);
	}
	
	@Test
	public void testGetTimeLocaleTimeZone() {
		assertEquals("2011-01-01 00:00:00,000", DateUtils.getTime(new Date(1293840000000L), "yyyy-MM-dd HH:mm:ss,SSS", Locale.US, TimeZone.getDefault()));
		assertTrue(true);
	}

	@Test
	public void testGetTimestamp() {
		DateUtils.getTimestamp("yyyy-MM-dd HH:mm:ss,SSS");
	}

	@Test
	public void testGetTimestampLocale() {
		DateUtils.getTimestamp("yyyy-MM-dd HH:mm:ss,SSS", Locale.US);
	}

	@Test
	public void testParseTime() {
		assertEquals(new Date(1293840000000L), DateUtils.parse("2011-01-01 00:00:00", "yyyy-MM-dd HH:mm:ss"));
		assertTrue(true);
	}

	@Test
	public void testParseTimeZone() {
		assertEquals(new Date(1293840000000L), DateUtils.parse("2011-01-01 00:00:00", "yyyy-MM-dd HH:mm:ss", Locale.US, TimeZone.getTimeZone("GMT")));
		assertTrue(true);
	}

	
	@Test
	public void testParseTimeError() {
		assertNull(DateUtils.parse("2011", "yyyy-MM-dd HH:mm:ss"));
	}

}
