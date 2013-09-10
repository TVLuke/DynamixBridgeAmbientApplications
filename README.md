DynamixBridgeAmbientApplications
================================

Examples of simple Ambient Applications that use the DynamixBridge to empower ambient actuation.

##Bridge Discovery

The DynamixBridge enables wireless dicovery using the Android NDS (Network Service Discovery) avaliable for Android 4.1+. 

On an Android Device, service discover can be done using build-in functions

```Java
import java.net.InetAddress;

import android.net.nsd.NsdManager;
import android.net.nsd.NsdManager.DiscoveryListener;
import android.net.nsd.NsdManager.ResolveListener;
import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity 
{

	DiscoveryListener mDiscoveryListener;
	ResolveListener mResolveListener;
	NsdServiceInfo mService;
	NsdManager mNsdManager;
	public static String TAG = "NSDTEST";
	public static String SERVICE_TYPE ="_http._tcp.";
	String mServiceName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mNsdManager = (NsdManager) this.getSystemService(Context.NSD_SERVICE);
		
		initializeResolveListener();
		
		initializeDiscoveryListener();
		
		mNsdManager.discoverServices(SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, mDiscoveryListener);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void initializeDiscoveryListener() 
	{

	    // Instantiate a new DiscoveryListener
	   mDiscoveryListener = new NsdManager.DiscoveryListener() 
	   {

	        //  Called as soon as service discovery begins.
	        @Override
	        public void onDiscoveryStarted(String regType) 
	        {
	            Log.d(TAG, "Service discovery started");
	        }

	        @Override
	        public void onServiceFound(NsdServiceInfo service) 
	        {
	            // A service was found!  Do something with it.
	            Log.d(TAG, "Service discovery success" + service);
	            if (!service.getServiceType().equals(SERVICE_TYPE)) 
	            {
	                // Service type is the string containing the protocol and
	                // transport layer for this service.
	                Log.d(TAG, "Unknown Service Type: " + service.getServiceType());
	            } 
	            else if (service.getServiceName().equals(mServiceName)) 
	            {
	                // The name of the service tells the user what they'd be
	                // connecting to. It could be "Bob's Chat App".
	                Log.d(TAG, "Same machine: " + mServiceName);
	            } 
	            else if (service.getServiceName().contains("DynamixBridge"))
	            {
	            	Log.e(TAG, "O YEAA");
					mNsdManager.resolveService(service, mResolveListener);
	            }
	        }

	        @Override
	        public void onServiceLost(NsdServiceInfo service) 
	        {
	            // When the network service is no longer available.
	            // Internal bookkeeping code goes here.
	            Log.e(TAG, "service lost" + service);
	        }

	        @Override
	        public void onDiscoveryStopped(String serviceType) 
	        {
	            Log.i(TAG, "Discovery stopped: " + serviceType);
	        }

	        @Override
	        public void onStartDiscoveryFailed(String serviceType, int errorCode) 
	        {
	            Log.e(TAG, "Discovery failed: Error code:" + errorCode);
	            mNsdManager.stopServiceDiscovery(this);
	        }

	        @Override
	        public void onStopDiscoveryFailed(String serviceType, int errorCode) 
	        {
	            Log.e(TAG, "Discovery failed: Error code:" + errorCode);
	            mNsdManager.stopServiceDiscovery(this);
	        }
	    };
	}
	
	public void initializeResolveListener() 
	{
	    mResolveListener = new NsdManager.ResolveListener()
	    {

	        @Override
	        public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) 
	        {
	            // Called when the resolve fails.  Use the error code to debug.
	            Log.e(TAG, "Resolve failed" + errorCode);
	        }

	        @Override
	        public void onServiceResolved(NsdServiceInfo serviceInfo) 
	        {
	            Log.e(TAG, "Resolve Succeeded. " + serviceInfo);

	            if (serviceInfo.getServiceName().equals(mServiceName)) 
	            {
	                Log.d(TAG, "Same IP.");
	                return;
	            }
	            mService = serviceInfo;
	            int port = mService.getPort();
	            Log.d(TAG, "Port="+port);
	            InetAddress host = mService.getHost();
	            Log.d(TAG, "Adresse"+host.getHostAddress());
	        }
	    };
	}
}
```  
(This code is from the java/android/NSDTest project.)

On a Java Application running on Desktop or other devices with network acces, using the [jmDNS-library](http://sourceforge.net/projects/jmdns/) one can discover the DnymixBridge on the same network.

```Java
try 
		{
			final JmDNS jmdns = JmDNS.create();
			jmdns.addServiceListener("_http._tcp.local.", new DiscoverServices());
			System.out.println("Press q and Enter, to quit");
            int b;
            while ((b = System.in.read()) != -1 && (char) b != 'q') 
            {
                /* Stub */
            }
            jmdns.close();
            Background.stop();
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
``` 
(This Code is from the java/java/ServiceDicoveryTest project)

The DiscoverServices class implements the ServiceListener. Using several methods to be called when a service is docovered.

```Java
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
```
(This Code is from the java/java/ServiceDicoveryTest project)

##Interacting With the DynamixBridge
When the Bridge has been discovered one can use the IP to interact with the Bridge. The HTTP-service is found on Port 8081, the CoaP-service on port 5683. Currently these ports are not flexible.

###Finding avaliavbel context types

### Getting Data

### Using Actuators

Sending a HTTP-POST or a CoaP-POST request can be used to send a configured request to dynamix via the Bridge, this may be done like this

```Java
HttpURLConnection conn = (HttpURLConnection) uri.toURL().openConnection();
conn.setDoOutput(true);
conn.setRequestMethod("POST");
conn.addRequestProperty("format", "xml");
Random generator = new Random();
int rr = generator.nextInt(255);
int gg = generator.nextInt(255);
int bb = generator.nextInt(255);
System.out.println(rr+" "+gg+" "+bb);
String payload="String action_type=setcolor;;String r_channel="+rr+";;String g_channel="+gg+";;String b_channel="+bb+"";
conn.getOutputStream().write(payload.getBytes());
new InputStreamReader((conn.getInputStream()));
								 
String output;
System.out.println("Output from Server .... \n");
conn.disconnect();
```
(This Code is from the java/java/Lightswitch project)