package messages;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import model.ACLMessage;
import proba.NodeLocal;
@Stateless
public class MessagesBean implements MessagesLocal,MessagesRemote{

	
	@Resource(mappedName = "java:/ConnectionFactory")
    private ConnectionFactory connectionFactory;

    @Resource(mappedName = "java:/jms/queue/ExpiryQueue")
    private Queue queue;
    
    @EJB
    private NodeLocal nodeBean;
	
	
	@Override
	public void sendMessage(ACLMessage message) {
		// TODO Auto-generated method stub
		sendJmsMessage(message);
		message.getReceivers().forEach(receiver -> {
	           if (receiver != null && !nodeBean.getNode().equals(receiver.getHost())) {
	               receiveMessage(receiver.getHost().getAddress(),message);
	           }
	    });
	}

	@Override
	public List<String> getPerformatives() {
		// TODO Auto-generated method stub
		List<String> performatives = new ArrayList<>();
        for (ACLMessage.Performative performative : ACLMessage.Performative.values()) {
            performatives.add(performative.name());
        }
        return performatives;
	}

	@Override
	public void receiveMessage(ACLMessage message) {
		// TODO Auto-generated method stub
		sendJmsMessage(message);
	}
	
	
	private void sendJmsMessage(ACLMessage message) {
        try {
            Connection connection = connectionFactory.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer producer = session.createProducer(queue);
            ObjectMessage objectMessage = session.createObjectMessage();
            objectMessage.setObject(message);
            producer.send(objectMessage);
            producer.close();
            session.close();
            connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
	
	
	public void receiveMessage(String address,ACLMessage message){
    	String url = String.format("http://%s//Project2/rest/messages", address);
    	ResteasyClient client = new ResteasyClientBuilder().build();
    	ResteasyWebTarget target = client.target(url);
    	target.request().put(Entity.entity(message, MediaType.APPLICATION_JSON));
    	
	}

}
