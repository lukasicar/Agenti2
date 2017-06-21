package agents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.Remote;
import javax.ejb.Stateful;

import model.ACLMessage;
import model.AID;
import model.Agent;
import model.AgentType;
import utility.AgentInterface;

@Stateful
@Remote(AgentInterface.class)
public class MapReduceMaster extends Agent{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String DEFAULT_DOCUMENTS[] = {"text1.txt", "text2.txt", "text3.txt"};

    private int numberOfSlaves;

    private int delivered;

    private String[] documents;

    private Map<Character, Integer> mapReduce = new HashMap<>();



    @Override
    protected void handleRequest(ACLMessage message) {
    	
        init(message.getContent());
        getLogBean().info("Request to MapReduceMaster: " + message.getContent());


        // Start agents
        ArrayList<AID> slaveAids = new ArrayList<>();

        for (int i = 0; i < numberOfSlaves; i++) {
            String slaveName = "MapReduceSlave" + i;
            AgentType slaveAt = new AgentType("MapReduceSlave","agents");
            AID slaveAid = getAgentManager().runAgent(slaveAt, slaveName);
            slaveAids.add(slaveAid);
        }



        // Send messages
        for (int i = 0; i < numberOfSlaves; i++) {
            ACLMessage msg = new ACLMessage();
            msg.setPerformative(ACLMessage.Performative.REQUEST);
            msg.setSender(id);
            msg.getReceivers().add(slaveAids.get(i));
            msg.setContent(documents[i]);
            getMessagesBean().sendMessage(msg);
        }
    }
    
    @Override
    protected void handleInform(ACLMessage message) {
        delivered++;
        String senderName = message.getSender().getName();
        getLogBean().info("Inform to MapReduceMaster from " + senderName + ": " + message.getContent());

        parseResponse(message.getContent());

        if (delivered == numberOfSlaves) {
            getLogBean().info("Total statistics: " + formStatistics());
        }
    }

    private void init(String content) {
        documents = (content == null || content.length() == 0) ? DEFAULT_DOCUMENTS : content.split(",");
        numberOfSlaves = documents.length;
        delivered = 0;
        mapReduce.clear();
    }



    private void parseResponse(String input) {
        String splits[] = input.split("\n");
        for (int i = 1; i < splits.length; i++) {
            Character c = splits[i].charAt(0);
            int count = Integer.parseInt(splits[i].split(":")[1]);

            if (mapReduce.containsKey(c)) {
                mapReduce.put(c, mapReduce.get(c) + count);
            } else {
                mapReduce.put(c, count);
            }
        }
    }



    private String formStatistics() {
        String retVal = "MapReduce:";
        for (Map.Entry<Character, Integer> entry : mapReduce.entrySet()) {
            retVal += "\n" + entry.getKey() + ":" + entry.getValue();
        }
        return retVal;
    }
}