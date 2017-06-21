package proba;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import model.AgentCenter;
import model.AgentType;

@Singleton
@Startup
public class StartUpBean implements StartUpLocal{

	private static final int DEFAULT_PORT = 8080;

    private static final String MASTER_ADDRESS_KEY = "master";

    private static final String LOCAL_ADDRESS_KEY = "local";

    private static final String ALIAS_KEY = "alias";

    private static final String PORT_OFFSET_KEY = "jboss.socket.binding.port-offset";


    @EJB
    private NodeLocal nodeBean;
    
    @EJB
    private AgentTypesLocal agentTypesBean;
    
    private String masterAddress;

    private String alias;

    private AgentCenter agc;

    @PostConstruct
    public void postConstruct() {

        masterAddress = System.getProperty(MASTER_ADDRESS_KEY);

        if (masterAddress == null) {

            System.out.println("This is master node!");

        } else {

            System.out.println("This is slave node! Master node is at: " + masterAddress);

            nodeBean.setMasterAddress(masterAddress);

        }



        // Get local IP address

        String localAddress = System.getProperty(LOCAL_ADDRESS_KEY);

        if (localAddress == null) {

            try {

                String portOffset = System.getProperty(PORT_OFFSET_KEY);

                int port = DEFAULT_PORT + (portOffset != null ? Integer.parseInt(portOffset) : 0);

                //InetAddress local = InetAddress.getLocalHost();

                localAddress = "127.0.0.1" + ":" + port;



            } catch (NumberFormatException e) {

                System.err.println("Cannot parse port offset.");

            }

        }

        System.out.println("Local IPv4 Address: " + localAddress);



        // Get alias

        alias = System.getProperty(ALIAS_KEY);

        if (alias == null) {

            try {

                InetAddress local = InetAddress.getLocalHost();

                alias = local.getHostName();

            } catch (UnknownHostException e) {

                System.err.println("Can't read Host Name.");

            }

        }

        System.out.println("Host Name: " + alias);



        AgentCenter agentCenter = new AgentCenter(localAddress, alias);
        agc=agentCenter;
        nodeBean.setNode(agentCenter);
        
        
        ArrayList<AgentType> lista=new ArrayList<>();
        lista.add(new AgentType("Ping","agents"));
        lista.add(new AgentType("Pong","agents"));
        lista.add(new AgentType("TestPingPong","agents"));
        lista.add(new AgentType("ContractNetMaster","agents")); 
        lista.add(new AgentType("ContractNetSlave","agents")); 
        lista.add(new AgentType("TestContractNet","agents")); 
        lista.add(new AgentType("MapReduceMaster","agents")); 
        lista.add(new AgentType("MapReduceSlave","agents")); 
        lista.add(new AgentType("TestMapReduce","agents")); 
        agentTypesBean.addClasses(localAddress, lista);
        
/*
        nodesDbBean.setLocal(agentCenter);

        agentClassesBean.addClasses(alias, AgentsReader.getAgentsList());


*/
        if (!nodeBean.isMaster()) {
        	new Thread(() -> {

                try {

                    Thread.sleep(2000);

                } catch (InterruptedException e) {

                    e.printStackTrace();

                }
            
             regiterNode(masterAddress,agentCenter);
        	}).start();
        }
       

    }



    @PreDestroy
    public void preDestroy() {
    	System.out.println("destroyy stary up bean");
        if (masterAddress != null) {
            unregister(masterAddress, agc);
        }
    }
    
    
    
    public void regiterNode(String address,AgentCenter agentCenter){
    	String url = String.format("http://%s/Project2/rest/center/node", address);
    	ResteasyClient client = new ResteasyClientBuilder().build();
    	ResteasyWebTarget target = client.target(url);
    	target.request().post(Entity.entity(agentCenter, MediaType.APPLICATION_JSON));
    	

    }
    
    
    public void unregister(String address,AgentCenter agentCenter){
    	String url = String.format("http://%s/Project2/rest/center/delete", address);
    	ResteasyClient client = new ResteasyClientBuilder().build();
    	ResteasyWebTarget target = client.target(url);
    	target.request().post(Entity.entity(agentCenter, MediaType.APPLICATION_JSON));
    }
	
    
}
