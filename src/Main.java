import jade.core.Runtime;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.*;

public class Main {

//	public static void main(String[] args) {
//		
//
//	}
	
	public static void main(String... args){
	       Runtime runtime = Runtime.instance();
	       Profile profile = new ProfileImpl();
	       profile.setParameter(Profile.MAIN_HOST, "localhost");
	       profile.setParameter(Profile.GUI, "true");
	       ContainerController containerController = runtime.createMainContainer(profile);

	       for(int i=1; i<6; i++){
	           AgentController policeAgentController;
	           try {
	               policeAgentController = containerController.createNewAgent("Agent"+i, "Agents", null);
	               policeAgentController.start();
	               
	           } catch (StaleProxyException e) {
	               e.printStackTrace();
	           }
	       }
	   }

}