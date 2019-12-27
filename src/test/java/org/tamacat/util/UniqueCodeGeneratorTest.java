/*
 * Copyright (c) 2008, tamacat.org
 * All rights reserved.
 */
package org.tamacat.util;

import static org.junit.Assert.*;

import java.io.File;
import java.util.HashSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UniqueCodeGeneratorTest {

	@Before
	public void setUp() throws Exception {
		System.setProperty("user.dir", new File(ClassUtils.getURL(".").getPath()).getAbsolutePath());
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGenerate() {
		for (int j=0; j<10; j++) {
			HashSet<String> uniq = new HashSet<>(1000);
			for (int i=0; i<1000; i++) {
				String uuid = UniqueCodeGenerator.generate();
				if (uniq.contains(uuid) == false) {
					uniq.add(uuid);
					//System.out.println(uuid);
				} else {
					fail();
				}
			}
		}
	}
	
	@Test
	public void testGenerateDelimiter() {
		assertTrue(UniqueCodeGenerator.generate(false).indexOf("-")>=0);
		assertTrue(UniqueCodeGenerator.generate(true).indexOf("-")==-1);
	}

	@Test
	public void testGenerateString() {
		assertTrue(UniqueCodeGenerator.generate("test").startsWith("test"));

		assertFalse(UniqueCodeGenerator.generate(null).startsWith("test"));
	}
}
