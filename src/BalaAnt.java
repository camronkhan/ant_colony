import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;

/**
 * Class BalaAnt
 * 
 * Encapsulates the basic functionality of a bala ant, which is an enemy of the
 * colony.  These ants move at random and attack encountered ants with 50%
 * success rate.
 * 
 * @author Camron Khan
 */
public class BalaAnt extends OffensiveAnt implements TimeDependent {
    
    /************
     * Constants
     ************/
    
    /*************
     * Attributes
     *************/
    
    /***************
     * Constructors
     ***************/
    
    public BalaAnt(SimModel mod, Integer id, Integer type, Node current) {
        
        // Reference to simulation model
        MODEL = mod;
        
        // Assign unique ID
        ID = id;
        
        // Assign an ant type
        TYPE = type;
        
        // Set current node
        hereNode = current;
        
        // Notify node of creation
        int numBala = hereNode.getNumBala();
        numBala++;
        hereNode.setNumBala(hereNode.getPosition(), numBala);
        
        // Add ID to list of ants present in current node
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
        
        // If bala is not dead...
        if (age < LIFE_SPAN) {
                        
            // Check current node for bala ants
            boolean hasPrey = preyCheck(hereNode);
                        
            // If current node has prey...
            if(hasPrey) {
                            
                // Select prey
                Pair<Integer, Integer> prey =
                                            findPrey(hereNode,
                                                     hereNode.getAntsPresent(),
                                                     MODEL.getAntTypes());
               
                // Attack prey
                attack(prey);
            }
        
        // Otherwise, if current node has no prey...
            else {
                
                // Find where bala will move next
                Node nextNode = chooseNextNode(hereNode);
                
                // Move to next node
                move(hereNode, nextNode);
            }
        }
    }
    
    /**
     * Responsible for removing bala from the simulation
     */
    @Override
    protected void die() {
        
        // Decrement bala count in current node
        int currentBala = hereNode.getNumBala();
        currentBala--;
        hereNode.setNumBala(hereNode.getPosition(), currentBala);
        
        // Remove from node's ID list
        hereNode.getAntsPresent().remove(ID);
        
        // Remove from environment
        MODEL.destroyAnt(ID, TYPE);
    }
    
    /**
     * Responsible for moving bala to the next node
     * 
     * @param currentNode   The current node
     * @param nextNode      The next node
     */
    @Override
    protected void move(Node currentNode, Node nextNode) {
        
        // Decrement bala count in current node
        int currentBala = currentNode.getNumBala();
        currentBala--;
        currentNode.setNumBala(currentNode.getPosition(), currentBala);
        
        // Remove ant ID from list of ants in current node
        currentNode.removeAnt(ID);
        
        // Get next node position
        String nextPos = nextNode.getPosition();
        
        // Increment bala count in next node
        int nextBala = nextNode.getNumBala();
        nextBala++;
        nextNode.setNumBala(nextPos, nextBala);
        
        // Add ant ID to list of ants in next node
        nextNode.addAnt(ID);
        
        // Next node becomes current node; move is complete
        hereNode = nextNode;
    }
    
    /**
     * Responsible selecting the node where the bala will move next
     * 
     * @param currentNode   The current node
     * @return              The next node
     */
    @Override
    protected Node chooseNextNode(Node currentNode) {
        
        // The next node
        Node nextNode;
        
        // Get a list of nodes adjacent to current node
        ArrayList<Node> adjacentList = currentNode.getAdjacentNodes();
        
        // Get number of adjacent nodes
        int numAdjacent = adjacentList.size();
        
        // Randomly select one of the nodes
        int random = RandomNumber.get(numAdjacent);
        
        // Get the randomly selected node
        nextNode = adjacentList.get(random);   
        
        // Return the next node
        return nextNode;
    }
    
    /**
     * Responsible for checking if prey is present in a node
     * 
     * @param currentNode   The current node
     * @return              True if prey is present; false otherwise
     */
    @Override
    protected boolean preyCheck(Node currentNode) {
        
        // Check if current node has prey
        boolean queen = currentNode.getQueen();
        int forager = currentNode.getNumForager();
        int scout = currentNode.getNumScout();
        int soldier = currentNode.getNumSoldier();       
        
        // Return true if any of the above are present
        return queen || (forager > 0) || (soldier > 0) || (scout > 0);
    }
    
