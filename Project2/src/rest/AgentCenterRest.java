package rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import model.AID;
import model.AgentCenter;
import model.AgentType;
import proba.AgentTypesLocal;
import proba.NodeLocal;
import proba.RunningAgentsLocal;

@Path("/center")
@Stateless
public class AgentCenterRest {

	@EJB
	private NodeLocal nodeBean;
	
	@EJB
	private AgentTypesLocal agentsTypeBean;
	
	@EJB
	private RunningAgentsLocal runningAgentsBean;
	
	@POST
	@Path("/node")
	public void register(AgentCenter agentCenter){
		if(nodeBean.isMaster()){
			nodeBean.register(agentCenter);
			System.out.println("registrovan novi agentcenter "+agentCenter.getAddress()+" na master cvor");
			ArrayList<AgentType> lista=(ArrayList<AgentType>)getAllSupportedAgentTypes(agentCenter.getAddress());
			/*for(AgentType t : lista){
				System.out.println(t.getName());
			}*/
			agentsTypeBean.addClasses(agentCenter.getAddress(), lista);
			for(AgentCenter ac : nodeBean.getRegisteredCenters()){
				if(!(ac.getAddress().equals(agentCenter.getAddress()))){
					registerNode(ac.getAddress(), agentCenter);
					putNewAgentTypes(ac.getAddress(), agentCenter.getAddress(), lista);
				}
				else
				{
					for(AgentCenter acc : nodeBean.getRegisteredCenters()){
						if(!(acc.getAddress().equals(agentCenter.getAddress()))){
							try{
								registerNode(agentCenter.getAddress(), acc);
							}catch(Exception e){
								System.out.println("desi ose except, saljem ponovo");
								try{
									registerNode(agentCenter.getAddress(), acc);
								}catch(Exception e1){
									unregister(nodeBean.getNode().getAddress(), agentCenter);
									return;
								}
							}
						}
					}
					registerNode(agentCenter.getAddress(), nodeBean.getNode());
				}
			}
		
			
			Iterator<Entry<String, List<AgentType>>> it = agentsTypeBean.getAllTypes().entrySet().iterator();
		    while (it.hasNext()) {
		        @SuppressWarnings("rawtypes")
				Map.Entry pair = (Map.Entry)it.next();
		        String key=(String) pair.getKey();
		        @SuppressWarnings("unchecked")
				List<AgentType> value=(List<AgentType>) pair.getValue();
		        if(!(key.equals(agentCenter.getAddress()))){
		        	try{
		        		putNewAgentTypes(agentCenter.getAddress(), key, (ArrayList<AgentType>) value);
		        	}catch(Exception e){
		        		System.out.println("greska pri dodavanju klasa u novi cvor,ponovooo");
		        		try{
		        			putNewAgentTypes(agentCenter.getAddress(), key, (ArrayList<AgentType>) value);
		        		}catch(Exception e1){
		        			unregister(nodeBean.getNode().getAddress(), agentCenter);
							return;
		        		}
		        	}
		        }
		    }
		        
		        it.remove(); // avoids a ConcurrentModificationException
		    
			try{
				postRunningAgents(agentCenter.getAddress(), (ArrayList<AID>) runningAgentsBean.getAllRunningAgents());
			}catch(Exception e){
				System.out.println("exception kod dodavanja running agenata u novi agent, ponovo");
				try{
				}catch(Exception e1){
					postRunningAgents(agentCenter.getAddress(), (ArrayList<AID>) runningAgentsBean.getAllRunningAgents());
					unregister(nodeBean.getNode().getAddress(), agentCenter);
					return;
				}
			}
		}else{
			nodeBean.register(agentCenter);
			System.out.println("registrovan novi agentcenter "+agentCenter.getAddress()+" na "+nodeBean.getNode().getAddress());
		}
		
	}
	
	
	@POST
	@Path("/delete")
	public void unregister(AgentCenter agentCenter){
		nodeBean.unregister(agentCenter);
		agentsTypeBean.removeClasses(agentCenter.getAlias());
		runningAgentsBean.removeRunningAgentsFromNode(agentCenter.getAddress());
		
		System.out.println("agentcki centar "+agentCenter.getAddress()+" deregestrovan");
		if(nodeBean.isMaster()){
			for(AgentCenter ac : nodeBean.getRegisteredCenters()){
				unregister(ac.getAddress(),agentCenter);
			}	
		}
		
	}
	
	@Path("/{alias}")
    @DELETE
    void removeNode(@PathParam("alias") String alias){
		
	}
	
	@GET
	@Path("/node")
	public void isAlive(){
		
	}
	
	
	public List<AgentType> getAllSupportedAgentTypes(String address){
		String url = String.format("http://%s/Project2/rest/agents/classes", address);
    	ResteasyClient client = new ResteasyClientBuilder().build();
    	ResteasyWebTarget target = client.target(url);
    	Response response=target.request(MediaType.APPLICATION_JSON).get();
    	return response.readEntity(new GenericType<ArrayList<AgentType>>(){});
	}
	
	
	public void registerNode(String address,AgentCenter agentCenter){
    	String url = String.format("http://%s/Project2/rest/center/node", address);
    	ResteasyClient client = new ResteasyClientBuilder().build();
    	ResteasyWebTarget target = client.target(url);
    	target.request().post(Entity.entity(agentCenter, MediaType.APPLICATION_JSON));
    	

    }
	
	public void putNewAgentTypes(String address,String agentCenterName,ArrayList<AgentType> list){
		String url = String.format("http://%s/Project2/rest/agents/classes", address);
    	ResteasyClient client = new ResteasyClientBuilder().build();
    	ResteasyWebTarget target = client.target(url);
    	HashMap<String, ArrayList<AgentType>> hash=new HashMap<>();
    	hash.put(agentCenterName, list);
    	target.request().post(Entity.entity(hash, MediaType.APPLICATION_JSON));
	}
	
	 public void unregister(String address,AgentCenter agentCenter){
	    	String url = String.format("http://%s/Project2/rest/center/delete", address);
	    	ResteasyClient client = new ResteasyClientBuilder().build();
	    	ResteasyWebTarget target = client.target(url);
	    	target.request().post(Entity.entity(agentCenter, MediaType.APPLICATION_JSON));
	 }

	 
	 public void deleteAgentTypes(String address,String s){
	    	String url = String.format("http://%s/Project2/rest/agents/deleteClasses", address);
	    	ResteasyClient client = new ResteasyClientBuilder().build();
	    	ResteasyWebTarget target = client.target(url);
	    	target.request().post(Entity.entity(s, MediaType.APPLICATION_JSON));
	 }
	 
	 
	 public void postRunningAgents(String address,ArrayList<AID> runningAgents){
	    	String url = String.format("http://%s/Project2/rest/agents/running", address);
	    	ResteasyClient client = new ResteasyClientBuilder().build();
	    	ResteasyWebTarget target = client.target(url);
	    	target.request().post(Entity.entity(runningAgents, MediaType.APPLICATION_JSON));
	 }
}
