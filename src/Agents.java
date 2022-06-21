
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.domain.AMSService;
import jade.domain.FIPAAgentManagement.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class Agents extends Agent {
	
	
	AMSAgentDescription [] agents = null;
	Alternative myAlt;
	ArrayList<Tache> myT = new ArrayList<Tache>();
	ArrayList<ArrayList<Tache>> myT1 = new ArrayList<ArrayList<Tache>>();


	//Les alternatives des autres agents
	ArrayList<Alternative> alt = new ArrayList<Alternative>();


	@Override
    public void setup()
    {
		Object[] args = getArguments();
		ArrayList<Tache> ts = (ArrayList<Tache>)args[0];
		
		/* Tous les agents du container */
	    try {
	        SearchConstraints c = new SearchConstraints();
	        c.setMaxResults ( new Long(-1) );
	        agents = AMSService.search( this, new AMSAgentDescription (), c );
	    }
	    catch (Exception e) { }
	    
	    for (Tache t : ts) {
	    	myT.add(t);
	    }
		
		/* Alternative de l'agent */
		myAlt = new Alternative("alt1",myT,getAID().getLocalName().toString());
		
       
        System.out.println("Alternative = [{" + myAlt.getAgent() + "},{" + myAlt.getTaches() + "}]");
        System.out.println();
        
        /*for (int i=0; i<agents.length;i++){
            AID agentID = agents[i].getName();
            System.out.println(agentID.getLocalName());
       }*/
        
        /*FSMBehaviour agentA_beh= new FSMBehaviour();
		  
		  agentA_beh.registerFirstState(new envoiPlan(), "envoiPlan");
		  agentA_beh.registerState(new OneShotBehaviour() 
						{
							 public void action() {
									try {
										Thread.sleep(5000);
									} catch (InterruptedException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}

								ACLMessage msg= receive();
								Alternative plan;
								ArrayList<Tache> plan1 = new ArrayList<Tache>();

								if (msg!=null) {
									try {
										plan1 =  (ArrayList<Tache>)msg.getContentObject();
									} catch (UnreadableException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									if (!(msg.getSender().getLocalName().equals(myAgent.getLocalName()))) {
									System.out.println( msg.getSender().getLocalName() + " - " +
									   myAgent.getLocalName() + " <- " +
									   plan1 );
									
									ArrayList<Tache> ts = new ArrayList<Tache>();
									for (Tache t : plan1) {
										ts.add(t);
									}
									
									alt.add(new Alternative("Alternative_",ts,msg.getSender().getLocalName()));
									myT1.add(ts);

									}
								}
								else {
								block();
								}
								
							 }
							 
						
						},"getAlternatives");
				  
				  
		  
		  agentA_beh.registerTransition("envoiPlan", "getAlternatives",0);

		  addBehaviour(agentA_beh); */
        
        SequentialBehaviour comportementSequentiel = new SequentialBehaviour();
        
        comportementSequentiel.addSubBehaviour(new envoiPlan());
        
        for (int i=0;i<3;i++) {
        comportementSequentiel.addSubBehaviour(new OneShotBehaviour() 
		{
			 public void action() {
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				ACLMessage msg= receive();
				Alternative plan;
				ArrayList<Tache> plan1 = new ArrayList<Tache>();

				if (msg!=null) {
					try {
						plan1 =  (ArrayList<Tache>)msg.getContentObject();
					} catch (UnreadableException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (!(msg.getSender().getLocalName().equals(myAgent.getLocalName()))) {
					System.out.println( msg.getSender().getLocalName() + " - " +
					   myAgent.getLocalName() + " <- " +
					   plan1 );
					
					ArrayList<Tache> ts = new ArrayList<Tache>();
					for (Tache t : plan1) {
						ts.add(t);
					}
					
					alt.add(new Alternative("Alternative_",ts,msg.getSender().getLocalName()));
					myT1.add(ts);

					}
				}
				else {
				block();
				}
				
			 }
			 
		
		});
        }
       

        comportementSequentiel.addSubBehaviour(new OneShotBehaviour() {
        	@Override
            public void action() {
        		try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		/*System.out.print("Taches Communes = ");
        		//System.out.print(myAlt.getTaches() + "/" + alt);
        		System.out.println(myAlt.getTachesCommunes(alt));*/
        		int i = 0;
        		for (Alternative a : alt) {
        			System.out.println(getAID().getLocalName() + " " + i + " " + a.getTaches() + "=>" + alt);
        			i = i+1;
        		}
        		try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		
        		ArrayList<Tache> ts0 = new ArrayList<Tache>();
     		   ts0.add(new Tache("t1",1.0,2.));
     		   ts0.add(new Tache("t2",1.0,2.));
     		   ts0.add(new Tache("t4",1.0,2.));
     		   
     		  ArrayList<Tache> ts1 = new ArrayList<Tache>();
     		  String t1 = String.valueOf(myT1.get(0).get(0).getIntitule());
     		  String t2 = String.valueOf(myT1.get(0).get(1).getIntitule());
     		  String t3 = String.valueOf(myT1.get(0).get(2).getIntitule());


    		   ts1.add(new Tache(t1,1.0,2.));
    		   ts1.add(new Tache(t2,1.0,2.));
    		   ts1.add(new Tache(t3,1.0,2.));

    		   
        		
        		ArrayList<Alternative> alternatives = new ArrayList<Alternative>();
        		alternatives.add(new Alternative("alt1",ts0,"agentA"));
        		alternatives.add(new Alternative("alt2",ts0,"agentB"));
        		//System.out.println(alternatives.get(0).getTaches().get(0).getIntitule() + " - " + alternatives.get(1).getTaches()) ;
        		
        		ArrayList<Tache> tc = myAlt.getTachesCommunes(alternatives);
        		//System.out.println(myAlt.getTaches() + "/" + alt + "TC = " + tc);
        		//System.out.println("TC = " +  myT1);
        		
        		/* TACHES COMMUNES */
        		ArrayList<Tache> TC = new ArrayList<Tache>();
        		for (Tache t: myAlt.getTaches()) {
        			for (ArrayList<Tache> at : myT1) {
        				for (Tache t0 : at) {
        					if (t.getIntitule().equalsIgnoreCase(t0.getIntitule())) {
        						//System.out.println(t.getIntitule() +  "==" +t0.getIntitule());
        						TC.add(t);
        				}
        			
        			}
        			}
        		}
        		System.out.println("TC = " +  TC);
        		
        		
        		/* ENSEMBLE COMMUNS */
        		ArrayList<EnsembleCommun> ecs= new ArrayList<EnsembleCommun>();

        		for (Tache t : TC) {
        			ArrayList<String> agents = new ArrayList<String>();
        			ArrayList<Tache> taches = new ArrayList<Tache>();
        			for (Alternative a : alt) {
        				for (Tache ts : a.getTaches()) {
        					if (ts.getIntitule().equals(t.getIntitule())) {
        						agents.add(a.getAgent());
        						taches.add(ts);
        				}
        					
        			}
        				
        		}
        		agents.add(getAID().getLocalName());
        		ecs.add(new EnsembleCommun(taches,agents));
        	}
        		
        		for (EnsembleCommun e : ecs) {
        			System.out.println("EC = " + e.getAgents() + "," + e.getTaches());
        		}
        		
        		/* STRUCUTURE DE COALITION */
        		
        		ArrayList<EnsembleCommun> sc= new ArrayList<EnsembleCommun>();
        		
        		for (EnsembleCommun e : ecs) {
        			for (EnsembleCommun e1 : ecs) {
        			//if (!(e.getTaches().equals(e1.getTaches())) && e.getAgents().equals(e1.getAgents())){
        				if (e.getAgents().equals(e1.getAgents())) {
        				ArrayList<Tache> ts = new ArrayList<Tache>();
        				for (Tache t : e.getTaches()) {
        					ts.add(t);
        				}
        				for (Tache t : e1.getTaches()) {
        					ts.add(t);
        				}
        				
        				sc.add(new EnsembleCommun(ts,e.getAgents()));
        				}
        			}
        			
        		}
        		
        		for (EnsembleCommun e : sc) {
      			  for (Tache t : e.getTaches()) {
      				  System.out.print("{" + t.getIntitule() + "},");
      			  }
      			  for (String a : e.getAgents()) {
      				  System.out.print("{" + a + "}");
      			  }
      			  System.out.println();
      		  }
        		
        		
        	        		
        	}
        });
            
        addBehaviour(comportementSequentiel);
 
    }
	
	private class envoiPlan extends OneShotBehaviour{
		
		public void action() {
			
			ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
			for (int i=0; i<agents.length;i++) {
				System.out.println(getAID().getLocalName() + " [ Envoi de Plan ] " + myAlt.getTaches() + " à " + agents[i].getName().getLocalName());
				AID agentID = agents[i].getName();
				
				if (agents[i].getName().getLocalName().contains("Agent")) {
				msg.addReceiver(new AID(agents[i].getName().getLocalName().toString(),AID.ISLOCALNAME));
				}
				
				try {
					msg.setContentObject(myT);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				send(msg);
			}
		}
		
		
		
		
	}
	
	private class getAlternatives extends OneShotBehaviour {
		public void action() {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("[ Attente des plans]");
			MessageTemplate modele = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
			ACLMessage msgRecu = receive();
			Alternative plan = null;

			if (msgRecu != null) {
				String n = msgRecu.getContent().toString();
				try {
					plan = (Alternative) msgRecu.getContentObject();
				} catch (UnreadableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				AID id = msgRecu.getSender();
				//System.out.println("[ Plan "+ n.getClass().getName() +" reçu par " + id.getLocalName() + "]");
                System.out.println(getAID().getLocalName() + "/Plan " + plan.getTaches() + "recu de " + msgRecu.getSender().getLocalName());
				System.out.println();
				}
			else {
				block();
			}
		}
		
	}
	
	
	
}