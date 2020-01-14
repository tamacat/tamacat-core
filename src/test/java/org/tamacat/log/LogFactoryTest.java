/*
 * Copyright 2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.tamacat.log;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.tamacat.log.impl.Log4j2Logger;
import org.tamacat.log.impl.Log4jDiagnosticContext;
import org.tamacat.log.impl.Log4jLogger;
import org.tamacat.log.impl.NoneDiagnosticContext;
import org.tamacat.util.ClassUtils;

import org.tamacat.log.Log;
import org.tamacat.log.LogFactory;
import org.tamacat.log.LogFactoryTest;
import org.tamacat.log.impl.Slf4jLogger;

public class LogFactoryTest {

	Log logger;

	@Before
	public void setUp() throws Exception {
		logger = LogFactory.getLog(LogFactoryTest.class);
	}

	@Test
	public void testDebugString() {
		logger.debug("test");
	}

	@Test
	public void testDebugStringStringArray() {
		logger.debug("arg0=${0}, arg1=${1}", "one", "two");
	}

	@Test
	public void testIsEnabled() {
		assertTrue(logger.isFatalEnabled());
		assertTrue(logger.isErrorEnabled());
		assertTrue(logger.isWarnEnabled());
		assertTrue(logger.isInfoEnabled());
		assertTrue(logger.isDebugEnabled());
		assertFalse(logger.isTraceEnabled());
	}

	@Test
	public void testGetLog() {
		Log l1 = LogFactory.getLog("test");
		Log l2 = LogFactory.getLog("test");
		assertSame(l1, l2);
	}

	@Test
	public void testLoadLogger() throws Exception {
		assertFalse(LogFactory.SELF.loadLogger("test") instanceof Log4j2Logger);
		assertTrue(LogFactory.SELF.loadLogger("test") instanceof Slf4jLogger);

		Log l1 = LogFactory.SELF.loadLogger("test");
		Log l2 = LogFactory.SELF.loadLogger("test");
		assertNotSame(l1, l2);


		LogFactory.SELF.setClassLoader(new DummyClassLoader());
		Log l3 = LogFactory.SELF.loadLogger("test");
		assertNotSame(l1, l3);
	}

	@Test
	public void testGetDiagnosticContext() {
		assertTrue(LogFactory.getDiagnosticContext(new Log4jLogger("test")) instanceof Log4jDiagnosticContext);
		assertTrue(LogFactory.getDiagnosticContext(null) instanceof NoneDiagnosticContext);
	}

	@Test
	public void testSetClassLoader() {
		LogFactory.SELF.setClassLoader(ClassUtils.getDefaultClassLoader());
		assertEquals(LogFactory.SELF.loader, ClassUtils.getDefaultClassLoader());
	}

	static class DummyClassLoader extends ClassLoader {
	}
}
