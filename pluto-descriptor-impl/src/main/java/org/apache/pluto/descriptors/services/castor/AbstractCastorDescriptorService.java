/*
 * Copyright 2005 The Apache Software Foundation
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
package org.apache.pluto.descriptors.services.castor;

import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.xml.sax.helpers.DefaultHandler;

import java.io.*;

/**
 * Abstract deployment descriptor support class.
 * This Base class provides support for reading
 * and writing deployment descriptors using Castor.
 *
 * @author <a href="ddewolf@apache.org">David H. DeWolf</a>
 * @version $Id: AbstractCastorDescriptorService.java 156743 2005-03-10 05:50:30Z ddewolf $
 * @since Mar 5, 2005
 */
abstract class AbstractCastorDescriptorService {

    /**
     * Read the and convert the descriptor into it's Object graph.
     * @return
     * @throws IOException
     */
    protected Object readInternal(InputStream is) throws IOException {
        Object object = null;
        try {
            Mapping mapping = getCastorMapping();
            Unmarshaller unmarshaller = new Unmarshaller(mapping);
            unmarshaller.setEntityResolver(new EntityResolverImpl());
            unmarshaller.setIgnoreExtraElements(getIgnoreExtraElements());
            if(is!=null) {
                InputStreamReader in = new InputStreamReader(is);
                object = unmarshaller.unmarshal(in);
            }
        }
        catch(Exception me) {
            me.printStackTrace();
            throw new IOException(me.getMessage());
        }
        finally {
            if(is != null) {
                is.close();
            }
        }
        return object;
    }

    /**
     * Write the object graph to it's descriptor.
     * @param object
     * @throws IOException
     */
    protected void writeInternal(Object object, OutputStream out) throws IOException {

        OutputStreamWriter writer =
            new OutputStreamWriter(out);

        try {
            Marshaller marshaller =
                new Marshaller(new DefaultHandler()); //serializer.asDocumentHandler());
            marshaller.setMapping(getCastorMapping());
            marshaller.marshal(object);
        } catch(IOException io) {
            throw io;
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
        finally {
            writer.flush();
            writer.close();
        }
    }

    protected boolean getIgnoreExtraElements() {
        return false;
    }

    protected abstract Mapping getCastorMapping() throws IOException, MappingException;
    protected abstract String getPublicId();
    protected abstract String getDTDUri();


}

