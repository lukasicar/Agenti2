package utility;

import javax.naming.NamingException;

import managers.AgentManagerBean;
import managers.AgentManagerRemote;
import managers.LogBean;
import managers.LogRemote;
import messages.MessagesBean;
import messages.MessagesRemote;



public class ManagerFactory {
    private static final String PREFIX = "ejb:/Project2/";
    private static final String LOG_MANAGER_NAME =
            PREFIX + LogBean.class.getSimpleName() + "!" + LogRemote.class.getName();
    private static final String AGENT_MANAGER_NAME =
            PREFIX + AgentManagerBean.class.getSimpleName() + "!" + AgentManagerRemote.class.getName();
    private static final String MESSAGES_MANAGER_NAME =
            PREFIX + MessagesBean.class.getSimpleName() + "!" + MessagesRemote.class.getName();
    public static LogRemote getLogManager() {
        return ManagerFactory.<LogRemote>contextLookup(LOG_MANAGER_NAME);
    }
    public static AgentManagerRemote getAgentManager() {
        return ManagerFactory.<AgentManagerRemote>contextLookup(AGENT_MANAGER_NAME);
    }
    public static MessagesRemote getMessagesManager() {
        return ManagerFactory.<MessagesRemote>contextLookup(MESSAGES_MANAGER_NAME);
    }
    @SuppressWarnings("unchecked")
    private static <T> T contextLookup(String name) {
        try {
            return (T) ContextSingleton.getContext().lookup(name);
        } catch (NamingException ex) {
            throw new IllegalStateException("Failed to lookup " + name, ex);
        }
    }
}