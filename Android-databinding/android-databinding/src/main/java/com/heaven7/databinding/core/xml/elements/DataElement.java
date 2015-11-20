package com.heaven7.databinding.core.xml.elements;

import com.heaven7.databinding.core.xml.AbsElement;
import com.heaven7.xml.XmlWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by heaven7 on 2015/8/10.
 */
public class DataElement extends AbsElement {

    List<VariableElement> variableElements;
    List<ImportElement> importElements;

    public DataElement(String mElementName) {
        super(mElementName);
    }

    public void addVariableElement(VariableElement e){
        if(variableElements == null)
            variableElements = new ArrayList<VariableElement>();
        variableElements.add(e);
    }
    public List<VariableElement> getVariableElements() {
        return variableElements;
    }
    public void setVariableElements(List<VariableElement> variableElements) {
        this.variableElements = variableElements;
    }

    public void addImportElement(ImportElement e){
        if(importElements == null)
            importElements = new ArrayList<ImportElement>();
        importElements.add(e);
    }
    public List<ImportElement> getImportElements() {
        return importElements;
    }
    public void setImportElements(List<ImportElement> importElements) {
        this.importElements = importElements;
    }

    @Override
    public void write(XmlWriter writer) throws IOException {
         writer.element(getElementName());
         writeAttrs(writer);
        if(variableElements != null ) {
            List<VariableElement> variableElements = this.variableElements;
            int len = variableElements.size();
            for (int i = len - 1; i >=0 ; i--) {
                variableElements.get(i).write(writer);
            }
        }
        if(importElements != null ) {
            List<ImportElement> importElements = this.importElements;
            int len = importElements.size();
            for (int i = len - 1; i >=0 ; i--) {
                importElements.get(i).write(writer);
            }
        }
         writer.pop();
    }

    @Override
    public void reset() {
        super.reset();
        variableElements.clear();
        importElements.clear();
    }
}
