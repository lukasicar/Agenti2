package proba;

import java.util.ArrayList;
import java.util.Collections;
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

import model.AID;
import model.Agent;

@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Singleton
public class RunningAgentsBean implements RunningAgentsLocal{
	
	
	private Map<AID, Agent> localAgents = new HashMap<>();

    private Set<AID> allAgents = new HashSet<>();

	@Override
	public Map<AID, Agent> getLocalRunningAgents() {
		// TODO Auto-generated method stub
		return localAgents;
	}

	@Override
	@Lock(LockType.READ)
	public List<AID> getAllRunningAgents() {
		// TODO Auto-generated method stub
		return new ArrayList<>(allAgents);
	}

	@Override
	public Agent getLocalAgent(AID aid) {
		// TODO Auto-generated method stub
		return localAgents.get(aid);
	}

	@Override
	@Lock(LockType.WRITE)
	public void addLocalAgent(Agent agent) {
		// TODO Auto-generated method stub
		localAgents.put(agent.getId(), agent);
        addRunningAgents(Collections.singletonList(agent.getId()));
	}

	@Override
	@Lock(LockType.WRITE)
	public void removeLocalAgent(AID aid) {
		// TODO Auto-generated method stub
		localAgents.remove(aid);
		//System.out.println(allAgents.contains(aid));
        removeRunningAgents(Collections.singletonList(aid));
	}

	@Override
	@Lock(LockType.READ)
	public boolean containsAgent(AID aid) {
		// TODO Auto-generated method stub
		
		return allAgents.contains(aid);
	}

	@Override
	@Lock(LockType.WRITE)
	public void addRunningAgents(List<AID> agents) {
		// TODO Auto-generated method stub
		allAgents.addAll(agents);
	}

	@Override
	@Lock(LockType.WRITE)
	public void removeRunningAgents(List<AID> agents) {
		// TODO Auto-generated method stub
		//System.out.println("stvarno brise listu");
		//System.out.println(allAgents.contains(agents));
		for(AID a : agents)
			allAgents.remove(a);
	}

	@Override
	@Lock(LockType.WRITE)
	public void removeRunningAgentsFromNode(String alias) {
		// TODO Auto-generated method stub
		List<AID> removeList = new ArrayList<>();
		//System.out.println("neko mi brise");
        allAgents.forEach(aid -> {
            if (aid.getHost().getAddress().equals(alias)) {
                removeList.add(aid);
            }
        });
        removeRunningAgents(removeList);
	}

}
