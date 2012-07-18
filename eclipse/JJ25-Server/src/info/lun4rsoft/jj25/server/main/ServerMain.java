package info.lun4rsoft.jj25.server.main;

import info.lun4rsoft.jj25.files.FileUtils;
import info.lun4rsoft.jj25.server.list.ListServer24Settings;
import info.lun4rsoft.jj25.server.list.ListServerThread24;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ServerMain {

	
	/**
	 * Message prefix. Every class using postmessage() must declare this.
	 */
	private static final String MSG_PREFIX = "[MAIN]";
	
	//Directories
	private static String mappoolDirectory;
	private static String applicationDirectory;
	
	//Threads
	private static ListServerThread24 list24;
	private static UDPListenerThread udplisten;
	
	//Settings
	private static ServerSettings settings;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		postCredits(false);
		postMessage("main function started");
		/*=======================================
		== Set up variables
		========================================*/
		
		//Is running? set to false to close server.
		boolean running = true;
		
		//Is the server up?
		boolean up = false;
		//Should the server be listed?
		boolean listed = true;
		
		//nl = FileUtils.getLineSeparator();
		applicationDirectory = System.getProperty("user.home");
		if (applicationDirectory == null)
		{
			postError("no home directory defined...");
			return;
		}
		applicationDirectory += FileUtils.getFileSeparator()+".Jazz25";
		
		
		
		//Try to get the path to the applications directory
		postMessage("Locating mappool directory...");
		mappoolDirectory = "";
		
		try {
			mappoolDirectory = URLDecoder.decode(ClassLoader.getSystemClassLoader().getResource(".").getPath(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			postError("Path to application directory has an unsupported encoding...");
			return;
		}
		if (mappoolDirectory.equals(""))
		{
			postError("Can't find local mappool directory...");
			return;
		}
		postMessage("- \""+mappoolDirectory+"\"");
		
		
		
		
		/*=======================================
		== Set up objects
		========================================*/
		
		
		
		//Scanner to read commands
		Scanner scan = new Scanner(System.in);
		
		//Settings
		settings = new ServerSettings();
		settings.setPlayers(4);
		
		
		
		
		//Mainloop
		postMessage("Mainloop started!");
		while (running)
		{
			
			//Wait for a command:
			String command = scan.nextLine();
			String arguments[] = command.split(" ");
			
			//Close server
			if (command.equalsIgnoreCase("exit") || command.equalsIgnoreCase("quit"))
			{
				running = false;
				postMessage("Exit command received.");
				
				//Close Listserver Threads
				if(list24!=null)
				{
					list24.setRunning(false);
				}
				
				//Close UDPListener
				if(udplisten!=null)
				{
					udplisten.setRunning(false);
				}
			}
			
			//Post server status
			if (command.equalsIgnoreCase("status"))
			{
				postStatus();
			}
			
			//Start server
			if (command.equalsIgnoreCase("start"))
			{
				if (!up)
				{
					up = true;
					postMessage("Starting server...");
					if (listed)
					{
						relist();
					}
					
					//Start threads:
					//-UDPListenerThread:
					RestartUDPListener();
					
					
				} else {
					postMessage("Server is already running.");
				}
			}
			
			//relist
			if (command.equalsIgnoreCase("relist"))
			{
				relist();
				listed = true;
			}
			
			//delist
			if (command.equalsIgnoreCase("delist"))
			{
				listed = false;
				delist();
			}
			
			
			
		}
		postMessage("main loop finished; free-ing/saving/deinitializing stuff...");
		
		
		
		postMessage("end of main.");
	}
	
	
	private static void relist()
	{
		if (list24 != null)
		{
			postMessage("Stopping old listserver24 thread...");
			list24.setRunning(false);
			while (list24.isAlive())
			{
				try {
					Thread.sleep(0);
				} catch (InterruptedException e) {
					// Do nothing...
				}
			}
		}
		
		postMessage("Starting new listserver24 thread...");
		list24 = new ListServerThread24();
		//Options & stuff
		ListServer24Settings s = new ListServer24Settings();
		s.setName(settings.getName());
		s.setPlayers(settings.getPlayers());
		s.setMaxplayers(settings.getMaxplayers());
		s.setVersion(settings.getVersion());
		s.setGamemode(settings.getGamemode24());
		try {
			s.setHost(InetAddress.getByName("list2.digiex.net"));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		list24.setSettings(s);
		//Run it!
		list24.start();
	}
	
	private static void delist()
	{
		if (list24 != null)
		{
			postMessage("Stopping old listserver24 thread...");
			list24.setRunning(false);
			while (list24.isAlive())
			{
				try {
					Thread.sleep(0);
				} catch (InterruptedException e) {
					// Do nothing...
				}
			}
			postMessage("Listserver24 Thread stopped.");
		} else {
			postMessage("No listserver thread running...");
		}
	}
	
	private static void RestartUDPListener()
	{
		if (udplisten !=null)
		{
			//Stop old thread
			postMessage("Stopping old UDPListenerThread...");
			udplisten.setRunning(false);
			while (udplisten.isAlive())
			{
				try {
					Thread.sleep(0);
				} catch (InterruptedException e) {
					// Do nothing...
				}
			}
		}
		postMessage("Starting new UDPListenerThread.");
		udplisten = new UDPListenerThread();
		udplisten.setSettings(settings);
		udplisten.start();
	}
	
	private static void postMessage(String message)
	{
		System.out.println(MSG_PREFIX+" "+message);
	}
	
	private static void postError(String message)
	{
		System.out.println(MSG_PREFIX+".[ERR] "+message);
	}
	
	private static void postStatus()
	{
		System.out.println("***************************************");
		System.out.println("**     S E R V E R   S T A T U S     **");
		System.out.println("***************************************");
		System.out.println("- Players: 0/0");
		System.out.println("- Blah.j2l");
		System.out.println("***************************************");
		System.out.println("- jj2 dir: \""+mappoolDirectory+"\"");
		System.out.println("- bin dir: \""+applicationDirectory+"\"");
		//System.out.println(System.getProperty("user.home"));
	}
	
	private static void postCredits(boolean full)
	{
		if (!full)
		{
			System.out.println("***************************************");
			System.out.println("**  Jazz25 Dedicated Server [main]   **");
			System.out.println("**     -     -     -     -     -     **");
			System.out.println("** version 0.0.1.1 D [1.25d] [DBG]  **");
			System.out.println("** by wKtK         http://jj25.info/ **");
			System.out.println("***************************************");
			System.out.println("** WARNING:                          **");
			System.out.println("**  This is a very early debug       **");
			System.out.println("**  version. Use AT YOUR OWN RISK!   **");
			System.out.println("**  Everything here subject to change**");
			System.out.println("***************************************");
			System.out.println();
			System.out.println("Enough ranting, let's start this thing up!");
			System.out.println();
		}
	}

}
