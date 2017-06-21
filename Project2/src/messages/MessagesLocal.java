package messages;

import java.util.List;

import javax.ejb.Local;

import model.ACLMessage;

@Local
public interface MessagesLocal extends MessagesRemote{

	List<String> getPerformatives();

    void receiveMessage(ACLMessage message);
	
}
