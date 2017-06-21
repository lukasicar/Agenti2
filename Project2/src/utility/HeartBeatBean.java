package utility;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import model.AgentCenter;
import proba.NodeLocal;

@Singleton
public class HeartBeatBean {

    @EJB
    private NodeLocal nodeBean;
    /*@EJB
    private ConfigurationLocal configurationBean;*/

    private Set<AgentCenter> preDestroy = new HashSet<>();



    //@Schedule(hour = "*", minute = "*", second = "*/15", info = "every 45th second")
    public void isAlive() {

        List<AgentCenter> destroy = new ArrayList<>();
        System.out.println("malo toga sejvam samo gudranje");
        

        // Check if nodes are alive
        nodeBean.getRegisteredCenters().forEach(node -> {
            if (nodeBean.getNode().equals(node)) {
                return;
            }
            System.out.println("provjeravamo");
            try {
                isAlive(node.getAddress());
                preDestroy.remove(node);
            } catch (Exception e) {
                if (preDestroy.contains(node)) {
                    preDestroy.remove(node);
                    destroy.add(node);
                    System.err.println("Removing node " + node.getAlias());
                } else {
                    System.err.println("First warning for " + node.getAlias());
                    preDestroy.add(node);
                }
            }
        });



        // Notify all nodes
        destroy.forEach(node -> nodeBean.removeNode(node.getAddress()));
        nodeBean.getRegisteredCenters().forEach(node -> {
            
            destroy.forEach(removeNode -> {
                try {
                    unregister(node.getAddress(), removeNode);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });

    }
    
    public void isAlive(String address){
		String url = String.format("http://%s/Project2/rest/agents/node", address);
    	ResteasyClient client = new ResteasyClientBuilder().build();
    	ResteasyWebTarget target = client.target(url);
    	Response response=target.request(MediaType.APPLICATION_JSON).get();
    	//return response.readEntity(new GenericType<ArrayList<AgentType>>(){});
	}
    
    public void unregister(String address,AgentCenter agentCenter){
    	String url = String.format("http://%s/Project2/rest/center/delete", address);
    	ResteasyClient client = new ResteasyClientBuilder().build();
    	ResteasyWebTarget target = client.target(url);
    	target.request().post(Entity.entity(agentCenter, MediaType.APPLICATION_JSON));
 }

}