import java.util.List;

import jade.core.Agent;

public class EnsembleCommun {
	protected List<Tache> taches;
	//protected List<Agent> agents;
	protected List<String> agents;

	

	public EnsembleCommun(List<Tache> taches, List<String> agents) {
		super();
		this.taches = taches;
		this.agents = agents;
	}

	public EnsembleCommun() {
		super();
	}

	public List<Tache> getTaches() {
		return taches;
	}

	public void setTaches(List<Tache> taches) {
		this.taches = taches;
	}

	public List<String> getAgents() {
		return agents;
	}

	public void setAgents(List<String> agents) {
		this.agents = agents;
	}
	
	
	
	
	
	
}
