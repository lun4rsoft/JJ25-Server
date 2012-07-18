package info.lun4rsoft.jj25.server.main;

import info.lun4rsoft.jj25.net.ListServer24GameMode;

public class ServerSettings {
	
	private String name;
	private String version;
	
	private int players;
	private int maxplayers;
	
	private ListServer24GameMode gamemode24;
	
	
	
	public ServerSettings()
	{
		name = "||Jazz25 Debug Server";
		version = "25da";
		
		players = 0;
		maxplayers = 8;
		
		gamemode24 = ListServer24GameMode.Pub_Unknown;
	}



	public synchronized String getName() {
		return name;
	}



	public synchronized void setName(String name) {
		this.name = name;
	}



	public synchronized String getVersion() {
		return version;
	}



	public synchronized void setVersion(String version) {
		this.version = version;
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



	public synchronized ListServer24GameMode getGamemode24() {
		return gamemode24;
	}



	public synchronized void setGamemode24(ListServer24GameMode gamemode24) {
		this.gamemode24 = gamemode24;
	}
	
}
