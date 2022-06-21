
import jade.core.Agent;

public class Agents extends Agent {
	
	private double coutReservation;
	private double coutLimite;
	private double utiliteReference;
	
	
	public double getCoutReservation() {
		return coutReservation;
	}
	public void setCoutReservation(double coutReservation) {
		this.coutReservation = coutReservation;
	}

	public double getCoutLimite() {
		return coutLimite;
	}
	public void setCoutLimite(double coutLimite) {
		this.coutLimite = coutLimite;
	}

	public double getUtiliteReference() {
		return utiliteReference;
	}
	public void setUtiliteReference(double utiliteReference) {
		this.utiliteReference = utiliteReference;
	}


	@Override
    public void setup()
    {
        System.out.println("Agent name is: " +getAID().getName());
    }
	
}