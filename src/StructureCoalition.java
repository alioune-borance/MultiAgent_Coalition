import java.io.Serializable;
import java.util.List;

public class StructureCoalition implements Serializable{
	private List<Coalition> coalitions;
	//private Alternative alternative;
	private String alternative;
	
	public StructureCoalition(List<Coalition> coalitions,String alternative) {
		this.coalitions = coalitions;
		this.alternative = alternative;
	}
	
	public StructureCoalition() {}
	
	
	public List<Coalition> getCoalitions() {
		return coalitions;
	}
	public void setCoalitions(List<Coalition> coalitions) {
		this.coalitions = coalitions;
	}
	public String getAlternative() {
		return alternative;
	}
	public void setAlternative(String alternative) {
		this.alternative = alternative;
	}
	
	
}
