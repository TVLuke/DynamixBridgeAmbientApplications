package de.lukeslog.discoverytest.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceListener;

public class DiscoverServices implements ServiceListener
{
	public static ArrayList<String> ips = new ArrayList<String>();

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
		// TODO Auto-generated method stub
		
	}
	
	private String parseDiscovery(ServiceEvent event, boolean add)
	{
		String ipport = "";
		if(event.getInfo().getName().equals("DynamixBridge"))
		{
			try 
			{
				//System.out.println("-->"+arg0.getSource());
				Object source = event.getSource();
				String sourcetext = source.toString();
				StringTokenizer tk1 = new StringTokenizer(sourcetext, "\n");
				while(tk1.hasMoreTokens())
				{
					String line = tk1.nextToken();
					if(line.contains("name: 'DynamixBridge._http._tcp.local.' address: '/"))
					{
			//			System.out.println("----"+line);
						StringTokenizer tk2 = new StringTokenizer(line, "'/");
						while(tk2.hasMoreTokens())
						{
							String line2 = tk2.nextToken().trim();
				//			System.out.println("--------"+line2);
							if(line2.equals("address:"))
							{
								ipport = tk2.nextToken();
					//			System.out.println("IP:Port="+ipport);
								if(add)
								{
									boolean contained=false;
									for(int i=0; i<ips.size(); i++)
									{
										if(ips.get(i).equals(ipport))
										{
											contained=true;
										}
									}
									if(!contained)
									{
										ips.add(ipport);
									}
								}
								else
								{
									for(int i=ips.size()-1; i>=0; i--)
									{
										if(ips.get(i).equals(ipport))
										{
											ips.remove(i);
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

	
}
