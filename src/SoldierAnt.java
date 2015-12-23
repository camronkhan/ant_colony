import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;


/**
 * Class SoldierAnt
 * 
 * Encapsulates the basic functionality of the soldier ant, which is responsible
 * for seeking out and destoying bala ants
 * 
 * @author Camron Khan
 */
public class SoldierAnt extends OffensiveAnt implements TimeDependent {
    
    /************
     * Constants
     ************/
    
    /*************
     * Attributes
     *************/
    
    /***************
     * Constructors
     ***************/
    
    public SoldierAnt(SimModel model, Integer id, Integer type, Node currentNode) {
        
        // Reference to simulation model
        MODEL = model;
        
        // Assign unique ID
        ID = id;
        
        // Assign an ant type
        TYPE = type;
        
        // Set current node
        hereNode = currentNode;
        
        // Notify node of creation
        int numSoldier = hereNode.getNumSoldier();
        numSoldier++;
        hereNode.setNumSoldier(hereNode.getPosition(), numSoldier);
        
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
        
        // If soldier is not dead...
        if (age < LIFE_SPAN) {
                        
            // Check current node for bala ants
            boolean hasPrey = preyCheck(hereNode);
                        
            // If current node has prey...
            if(hasPrey) {
                                
                // Select prey
                Pair prey = findPrey(hereNode,
                                     hereNode.getAntsPresent(),
                                     MODEL.getAntTypes());
                                
                // Attack prey
                attack(prey);
            }
            
            // Otherwise, if current node has no prey...
            else {
                
                // Find where soldier will move next
                Node nextNode = chooseNextNode(hereNode);
                
                // Move to next node
                move(hereNode, nextNode);
            }
        }
    }
    
    /**
     * Responsible for removing soldier from the simulation
     */
    @Override
    protected void die() {
        
        // Decrement soldier count in current node
        int currentSoldier = hereNode.getNumSoldier();
        currentSoldier--;
        hereNode.setNumSoldier(hereNode.getPosition(), currentSoldier);
        
        // Remove from node's ID list
        hereNode.getAntsPresent().remove(ID);
        
        // Remove from environment
        MODEL.destroyAnt(ID, TYPE);
    }
    
    /**
     * Responsible for moving soldier to the next node
     * 
     * @param currentNode   The current node
     * @param nextNode      The next node
     */
    @Override
    protected void move(Node currentNode, Node nextNode) {
        
        // Decrement soldier count in current node
        int currentSoldier = currentNode.getNumSoldier();
        currentSoldier--;
        currentNode.setNumSoldier(currentNode.getPosition(), currentSoldier);
        
        // Remove ant ID from list of ants in current node
        currentNode.removeAnt(ID);
        
        // Get next node position
        String nextPos = nextNode.getPosition();
        
        // Increment soldier count in next node
        int nextSoldier = nextNode.getNumSoldier();
        nextSoldier++;
        nextNode.setNumSoldier(nextPos, nextSoldier);
        
        // Add ant ID to list of ants in next node
        nextNode.addAnt(ID);
        
        // Next node becomes current node; move is complete
        hereNode = nextNode;
    }
    
    /**
     * Responsible selecting the node where the soldier will move next
     * 
     * @param currentNode   The current node
     * @return              The next node
     */
    @Override
    protected Node chooseNextNode(Node currentNode) {
        
        // The next node
        Node nextNode = null;
       
        // Get list of accessible nodes
        ArrayList<Node> visitedNodes = currentNode.getVisitedAdjacentNodes();
        
        // Create a new list to store only nodes that contain balas
        ArrayList<Node> balaNodes = new ArrayList<>();
                
        // Iterate through the list to locate nodes with balas
        ListIterator<Node> nodeItr = visitedNodes.listIterator();
        while(nodeItr.hasNext()) {
            
            // Get the next node
            Node node = nodeItr.next();
            
            // Get the node's ID list to locate balas
            ArrayList<Integer> idList = node.getAntsPresent();
                        
            // Iterate through the list to find out if balas are present
            ListIterator<Integer> idItr = idList.listIterator();
            while(idItr.hasNext()) {
                
                // Get the ID
                Integer id = idItr.next();
                
                // Get the potential prey's type
                Integer type = (Integer)MODEL.getAntTypes().get(id);
                
                // If it's a bala...
                if(type.intValue() == MODEL.BALA.intValue()) {
                    
                    // Add the node to the list
                    balaNodes.add(node);
                    break;
                }
            }
        }
        
        // If the list of nodes with balas is not empty
        if(!balaNodes.isEmpty()) {

            // Get the number of nodes with balas
            int numWithBala = balaNodes.size();

            // Randomly select one of the nodes
            int random = RandomNumber.get(numWithBala);

            // Get the randomly selected node
            nextNode = balaNodes.get(random);
        }

        // Otherwise, if list of nodes with bala is empty
        else {

            // Get the number of visited nodes
            int numVisited = visitedNodes.size();

            // Randomly select one of these nodes
            int random = RandomNumber.get(numVisited);

            // Get the randomly selected node
            nextNode = visitedNodes.get(random);
        }

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
        
        // Check if current node has bala ants
        int bala = currentNode.getNumBala();
        
        // Return true if bala is present
        return bala > 0;
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
    protected Pair findPrey(Node currentNode,
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
                        
            // Get the ID
            Integer id = idItr.next();
                        
            // Get the ant type
            Integer type = (Integer)MODEL.getAntTypes().get(id);
                        
            // If the ant is a bala...
            if(type.intValue() == MODEL.BALA.intValue()) {
                
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
     * Responsible for the soldier's attack behavior
     * 
     * @param preyPair      The ID-TYPE pair of the selected prey
     */
    @Override
    protected void attack(Pair<Integer, Integer> preyPair) {
                
        // Get prey ID and type
        Integer preyID = preyPair.getT();
        
        // Get the bala to be attacked
        BalaAnt bala = MODEL.getBala(preyID);
        
        // 50% success rate
        int random = RandomNumber.get(2);
        
        // If successful, bala dies
        if(random == 0) {
            bala.die();
        }
    }
}
