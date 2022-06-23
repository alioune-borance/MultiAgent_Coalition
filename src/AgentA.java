import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class AgentA extends Agent{
	
	private static List<Alternative> alternatives;
	
	 //AgentB agentb = new AgentB();
	
	 //AgentA agenta = new AgentA();
	
	 public static Alternative initialiserAlternative() {
		List<Tache> ts = new ArrayList<Tache>();
		ts.add(new Tache("t3",1.0,2.));
		ts.add(new Tache("t2",1.0,2.));
		ts.add(new Tache("t4",1.0,2.));
		
		//alternatives.add(new Alternative("alternativeB",ts,agentb));
		
		//new AID("AgentB",AID.ISLOCALNAME);
		
		return new Alternative("alternativeA",ts,"AgentA");
		//System.out.println("OUI");
		
	}
	 
	 public static ArrayList<Alternative> autreInitialiserAlternative() {
		 	ArrayList<Alternative> alt = new ArrayList<Alternative>();
			List<Tache> ts = new ArrayList<Tache>();
			ts.add(new Tache("t1",1.0,2.));
			ts.add(new Tache("t2",1.0,2.));
			ts.add(new Tache("t4",1.0,2.));
			
			List<Tache> ts1 = new ArrayList<Tache>();
			ts1.add(new Tache("t1",1.0,2.));
			ts1.add(new Tache("t2",1.0,2.));
			ts1.add(new Tache("t3",1.0,2.));
			
			alt.add(new Alternative("alternativeB",ts,"AgentB"));
			alt.add(new Alternative("alternativeC",ts1,"AgentC"));

			//System.out.println(agentb.getName());
			
			return alt;
			//System.out.println("OUI");
			
		}
	
	/*public List<Tache> tachesCommunes() {
	
	List<Tache> tachescommunes= new ArrayList<Tache>();
	List<Alternative> alts = initialiserAlternative();
	//exemple d'une alternative
	List<Tache> ts = new ArrayList<Tache>();
	ts.add(new Tache("t1",1.0,2.));
	ts.add(new Tache("t2",1.0,2.));
	ts.add(new Tache("t4",1.0,2.));
	Alternative alt = new Alternative(ts);
	
	for (Tache t: alt.getTaches()) {
		for (Tache t0 : alts.get(0).getTaches()) {
			if (t.getIntitule() == t0.getIntitule()) {
				tachescommunes.add(t);
			}
		}
	}
	
	System.out.print("{");
	for (Tache t: tachescommunes) {
		System.out.print(t.getIntitule() + ", ");
	}
	System.out.println();
	
	return tachescommunes;
	
	}
	*/
	public void setup() {
		System.out.println("Agent A : " + getAID().getName());
		
		  FSMBehaviour agentA_beh= new FSMBehaviour();
		  
		  agentA_beh.registerFirstState(new envoiProposition(), "envoiPropositionA");
		  agentA_beh.registerState(new attendreAcceptation(), "attendreAcceptationA");
		  agentA_beh.registerState(new confirmerProposition(), "confirmerPropositionA");
		  agentA_beh.registerLastState(new fin(), "fin");

		  agentA_beh.registerDefaultTransition("envoiPropositionA", "attendreAcceptationA");
		  agentA_beh.registerTransition("attendreAcceptationA", "confirmerPropositionA",0);
		  agentA_beh.registerTransition("confirmerPropositionA", "fin", 1);
		  
		  addBehaviour(agentA_beh);  
		  
		  Alternative alternativeA = initialiserAlternative();
		  
		  
		  ArrayList<Alternative> alternativeB = new ArrayList<Alternative>();
		  ArrayList<Tache> tc = new ArrayList<Tache>();
		  ArrayList<EnsembleCommun> ec = new ArrayList<EnsembleCommun>();
		  
		  try {
		  alternativeB = autreInitialiserAlternative();
		  tc = alternativeA.getTachesCommunes(alternativeB);
		  ec = alternativeA.getEnsemblesCommuns(tc, alternativeB);
		  }catch(NullPointerException e) {
			  System.out.println(alternativeB);
		  }
		  
		  /*for (Tache t: tc) {
			  System.out.println(t.getIntitule());
		  }*/

		  
		  /*for (EnsembleCommun e : ec) {
			  for (Tache t : e.getTaches()) {
				  System.out.print("{" + t.getIntitule() + "},");
			  }
			  for (String a : e.getAgents()) {
				  System.out.print("{" + a + "}");
			  }
			  System.out.println();
		  }*/
		  
		  System.out.println();

		  ArrayList<EnsembleCommun> sc = new ArrayList<EnsembleCommun>();
		  
		  sc = alternativeA.getStructureCoalition(ec);
		  
		  for (EnsembleCommun e : sc) {
			  for (Tache t : e.getTaches()) {
				  System.out.print("{" + t.getIntitule() + "},");
			  }
			  for (String a : e.getAgents()) {
				  System.out.print("{" + a + "}");
			  }
			  System.out.print(" cout = " + alternativeA.getCout(e) + " ");
			  System.out.println();
		  }
		  

	}
	
	protected void takeDown() {
		// Printout a dismissal message
		System.out.println("agent "+getAID().getName()+" terminating.");
		  
		}
	
	
	
	
	private class envoiProposition extends OneShotBehaviour {
		public void action() {
			System.out.println("[ Envoi de proposition A /A ] par " + getAID().getLocalName());
			ACLMessage msg = new ACLMessage(ACLMessage.PROPOSE);
			msg.addReceiver(new AID("AgentB",AID.ISLOCALNAME));
			List<Tache> t = new ArrayList<Tache>();
			List<String> a = new ArrayList<String>();
			t.add(new Tache("t1",1.0,2.));
			t.add(new Tache("t2",1.0,2.));
			a.add("AgentA");
			a.add("AgentB");
			Coalition coalition = new Coalition(a,t);
			Proposition prop = new  Proposition("AgentA","AgentB",coalition);
			try {
				msg.setContentObject(prop);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			send(msg);
			System.out.println();
		}
		
	}
	
	
	private class attendreAcceptation extends OneShotBehaviour {
		public void action() {
			System.out.println("[ Attendre acceptation proposition A /A ] par " + getAID().getLocalName());
			MessageTemplate modele = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
			ACLMessage msgRecu = receive(modele);
			if (msgRecu != null) {
				String n = msgRecu.getContent();
				AID id = msgRecu.getSender();
				System.out.println("Acceptation "+ n +" reçu par " + id.getLocalName());
				}
			else {
				block();
			}
			System.out.println();

		}
		
	}
	
	private class confirmerProposition extends OneShotBehaviour {
		public void action() {
			System.out.println("[ Confirmer proposition A /A ] par " + getAID().getLocalName());
			ACLMessage msg = new ACLMessage(ACLMessage.CONFIRM);
			msg.addReceiver(new AID("AgentB",AID.ISLOCALNAME));
			msg.setContent("Confirmation A1");
			send(msg);
			System.out.println();

		}
		
		public int onEnd() {
			myAgent.doDelete();
		    return super.onEnd();
		}
	}
	
	private class fin extends OneShotBehaviour {
		public void action() {
			MessageTemplate modele = MessageTemplate.MatchPerformative(ACLMessage.CONFIRM);
			ACLMessage msgRecu = receive(modele);
			if (msgRecu != null) {
			System.out.println("Fin de l'agent " + myAgent.getLocalName());
			myAgent.doDelete();
		
		}
			else {
				block();
			}
			System.out.println();


	}
	}
}
