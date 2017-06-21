package agents;

import java.util.Random;

import javax.ejb.Remote;
import javax.ejb.Stateful;

import model.ACLMessage;
import model.Agent;
import utility.AgentInterface;

@Stateful
@Remote(AgentInterface.class)
public class ContractNetSlave extends Agent {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;



	@Override
    protected void handleCFP(ACLMessage message) {
		
        String receiverName = id.getName();
        getLogBean().info("Call For Proposal to " + receiverName + ": " + message.getContent());



        // checking if want to answer
        Random random = new Random();
        int percentage = random.nextInt(100);


        ACLMessage reply = new ACLMessage();
        reply.setSender(id);
        reply.getReceivers().add(message.getSender());

        if (percentage < 50) {
            // reject
            reply.setPerformative(ACLMessage.Performative.REFUSE);
            reply.setContent("I don't want to participate!");
        } else {
            // accept
            int bid = random.nextInt(1000);
            reply.setPerformative(ACLMessage.Performative.PROPOSE);
            reply.setContent(Integer.toString(bid));
        }

        getMessagesBean().sendMessage(reply);
    }



    @Override
    protected void handleAcceptProposal(ACLMessage message) {
        String receiverName = id.getName();
        getLogBean().info("Accept Proposal to " + receiverName + ": " + message.getContent());
        Random random = new Random();
        int percentage = random.nextInt(100);
        ACLMessage reply = new ACLMessage();
        reply.setSender(id);

        reply.getReceivers().add(message.getSender());

        if (percentage < 10) {
            // failure
            reply.setPerformative(ACLMessage.Performative.FAILURE);
            reply.setContent("Failed!");
        } else {
            // inform
            reply.setPerformative(ACLMessage.Performative.INFORM);
            reply.setContent("Completed!");
        }

        getMessagesBean().sendMessage(reply);
    }


    @Override
    protected void handleRejectProposal(ACLMessage message) {
        String receiverName = id.getName();
        getLogBean().info("Reject Proposal to " + receiverName + ": " + message.getContent());
    }
}