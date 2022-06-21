import jade.core.Runtime;

import java.util.ArrayList;
import java.util.List;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.*;

public class Main {

	public static void main(String... args){
	       Runtime runtime = Runtime.instance();
	       Profile profile = new ProfileImpl();
	       profile.setParameter(Profile.MAIN_HOST, "localhost");
	       profile.setParameter(Profile.GUI, "true");
	       ContainerController containerController = runtime.createMainContainer(profile);

	       ArrayList<Tache> ts = new ArrayList<Tache>();
		   ts.add(new Tache("t1",1.0,2.));
		   ts.add(new Tache("t2",1.0,2.));
		   ts.add(new Tache("t4",1.0,2.));
		   
		   ArrayList<Tache> ts1 = new ArrayList<Tache>();
			ts1.add(new Tache("t3",1.0,2.));
			ts1.add(new Tache("t2",1.0,2.));
			ts1.add(new Tache("t4",1.0,2.));
			
			ArrayList<Tache> ts2 = new ArrayList<Tache>();
			ts2.add(new Tache("t3",1.0,2.));
			ts2.add(new Tache("t2",1.0,2.));
			ts2.add(new Tache("t4",1.0,2.));
		   
		   ArrayList<ArrayList<Tache>> taches = new ArrayList<ArrayList<Tache>>();
		   taches.add(ts);
		   taches.add(ts1);
		   taches.add(ts2);
		   
		   ArrayList<Double> coutsPrives = new ArrayList<Double>();
		   ArrayList<Double> coutsPublics = new ArrayList<Double>();
		   
		   ArrayList<Double> coutsPrives1 = new ArrayList<Double>();
		   ArrayList<Double> coutsPublics1 = new ArrayList<Double>();
		   
		   ArrayList<Double> coutsPrives2 = new ArrayList<Double>();
		   ArrayList<Double> coutsPublics2 = new ArrayList<Double>();
		  
		   for (Tache t:ts) {
			   coutsPrives.add(t.getCoutPrive());
			   coutsPublics.add(t.getCoutPublic());
		   }
		   
		   for (Tache t:ts1) {
			   coutsPrives1.add(t.getCoutPrive());
			   coutsPublics1.add(t.getCoutPublic());
		   }
		   
		   for (Tache t:ts2) {
			   coutsPrives2.add(t.getCoutPrive());
			   coutsPublics2.add(t.getCoutPublic());
		   }
		   
	       Object[][] objects = new Object[][] {
	    	   new Object[] {
	    			   ts,
	    			   //coutsPrives,
	    			   //coutsPublics,
	    	   },
	    	   new Object[] {
	    			   ts1,
	    			   //coutsPrives1,
	    			   //coutsPublics1,
	    	   },
	    	   
	    	   new Object[] {
	    			   ts2,
	    			   //coutsPrives2,
	    			   //coutsPublics2,
	    	   },
	    	   
	       };
	       
	       for(int i=0; i<3; i++){
	           AgentController policeAgentController;
	           try {
	               policeAgentController = containerController.createNewAgent("Agent"+i, "Agents", objects[i]);
	               policeAgentController.start();
	               
	           } catch (StaleProxyException e) {
	               e.printStackTrace();
	           }
	       }
	   }

}