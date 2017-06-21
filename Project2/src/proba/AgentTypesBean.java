package proba;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;

import model.AgentType;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class AgentTypesBean implements AgentTypesLocal{

	private HashMap<String, List<AgentType>> allTypes = new HashMap<>();
	
	@Lock(LockType.WRITE)
	@Override
	public void addClasses(String address, List<AgentType> types) {
		// TODO Auto-generated method stub
		if(allTypes.containsKey(address)){
			allTypes.get(address).addAll(types);
		}else{
			allTypes.put(address, types);
		}
	}

	@Override
	public List<AgentType> getAllClasses() {
		Set<AgentType> set = new HashSet<>();
        allTypes.forEach((address, list) -> set.addAll(list));
        return new ArrayList<>(set);	
	}

	@Override
	public HashMap<String, List<AgentType>> getAllTypes() {
		// TODO Auto-generated method stub
		return allTypes;
	}

	@Override
	public HashMap<String, ArrayList<AgentType>> getSpecificTypes(String string) {
		// TODO Auto-generated method stub
		HashMap<String, ArrayList<AgentType>> hash=new HashMap<>();
		hash.put(string, (ArrayList<AgentType>) allTypes.get(string));
		return hash;
	}

	@Override
	public void removeClasses(String address) {
		// TODO Auto-generated method stub
		allTypes.remove(address);
	}
	
	@Lock(LockType.READ)
    @Override
    public String findAgentCenter(AgentType agentType) {
        for (Map.Entry<String, List<AgentType>> entry : allTypes.entrySet()) {
            for (AgentType type : entry.getValue()) {
                if (agentType.equals(type)) {
                    return entry.getKey();
                }
            }
        }
		return null;
    }
	
	
}
