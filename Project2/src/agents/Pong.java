package agents;

import javax.ejb.Remote;
import javax.ejb.Stateful;

import model.ACLMessage;
import model.Agent;
import utility.AgentInterface;

@Stateful
@Remote(AgentInterface.class)
public class Pong extends Agent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	
	@Override

    protected void handleRequest(ACLMessage message) {

        getLogBean().info("Request to Pong: " + message.getContent());
        ACLMessage reply = new ACLMessage();
        reply.setPerformative(ACLMessage.Performative.INFORM);
        reply.getReceivers().add(message.getSender());
        reply.setContent("Hello Ping!");
        getMessagesBean().sendMessage(reply);

    }
	

}
