package proba;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Local;

import model.AgentType;

@Local
public interface AgentTypesLocal {

	void addClasses(String address, List<AgentType> types);
	
	List<AgentType> getAllClasses();
	
	HashMap<String, List<AgentType>> getAllTypes();
	
	HashMap<String, ArrayList<AgentType>> getSpecificTypes(String string);
	
	void removeClasses(String address);

	String findAgentCenter(AgentType agentType);
	
}
