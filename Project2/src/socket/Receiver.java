package socket;

import java.io.IOException;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Singleton;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.google.gson.Gson;
import com.oracle.xmlns.internal.webservices.jaxws_databinding.WebParamMode;

import managers.AgentManagerLocal;
import messages.MessagesLocal;
import model.ACLMessage;
import model.AID;
import proba.AgentTypesLocal;
import proba.RunningAgentsLocal;

@ServerEndpoint("/socket")
@Singleton
public class Receiver {

	@EJB
    private AgentTypesLocal agentTypesBean;

    @EJB
    private RunningAgentsLocal runningAgentsBean;

    @EJB
    private MessagesLocal messagesBean;

    //@EJB
    //private SessionsDbLocal sessionsBean;

    @EJB
    private AgentManagerLocal agentManagerBean;



    @OnMessage
    public void onMessage(String message, Session session) {
        if (!session.isOpen()) {
            return;
        }

        System.out.println("WebSocketEndpoint receiver message:" + message);
        WebSocketMessage webSocketMessage = new Gson().fromJson(message, WebSocketMessage.class);
        try {
            switch (webSocketMessage.getType()) {
                case getClasses:
                    handleGetClasses(session);
                    break;
                case getRunning:
                    handleGetRunning(session);
                    break;
                case runAgent:
                    handleRunAgent(webSocketMessage.getData(), session);
                    break;
                case stopAgent:
                    handleStopAgent(webSocketMessage.getData(),session);
                    break;
                case sendMessage:
                    handleSendMessage(webSocketMessage.getData(), session);
                    break;
                case getPerformatives:
                    handleGetPerformatives(session);
                    break;
                default:
                    System.err.println("Unknown WebSocket type: " + webSocketMessage.getType());
            }
        } catch (IOException e) {
            System.err.println("WebSocket Exception: " + e.getMessage());
        }

    }



    @OnError
    public void onError(Throwable error) {
        System.err.println("onError: " + error.getMessage());
    }

    @OnOpen
    public void onOpen(Session session) {
    	//System.out.println("jebes mu misa");
    	System.out.println("Socket opened");
        //sessionsBean.addSession(session);
    }

    @OnClose
    public void onClose(Session session) {
    	System.out.println("zatvara session");
        //sessionsBean.removeSession(session);
    }
 
    private void handleGetClasses(Session session) throws IOException {
        createAndSendPackage(session, WebSocketMessage.Type.getClasses, agentTypesBean.getAllClasses());
    }

    private void handleGetRunning(Session session) throws IOException {
        createAndSendPackage(session, WebSocketMessage.Type.getRunning, runningAgentsBean.getAllRunningAgents());
    }

    private void handleRunAgent(String data, Session session) throws IOException {
        String errorMessage = null;
        RunAgentDTO request = new Gson().fromJson(data, RunAgentDTO.class);
        try {
            AID aid = agentManagerBean.runAgent(request.getAgentType(), request.getName());
            createAndSendPackage(session, WebSocketMessage.Type.runAgent, aid);
        } catch (EJBException e) {
            errorMessage=e.getLocalizedMessage();
        }
        createAndSendPackage(session, WebSocketMessage.Type.runAgent, null, false);
    }

    private void handleStopAgent(String data,Session session) throws IOException {
        AID aid = new Gson().fromJson(data, AID.class);
        agentManagerBean.stopAgent(aid);
        createAndSendPackage(session,WebSocketMessage.Type.stopAgent,"");
    }

    private void handleSendMessage(String data, Session session) throws IOException {
        messagesBean.sendMessage(new Gson().fromJson(data, ACLMessage.class));
        createAndSendPackage(session, WebSocketMessage.Type.sendMessage, "");
    }

    private void handleGetPerformatives(Session session) throws IOException {
        createAndSendPackage(session, WebSocketMessage.Type.getPerformatives, messagesBean.getPerformatives());
    }

    private void createAndSendPackage(Session session, WebSocketMessage.Type type, Object object) throws IOException {
        createAndSendPackage(session, type, object, true);
    }

    private void createAndSendPackage(Session session, WebSocketMessage.Type type, Object object, boolean success)
            throws IOException {
        WebSocketMessage webSocketMessage = new WebSocketMessage(type, object, success);
        session.getBasicRemote().sendText(new Gson().toJson(webSocketMessage));
    }
	
}
