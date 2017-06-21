package proba;

import java.util.List;
import java.util.Map;

import javax.ejb.Local;

import model.AID;
import model.Agent;

@Local
public interface RunningAgentsLocal {

	Map<AID, Agent> getLocalRunningAgents();



    List<AID> getAllRunningAgents();



    Agent getLocalAgent(AID aid);



    void addLocalAgent(Agent agent);



    void removeLocalAgent(AID aid);



    boolean containsAgent(AID aid);



    void addRunningAgents(List<AID> agents);



    void removeRunningAgents(List<AID> agents);



    void removeRunningAgentsFromNode(String alias);
	
}
