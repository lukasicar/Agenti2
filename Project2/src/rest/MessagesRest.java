package rest;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

import messages.MessagesLocal;
import model.ACLMessage;

@Path("/messages")
@Stateless
public class MessagesRest {

	@EJB
	private MessagesLocal messagesBean;
	
	@GET
	public List<String> getPerformatives(){
		return messagesBean.getPerformatives();
	}
	
	@POST
	public void sendMessage(ACLMessage message){
		messagesBean.sendMessage(message);
	}
	
	@PUT
	public void receiveMessage(ACLMessage message){
		messagesBean.receiveMessage(message);
	}
	
}
