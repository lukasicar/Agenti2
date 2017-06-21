package managers;

import java.util.Collections;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import com.google.gson.Gson;

import model.AID;
import model.Agent;
import model.AgentCenter;
import model.AgentType;
import proba.AgentTypesLocal;
import proba.NodeLocal;
import proba.RunningAgentsLocal;

@Stateless
public class AgentManagerBean implements AgentManagerLocal, AgentManagerRemote {

    @EJB
    private AgentTypesLocal agentTypesBean;

    @EJB
    private RunningAgentsLocal runningAgentsBean;

    @EJB
    private NodeLocal nodeBean;
    
    
	@Override
	public AID runAgent(AgentType agentType, String name) {
		// TODO Auto-generated method stub
		Class<?> agentClass;
		try {
			agentClass = Class.forName(agentType.getModule() + "." + agentType.getName());
			Agent agent = (Agent) agentClass.newInstance();
			agent.setId(new AID(name, agentType,nodeBean.getNode()));
			
			if (runningAgentsBean.containsAgent(agent.getId())) {
				System.out.println("postoji agent sa istim imenom");
				return null;
	        }
			runningAgentsBean.addLocalAgent(agent);
			for(AgentCenter agentCenter : nodeBean.getRegisteredCenters()){
				if(!(agentCenter.getAddress().equals(nodeBean.getNode().getAddress())))
					runAgents(agentCenter.getAddress(),Collections.singletonList(agent.getId()));
			}
			return agent.getId();
			
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("nije dobar name");
			String alias = agentTypesBean.findAgentCenter(agentType);
			runRemoteAgent(nodeBean.getRemoteNode(alias).getAddress(), agentType, name);
		}
		return null;
		
	}
	@Override
	public void stopAgent(AID aid) {
		// TODO Auto-generated method stub
		runningAgentsBean.removeLocalAgent(aid);
		for(AgentCenter agentCenter : nodeBean.getRegisteredCenters()){
			if(!(agentCenter.getAddress().equals(nodeBean.getNode().getAddress()))){
				removeRemoteAgent(agentCenter.getAddress(), Collections.singletonList(aid));
			}
		}
	}
	@Override
	public Agent getAgent(AID aid) {
		// TODO Auto-generated method stub
		return runningAgentsBean.getLocalAgent(aid);
	}
	
	
	
	
	public void runAgents(String address,List<AID> runningAgents){
    	String url = String.format("http://%s/Project2/rest/agents/running", address);
    	ResteasyClient client = new ResteasyClientBuilder().build();
    	ResteasyWebTarget target = client.target(url);
    	target.request().post(Entity.entity(runningAgents, MediaType.APPLICATION_JSON));
	}
	
	public void runRemoteAgent(String address,AgentType agentType, String name){
		String string=new Gson().toJson(agentType);
    	String url = String.format("http://%s//Project2/rest/agents/running/"+string+"/"+name, address);
    	ResteasyClient client = new ResteasyClientBuilder().build();
    	ResteasyWebTarget target = client.target(url);
    	target.request().put(null);
	}
	
	public void removeRemoteAgent(String address,List<AID> aid){
    	String url = String.format("http://%s//Project2/rest/agents/running/deleteRunning", address);
    	ResteasyClient client = new ResteasyClientBuilder().build();
    	ResteasyWebTarget target = client.target(url);
    	target.request().post(Entity.entity(aid, MediaType.APPLICATION_JSON));
    	
	}
}