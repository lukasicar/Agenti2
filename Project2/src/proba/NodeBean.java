package proba;

import java.util.ArrayList;
import java.util.HashMap;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;

import model.AgentCenter;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class NodeBean implements NodeLocal{

	private HashMap<String, AgentCenter> agentCenters=new HashMap<>();
	private AgentCenter node;
	private String masterAdress;
	
	
	@Override
	public boolean isMaster() {
		// TODO Auto-generated method stub
		return masterAdress==null;
	}

	
	@Override
	public void setMasterAddress(String masterAddress) {
		// TODO Auto-generated method stub
		this.masterAdress=masterAddress;
	}


	@Override
	public void setNode(AgentCenter agentCenter) {
		// TODO Auto-generated method stub
		this.node=agentCenter;
	}

	@Lock(LockType.WRITE)
	@Override
	public void register(AgentCenter agentCenter) {
		// TODO Auto-generated method stub
		agentCenters.put(agentCenter.getAddress(), agentCenter);
	}

	@Lock(LockType.READ)
	@Override
	public String getMasterAddress() {
		// TODO Auto-generated method stub
		return masterAdress;
	}

	@Lock(LockType.WRITE)
	@Override
	public void unregister(AgentCenter agentCenter) {
		// TODO Auto-generated method stub
		agentCenters.remove(agentCenter.getAddress());
	}

	@Lock(LockType.READ)
	@Override
	public AgentCenter getNode() {
		// TODO Auto-generated method stub
		return node;
	}

	@Lock(LockType.READ)
	@Override
	public ArrayList<AgentCenter> getRegisteredCenters() {
		// TODO Auto-generated method stub
		return new ArrayList<AgentCenter>(agentCenters.values());
	}
	
	@Lock(LockType.READ)
	@Override
	public AgentCenter getRemoteNode(String string) {
		// TODO Auto-generated method stub
		return agentCenters.get(string);
	}


	@Lock(LockType.WRITE)
	@Override
	public void removeNode(String alias) {
		// TODO Auto-generated method stub
		agentCenters.remove(alias);
	}
	
	
	
}
