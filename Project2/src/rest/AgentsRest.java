package rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;

import managers.AgentManagerLocal;
import model.AID;
import model.AgentType;
import proba.AgentTypesLocal;
import proba.NodeLocal;
import proba.RunningAgentsLocal;

@Path("/agents")
@Stateless
public class AgentsRest {

	
	@EJB
	private AgentTypesLocal agentTypesBean;
	
	@EJB
	private RunningAgentsLocal runningAgentsBean;
	
	@EJB
	private NodeLocal nodeBean;
	
	@EJB
	private AgentManagerLocal agentManagerBean;
	
	@GET
	@Path("/classes")
	@Produces(MediaType.APPLICATION_JSON)
	public List<AgentType> getAvailibleClasses(){
		return agentTypesBean.getAllClasses();
	}
	
	@POST
	@Path("/classes")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void putNewTypes(HashMap<String, List<AgentType>> map){
		
		Iterator<Entry<String, List<AgentType>>> it = map.entrySet().iterator();
	    while (it.hasNext()) {
	        @SuppressWarnings("rawtypes")
			Map.Entry pair = (Map.Entry)it.next();
	        String s=(String) pair.getKey();
	        System.out.println("Dodajemo nove tipove od strane "+s);
	        @SuppressWarnings("unchecked")
			ArrayList<AgentType> value = (ArrayList<AgentType>) pair.getValue();
			ArrayList<AgentType> list=value;
	        agentTypesBean.addClasses(s, list);
	        it.remove(); // avoids a ConcurrentModificationException
	    }
	}
	
	
	@POST
	@Path("/deleteClasses")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void removeNewTypes(String s){
		
		agentTypesBean.removeClasses(s);
	}
	
	@PUT
	@Path("/running/{type}/{name}")
	@Produces(MediaType.TEXT_PLAIN)
	public String runAgent(@PathParam("type") String tip, @PathParam("name") String name){
		AgentType type = new Gson().fromJson(tip, AgentType.class);
		if(agentManagerBean.runAgent(type,name)==null)
			return "postoji agent sa istim imenom";
		return "all clear";
	}
	
	
	@POST
	@Path("/running")
	public void addRunningAgents(List<AID> runningAgents){
		runningAgentsBean.addRunningAgents(runningAgents);
	}
	
	@GET
	@Path("/running")
	public ArrayList<AID> getAllRunningAgents(){
		//System.out.println(runningAgentsBean.getAllRunningAgents().size());
		return (ArrayList<AID>) runningAgentsBean.getAllRunningAgents();
	}
	
	
	@DELETE
	@Path("/running/{aid}")
	public void deleteRunningAgent(@PathParam("aid") String string){
		
		AID aid=new Gson().fromJson(string, AID.class);
		agentManagerBean.stopAgent(aid);
		
	}
	
	@POST
	@Path("/running/deleteRunning")
	public void removeRemoteRunningAgent(List<AID> list){
		runningAgentsBean.removeRunningAgents(list);
	}
	
	
	
	
	
	
}
