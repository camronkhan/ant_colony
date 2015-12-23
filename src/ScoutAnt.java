import java.util.ArrayList;

/**
 * Class ScoutAnt
 * 
 * Encapsulates the basic functionality of the scout, which is responsible for:
 * (1) Revealing unopened nodes
 * (2) Assigning food amounts to newly opened nodes
 * 
 * @author Camron Khan
 */
public class ScoutAnt extends MobileAnt implements TimeDependent {
    
    /************
     * Constants
     ************/
    
    /*************
     * Attributes
     *************/
    
    /***************
     * Constructors
     ***************/
    
    public ScoutAnt(SimModel mod, Integer id, Integer type, Node current) {
        
        // Reference to simulation model
        MODEL = mod;
        
        // Assign unique ID
        ID = id;
        
        // Assign an ant type
        TYPE = type;
        
        // Set current node
        hereNode = current;
        
        // Notify node of creation
        int numScout = hereNode.getNumScout();
        numScout++;
        hereNode.setNumScout(hereNode.getPosition(), numScout);
        
        // Add the scout's ID to the list of ants at current node
        hereNode.addAnt(ID);
    }
    
    /**********
     * Methods
     **********/
    
    /**
     * Responsible for performing actions dependent on the simulation clock
     */
    @Override
    public void performActions() {
        
        // Grow older
        age();
        
        // If scout is not dead...
        if (age < LIFE_SPAN) {
            
            // Choose next node to move
            Node nextNode = chooseNextNode(hereNode);
            
            // If next node has not been visited...
            if(!nextNode.getVisited()) {
                
                // Assign a food value to the node
                assignFoodValue(nextNode);
                
                // Reveal the node
                revealNode(nextNode);
            }
            
            // Move to next node
            move(hereNode, nextNode); 
        }
    }
    
    /**
     * Responsible for killing the scout
     */
    @Override
    protected void die() {
        
        // Decrement scout count in current node
        int currentScout = hereNode.getNumScout();
        currentScout--;
        hereNode.setNumScout(hereNode.getPosition(), currentScout);
        
        // Remove from node's ID list
        hereNode.getAntsPresent().remove(ID);
        
        // Remove from environment
        MODEL.destroyAnt(ID, TYPE);
    }
    
    /**
     * Responsible for moving scout to the next node
     * 
     * @param currentNode   The current node
     * @param nextNode      The next node
     */
    @Override
    protected void move(Node currentNode, Node nextNode) {

        // Decrement scout count in current node
        int currentScout = currentNode.getNumScout();
        currentScout--;
        currentNode.setNumScout(currentNode.getPosition(), currentScout);
        
        // Remove ant ID from list of ants in current node
        currentNode.removeAnt(ID);
        
        // Get next node position
        String nextPos = nextNode.getPosition();
        
        // Increment scout count in next node
        int nextScout = nextNode.getNumScout();
        nextScout++;
        nextNode.setNumScout(nextPos, nextScout);

        // Add ant ID to list of ants in next node
        nextNode.addAnt(ID);

        // Next node becomes current node; move is complete
        hereNode = nextNode;
    }
    
    /**
     * Responsible for selecting where ant will move next
     * 
     * @param currentNode   The current node
     * @return              The next node
     */
    @Override
    protected Node chooseNextNode(Node currentNode) {
        
        // The next node
        Node nextNode;
        
        // Get set of adjacent nodes
        ArrayList<Node> adjNodes = currentNode.getAdjacentNodes();
        
        // Get size of ArrayList
        int numAdjNodes = adjNodes.size();

        // Choose adjacent node randomly
        int random = RandomNumber.get(numAdjNodes);
        
        // Identify node where scout will move next
        nextNode = adjNodes.get(random);
        
        // Return node
        return nextNode;
    }
    
    /**
     * Responsible for determining whether a newly revealed node has food
     * and if so, assigning how much
     * 
     * @param nextNode      The node where the scout will move next
     */
    private void assignFoodValue(Node nextNode) {
        
        // Get the next node's position
        String nextPos = nextNode.getPosition();
        
        // Chance next node will contain food is 25%
        final int YES_FOOD_FREQ = 25;
        final int NO_FOOD_FREQ = 75;
        final int FOOD_MIN = 500;
        final int FOOD_MAX = 1000;
        
        // Choose food availability randomly
        int foodAvailable = RandomNumber.get(YES_FOOD_FREQ + NO_FOOD_FREQ);
        
        // If next node contains food...
        if(foodAvailable < YES_FOOD_FREQ) {
            
            // Randomly assign a food value between min and max (inclusive)
            int foodValue = RandomNumber.get(FOOD_MAX - FOOD_MIN + 1) + FOOD_MIN;
            nextNode.setFoodAmount(nextPos, foodValue);
        }
        
        // Otherwise there is no food in the node
        else {
            nextNode.setFoodAmount(nextPos, 0);
        }
    }
    
    /**
     * Responsible for revealing nodes
     * 
     * @param nextNode      The node where the scout will move next
     */
    private void revealNode(Node nextNode) {
        
        // Get the position of the next node
        String nextPos = nextNode.getPosition();
        
        // Instruct node to set itself to visited
        nextNode.setVisited(nextPos, true);
    }
}
