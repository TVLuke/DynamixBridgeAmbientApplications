/*
 * Copyright (C) Institute of Telematics, Lukas Ruge
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

package de.lukeslog.discoverytest.main;


import com.strategicgains.restexpress.serialization.xml.DefaultXmlProcessor;


/**
 * @author toddf
 * @since Feb 16, 2011
 */
public class XmlSerializationProcessor extends DefaultXmlProcessor
{
	private static String TAG ="SSPBridge";
	
	public XmlSerializationProcessor()
    {
	    super();
    }
}
