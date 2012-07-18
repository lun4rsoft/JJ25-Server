package info.lun4rsoft.jj25.server.list;

import info.lun4rsoft.jj25.net.ListServer24GameMode;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ListServer24Settings {
	private InetAddress host;
	private int port;
	
	private int players;
	private int maxplayers;
	
	private String version;
	private ListServer24GameMode gamemode;
	
	private String name;
	
	
	public ListServer24Settings()
	{
		try {
			host = InetAddress.getByName("94.23.157.50");
		} catch (UnknownHostException e) {
			// nothing...
		}
		
		port = 10054;
		players = 0;
		maxplayers = 8;
		
		version = "25da";
		gamemode = ListServer24GameMode.Pub_Unknown;
	
		name = "|Server";
	}
	
	
	public synchronized InetAddress getHost() {
		return host;
	}
	public synchronized void setHost(InetAddress host) {
		this.host = host;
	}
	public synchronized int getPort() {
		return port;
	}
	public synchronized void setPort(int port) {
		this.port = port;
	}
	public synchronized int getPlayers() {
		return players;
	}
	public synchronized void setPlayers(int players) {
		this.players = players;
	}
	public synchronized int getMaxplayers() {
		return maxplayers;
	}
	public synchronized void setMaxplayers(int maxplayers) {
		this.maxplayers = maxplayers;
	}
	public synchronized String getVersion() {
		return version;
	}
	public synchronized void setVersion(String version) {
		this.version = version;
	}
	public synchronized ListServer24GameMode getGamemode() {
		return gamemode;
	}
	public synchronized void setGamemode(ListServer24GameMode gamemode) {
		this.gamemode = gamemode;
	}


	public synchronized String getName() {
		return name;
	}


	public synchronized void setName(String name) {
		this.name = name;
	}
}
