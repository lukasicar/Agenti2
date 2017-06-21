package utility;

import model.ACLMessage;
import model.AID;


public interface AgentInterface {

	void init(AID aid);

    void handleMessage(ACLMessage msg);
	
}
