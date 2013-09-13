package de.lukeslog.discoverytest.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.StringTokenizer;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;

public class DiscoverServices implements ServiceListener
{
	public static HashMap<String, ServiceInfo> ips = new HashMap<String, ServiceInfo>();
	

	public static HashMap<String, ServiceInfo> getListOfDynamixBridges()
	{
		return ips;
	}
	
	public void serviceAdded(ServiceEvent arg0) 
	{
		System.out.println(parseDiscovery(arg0, true));
		//System.out.println("-");
		//System.out.println("->"+arg0.getInfo().getInet4Address().getHostAddress());
		//System.out.println("-");
		//System.out.println("->"+arg0.getInfo().getPort());
		//System.out.println("-");	
	}

	public void serviceRemoved(ServiceEvent arg0) 
	{
		System.out.println("removed");
		System.out.println("->"+arg0.getInfo().getName());
		System.out.println(parseDiscovery(arg0, false));
		// TODO Auto-generated method stub
		
	}

	public void serviceResolved(ServiceEvent arg0) 
	{
		System.out.println("resolved");
		System.out.println("->"+arg0.getInfo().getName());
		System.out.println(parseDiscovery(arg0, true));
		// TODO Auto-generated method stub
		
	}
	
	private String parseDiscovery(ServiceEvent event, boolean add)
	{
		System.out.println("parsediscovery->");
		String ipport = "";
		if(event.getInfo().getName().contains("DynamixBridge"))
		{
			System.out.println("->bridge");
			try 
			{
				System.out.println("-->"+event.getSource());
				Object source = event.getSource();
				String sourcetext = source.toString();
				StringTokenizer tk1 = new StringTokenizer(sourcetext, "\n");
				while(tk1.hasMoreTokens())
				{
					String line = tk1.nextToken();
					if(line.contains("name: 'DynamixBridge") && line.contains("._http._tcp.local.' address: '/"))
					{
						System.out.println("----"+line);
						StringTokenizer tk2 = new StringTokenizer(line, "'/");
						while(tk2.hasMoreTokens())
						{
							String line2 = tk2.nextToken().trim();
				 			System.out.println("--------"+line2);
							if(line2.equals("address:"))
							{
								ipport = tk2.nextToken();
								StringTokenizer tk3 = new StringTokenizer(ipport, ":");
								ipport = tk3.nextToken();
								System.out.println("IP:Port="+ipport);
								if(add)
								{
									boolean contained=false;
									Set<String> keys = ips.keySet();
									for(String key: keys)
									{
										if(key.equals(ipport))
										{
											contained=true;
										}
									}
									if(!contained)
									{
										ips.put(ipport, event.getInfo());
									}
								}
								else
								{
									Set<String> keys = ips.keySet();
									for(String key: keys)
									{
										if(key.equals(ipport))
										{
											deleteIP(ipport);
										}
									}
								}
							}
						}
					}
				}
			} 
			catch (Exception e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return ""+event.getInfo().getName()+" "+ipport;
	}

	public static void deleteIP(String ip)
	{
		System.out.println("delete"+ip);
		int x=-1;
		Set<String> keys = ips.keySet();
		for(String key: keys)
		{
			if(key.startsWith(ip))
			{
				ServiceInfo arg0 = ips.get(key);
				JmDNS jmdns;
				try 
				{
					jmdns = JmDNS.create();
					jmdns.unregisterService(arg0);
					jmdns.close();
				} 
				catch (IOException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ips.remove(key);
				break;
			}
		}
	}
	
}
