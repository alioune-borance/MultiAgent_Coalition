import java.io.Serializable;
import java.util.List;

import jade.core.Agent;

public class Coalition implements Serializable{
	//private List<Agent> agents;
	private List<String> agents;
	private List<Tache> taches;
	
	public Coalition() {}
	
	public Coalition(List<String> agents,List<Tache> taches) {
		this.agents = agents;
		this.taches = taches;
	}

	public List<String> getAgents() {
		return agents;
	}

	public void setAgents(List<String> agents) {
		this.agents = agents;
	}

	public List<Tache> getTaches() {
		return taches;
	}

	public void setTaches(List<Tache> taches) {
		this.taches = taches;
	}
	
}
