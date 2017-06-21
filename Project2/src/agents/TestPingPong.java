package agents;

import java.util.ArrayList;

import javax.ejb.Remote;
import javax.ejb.Stateful;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import model.ACLMessage;
import model.AID;
import model.Agent;
import model.AgentType;
import utility.AgentInterface;

@Stateful
@Remote(AgentInterface.class)
public class TestPingPong extends Agent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    protected void handleRequest(ACLMessage message) {
		getLogBean().info("Starting PingPong example...");
        // Start Ping
        AgentType atPing = new AgentType("Ping","agents");
        AID p1=getAgentManager().runAgent(atPing, "PingAgent");
        
        // Start Pong
        AgentType atPong = new AgentType("Pong","agents");
        AID p2=getAgentManager().runAgent(atPong, "PongAgent");
        
        
        ACLMessage msgToPing = new ACLMessage();
        msgToPing.setPerformative(ACLMessage.Performative.REQUEST);
        msgToPing.getReceivers().add(p1);
        msgToPing.setContent("Say hello to Pong!");
        msgToPing.setReplyTo(p2);
        getMessagesBean().sendMessage(msgToPing);
    }
	
	public void runAgents(String address,ArrayList<AID> runningAgents){
    	String url = String.format("http://%s/Project2/rest/agents/running", address);
    	ResteasyClient client = new ResteasyClientBuilder().build();
    	ResteasyWebTarget target = client.target(url);
    	target.request().post(Entity.entity(runningAgents, MediaType.APPLICATION_JSON));
	}
}
