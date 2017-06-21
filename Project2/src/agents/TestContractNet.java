package agents;

import javax.ejb.Remote;
import javax.ejb.Stateful;

import managers.AgentManagerRemote;
import model.ACLMessage;
import model.AID;
import model.Agent;
import model.AgentType;
import utility.AgentInterface;
import utility.ManagerFactory;

@Stateful
@Remote(AgentInterface.class)
public class TestContractNet extends Agent {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override

    protected void handleRequest(ACLMessage message) {

        getLogBean().info("Starting ContractNetProtocol Example...");

        AgentManagerRemote agm = ManagerFactory.getAgentManager();

        // starting master

        AgentType cnmAt = new AgentType("ContractNetMaster","agents");

        AID masterAid = agm.runAgent(cnmAt, "ContractNetMasterCooler");


        // sending initial message
        ACLMessage msg = new ACLMessage();
        msg.setPerformative(ACLMessage.Performative.REQUEST);
        msg.getReceivers().add(masterAid);
        msg.setContent("Start!");
        getMessagesBean().sendMessage(msg);

    }

}