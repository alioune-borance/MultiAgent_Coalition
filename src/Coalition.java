import java.io.Serializable;
import java.util.List;

import jade.core.Agent;

public class Coalition implements Serializable{
	//private List<Agent> agents;
	private List<String> agents;
	private List<String> taches;
	
	public Coalition() {}
	
	public Coalition(List<String> agents,List<String> taches) {
		this.agents = agents;
		this.taches = taches;
	}

	public List<String> getAgents() {
		return agents;
	}

	public void setAgents(List<String> agents) {
		this.agents = agents;
	}

	public List<String> getTaches() {
		return taches;
	}

	public void setTaches(List<String> taches) {
		this.taches = taches;
	}
	
}
