package model;

import java.io.Serializable;

public class AID implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private AgentType type;
	private AgentCenter host;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public AgentType getType() {
		return type;
	}
	public void setType(AgentType type) {
		this.type = type;
	}
	public AgentCenter getHost() {
		return host;
	}
	public void setHost(AgentCenter host) {
		this.host = host;
	}
	public AID(String name, AgentType type, AgentCenter host) {
		super();
		this.name = name;
		this.type = type;
		this.host = host;
	}
	
	public AID(){
		
	}
	
	@Override
    public boolean equals(Object o) {

        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;
        AID aid = (AID) o;
        return name != null ? name.equals(aid.name) : aid.name == null

                && (host != null ? host.equals(aid.host) : aid.host == null);



    }
	
	@Override
    public int hashCode() {

        int result = name != null ? name.hashCode() : 0;

        result = 31 * result + (host != null ? host.hashCode() : 0);

        return result;

    }
}
