package model;

import java.io.Serializable;

public class AgentCenter implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String alias;
	private String address;
	
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public AgentCenter(String address,String alias) {
		super();
		this.alias = alias;
		this.address = address;
	}
	
	public AgentCenter(){
		
	}
	
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AgentCenter that = (AgentCenter) o;
        return alias != null ? alias.equals(that.alias) : that.alias == null;
    }



    @Override
    public int hashCode() {
        return alias != null ? alias.hashCode() : 0;
    }
	
}
