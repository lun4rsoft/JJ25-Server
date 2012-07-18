package info.lun4rsoft.jj25.server.main;

import info.lun4rsoft.jj25.net.PackageFactory;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class UDPListenerThread extends Thread {
	
	/**
	 * Message prefix. Every class using postmessage() must declare this.
	 */
	private static final String MSG_PREFIX = "[UDPL]";
	//Port to listen on
	private static final int listport = 10052; 
	
	private boolean running;
	
	//Copy of the servers settings.
	private boolean settingsChanged;
	private ServerSettings settings;
	
	public UDPListenerThread()
	{
		super();
		running = true;
		settings = new ServerSettings();
	}
	
	public void run()
	{
		DatagramSocket listenSocket;
		
		postMessage("Starting to listen for UDP on port "+listport);
		try {
			listenSocket = new DatagramSocket(listport);
			listenSocket.setSoTimeout(1000);
		} catch (SocketException e) {
			postError("Error listening for incoming UDP on port "+listport);
			return;
		}
		
		while (isRunning())
		{
			//Check if the socket closed
			if (listenSocket.isClosed())
			{
				postError("Socket closed...");
				return;
			}
			
			//Listen for a packet
			try {
				//Create a datagramPacket, and the underlying buffer
				byte buffer[] = new byte[512];
				DatagramPacket p = new DatagramPacket(buffer, buffer.length);
				//try to read
				listenSocket.receive(p);
				
				//Time to take actions according to the packet. the 3rd byte of the buffer (index=2) holds the packet ID.
				
				/*
				 * 0x03 : PING
				 * 
				 * A client is checking if it can connect to us. We should reply with
				 * a 0x04 Pong-packet.
				 */
				
				if (buffer[2] == 0x03) 
				{
					postMessage("DEBUG: Ping from \""+p.getAddress()+"\"");
					byte pong[] =PackageFactory.makePongData(buffer[3], buffer[4], buffer[5], buffer[6], buffer[7],(byte) getSettings().getGamemode24().ordinal());
					listenSocket.send(new DatagramPacket(pong, pong.length, p.getAddress(), p.getPort()));
				} else
				
				/*
				 * 0x05 : QUERY
				 * 
				 * A client is requesting info on our server to display it in it's GIP list.
				 * We should reply by sending a 0x06 Query Reply.
				 */
				if (buffer[2] == 0x05)
				{
					postMessage("DEBUG: Query from \""+p.getAddress()+"\"");
					byte qreply[] = PackageFactory.makeQueryReplyData(buffer[3], 0, settings.getVersion(),(byte) settings.getPlayers(),(byte) settings.getMaxplayers(), (byte) getSettings().getGamemode24().ordinal(), settings.getName());
					listenSocket.send(new DatagramPacket(qreply, qreply.length, p.getAddress(), p.getPort()));
				}
				
			} catch (Exception e) {
				if (e instanceof SocketTimeoutException)
				{
					//Socket timed out, go to the next iteration of the loop.
					
				} else {
					postError("Error while waiting for UDP data...");
					e.printStackTrace();
					return;
				}
			}
			
		}
		
		postMessage("Stopping UDPListenerThread.");
		
	}

	public synchronized boolean isRunning() {
		return running;
	}

	public synchronized void setRunning(boolean running) {
		this.running = running;
	}
	
	public synchronized boolean isSettingsChanged() {
		return settingsChanged;
	}

	public synchronized void setSettingsChanged(boolean settingsChanged) {
		this.settingsChanged = settingsChanged;
	}

	public synchronized ServerSettings getSettings() {
		return settings;
	}

	public synchronized void setSettings(ServerSettings settings) {
		this.settings = settings;
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
