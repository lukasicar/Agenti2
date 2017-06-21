package rest;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/test")
public class TestRest {
	
	@GET
	@Path("/get")
	public String get(){
		try {
			InetAddress adresa=InetAddress.getLocalHost();
			return adresa.getHostAddress();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "fsafas";
	}
	
}
