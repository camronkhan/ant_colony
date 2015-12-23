import java.util.ArrayList;
import java.util.Collections;
import java.util.ListIterator;
import java.util.Stack;

/**
 * Class ForagerAnt
 * 
 * Encapsulates the basic functionality of the forager ant, which is responsible
 * for seeking out and returning food to the queen
 * 
 * @author Camron Khan
 */
public class ForagerAnt extends MobileAnt implements TimeDependent {
    
    /************
     * Constants
     ************/
    
    // Foragers deposit 10 units of pheromone in return-to-nest mode
    private final int PHEROMONE_DEPOSIT = 10;
    
    // Maximum allowable pheromone level in a node
    private final int PHEROMONE_MAX = 1000;
    
    
    /*************
     * Attributes
     *************/
    
    // Movement history
    public Stack<Node> moveStack;
    
    // Loop check
    ArrayList<Node> lastEight;
    
    // Carrying food
    private boolean hasFood;
    
    
    /***************
     * Constructors
     ***************/
    
    public ForagerAnt(SimModel model,
                      Integer id,
                      Integer type,
                      Node currentNode) {
        
        // Reference to simulation model
        MODEL = model;
        
        // Assign unique ID
        ID = id;
        
        // Assign an ant type
        TYPE = type;
        
        // Set current node
        hereNode = currentNode;
        
        // Create stack tot store movement history
        moveStack = new Stack<>();
        
        // Create ArrayList to prevent looping
        lastEight = new ArrayList<>();
        
        // Initialize ArrayList with placeholder values
        for(int i = 0; i < 8; i++) {
            lastEight.add(currentNode);
        }
        
        // Notify node of creation
        int numForager = hereNode.getNumForager();
        numForager++;
        hereNode.setNumForager(hereNode.getPosition(), numForager);
        
        // Add ant's ID to the node's list of present ID's
        hereNode.addAnt(ID);
        
        // Initially not carrying food
        hasFood = false;
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
        
        // If forager is not dead...
        if (age < LIFE_SPAN) {
            
            // Choose the next node
            Node nextNode = chooseNextNode(hereNode);
            
            // Move to next node
            move(hereNode, nextNode);
            
            // Update loop check
            lastEight.remove(0);
            lastEight.add(nextNode);
        }
    }
    
    /**
     * Responsible for removing forager from the simulation
     * 
     * @param current   The current node
     * @param next      The next node
     */
    @Override
    protected void die() {
        
        // Decrement forager count in current node
        int currentForager = hereNode.getNumForager();
        currentForager--;
        hereNode.setNumForager(hereNode.getPosition(), currentForager);
        
        // Remove from node's ID list
        hereNode.getAntsPresent().remove(ID);
        
        // Leave food at current node
        int foodAmt = hereNode.getFoodAmount();
        foodAmt++;
        hereNode.setFoodAmount(hereNode.getPosition(), foodAmt);
        
        // Remove from environment
        MODEL.destroyAnt(ID, TYPE);
    }
    
    /**
     * Responsible for moving forager to the next node
     * 
     * @param current   The current node
     * @param next      The next node
     */
    @Override
    protected void move(Node currentNode, Node nextNode) {
            
        // Decrement forager count in current node
        int currentForager = currentNode.getNumForager();
        currentForager--;
        currentNode.setNumForager(currentNode.getPosition(), currentForager);
        
        // Remove ant ID from list of ants in current node
        currentNode.removeAnt(ID);
        
        // Get next node position
        String nextPos = nextNode.getPosition();
        
        // Increment forager count in next node
        int nextForager = nextNode.getNumForager();
        nextForager++;
        nextNode.setNumForager(nextPos, nextForager);
        
        // Add ant ID to list of ants in next node
        nextNode.addAnt(ID);
        
        // Next node becomes current node; move is complete
        hereNode = nextNode;  
    }
    
    /**
     * Responsible for selecting where ant will move next
     * 
     * @param current   The current node
     * @return          The next node
     */
    @Override
    protected Node chooseNextNode(Node currentNode) {

        // The node where the ant will move next
        Node nextNode;
        
        // If forager is carrying food...
            if(hasFood) {

                // Return to the nest
                nextNode = returnToNest(currentNode);
            }
            
            // Otherwise, if forager is not carrying food...
            else {

                // Forage for food
                nextNode = forage(currentNode);
                
                // Push the current node on to the movement history stack
                moveStack.push(currentNode);
            }
        
        // Return where the ant will move next
        return nextNode;
    }
    
