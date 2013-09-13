package de.lukeslog.discoverytest.main;

import org.jboss.netty.handler.codec.http.HttpMethod;

import com.strategicgains.restexpress.Format;
import com.strategicgains.restexpress.RestExpress;

public class Server
{

	public Server()
	{
		RestExpress s = new RestExpress()
		.setName("Bridgefinder")
	    .setPort(53486)
	    .setDefaultFormat(Format.XML)
	    .putResponseProcessor(Format.JSON, ResponseProcessors.json())
	    .putResponseProcessor(Format.XML, ResponseProcessors.xml());
		s.uri("/bridges", new ObservationControler())
		.action("read", HttpMethod.GET)
		.defaultFormat(Format.XML)
		.noSerialization();
		try
		{
			s.bind();
		}
		catch(Exception e)
		{
			System.out.println("Bind failed");
			e.printStackTrace();
		}
		System.out.println("Server: "+s.getBaseUrl()+" "+s.getPort());
		s.awaitShutdown();
	}
}
