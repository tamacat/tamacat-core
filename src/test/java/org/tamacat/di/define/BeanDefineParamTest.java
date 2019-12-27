/*
 * Copyright (c) 2014, tamacat.org
 * All rights reserved.
 */
package org.tamacat.di.define;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.tamacat.util.ClassUtils;

public class BeanDefineParamTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testSetNameString() {
		BeanDefineParam param = new BeanDefineParam();
		param.setName("interceptor");
		assertEquals("setInterceptor", param.getName());
	}

	@Test
	public void testSetNameStringString() {
		BeanDefineParam param = new BeanDefineParam();
		param.setName("interceptor","add");
		assertEquals("addInterceptor", param.getName());

		param.setName("interceptor","");
		assertEquals("Interceptor", param.getName());

		param.setName("interceptor", null);
		assertEquals("setInterceptor", param.getName());
	}

	@Test
	public void testReturnType() {
		BeanDefineParam param = new BeanDefineParam();
		param.setReturnType(String.class);
		assertEquals(String.class, param.getReturnType());
	}

	@Test
	public void testIsRegistMethod() {
		BeanDefineParam param = new BeanDefineParam();
		assertFalse(param.isRegistMethod());

		param.setMethod(ClassUtils.getMethod(String.class, "toString"));
		assertTrue(param.isRegistMethod());
	}

	@Test
	public void testClone() {
		BeanDefineParam param = new BeanDefineParam();
		param.setName("value");
		BeanDefineParam clone = param.clone();
		assertEquals("setValue", clone.getName());
	}
}
