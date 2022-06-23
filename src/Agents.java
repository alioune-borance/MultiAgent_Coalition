
import java.util.ArrayList;

import jade.core.Agent;

public class Agents extends Agent {
	
	private double coutLimite;
	private double coutMinIndividuel; // a calculer dans la classe agent et à retransmettre aux alternatives
	private ArrayList<Proposition> listePropositionsEnvoyees;
	private ArrayList<Proposition> listePropositionsRecues;
	private int NbPropositionsReçues;
	private int NbCoalitions;
	private ArrayList<Coalition> listeCoalitions;

	public double getCoutLimite() {
		return coutLimite;
	}
	public void setCoutLimite(double coutLimite) {
		this.coutLimite = coutLimite;
	}
	public ArrayList<Proposition> getListePropositionsEnvoyees() {
		return listePropositionsEnvoyees;
	}
	public void setListePropositionsEnvoyees(ArrayList<Proposition> listePropositionsEnvoyees) {
		this.listePropositionsEnvoyees = listePropositionsEnvoyees;
	}
	public ArrayList<Proposition> getListePropositionsRecues() {
		return listePropositionsRecues;
	}
	public void setListePropositionsRecues(ArrayList<Proposition> listePropositionsRecues) {
		this.listePropositionsRecues = listePropositionsRecues;
	}
	public int getNbPropositionsReçues() {
		return NbPropositionsReçues;
	}
	public void setNbPropositionsReçues(int nbPropositionsReçues) {
		NbPropositionsReçues = nbPropositionsReçues;
	}
	public int getNbCoalitions() {
		return NbCoalitions;
	}
	public void setNbCoalitions(int nbCoalitions) {
		NbCoalitions = nbCoalitions;
	}
	public ArrayList<Coalition> getListeCoalitions() {
		return listeCoalitions;
	}
	public void setListeCoalitions(ArrayList<Coalition> listeCoalitions) {
		this.listeCoalitions = listeCoalitions;
	}
	
	
	public double getCoutMinIndividuel() {
		return coutMinIndividuel;
	}
	public void setCoutMinIndividuel(double coutMinIndividuel) {
		this.coutMinIndividuel = coutMinIndividuel;
	}
	
	public double min(double[] coutIndividuelAlt) {
		double coutMinIndividuel = Integer.MAX_VALUE;
		
		for (double cout : coutIndividuelAlt) {
			if (cout < coutMinIndividuel) {
				coutMinIndividuel = cout;
			}
		}
		
		return coutMinIndividuel;
	}

	@Override
    public void setup()
    {
        System.out.println("Agent name is: " +getAID().getName());
    }
	
}