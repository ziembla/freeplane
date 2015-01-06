package org.freeplane.core.ui.menubuilders;

import java.io.Reader;
import java.util.Arrays;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class EntryStructureBuilder {


	static final String ENTRY = "entry";
	private Reader stringReader;

	public EntryStructureBuilder(Reader stringReader) {
		this.stringReader = stringReader;
		
	}

	public Entry build(Entry menuStructure) {
		try {
			SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
			XMLReader xmlReader = saxParser.getXMLReader();
			xmlReader.setContentHandler(new MenuStructureXmlHandler(menuStructure));
			xmlReader.parse(new InputSource(stringReader));
			return menuStructure;
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}

class MenuStructureXmlHandler extends DefaultHandler {
	private static final String BUILDER = "builder";
	private Entry menuStructure;

	public MenuStructureXmlHandler(Entry menuStructure) {
		this.menuStructure = menuStructure;
	}

	@Override
	public void startElement(String uri, String localName,
			String qName, Attributes attributes)
			throws SAXException {
		if(qName.equals(EntryStructureBuilder.ENTRY)){
			final Entry child = new Entry();
			for (int attributeIndex = 0; attributeIndex < attributes.getLength(); attributeIndex++){
				final String attributeName = attributes.getQName(attributeIndex);
				final String attributeValue = attributes.getValue(attributeName);
				if(attributeName.equals(BUILDER))
					child.setBuilders(Arrays.asList(attributeValue.split("\\s*,\\s*")));
				else
					child.setAttribute(attributeName, attributeValue);
			}
			menuStructure.addChild(child);
		}
	}
}
