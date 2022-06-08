
import jade.core.Agent;

public class Agents extends Agent {
	@Override
    public void setup()
    {
        System.out.println("Agent name is: " +getAID().getName());
    }
	
}