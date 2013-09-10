package de.lukeslog.discoverytest.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Background implements Runnable 
{
	static boolean run=true;
	
	public Background()
	{
		run();
	}
	
	public void run() 
	{
		run=true;
		while(run)
		{
			//TODO:
			ArrayList<String> currentips = DiscoverServices.ips;
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
			System.out.println(homedir);
			for(int i=0; i<currentips.size(); i++)
			{
				//TODO: Request stuff from the phone
				//TODO: Store what exists right now (some configuration, background wallpaper, log in data or whatever
				//TODO: Change to settings from the phone
				//TODO: If the device drops out, delete the preferences again. 
				System.out.println(currentips.get(i));
			}
			try 
			{
				Thread.sleep(1000);
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

}
