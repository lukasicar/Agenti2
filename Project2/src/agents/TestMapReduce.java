package agents;

import javax.ejb.Remote;
import javax.ejb.Stateful;

import model.ACLMessage;
import model.AID;
import model.Agent;
import model.AgentType;
import utility.AgentInterface;

@Stateful
@Remote(AgentInterface.class)
public class TestMapReduce extends Agent {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    protected void handleRequest(ACLMessage message) {
        getLogBean().info("Starting MapReduce Example...");
        
        // Start master
        AgentType mrmAt = new AgentType("MapReduceMaster","agents");
        AID masterAid = getAgentManager().runAgent(mrmAt, "CoolerMaster");

        // Send initial message
        ACLMessage msg = new ACLMessage();
        msg.setPerformative(ACLMessage.Performative.REQUEST);
        msg.getReceivers().add(masterAid);
        msg.setContent(message.getContent());
        getMessagesBean().sendMessage(msg);
    }
}