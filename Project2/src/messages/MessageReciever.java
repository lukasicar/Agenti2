package messages;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import model.ACLMessage;
import model.AID;
import model.Agent;
import proba.RunningAgentsLocal;

import javax.ejb.MessageDriven;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;

@MessageDriven(
        activationConfig = {
                @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
                @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
                @ActivationConfigProperty(propertyName = "destination", propertyValue = "jms/queue/ExpiryQueue")
        }
)
public class MessageReciever implements MessageListener{
	
	@EJB
	private RunningAgentsLocal runningAgentsBean;

	@Override
	public void onMessage(Message message) {
		// TODO Auto-generated method stub
		if (message instanceof ObjectMessage) {
            try {
                Object object = ((ObjectMessage) message).getObject();
                if (object instanceof ACLMessage) {
                    ACLMessage aclMessage = ((ACLMessage) object);
                    for(AID a : aclMessage.getReceivers()){
                    	Agent agent=runningAgentsBean.getLocalAgent(a);
                    	agent.handleMessage(aclMessage);
                    }
                }
            } catch (JMSException e) {
                e.printStackTrace();
            } catch (Exception e) {
                System.err.println("onMessage Exception: " + e.getMessage());
            }

        }
	}

}
