import java.util.List;

public class AlternativeProfil {
	private StructureProfil structureProfil;
	private List<Boolean> taches;
	//private Alternative alternative;
	
	public AlternativeProfil(StructureProfil structureProfil, List<Boolean> taches) {
		super();
		this.structureProfil = structureProfil;
		this.taches = taches;
	}
	
	public AlternativeProfil() {}

	public StructureProfil getStructureProfil() {
		return structureProfil;
	}

	public void setStructureProfil(StructureProfil structureProfil) {
		this.structureProfil = structureProfil;
	}

	public List<Boolean> getTaches() {
		return taches;
	}

	public void setTaches(List<Boolean> taches) {
		this.taches = taches;
	}
	
	
	public void initialiserAlternativeProfil() {
		
	}
	
}
