package com.heaven7.databinding.core.xml;

import com.heaven7.xml.XmlReader.Element;

public interface IElementParser {

	/** @return  parse is success or not */
	boolean parse(Element root);
}
