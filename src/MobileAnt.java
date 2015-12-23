/**
 * Abstract Class MobileAnt
 * 
 * Encapsulates the basic functionality of an at that can move around the
 * environment
 * 
 * @author Camron Khan
 */
public abstract class MobileAnt extends Ant {
    
    /************
     * Constants
     ************/
    
    // Maximum life span is one (1) year or 3,650 turns
    protected final int LIFE_SPAN = 3650;
    
    
    /**********
     * Methods
     **********/
    
    /**
     * Responsible for transferring a mobile ant from the current node to the next
     * 
     * @param currentNode       The current node
     * @param nextNode          The next node
     */
    protected abstract void move(Node currentNode, Node nextNode);
    
    /**
     * Responsible for selecting the next node where the mobile ant will be moved to
     * 
     * @param currentNode       The current node
     * @return                  The node where the mobile ant will move next
     */
    protected abstract Node chooseNextNode(Node currentNode);
    
    /**
     * Responsible for maintaining age of ant based on simulation clock.  Kills
     * the ant if its age reaches its maximum allotted lifespan.
     */
    @Override
    protected void age() {
        
        // Grow older
        age++;
        
        // If age reaches maximum life span, then die
        if(age >= LIFE_SPAN)
            die();
    }
}
