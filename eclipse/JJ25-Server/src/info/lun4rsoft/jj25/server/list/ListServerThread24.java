package info.lun4rsoft.jj25.server.list;

import info.lun4rsoft.jj25.net.PackageFactory;
import info.lun4rsoft.jj25.utils.DataConv;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class ListServerThread24 extends Thread {
	
	/**
	 * Message prefix. Every class using postmessage() must declare this.
	 */
	private static final String MSG_PREFIX = "[LIST24]";
	
	private ListServer24Settings settings;
	
	private boolean running;
	//true if data needs to be resent.
	private boolean changed;
	

	public ListServerThread24()
	{
		super();
		running = true;
		changed = true;
	}
	
	public void run()
	{
		Socket s;
		OutputStream str_out;
		
		InetAddress server = settings.getHost();
		int port = settings.getPort();
		
		//Connect
		try {
			s = new Socket(server, port);
			str_out = s.getOutputStream();
		} catch (IOException e) {
			postError("Can't connect to \""+server+":"+port+"\"...");
			return;
		}
		
		postMessage("Connected to \""+server+":"+port+"\" with JJ24_listserver protocol!");
		
		while (running)
		{
			if (isChanged())
			{
				byte data[] = PackageFactory.makeListServer24Data(settings.getName(), settings.getVersion(), settings.getMaxplayers(), settings.getPlayers(), settings.getGamemode().ordinal());
				try {
					str_out.write(data, 0, data.length);
				} catch (IOException e) {
					// Error occured...
					postError("Error sending data to listserver. Make sure data is correct and relist.");
					return;
				}
				postMessage("Sent data to listserver: ");
				System.out.println(">"+DataConv.BytesValueToString(data, 0, data.length));
			
				setChanged(false);
			}
		}
		postMessage("Closing connection to listserver...");
		try {
			s.close();
		} catch (IOException e) {
			// error disconnecting
			postMessage("Error disconnecting from listserver");
		}
		
		postMessage("Disconnected, closing thread");
		
	}
	
	public synchronized ListServer24Settings getSettings() {
		return settings;
	}

	public synchronized void setSettings(ListServer24Settings settings) {
		this.settings = settings;
	}

	public synchronized boolean isChanged() {
		return changed;
	}

	public synchronized void setChanged(boolean changed) {
		this.changed = changed;
	}

	public synchronized boolean isRunning() {
		return running;
	}

	public synchronized void setRunning(boolean running) {
		this.running = running;
	}
	
	private static void postMessage(String message)
	{
		System.out.println(MSG_PREFIX+" "+message);
	}
	
	private static void postError(String message)
	{
		System.out.println(MSG_PREFIX+".[ERR] "+message);
	}
	
	
}
