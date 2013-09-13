package de.lukeslog.discoverytest.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.StringTokenizer;

import de.uniluebeck.itm.ncoap.application.client.CoapClientApplication;
import de.uniluebeck.itm.ncoap.application.client.CoapResponseProcessor;
import de.uniluebeck.itm.ncoap.communication.reliability.outgoing.EmptyAcknowledgementProcessor;
import de.uniluebeck.itm.ncoap.communication.reliability.outgoing.InternalEmptyAcknowledgementReceivedMessage;
import de.uniluebeck.itm.ncoap.communication.reliability.outgoing.InternalRetransmissionTimeoutMessage;
import de.uniluebeck.itm.ncoap.communication.reliability.outgoing.RetransmissionTimeoutProcessor;
import de.uniluebeck.itm.ncoap.message.CoapRequest;
import de.uniluebeck.itm.ncoap.message.CoapResponse;
import de.uniluebeck.itm.ncoap.message.header.Code;
import de.uniluebeck.itm.ncoap.message.header.MsgType;

public class Background implements Runnable 
{
	static boolean run=true;
	HashMap<String, Integer> errorcounter = new HashMap<String, Integer>();
	
	public Background()
	{
		run();
	}
	
	public void run() 
	{
		run=true;
		CoapClientApplication client = new CoapClientApplication();
		while(run)
		{
			//TODO:
			System.out.println("run ");
			Set<String> cc = DiscoverServices.ips.keySet();
			ArrayList<String> currentips = new ArrayList<String>();
			for(String key: cc)
			{	
				currentips.add(key);
			}
			System.out.println(currentips.size());
			String homedir = System.getProperty("user.home");
			File yourFile = new File(homedir+ File.separator +"DynamixBridge.dat");
			if(!yourFile.exists()) 
			{
			    try 
			    {
					yourFile.createNewFile();
				} 
			    catch (IOException e) 
			    {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} 
				for(int i=0; i<currentips.size(); i++)
				{
					StringTokenizer tk = new StringTokenizer(currentips.get(i), ":");
					String ip = tk.nextToken();
					try
					{		
						URI targetURI = new URI ("coap://"+ip+":5683/.well-known/core");
						CoapRequest coapRequest =  new CoapRequest(MsgType.CON, Code.GET, targetURI);
						client.writeCoapRequest(coapRequest, new SimpleResponseProcessor(ip));
					}
					catch (Exception e)
					{
						System.out.println("error");
						e.printStackTrace();
						int c = errorcounter.get(ip);
						c ++;
						errorcounter.put(ip, c);
					}
				}
			try 
			{
				FileOutputStream oFile = new FileOutputStream(yourFile, false);
				String x="";
				for(int i=0; i<currentips.size(); i++)
				{
					x=x+currentips.get(i)+"\n";
				}
				oFile.write(x.getBytes());
				oFile.close();
			} 
			catch (FileNotFoundException e1) 
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			//System.out.println(homedir);
			for(int i=0; i<currentips.size(); i++)
			{
				//TODO: Request stuff from the phone
				//TODO: Store what exists right now (some configuration, background wallpaper, log in data or whatever
				//TODO: Change to settings from the phone
				//TODO: If the device drops out, delete the preferences again. 
				System.out.println("currentIP: "+currentips.get(i));
			}
			try 
			{
				Thread.sleep(60000);
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	public static void stop()
	{
		run =false;
	}
	
	public class SimpleResponseProcessor implements CoapResponseProcessor, EmptyAcknowledgementProcessor, RetransmissionTimeoutProcessor 
	{

		String ip;

		public SimpleResponseProcessor(String ip)
		{
			super();
			errorcounter.put(ip, 0);
			this.ip = ip;

		}
		
		@Override
		public void processCoapResponse(CoapResponse coapResponse) 
		{
		   System.out.println("response from "+ip);
		}
		
		@Override
		public void processEmptyAcknowledgement(InternalEmptyAcknowledgementReceivedMessage message) 
		{
			System.out.println("Received empty ACK: " + message+" from "+ip);
		}
		
		@Override
		public void processRetransmissionTimeout(InternalRetransmissionTimeoutMessage timeoutMessage) 
		{
			System.out.println("Transmission timed out: " + timeoutMessage);
			int c = errorcounter.get(ip);
			c ++;
			errorcounter.put(ip, c);
			if(c>5)
			{
				DiscoverServices.deleteIP(ip);
			}
		}
	}

}
