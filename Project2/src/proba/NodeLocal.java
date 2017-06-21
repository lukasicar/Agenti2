package proba;

import java.util.List;

import javax.ejb.Local;

import model.AgentCenter;

@Local
public interface NodeLocal {
	
	public boolean isMaster();
	
	public void setMasterAddress(String masterAddress);
	
	public String getMasterAddress();
	
	public void setNode(AgentCenter agentCenter);
	
	public AgentCenter getNode();
	
	public void register(AgentCenter agentCenter);
	
	public void unregister(AgentCenter agentCenter);
	
	public List<AgentCenter> getRegisteredCenters();

	public AgentCenter getRemoteNode(String string);

	public void removeNode(String alias);
}
