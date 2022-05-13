import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class AgentB extends Agent{
	public void setup() {
		try {
			Thread.sleep(7000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Agent B : " + getAID().getName());
		
		  FSMBehaviour agentB_beh= new FSMBehaviour();
		  
		  agentB_beh.registerFirstState(new attendreProposition(), "attendrePropositionA");
		  agentB_beh.registerState(new accepterPropostion(), "acceptationPropositionA");
		  agentB_beh.registerState(new attendreConfirmation(), "attendreConfirmationA");
		  agentB_beh.registerState(new confirmerProposition(), "confirmerPropositionA");
		  agentB_beh.registerState(new fin(), "fin");

		  agentB_beh.registerDefaultTransition("attendrePropositionA", "acceptationPropositionA");
		  agentB_beh.registerTransition("acceptationPropositionA", "attendreConfirmationA",0);
		  agentB_beh.registerTransition("attendreConfirmationA", "confirmerPropositionA",0);
		  agentB_beh.registerTransition("confirmerPropositionA", "fin", 1);
		  
		  addBehaviour(agentB_beh);
		  
	}
	
	protected void takeDown() {
		// Printout a dismissal message
		System.out.println("agent "+getAID().getName()+" terminating.");
		}
	
	private class attendreProposition extends OneShotBehaviour {
		public void action() {
			System.out.println("[ Attente de la proposition A /B ]");
			MessageTemplate modele = MessageTemplate.MatchPerformative(ACLMessage.PROPOSE);
			ACLMessage msgRecu = receive(modele);
			if (msgRecu != null) {
				Proposition n = new Proposition();
				try {
					n = (Proposition) msgRecu.getContentObject();
				} catch (UnreadableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				AID id = msgRecu.getSender();
				System.out.println("Propostion {"+ n.getCoalition().getTaches() + "},{"+ n.getCoalition().getAgents() +"}" + " reçu par " + id.getLocalName());
				}
			else {
				block();
			}
		}
		
	}
	
	private class accepterPropostion extends OneShotBehaviour {
		public void action() {
			System.out.println("[ Accepter proposition A /B ]");
			ACLMessage msg = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
			msg.addReceiver(new AID("AgentA",AID.ISLOCALNAME));
			msg.setContent("Acceptation A1");
			send(msg);
		}
		
	}
	
	
	private class attendreConfirmation extends OneShotBehaviour {
		public void action() {
			System.out.println("[ Attendre confirmation proposition A /B ]");
			MessageTemplate modele = MessageTemplate.MatchPerformative(ACLMessage.CONFIRM);
			ACLMessage msgRecu = receive(modele);
			if (msgRecu != null) {
				String n = msgRecu.getContent();
				AID id = msgRecu.getSender();
				System.out.println("[ Confirmation "+ n +" reçu par " + id.getLocalName() + "]");
				}
			else {
				block();
			}
		}
		
	}
	
	private class confirmerProposition extends OneShotBehaviour {
		public void action() {
			System.out.println("[ Confirmer proposition A /B ]");
			ACLMessage msg = new ACLMessage(ACLMessage.CONFIRM);
			msg.addReceiver(new AID("AgentA",AID.ISLOCALNAME));
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
