import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import jade.core.Agent;

/*
 * -> t.coutPublic : coût public de la tâche t, connu de tous les individus 
 * -> t.coutPrive : coût privé de la tâche t, n’est connu que par l’individu ai
 * 
 * -> t.coutIndividuelTache = t.coutPublic + t.coutPrive : coût individuel d'une tâche effectuée sans coopération 
 * -> alt.coutMinIndividuel : coût de référence utilisé pour calculer l'utilité estimée d'une alternative
 * 
 * -> alt.coutIndividuel = somme(coutIndividuelTache), pour chaque t dans taches : coût individuel d'une alternative effectuée sans coopération
 * 
 * -> alt.coutEstime = somme(t.coutPub/n + t.coutPriv), pour chaque t dans taches : coût estimé de l'alternative
 * 		- coût d'une alternative effectuée en coopération
 * 		- n : nombre d'agent effectuant la tâche conjointement
 * 
 * -> alt.utiliteEstime = alt.coutMinIndividuel - alt.coutEstime : utilité d'une alternative pour un agent donné
 * 
 * -> alt.coutReservation = alt.coutMinIndividuel * agent.coutLimite : 
 * 		qu’il ne peut pas dépasser durant les négociations, cout de reservation pour une alternative ?
 * 		- coutMinIndividuel : fixé par l'agent
 *		- coutLimite : fixé par l'agent, compris entre [0;1]
 * 
 * -> agent.utiliteReference = alt.coutMinIndividuel - agent.coutReservation
 * 
 * Enchainement :
 * 		- Avant d'entamer les négociations, chaque agent doit calculer l'utilité de chacune de ses alternatives
 * 		- Chaque agent calcul : 
 * 			. son cout de réservation, qu'il ne peut pas dépasser durant les négociations
 * 			. son utilité de référence
 * 		- Négociations
 * 		- L'agent considère uniquement les alternatives respéctant les conditions suivantes:
 * 			. ( alt.coutEstime < agent.coutReservation ) & ( alt.utiliteEstime > agent.utiliteReference)
 * 		- Avant d'entamer le premier cycle de négociations, l'agent choisit l'alternative ayant alt.utiliteEstime
 * 		  la plus importante, pour soumettre sa proposition.
 * 		  Premier cycle de négociations : ordonnancement des alternatives sur la base de leurs utilités estimées.
 * 		- Second cycle de négociations (cas général) : ordonnancement des alternatives sur la base de leur désirabilité.
 * 
 */

// alternative appartenant à l'agent
// coalitions autour d'un groupe de tache et pas de l'alternative
/* Quand un agent B recçoit une proposition de formation de coalition de la part d'un agent A,
	sur un groupe de tâches à effectuer en commun, l'agent B reconsidère une alternative qu'il avait déjà
	en recalculant les coûts publics qu'il va partager avec l'agent A.
	=> L'agent B recalcule l'utilité estimé de l'alternative.
*/


public class Alternative implements Serializable{
	
	private String intitule;
	private List<Tache> taches;
	private Agents agentObj;
	private String agent;
	
	private double coutIndividuel;
	private double coutEstime;
	private double coutMinIndividuel; //fixe, définit par l'agent ?
	private double utiliteEstime;
	
	
	private double coutReservation; 
	private double utiliteReference;
	
	
	public Alternative() {
		super();
	}
	
	public Alternative(String intitule, List<Tache> taches, String agent) { //coutMinIndividuel, coutLimite
		super();
		this.intitule = intitule;
		this.taches = taches;
		this.agent = agent;
		this.coutIndividuel = this.calculCoutIndividuel(this.taches);
		this.coutEstime = this.calculCoutEstime(this.taches);
		this.utiliteEstime = this.coutMinIndividuel - this.coutEstime;
		//this.coutReservation = this.coutMinIndividuel * this.agentObj.coutLimite; //intégrer le coutLimite
		this.coutReservation = this.coutMinIndividuel * 0.5; //intégrer le coutLimite
		this.utiliteReference = this.coutIndividuel - this.coutReservation;
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
	
	public double getCoutIndividuel() {
		return coutIndividuel;
	}
	
	public double getCoutEstime() {
		return coutEstime;
	}
	
	public double calculCoutIndividuel(List<Tache> taches) {
		double sommeCoutIndividuel = 0;
		
		for (Tache tache : taches) {
			sommeCoutIndividuel += tache.getCoutcoutIndividuelTache();
		}
		
		return sommeCoutIndividuel;
	}
	
	//gérer l'input pr le NbAgentParticipant
	public double calculCoutEstime(List<Tache> taches) {
		double sommeCoutEstime = 0;
		int NbAgentParticipant = 1;

		
		for (Tache tache : taches) {
			sommeCoutEstime += (tache.getCoutPublic()/NbAgentParticipant) + tache.getCoutPrive();
		}
		
		return sommeCoutEstime;
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
