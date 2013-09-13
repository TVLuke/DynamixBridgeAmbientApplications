package de.lukeslog.discoverytest.main;

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


import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import javax.jmdns.ServiceInfo;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;

import com.strategicgains.restexpress.ContentType;
import com.strategicgains.restexpress.Format;
import com.strategicgains.restexpress.Request;
import com.strategicgains.restexpress.Response;

public class ObservationControler 
{

	public void create(Request request, Response response)
	{

	}

	public String delete(Request request, Response response)
	{
		return "";
	}

	public void read(Request request, Response response)
	{
		System.out.println("get");
		String format = request.getFormat();
		if(!(format!=null))
		{
			format=Format.XML;
		}
		HashMap<String, ServiceInfo> list = DiscoverServices.getListOfDynamixBridges();
		String x="";
		for(String key: list.keySet())
		{
			x=x+key+",";
		}
		if(x.length()>0)
		{
			x = x.substring(0, x.length() - 1);
		}
		byte[] r = x.getBytes();
		System.out.println("x "+x);
		response.setResponseStatus(HttpResponseStatus.OK);
		if(format.equals(Format.XML))
		{
			System.out.println("Format XML");
			response.setContentType(Format.XML);
			response.addHeader("version", "1.0");
			response.setResponseProcessor(ResponseProcessors.xml());
			response.setBody(new String(r));			
			response.setResponseCreated();
		}
		if(format.equals(Format.JSON))
		{
			response.setResponseStatus(HttpResponseStatus.UNSUPPORTED_MEDIA_TYPE);
			response.setResponseCreated();
		}
		response.setResponseStatus(HttpResponseStatus.UNSUPPORTED_MEDIA_TYPE);
		response.setResponseCreated();
	}

	public String update(Request request, Response response)
	{
		return "";
	}
	
	
}
