import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jade.core.Agent;

public class Alternative implements Serializable{
	
	private String intitule;
	private List<Tache> taches;
	//private Agent agent;
	private String agent;
	
	public Alternative() {
		super();
	}
	

	public Alternative(String intitule, List<Tache> taches, String agent) {
		super();
		this.intitule = intitule;
		this.taches = taches;
		this.agent = agent;
	}



	public String getIntitule() {
		return intitule;
	}

	public void setIntitule(String intitule) {
		this.intitule = intitule;
	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}


	public List<Tache> getTaches() {
		return taches;
	}

	public void setTaches(List<Tache> taches) {
		this.taches = taches;
	}
	
	public ArrayList<Tache> getTachesCommunes(ArrayList<Alternative> alternatives){
		
		ArrayList<Tache> tachescommunes= new ArrayList<Tache>();
		
		for (Tache t: this.getTaches()) {
			for (Alternative alt : alternatives) {
				for (Tache t0 : alt.getTaches()) {
					if (t.getIntitule() == t0.getIntitule()) {
					tachescommunes.add(t);
				}
			}
			}
		}
		
		return tachescommunes;
	}
	
	public ArrayList<EnsembleCommun> getEnsemblesCommuns(ArrayList<Tache> tachescommunes,ArrayList<Alternative> alternatives){
	
		ArrayList<EnsembleCommun> ecs= new ArrayList<EnsembleCommun>();
		
		for (Tache t : tachescommunes) {
			ArrayList<String> agents = new ArrayList<String>();
			ArrayList<Tache> taches = new ArrayList<Tache>();
			for (Alternative a : alternatives) {
				for (Tache ts : a.getTaches()) {
					if (ts.getIntitule() == t.getIntitule()) {
						agents.add(a.getAgent());
						taches.add(ts);
				}
					
			}
				
		}
		agents.add(this.getAgent());
		ecs.add(new EnsembleCommun(taches,agents));
	}
		
		return ecs;
	}
	
	public ArrayList<EnsembleCommun> getStructureCoalition(ArrayList<EnsembleCommun> ecs){
		ArrayList<EnsembleCommun> ec= new ArrayList<EnsembleCommun>();
		
		for (EnsembleCommun e : ecs) {
			for (EnsembleCommun e1 : ecs) {
			//if (!(e.getTaches().equals(e1.getTaches())) && e.getAgents().equals(e1.getAgents())){
				if (e.getAgents().equals(e1.getAgents())) {
				ArrayList<Tache> ts = new ArrayList<Tache>();
				for (Tache t : e.getTaches()) {
					ts.add(t);
				}
				for (Tache t : e1.getTaches()) {
					ts.add(t);
				}
				
				ec.add(new EnsembleCommun(ts,e.getAgents()));
				}
			}
			
		}
		
		return ec;

	}
	
	public Double getCout(EnsembleCommun ecs){
		Double coutTotal = 0.;
		for (Tache t :ecs.getTaches()) {
			coutTotal = coutTotal + ( t.getCoutPublic()/ecs.getAgents().size() + t.getCoutPrive() );
		}
		
		return coutTotal;
	}
	
}
