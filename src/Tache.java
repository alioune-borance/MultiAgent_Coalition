import java.io.Serializable;

public class Tache implements Serializable{
	private String intitule;
	private Double coutPrive;
	private Double coutPublic;
	
	
	public Tache(String intitule, Double coutPrive, Double coutPublic) {
		super();
		this.intitule = intitule;
		this.coutPrive = coutPrive;
		this.coutPublic = coutPublic;
	}
	
	public Tache() {}
	
	
	public String getIntitule() {
		return intitule;
	}
	public void setIntitule(String intitule) {
		this.intitule = intitule;
	}
	public Double getCoutPrive() {
		return coutPrive;
	}
	public void setCoutPrive(Double coutPrive) {
		this.coutPrive = coutPrive;
	}
	public Double getCoutPublic() {
		return coutPublic;
	}
	public void setCoutPublic(Double coutPublic) {
		this.coutPublic = coutPublic;
	}
	
	
}
