package info.lun4rsoft.jj25.server.client;

public enum JJ2ClientStatus {
Disconnected, //Client not active, or disconnected. ignore this client
Connecting, //Connecting. Show client in lists, but dont bother much.
Downloading, //Downloading. Client can receive chat and stuff.
Active,	//Active, ingame.
Spectating, //Spectating.
CTOing, //nub is Timing out... stop sending junk.
}
