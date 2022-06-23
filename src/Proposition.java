import java.io.Serializable;
import java.util.List;

public class Proposition implements Serializable{
	private String agentEmeteur;
	private String agentRecepteur;
	/*private List<String> taches;
	private List<String> agentConcernes;*/
	private Coalition coalition;
	private String intituleAlternative;

	public Proposition(String agentEmeteur,String agentRecepteur,Coalition coalition) {
		this.agentEmeteur = agentEmeteur;
		this.agentRecepteur = agentRecepteur;
		/*this.taches = taches;
		this.agentConcernes = agentConcernes;*/
		this.coalition = coalition;
 	}
	
	public String getIntituleAlternative() {
		return intituleAlternative;
	}
	public void setIntituleAlternative(String intituleAlternative) {
		this.intituleAlternative = intituleAlternative;
	}
	public Coalition getCoalition() {
		return coalition;
	}
	public void setCoalition(Coalition c) {
		this.coalition = c;
	}
	public Proposition() {}
	
	public String getAgentEmeteur() {
		return agentEmeteur;
	}

	public void setAgentEmeteur(String agentEmeteur) {
		this.agentEmeteur = agentEmeteur;
	}

	public String getAgentRecepteur() {
		return agentRecepteur;
	}

	public void setAgentRecepteur(String agentRecepteur) {
		this.agentRecepteur = agentRecepteur;
	}
	/*
	public List<String> getTaches() {
		return taches;
	}

	public void setTaches(List<String> taches) {
		this.taches = taches;
	}

	public List<String> getAgentConcernes() {
		return agentConcernes;
	}

	public void setAgentConcernes(List<String> agentConcernes) {
		this.agentConcernes = agentConcernes;
	}
	*/
	
	
	
} 
