import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.lang.Math;
import jade.core.Agent;

/*
 * -> t.coutPublic : co�t public de la t�che t, connu de tous les individus 
 * -> t.coutPrive : co�t priv� de la t�che t, n�est connu que par l�individu ai
 * 
 * -> t.coutIndividuelTache = t.coutPublic + t.coutPrive : co�t individuel d'une t�che effectu�e sans coop�ration 
 * 
 * -> alt.coutIndividuel = somme(coutIndividuelTache), pour chaque t dans taches : co�t individuel d'une alternative effectu�e sans coop�ration
 * 
 * -> alt.coutMinIndividuel = min(alt.coutIndividuel) : co�t mimimum parmis toutes les alternatives de l'agent
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
 * Pi = {epsilon, omega, gamma, rho, mu} o� pour tout 'e' appartenant � Pi : -1 <= e <= 1 est un ensemble de param�tres caract�ristiques 
 * pour caract�riser chaque alternative � chaque cycle de n�gociations
 * ils sont mis � jour avant chaque �tape d��change de pro-positions
 * 
 * Pi_ = {epsilon_, omega_, gamma_ , rho_, mu_} est un ensemble de param�tres de valorisation permettant
 * d�attribuer des poids � chacun des param�tres dans Pi
 * 
 * Poids de l�utilit� estim� (epsilon), Poids d�alternative (omega), Poids des t�ches (gamma), Poids des propositions envoy�es (rho), Poids de la distance (mu)
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
	private double coutMinIndividuel; 
	private double utiliteEstime;
	private double coutReservation; 
	private double utiliteReference;
	private double desirabilite;
	private StructureCoalition cs; // Structure de coalition associ�e � l'aternative
	private int tourNegociation;
	
	//Param�tres caract�ristiques de Pi = {epsilon, omega, gamma, rho, mu} 
	private double poidsUtiliteEstime;
	private double poidsAlternative;
	private double poidsTaches;
	private double poidsPropositionsEnvoy�es;
	private double poidsDistance;
	
	//Param�tres de valorisation de Pi_ = {epsilon_, omega_, gamma_ ,  rho_, mu_} 
	private double valorisationUtiliteEstime;
	private double valorisationAlternative;
	private double valorisationTaches;
	private double valorisationPropositionsEnvoy�es;
	private double valorisationDistance;
	
	public Alternative() {
		super();
	}
	
	public Alternative(String intitule, List<Tache> taches, String agent) { //coutMinIndividuel, coutLimite
		super();
		this.intitule = intitule;
		this.taches = taches;
		this.agent = agent;
		this.coutIndividuel = this.calculCoutIndividuel(this.taches);
		this.coutMinIndividuel = this.agentObj.getCoutMinIndividuel();
		this.coutEstime = this.calculCoutEstime(this.taches);
		this.utiliteEstime = this.coutMinIndividuel - this.coutEstime;

		this.coutReservation = this.coutMinIndividuel * this.agentObj.getCoutLimite(); //int�grer le coutLimite
		this.utiliteReference = this.coutIndividuel - this.coutReservation;
		this.desirabilite = this.getDesirabilite(this.poidsUtiliteEstime, this.poidsAlternative, this.poidsTaches, this.poidsPropositionsEnvoy�es, this.poidsDistance, 
				this.valorisationUtiliteEstime, this.valorisationAlternative, this.valorisationTaches, this.valorisationPropositionsEnvoy�es, this.valorisationDistance);
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
	
	// Impl�mentation de la fonction de fr�quence d'une tache :
	// Permet d'�valuer le nombre de coalitions contenant la t�che, parmis toutes les propositions de coalitions re�ues
	public double frequenceTache(Tache t) {
		double frequenceTache = 0.0;
		int NbApparitionTache = 0;
		// Nombre d'apparition de la t�che dans les coalitions de l'agent
		for (Coalition coalition : this.agentObj.getListeCoalitions()) {
			for (Tache tache : coalition.getTaches()) {
				if (tache.equals(t)) {
					NbApparitionTache++;
				}
			}
		}
		frequenceTache = NbApparitionTache/this.agentObj.getNbPropositionsRe�ues();
		
		return frequenceTache;
	}
	
	// Fonction calculant la mis � jour du poids des taches
	public double getPoidsTaches(StructureCoalition cs) {
		double poidsTaches = 0.0;
		double sommeFrequence = 0.0;
		double sommeFrequencesCoalitions = 0.0;
		double sommeFrequencesAllCoalitions = 0.0;
		
		// somme des fr�quences des taches dans la structure de coalition associ�e � l'alternative, 
		// divis�e par le nombre d'agent dans la coalition
		for (Coalition coalition : cs.getCoalitions()) {
			for (Tache tache : coalition.getTaches()) {
				sommeFrequence += frequenceTache(tache)/coalition.getAgents().size();
			}
			sommeFrequencesCoalitions += sommeFrequence;
			sommeFrequence = 0.0;
		}
		
		// Somme des fr�quences des t�ches de l'ensemble des coalitions de l'agent 
		for (Coalition coalition : this.agentObj.getListeCoalitions()) {
			for (Tache tache : coalition.getTaches()) {
				sommeFrequence += frequenceTache(tache); 
			}
			sommeFrequencesAllCoalitions += sommeFrequence;
			sommeFrequence = 0.0;
		}
		
		poidsTaches = sommeFrequencesCoalitions/sommeFrequencesAllCoalitions;
		
		return poidsTaches;
	}
	
	// Mesure l'importance d'une alternative alpha, par rapport � d'autre alternatives, pour les autres agents
	public double getPoidsAlternative() {
		double poidsAlternative = 0.0;
		int tachesCommunes = 0;
		int nbAgentEmmeteurs = 0;
		int nbAgentCS = 0;
		ArrayList<String> listeAgentEmmeteurs = new ArrayList<String>();
		ArrayList<String> listeAgentCS = new ArrayList<String>();
		
		//nombre d'agent ayant envoy� une proposition de coalition c, tq. : Tc n'ait aucune tache dans Talt
		for (Proposition proposition : this.agentObj.getListePropositionsRecues()) {
			for (Tache tache : proposition.getCoalition().getTaches()) {
				for (Tache t : this.taches) {
					if (tache.equals(t)) {
						tachesCommunes++;
					}
				}
			}
			if(tachesCommunes == 0 && (listeAgentEmmeteurs.contains(proposition.getAgentEmeteur()) == false)){
				listeAgentEmmeteurs.add(proposition.getAgentEmeteur());
			}
			tachesCommunes = 0;
		}
		nbAgentEmmeteurs = listeAgentEmmeteurs.size();
		
		// On compte le nombre d'agent dans la structure de coalition associ�e � l'alternative
		for (Coalition coalition : this.cs.getCoalitions()) {
			for (String string : coalition.getAgents()) {
				if(listeAgentCS.contains(string) == false){
					listeAgentCS.add(string);
				}
			}
			
		}
		nbAgentCS = listeAgentCS.size();
		
		// On divise le nombre d'agent emmeteurs obtenus par le nombre d'agent dans la structure de coalition associ�e � l'alternative
		poidsAlternative = nbAgentEmmeteurs/nbAgentCS;

		return poidsAlternative;
	}
	
	public double getPoidsPropositionsEnvoyees(int tourNegociation) {
		ArrayList<String> listeAlternativePropositions = new ArrayList<String>(); // Liste des alternatives choisies pour envoy� une proposition de coalition
		int nbFoisAlternativeChoisie = 0;
		double poidsPropositionsEnvoyees = 0.0;
		
		// faire le compte du nombre de fois o� l'alternative a �t� choisie pour envoyer une propositions de coalition c appartenant � la structure de coalition associ�e � l'alternative.
		// on parcourt la cs associ�e � l'alternative
		for (Coalition coalition : this.cs.getCoalitions()) {
			listeAlternativePropositions.add(coalition.getProposition().getIntituleAlternative());
		}
		for (String string : listeAlternativePropositions) {
			if (this.intitule.equals(string)) {
				nbFoisAlternativeChoisie++;
			}
		}
		poidsPropositionsEnvoyees = nbFoisAlternativeChoisie/tourNegociation;
		
		return poidsPropositionsEnvoyees;
	}
	
	// Fonction effectuant la mis � jour des param�tres caract�ristiques de l'alternative.
	public void misAJourPoids() {
		//epsilon
		this.poidsUtiliteEstime = 1 - ((this.coutMinIndividuel - this.utiliteEstime)/this.coutIndividuel);
		//omega
		this.poidsAlternative = this.getPoidsAlternative();
		//gamma
		this.poidsTaches = this.getPoidsTaches(this.cs);
		//rho
		this.poidsPropositionsEnvoy�es = getPoidsPropositionsEnvoyees(this.tourNegociation);
	}
	
	// Fonction effectuant le calcul de la d�sirabilit� de l'alternative
	//{epsilon, omega, gamma, rho, mu}
	public double getDesirabilite(double poidsutiliteEstime, double poidsAlternative, double poidsTaches, double poidsPropositionsEnvoy�es, double poidsDistance,
			double valorisationUtiliteEstime, double valorisationAlternative, double valorisationTaches, double valorisationPropositionsEnvoy�es, double valorisationDistance) {
		
		desirabilite = Math.pow(valorisationUtiliteEstime, poidsutiliteEstime)
				+ Math.pow(valorisationAlternative, poidsAlternative)
				+ Math.pow(valorisationTaches, poidsTaches)
				+ Math.pow(valorisationDistance, poidsDistance)
				- Math.pow(valorisationPropositionsEnvoy�es, poidsPropositionsEnvoy�es);
		
		return desirabilite;
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
