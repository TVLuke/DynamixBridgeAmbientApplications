package de.lukeslog.discoverytest.main;

import java.awt.AWTException;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Label;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.jmdns.ServiceInfo;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 * Mostly based on http://docs.oracle.com/javase/tutorial/uiswing/examples/misc/TrayIconDemoProject/src/misc/TrayIconDemo.java
 * 
 * Help from http://stackoverflow.com/questions/573679/open-resource-with-relative-path-in-java
 * and http://stackoverflow.com/questions/12287137/system-tray-icon-looks-distorted
 * and http://www.mathematik.uni-marburg.de/~gumm/Buch/8.Auflage/progs/kap08/TCPVerbAlternativ/TCP1/TCP1.java
 * 
 * @author lukas
 *
 */
public class SystemTrayUpdate 
{

	SystemTrayUpdate()
	{
		final Frame frame = new Frame("");
	    frame.setUndecorated(true);
		if (!SystemTray.isSupported()) 
		{
            System.out.println("SystemTray is not supported");
            return;
        }
		else
		{
			System.out.println("System Tray is Supported");
		}
        final TrayIcon trayIcon = createImage("/images/bridge_icon.png", "Bridge");
        final SystemTray tray = SystemTray.getSystemTray();
        try 
        {
            tray.add(trayIcon);
        } 
        catch (AWTException e) 
        {
            System.out.println("TrayIcon could not be added.");
        }
        trayIcon.addActionListener(new ActionListener() 
        {
        	public void actionPerformed(ActionEvent e) 
        	{
        		showAboutBox();
        	}
        });
	}
	
    //Obtain the image URL
    protected static TrayIcon createImage(String path, String description) 
    {
    	URL imageURL = SystemTrayUpdate.class.getResource(path);
    	BufferedImage trayIconImage;
		try 
		{
	        if (imageURL == null) 
	        {
	            System.err.println("Resource not found: " + path);
	            return null;
	        } 
	        else
	        {
				trayIconImage = ImageIO.read(imageURL);
				int trayIconWidth = new TrayIcon(trayIconImage).getSize().width;
		    	TrayIcon trayIcon = new TrayIcon(trayIconImage.getScaledInstance(trayIconWidth, -1, Image.SCALE_SMOOTH));
		    	return trayIcon;
	        }
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }
    
	void showAboutBox()
	{
		final Frame 	f 	= new Frame("About Box");
	
		f.addWindowListener(new WindowAdapter() {
		    public void windowClosing(WindowEvent e) {
				f.dispose();
			}
		});
		    

		f.setLayout	(new GridLayout(0, 1));
	
		f.add(new Label(" ", Label.CENTER));
		f.add(new Label("http://localhost:53486/bridges", Label.LEFT));
		f.add(new Label("Bridges:", Label.LEFT));
		HashMap<String, ServiceInfo> list = DiscoverServices.getListOfDynamixBridges();
		for(String key: list.keySet())
		{
			f.add(new Label(">"+key, Label.LEFT));
		}
		f.add(new Label(" ", Label.CENTER));
		
		f.pack(); 
		f.setVisible(true);
	}
}
