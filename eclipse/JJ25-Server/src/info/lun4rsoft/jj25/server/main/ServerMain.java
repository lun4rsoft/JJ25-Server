package info.lun4rsoft.jj25.server.main;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Scanner;

public class ServerMain {

	/**
	 * Message prefix. Every class using postmessage() must declare this.
	 */
	private static final String MSG_PREFIX = "[MAIN]";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		/*=======================================
		== Set up variables
		========================================*/
		
		//Is running? set to false to close server.
		boolean running = true;
		
		//Try to get the path to the applications directory
		String applicationDirectory = "";
		try {
			applicationDirectory = URLDecoder.decode(ClassLoader.getSystemClassLoader().getResource(".").getPath(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			postError("Path to application directory has an unsupported encoding...");
			return;
		}
		if (applicationDirectory.equals(""))
		{
			postError("Can't find local application directory...");
			return;
		}
		
		
		
		
		/*=======================================
		== Set up objects
		========================================*/
		
		
		
		//Scanner to read commands
		Scanner scan = new Scanner(System.in);
		
		
		
		//Mainloop
		while (running)
		{
			
		}
		
		postMessage("main loop finished; freeing/saving/deinitializing stuff...");
		

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
