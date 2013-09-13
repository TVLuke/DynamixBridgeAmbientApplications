package de.lukeslog.discoverytest.main;

import java.io.IOException;

import javax.jmdns.JmDNS;

public class MainClass 
{

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		new Thread(new Runnable() 
        {
            public void run() 
            {
            	Background bg = new Background();
            }
        }).start();
		new Thread(new Runnable() 
        {
            public void run() 
            {
            	Server s = new Server();
            }
        }).start();
		new Thread(new Runnable() 
        {
            public void run() 
            {
            	SystemTrayUpdate stu = new SystemTrayUpdate();
            }
        }).start();
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
	}

}
