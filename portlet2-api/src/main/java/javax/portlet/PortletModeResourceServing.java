/*  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
/*
 * NOTE: this source code is based on an early draft version of JSR 286 and not intended for product
 * implementations. This file may change or vanish in the final version of the JSR 286 specification.
 */
/*
 * This source code implements specifications defined by the Java
 * Community Process. In order to remain compliant with the specification
 * DO NOT add / change / or delete method signatures!
 */
/*
 * Copyright 2006 IBM Corporation.
 *
 */
package javax.portlet;

public interface PortletModeResourceServing {
    /**
     * Render the icon in disabled state.
     * <p>
     * The value of this constant is IconStateDisabled.
     */
    public static final String IconStateDisabled = "IconStateDisabled";
    
    /**
     * Render the icon in enabled state.
     * <p>
     * The value of this constant is IconStateEnsabled.
     */
    public static final String IconStateEnabled ="IconStateEnabled";
    
    /**
     * Render the icon in enabled state with the mouse hovering over the icon.
     * <p>
     * The value of this constant is IconStateEnabledHover.
     */
    public static final String IconStateEnabledHover = "IconStateEnabledHover";
    
    /**
     * Indicating that no that is provided for serving this portlet mode resource.
     * <p>
     * The value of this constant is IconStateDisabled.
     */
    public static final String NoState = "NoState";
    
    /**
     * @param contentType
     * @param characterEncoding
     * @param mode
     * @param resourceState
     * @param output
     */
    public void servePortletModeResource(String contentType, String characterEncoding, 
            PortletMode mode, String resourceState, java.io.OutputStream output);
}