    /**
     * Responsible for selecting the prey that will be attacked
     * 
     * @param currentNode   The current node
     * @param idList        The list of ant's IDs who are present in the node
     * @param typeMap       A mapping of ant IDs to types
     * @return              An ID-TYPE pair for the selected prey
     */
    @Override
    protected Pair<Integer, Integer> findPrey(Node currentNode,
                                              ArrayList<Integer> idList,
                                              HashMap<Integer, Integer> typeMap) {
        
        // The pair object to be returned
        Pair<Integer, Integer> prey;
        
        // The pair object will be comprised of the prey's ID and ant type
        Integer preyID;
        Integer preyType;
        
        // Create a deep copy of the ID list
        ArrayList<Integer> preyList = new ArrayList<>();
        
        // Iterate through node's ID list to copy IDs to prey list
        ListIterator<Integer> idItr = idList.listIterator();
        while(idItr.hasNext()) {
                        
            // Get the ant ID
            Integer id = idItr.next();
                        
            // Get the ant type
            Integer type = (Integer)MODEL.getAntTypes().get(id);
                        
            // If the ant is a queen, forager, scout, or soldier...
            if( (type.intValue() == MODEL.QUEEN.intValue()) ||
                (type.intValue() == MODEL.FORAGER.intValue()) ||
                (type.intValue() == MODEL.SCOUT.intValue()) ||
                (type.intValue() == MODEL.SOLDIER.intValue()) ) {
                
                // Add the ID to the copy
                preyList.add(id);
            }
        }
        
        // Get the number prey in the node
        int numPrey = preyList.size();
                
        // Randomly select one of the non-balas to attack
        int random = RandomNumber.get(numPrey);
            
        // Get the randomly selected ant's ID and type
        preyID = preyList.get(random);
        preyType = typeMap.get(preyID);
        
        // Initialize prey pair
        prey = new Pair<>(preyID, preyType);
        
        // Return the prey to attack
        return prey;
    }
    
    /**
     * Responsible for the bala's attack behavior
     * 
     * @param preyPair      The ID-TYPE pair of the selected prey
     */
    @Override
    protected void attack(Pair<Integer, Integer> preyPair) {
                
        // Get prey ID and type
        Integer preyID = preyPair.getT();
        Integer preyType = preyPair.getU();
        
        // Initialize possible prey types
        QueenAnt queen;
        ForagerAnt forager;
        ScoutAnt scout;
        SoldierAnt soldier;
            
        // If prey is the queen
        if(preyType.intValue() == MODEL.QUEEN) {
            
            // Get queen
            queen = MODEL.getQueen();
            
            // 50% success rate
            int random = RandomNumber.get(2);
        
            // If successful, queen dies
            if(random == 0) {
                queen.die();
            }
        }
        
        // Else if prey is a forager
        else if(preyType.intValue() == MODEL.FORAGER) {

            // Get forager
            forager = MODEL.getForager(preyID);
            
            // 50% success rate
            int random = RandomNumber.get(2);
        
            // If successful, forager dies
            if(random == 0) {
                forager.die();
            }
        }
        
        // Else if prey is a scout
        else if(preyType.intValue() == MODEL.SCOUT) {
            
            // Get scout
            scout = MODEL.getScout(preyID);
            
            // 50% success rate
            int random = RandomNumber.get(2);
        
            // If successful, scout dies
            if(random == 0) {
                scout.die();
            }
        }
        
        // Else if prey is a soldier
        else if(preyType.intValue() == MODEL.SOLDIER) {
            
            // Get soldier
            soldier = MODEL.getSoldier(preyID);
            
            // 50% success rate
            int random = RandomNumber.get(2);
        
            // If successful, soldier dies
            if(random == 0) {
                soldier.die();
            }
        }
        
        // Otherwise, error message
        else
            System.out.println("Error in BalaAnt attack()"); 
    }
}
