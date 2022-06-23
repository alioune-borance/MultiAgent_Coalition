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

	       ArrayList<Tache> ts01 = new ArrayList<Tache>();
		   ts01.add(new Tache("t1",1.0,2.));
		   ts01.add(new Tache("t2",1.0,2.));
		   ts01.add(new Tache("t4",1.0,2.));
		   ArrayList<Tache> ts02 = new ArrayList<Tache>();
		   ts02.add(new Tache("t1",1.0,2.));
		   ts02.add(new Tache("t5",1.0,2.));
		   ts02.add(new Tache("t6",1.0,2.));
		   
		   ArrayList<Tache> ts11 = new ArrayList<Tache>();
			ts11.add(new Tache("t2",1.0,2.));
			ts11.add(new Tache("t3",1.0,2.));
			ts11.add(new Tache("t6",1.0,2.));
			ArrayList<Tache> ts12 = new ArrayList<Tache>();
			ts12.add(new Tache("t1",1.0,2.));
			ts12.add(new Tache("t4",1.0,2.));
			ts12.add(new Tache("t6",1.0,2.));
			
			ArrayList<Tache> ts21 = new ArrayList<Tache>();
			ts21.add(new Tache("t1",1.0,2.));
			ts21.add(new Tache("t5",1.0,2.));
			ts21.add(new Tache("t7",1.0,2.));
			ArrayList<Tache> ts22 = new ArrayList<Tache>();
			ts22.add(new Tache("t1",1.0,2.));
			ts22.add(new Tache("t4",1.0,2.));
			ts22.add(new Tache("t7",1.0,2.));
		   
		   ArrayList<ArrayList<Tache>> alternatives1 = new ArrayList<ArrayList<Tache>>();
		   alternatives1.add(ts01);
		   alternatives1.add(ts02);
		   
		   ArrayList<ArrayList<Tache>> alternatives2 = new ArrayList<ArrayList<Tache>>();
		   alternatives2.add(ts11);
		   alternatives2.add(ts12);
		   
		   ArrayList<ArrayList<Tache>> alternatives3 = new ArrayList<ArrayList<Tache>>();
		   alternatives3.add(ts21);
		   alternatives3.add(ts22);
		   
		   
		   /*ArrayList<Double> coutsPrives = new ArrayList<Double>();
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
		   }*/
		   
	       Object[][] objects = new Object[][] {
	    	   new Object[] {
	    			   alternatives1,
	    			   //coutsPrives,
	    			   //coutsPublics,
	    	   },
	    	   new Object[] {
	    			   alternatives2,
	    			   //coutsPrives1,
	    			   //coutsPublics1,
	    	   },
	    	   
	    	   new Object[] {
	    			   alternatives3,
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