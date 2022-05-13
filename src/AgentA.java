import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class AgentA extends Agent{
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
		
	}
	
	protected void takeDown() {
		// Printout a dismissal message
		System.out.println("agent "+getAID().getName()+" terminating.");
		}
	
	
	
	
	private class envoiProposition extends OneShotBehaviour {
		public void action() {
			System.out.println("[ Envoi de proposition A /A ]");
			ACLMessage msg = new ACLMessage(ACLMessage.PROPOSE);
			msg.addReceiver(new AID("AgentB",AID.ISLOCALNAME));
			List<String> t = new ArrayList<String>();
			List<String> a = new ArrayList<String>();
			t.add("t1");
			t.add("t2");
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
		}
		
	}
	
	
	private class attendreAcceptation extends OneShotBehaviour {
		public void action() {
			System.out.println("[ Attendre acceptation proposition A /A ]");
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
		}
		
	}
	
	private class confirmerProposition extends OneShotBehaviour {
		public void action() {
			System.out.println("[ Confirmer proposition A /A ]");
			ACLMessage msg = new ACLMessage(ACLMessage.CONFIRM);
			msg.addReceiver(new AID("AgentB",AID.ISLOCALNAME));
			msg.setContent("Confirmation A1");
			send(msg);
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
			

	}
	}
}
