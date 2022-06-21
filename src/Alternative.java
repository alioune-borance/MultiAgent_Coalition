import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import jade.core.Agent;

/*
 * -> t.coutPublic : co�t public de la t�che t, connu de tous les individus 
 * -> t.coutPrive : co�t priv� de la t�che t, n�est connu que par l�individu ai
 * 
 * -> t.coutIndividuelTache = t.coutPublic + t.coutPrive : co�t individuel d'une t�che effectu�e sans coop�ration 
 * -> alt.coutMinIndividuel : co�t de r�f�rence utilis� pour calculer l'utilit� estim�e d'une alternative
 * 
 * -> alt.coutIndividuel = somme(coutIndividuelTache), pour chaque t dans taches : co�t individuel d'une alternative effectu�e sans coop�ration
 * 
 * -> alt.coutEstime = somme(t.coutPub/n + t.coutPriv), pour chaque t dans taches : co�t estim� de l'alternative
 * 		- co�t d'une alternative effectu�e en coop�ration
 * 		- n : nombre d'agent effectuant la t�che conjointement
 * 
 * -> alt.utiliteEstime = alt.coutMinIndividuel - alt.coutEstime : utilit� d'une alternative pour un agent donn�
 * 
 * -> alt.coutReservation = alt.coutMinIndividuel * agent.coutLimite : 
 * 		qu�il ne peut pas d�passer durant les n�gociations, cout de reservation pour une alternative ?
 * 		- coutMinIndividuel : fix� par l'agent
 *		- coutLimite : fix� par l'agent, compris entre [0;1]
 * 
 * -> agent.utiliteReference = alt.coutMinIndividuel - agent.coutReservation
 * 
 * Enchainement :
 * 		- Avant d'entamer les n�gociations, chaque agent doit calculer l'utilit� de chacune de ses alternatives
 * 		- Chaque agent calcul : 
 * 			. son cout de r�servation, qu'il ne peut pas d�passer durant les n�gociations
 * 			. son utilit� de r�f�rence
 * 		- N�gociations
 * 		- L'agent consid�re uniquement les alternatives resp�ctant les conditions suivantes:
 * 			. ( alt.coutEstime < agent.coutReservation ) & ( alt.utiliteEstime > agent.utiliteReference)
 * 		- Avant d'entamer le premier cycle de n�gociations, l'agent choisit l'alternative ayant alt.utiliteEstime
 * 		  la plus importante, pour soumettre sa proposition.
 * 		  Premier cycle de n�gociations : ordonnancement des alternatives sur la base de leurs utilit�s estim�es.
 * 		- Second cycle de n�gociations (cas g�n�ral) : ordonnancement des alternatives sur la base de leur d�sirabilit�.
 * 
 */

// alternative appartenant � l'agent
// coalitions autour d'un groupe de tache et pas de l'alternative
/* Quand un agent B rec�oit une proposition de formation de coalition de la part d'un agent A,
	sur un groupe de t�ches � effectuer en commun, l'agent B reconsid�re une alternative qu'il avait d�j�
	en recalculant les co�ts publics qu'il va partager avec l'agent A.
	=> L'agent B recalcule l'utilit� estim� de l'alternative.
*/


public class Alternative implements Serializable{
	
	private String intitule;
	private List<Tache> taches;
	private Agents agentObj;
	private String agent;
	
	private double coutIndividuel;
	private double coutEstime;
	private double coutMinIndividuel; //fixe, d�finit par l'agent ?
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
		//this.coutReservation = this.coutMinIndividuel * this.agentObj.coutLimite; //int�grer le coutLimite
		this.coutReservation = this.coutMinIndividuel * 0.5; //int�grer le coutLimite
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
	
	//g�rer l'input pr le NbAgentParticipant
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
