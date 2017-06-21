package socket;

import model.AgentType;

public class RunAgentDTO {

	private AgentType agentType;

    private String name;



    public RunAgentDTO() {

    }



    public RunAgentDTO(AgentType agentType, String name) {

        this.agentType = agentType;

        this.name = name;

    }



    public AgentType getAgentType() {

        return agentType;

    }



    public void setAgentType(AgentType agentType) {

        this.agentType = agentType;

    }



    public String getName() {

        return name;

    }



    public void setName(String name) {

        this.name = name;

    }
	
}
