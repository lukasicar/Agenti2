package managers;

import model.AID;
import model.Agent;
import model.AgentType;

public interface AgentManager {

	AID runAgent(AgentType agentType, String name);

    void stopAgent(AID aid);

    Agent getAgent(AID aid);
	
}
