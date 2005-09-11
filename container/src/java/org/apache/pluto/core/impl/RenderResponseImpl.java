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
/*

 */

package org.apache.pluto.core.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Iterator;

import javax.portlet.PortletURL;
import javax.portlet.RenderResponse;

import org.apache.pluto.factory.PortletObjectAccess;
import org.apache.pluto.om.entity.PortletEntity;
import org.apache.pluto.om.portlet.ContentType;
import org.apache.pluto.om.portlet.ContentTypeSet;
import org.apache.pluto.om.portlet.PortletDefinition;
import org.apache.pluto.om.window.PortletWindow;
import org.apache.pluto.services.title.DynamicTitle;
import org.apache.pluto.util.NamespaceMapperAccess;

public class RenderResponseImpl extends PortletResponseImpl implements RenderResponse {
    private static final String illegalStateExceptionText = "No content type set.";

    private boolean containerSupportsBuffering;

    private String currentContentType = null;   // needed as servlet 2.3 does not have a response.getContentType

    private boolean usingWriter;
    private boolean usingStream;

    public RenderResponseImpl(PortletWindow portletWindow,
                              javax.servlet.http.HttpServletRequest servletRequest,
                              javax.servlet.http.HttpServletResponse servletResponse,
                              boolean containerSupportsBuffering)
    {
        super(portletWindow, servletRequest, servletResponse);
        this.containerSupportsBuffering = containerSupportsBuffering;
    }

    // javax.portlet.RenderResponse ---------------------------------------------------------------
    public String getContentType()
    {
        // in servlet 2.4 we could simply use this:
        // return this._getHttpServletResponse().getContentType();
        return currentContentType;
    }

    public PortletURL createRenderURL()
    {
        PortletURL url = createURL(false);
        return url;
    }

    public PortletURL createActionURL()
    {
        PortletURL url = createURL(true);
        return url;
    }

    public String getNamespace()
    {
        String namespace = NamespaceMapperAccess.getNamespaceMapper().encode(getInternalPortletWindow().getId(), "");

         // replace all characters in the 'namespace + name' that are not valid
         // javascript variable or function name characters by '_'.
         StringBuffer validNamespace = new StringBuffer();
         for (int i = 0; i < namespace.length(); i++) {
         	char ch = namespace.charAt(i);
         	if (Character.isJavaIdentifierPart(ch)) {
         		validNamespace.append(ch);
         	} else {
         		validNamespace.append('_');
         	}
		}

        return validNamespace.toString();
    }

    public void setTitle(String title)
    {
        DynamicTitle.setDynamicTitle(getInternalPortletWindow(),
                                     getHttpServletRequest(),
                                     title);
    }

    public void setContentType(String type)
    {
        String mimeType = stripCharacterEncoding(type);
        if (!isValidContentType(mimeType)) {
            throw new IllegalArgumentException(mimeType);
        }
        if (!usingWriter && !usingStream) {
        	this._getHttpServletResponse().setContentType(mimeType);
        	currentContentType = mimeType;
		}
    }

    public String getCharacterEncoding()
    {
        return this._getHttpServletResponse().getCharacterEncoding();
    }

    public PrintWriter getWriter() throws IOException, IllegalStateException {
        if (currentContentType == null) {
            throw new java.lang.IllegalStateException(illegalStateExceptionText);
        }
		usingWriter = true;
        return super.getWriter();
    }

    public java.util.Locale getLocale()
    {
        return this.getHttpServletRequest().getLocale();
    }

    public void setBufferSize(int size)
    {
    	if (!containerSupportsBuffering) {
    		// default behaviour if property pluto.allowSetBufferSize in file
    		// ConfigService.properties wasn't set or was set to a value not equal to "yes"
    		throw new IllegalStateException("portlet container does not support buffering");
    	} else {
    		this._getHttpServletResponse().setBufferSize(size);
    	}
    }

    public int getBufferSize()
    {
    	if (!containerSupportsBuffering) {
    		return 0;
    	} else {
    		return this._getHttpServletResponse().getBufferSize();
    	}
    }

    public void flushBuffer() throws java.io.IOException
    {
        this._getHttpServletResponse().flushBuffer();
    }

    public void resetBuffer()
    {
        this._getHttpServletResponse().resetBuffer();
    }

    public boolean isCommitted()
    {
        return this._getHttpServletResponse().isCommitted();
    }

    public void reset()
    {
        this._getHttpServletResponse().reset();
    }

    public OutputStream getPortletOutputStream() throws java.io.IOException,java.lang.IllegalStateException
    {
        if (currentContentType == null) {
            throw new java.lang.IllegalStateException(illegalStateExceptionText);
        }
		usingStream = true;
        return getOutputStream();
    }
    // --------------------------------------------------------------------------------------------

    // internal methods ---------------------------------------------------------------------------
    private PortletURL createURL(boolean isAction)
    {
        return PortletObjectAccess.getPortletURL(getInternalPortletWindow(),
                                                 getHttpServletRequest(),
                                                 _getHttpServletResponse(),
                                                 isAction);
    }

    private boolean isValidContentType(String type)
    {
        type = stripCharacterEncoding(type);
        PortletEntity entity = portletWindow.getPortletEntity();
        PortletDefinition def = entity.getPortletDefinition();
        ContentTypeSet contentTypes = def.getContentTypeSet();
        Iterator it = contentTypes.iterator();
        while(it.hasNext()) {
            ContentType ct = (ContentType)it.next();
            String supportedType = ct.getContentType();
            if (supportedType.equals(type)) {
                return true;
            } else if (supportedType.indexOf("*") >= 0) {
                // the supported type contains a wildcard
                int index = supportedType.indexOf("/");
                String supportedPrefix = supportedType.substring(0, index);
                String supportedSuffix = supportedType.substring(index + 1, supportedType.length());

                index = type.indexOf("/");
                String typePrefix = type.substring(0, index);
                String typeSuffix = type.substring(index + 1, type.length());

                if (supportedPrefix.equals("*") || supportedPrefix.equals(typePrefix)) {
                    // the prefixes match
                    if (supportedSuffix.equals("*") || supportedSuffix.equals(typeSuffix)) {
                        // the suffixes match
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private String stripCharacterEncoding(String type)
    {
        int xs = type.indexOf(';');
        String strippedType;
        if (xs == -1) {
            strippedType = type;
        } else {
            strippedType = type.substring(0,xs);
        }
        return strippedType.trim();
    }
    // --------------------------------------------------------------------------------------------
}
