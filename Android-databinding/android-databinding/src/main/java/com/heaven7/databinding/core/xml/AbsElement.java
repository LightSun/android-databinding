package com.heaven7.databinding.core.xml;

import android.support.v4.util.ArrayMap;

import com.heaven7.databinding.util.IResetable;
import com.heaven7.xml.XmlWriter;

import java.io.IOException;

public abstract class AbsElement implements ISerializeXml,IResetable{
	
	private ArrayMap<String, String> mAttrMap;
	private final String mElementName;

	public AbsElement(String mElementName) {
		this.mElementName = mElementName;
		this.mAttrMap = new ArrayMap<String, String>();
	}

	public String getElementName(){
		return mElementName;
	}

	public AbsElement addAttribute(String name, String value) {
		mAttrMap.put(name, value);
		return this;
	}
	public String getAttribute(String name){
		return getAttributeMap().get(name);
	}

	public ArrayMap<String, String> getAttributeMap() {
		return mAttrMap;
	}

	protected void writeAttrs(XmlWriter writer) {
		try {
			final ArrayMap<String, String> attrMap = getAttributeMap();
			for(int i=0,size=attrMap.size() ;  i< size ;i++){
				writer.attribute(attrMap.keyAt(i),attrMap.valueAt(i));
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void reset() {
		mAttrMap.clear();
	}

	@Override
	public String toString() {
		return getClass().getSimpleName()+"{" +
				"mAttributeMap=" + mAttrMap +
				'}';
	}
}
