package agents;

import javax.ejb.Remote;
import javax.ejb.Stateful;

import model.ACLMessage;
import model.AID;
import model.Agent;
import utility.AgentInterface;

@Stateful
@Remote(AgentInterface.class)
public class Ping extends Agent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	
	@Override
    protected void handleRequest(ACLMessage message) {
        getLogBean().info("Request to Ping: " + message.getContent());
        AID pongAid = message.getReplyTo();
        ACLMessage msgToPong = new ACLMessage();
        msgToPong.setPerformative(ACLMessage.Performative.REQUEST);
        msgToPong.setSender(id);
        msgToPong.getReceivers().add(pongAid);
        msgToPong.setContent("Hello Pong!");
        getMessagesBean().sendMessage(msgToPong);
    }
	
    @Override
    protected void handleInform(ACLMessage message) {
    	getLogBean().info("Inform to Ping: " + message.getContent());
    	getLogBean().info("Ping received INFORM from Pong: " + message.getContent());
    }
	

}
