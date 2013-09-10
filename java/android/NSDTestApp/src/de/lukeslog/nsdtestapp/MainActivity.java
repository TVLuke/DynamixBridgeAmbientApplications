package de.lukeslog.nsdtestapp;

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
