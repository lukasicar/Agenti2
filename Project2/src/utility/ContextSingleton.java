package utility;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public abstract class ContextSingleton {

    private static Context context;

    private static Context initContext() {
        try {
            Hashtable<String, Object> jndiProps = new Hashtable<>();
            jndiProps.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            //RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
            //List<String> arguments = runtimeMxBean.getInputArguments();
            return new InitialContext(jndiProps);
        } catch (NamingException e) {
            System.err.println("Context initialization error." + e.getMessage());
            return null;
        }
    }
    public static Context getContext() {
        if (context == null) {
            context = initContext();
        }
        return context;
    }
}