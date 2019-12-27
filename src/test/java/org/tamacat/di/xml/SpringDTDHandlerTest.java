package org.tamacat.di.xml;

import org.junit.Test;
import org.xml.sax.SAXException;

public class SpringDTDHandlerTest {

	@Test
	public void testNotationDecl() throws SAXException {
		new SpringDTDHandler().notationDecl(null, null, null);
	}

	@Test
	public void testUnparsedEntityDecl() throws SAXException {
		new SpringDTDHandler().unparsedEntityDecl(null, null, null, null);
	}
}