    /**
     * Responsible for forager food-seeking behavior
     * 
     * @return      The node where the ant will move next
     */
    private Node forage(Node currentNode) {

        // The next node
        Node nextNode;
        
        // Create a deep copy of the list of accessible adjacent nodes
        ArrayList<Node> accessible = new ArrayList<>();
        
        // Iterate through the original list
        ListIterator<Node> copyItr =
                           currentNode.getVisitedAdjacentNodes().listIterator();
        while(copyItr.hasNext()) {
            
            // Get the next node
            Node node = copyItr.next();
            
            // Add the node to the new list
            accessible.add(node);
        }
        
        // If there is exactly one node in the list...
        if(accessible.size() == 1) {

            // Ant will move to the only available node next
            nextNode = accessible.get(0);
            
            // If next node has food...
            if(foodCheck(nextNode)) {
                
                // Acquire food
                acquireFood(nextNode);
            }
            
            // Return next node
            return nextNode;
        }
        
        // Else if there are exactly two nodes in the list...
        else if(accessible.size() == 2) {

            // Get the node at top of movement stack without removing it
            Node lastVisited = moveStack.peek();

            // The first node in the list to be considered
            Node first = accessible.get(0);

            // If the first node in the list is the last visited node...
            if(first.compareTo(lastVisited) == 0) {
                
                // The second node in the list will be the next node
                nextNode = accessible.get(1);
            }
            
            // Otherwise, if the second node in the list is the last visited...
            else {
                
                // The first node in the list will be the next node
                nextNode = accessible.get(0);
            }

            // If next node has food...
            if(foodCheck(nextNode)) {
                
                // Acquire food
                acquireFood(nextNode);
            }

            // Return next node
            return nextNode;
        }
            
        // Otherwise, if there is more than two nodes in the list...
        else {

            // If the forager is in any node but the queen's...
            if(!currentNode.getQueen()) {
                
                // Get the node at top of movement stack without removing it
                Node lastVisited = moveStack.peek();
                
                // Iterate through the list and remove the last visited node
                ListIterator<Node> lastItr = accessible.listIterator();
                while(lastItr.hasNext()) {

                    // Get the next node
                    Node node = lastItr.next();

                    // Compare the node to the last visited node
                    int isLast = node.compareTo(lastVisited);

                    // If last visited node is found...
                    if(isLast == 0) {

                        // Remove the node from the list
                        lastItr.remove();
                        break;
                    }
                }
            }
            
            // Create a new list of nodes containing only non-zero values
            ArrayList<Node> nonZero = new ArrayList<>();
            
            // Iterate through the list
            ListIterator<Node> pherItr = accessible.listIterator();
            while(pherItr.hasNext()) {
                
                // Get the next node
                Node node = pherItr.next();
                
                // If node has a non-zero pheromone level value...
                if(node.getPheromoneLevel() != 0) {
                    
                    // Add the node to the non-zero list
                    nonZero.add(node);
                }
            }
                       
            // Verify forager is not looping
            if( (lastEight.get(0).compareTo(lastEight.get(4)) == 0) &&
                (lastEight.get(1).compareTo(lastEight.get(5)) == 0) &&
                (lastEight.get(2).compareTo(lastEight.get(6)) == 0) &&
                (lastEight.get(3).compareTo(lastEight.get(7)) == 0) ) {
                
                // Get number of nodes to choose among
                int numNodes = accessible.size();
                
                // Randomly select one of the nodes
                int random = RandomNumber.get(numNodes);
                
                // Get the randomly selected node
                nextNode = accessible.get(random);
            }
            
            // Else if the non-zero list contains somee values...
            if(!nonZero.isEmpty()) {
                
                // Sort the list of remaining nodes based on pheromone level
                Collections.sort(nonZero, new PheromoneComparator());
            
                // Reverse the list so items are in descending order
                Collections.reverse(nonZero);
                
                // Get the node with the highest pheromone level
                nextNode = nonZero.get(0);
            }
            
            // Otherwise, randomly choose a node from the accessible list
            else {
                
                // Get number of nodes to choose among
                int numNodes = accessible.size();
                
                // Randomly select one of the nodes
                int random = RandomNumber.get(numNodes);
                
                // Get the randomly selected node
                nextNode = accessible.get(random);
            }

            // If next node has food...
            if(foodCheck(nextNode)) {

                // Acquire food
                acquireFood(nextNode);
            }
                        
            // Return node with highest pheromone level
            return nextNode;
        } 
    }
    
    /**
     * Responsible for directing forager back to the nest
     * 
     * @param       The current node
     * @return      The node where the ant will move next
     */
    private Node returnToNest(Node currentNode) {
        
        // The next node
        Node nextNode;
        
        // If queen is not present and node has not reached its pheromone max...
        if( (!currentNode.getQueen()) &&
            (currentNode.getPheromoneLevel() < PHEROMONE_MAX) ) {
            
            // Deposit phermomone here
            depositPheromone(currentNode);
        }

        // Pop last visited node from the movement history stack
        nextNode = moveStack.pop();
        
        // If next node contains the queen...
        if(nextNode.getQueen()) {
            
            // Drop off food
            deliverFood(nextNode);
        }
        
        // Return last visited node
        return nextNode;
    }
    
    /**
     * Responsible for checking if the next node has food
     * 
     * @param next  The next node
     * @return      True if food present; false otherwise
     */
    private boolean foodCheck(Node nextNode) {

        // Get amount of food in next node
        int foodAmt = nextNode.getFoodAmount();
        
        // If food is present and it's not the queen's node...
        if( (foodAmt > 0) && !nextNode.getQueen() )
            return true;
        
        // Otherwise, if no food is present...
        else
            return false;
    }
    
    /**
     * Responsible for acquiring food from a food source
     * 
     * @param source    The node containing food
     */
    private void acquireFood(Node foodSource) {
                
        // Decrement the food amount at the source
        int foodAmt = foodSource.getFoodAmount();
        foodAmt--;
        foodSource.setFoodAmount(foodSource.getPosition(), foodAmt);
        
        // Indicate forager is carrying food
        hasFood = true;
    }
    
    /**
     * Responsible for dropping off food at the queen's node
     * 
     * @param destination   The queen's node
     */
    private void deliverFood(Node destination) {

        // Increment the food amount at the drop off point
        int foodAmt = destination.getFoodAmount();
        foodAmt++;
        destination.setFoodAmount(destination.getPosition(), foodAmt);
        
        // Indicate forager is no longer carrying food
        hasFood = false;
    }
    
    /**
     * Responsible for depositing pheromone in return-to-nest mode
     * 
     * @param current   The current node where pheromone will be deposited
     */
    private void depositPheromone(Node currentNode) {

        // Add the deposit to current pheromone level in the node
        int pheromoneLevel = currentNode.getPheromoneLevel();
        pheromoneLevel += PHEROMONE_DEPOSIT;
        currentNode.setPheromoneLevel(currentNode.getPosition(),
                                      pheromoneLevel);
    }
}
