/*
 * Copyright 2003,2004 The Apache Software Foundation.
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
package org.apache.pluto.descriptors.portlet;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Representation of the supports element within the portlet.xml
 * 
 *
 * 
 * 			Supports indicates the portlet modes a 
 * 			portlet supports for a specific content type. All portlets must 
 * 			support the view mode. 
 * 			Used in: portlet
 * 			
 * 
 * <p>Java class for supportsType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="supportsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="mime-type" type="{http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd}mime-typeType"/>
 *         &lt;element name="portlet-mode" type="{http://java.sun.com/xml/ns/portlet/portlet-app_2_0.xsd}portlet-modeType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "supportsType", propOrder = {
    "mimeType","mimeType1",
    "portletModes","portletModes1"
})
public class SupportsDD {

	@XmlElement(name = "mime-type")
    private String mimeType;
	
	@XmlElement(name = "mime-type", namespace = PortletDD.QNAME_JSR168)
    private String mimeType1;
	
	@XmlElement(name = "portlet-mode")
    private List<String> portletModes = null;
	
	@XmlElement(name = "portlet-mode", namespace = PortletDD.QNAME_JSR168)
    private List<String> portletModes1 = null;

    public String getMimeType() {
    	if (mimeType != null)
    		return mimeType;
    	return mimeType1;
    }

    public void setMimeType(String mimeType) {
    	this.mimeType = mimeType;
    	this.mimeType1 = mimeType;
    }

    public List<String> getPortletModes() {
    	if (portletModes != null)
    		return portletModes;
    	return portletModes1;
    }

    public void setPortletModes(List<String> portletModes) {
    	this.portletModes = portletModes;
    	this.portletModes1 = portletModes;
    }
}
