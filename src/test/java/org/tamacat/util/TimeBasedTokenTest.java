/*
 * Copyright (c) 2016 tamacat.org
 * All rights reserved.
 */
package org.tamacat.util;

import static org.junit.Assert.*;

import java.util.Calendar;

import org.junit.Test;

public class TimeBasedTokenTest {

	@Test
	public void testGenerateString() {
		String secret = "1234567890";
		String token = TimeBasedToken.getDefault().timeUnit(Calendar.HOUR).period(1).generate(secret);
		
		//System.out.println(token);
		assertTrue(TimeBasedToken.getDefault().timeUnit(Calendar.HOUR).period(1).validate(secret, token));
	}

}
