/*
 * Copyright (c) 2009, tamacat.org
 * All rights reserved.
 */
package org.tamacat.di;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.tamacat.core.Core;
import org.tamacat.core.SampleCore;
import org.tamacat.di.define.BeanDefine;
import org.tamacat.di.define.BeanDefineMap;
import org.tamacat.util.ClassUtils;

public class DITest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testConfigure() {
		DIContainer di = DI.configure("test.xml");
		assertEquals(SampleCore.class, di.getBean("Core").getClass());
	}
	
	@Test
	public void testConfigureBeanDefine() {
		BeanDefine define = new BeanDefine();
		define.setId("Core");
		define.setType(SampleCore.class);
		DIContainer di = DI.configure(define);
		assertEquals(SampleCore.class, di.getBean("Core").getClass());
	}
	
	@Test
	public void testConfigureBeanDefineMap() {
		BeanDefineMap map = new BeanDefineMap();
		BeanDefine define = new BeanDefine();
		define.setId("Core");
		define.setType(SampleCore.class);
		map.add(define);
		
		DIContainer di = DI.configure(map);
		assertEquals(SampleCore.class, di.getBean("Core").getClass());
	}
	
	@Test
	public void testDI() {
		DI di = ClassUtils.newInstance(DI.class);
		assertNull(di);
	}
	
	@Test
	public void testRepeat() {
		DIContainer di = DI.configure("test.xml");
		Core core = di.getBean("Core3", Core.class);
		assertEquals("CoreName", core.getCoreName());
		
		Core core2 = di.getBean("Core3", Core.class);
		assertEquals("CoreName", core2.getCoreName());
		
		assertNotSame(core, core2);
	}
}
