import java.io.Serializable;
import java.util.List;

public class StructureProfil implements Serializable{
	
	private List<Tache> taches;

	public StructureProfil(List<Tache> taches) {
		super();
		this.taches = taches;
	}
	
	public StructureProfil() {}

	public List<Tache> getTaches() {
		return taches;
	}

	public void setTaches(List<Tache> taches) {
		this.taches = taches;
	}
	
	
}
