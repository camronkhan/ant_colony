/**
 * Class QueenAnt
 * 
 * Encapsulates the basic functionality of the queen ant, which is responsible
 * for hatching forager, scout, and soldier ants
 * 
 * Simulation ends if queen ant dies.  The queen dies if:
 * (1) Maximum life span is met
 * (2) Killed by bala ant
 * 
 * @author Camron Khan
 */
public class QueenAnt extends Ant implements TimeDependent {
    
    /************
     * Constants
     ************/
    
    // Maximum life span is twenty (20) years or 73,000 turns
    private final int LIFE_SPAN = 73000;
    
    // Number of turns per hatch
    private final int HATCH_RATE = 10;
    
    // Frequency of hatches per 100 total hatches
    private final int FORAGER_FREQ = 50;
    private final int SCOUT_FREQ = 25;
    private final int SOLDIER_FREQ = 25;
    
    
    /*************
     * Attributes
     *************/
    
    /***************
     * Constructors
     ***************/
    
    public QueenAnt(SimModel mod, Integer id, Integer type, Node current) {
        
        // Reference to simulation model
        MODEL = mod;
        
        // Assign unique ID
        ID = id;
        
        // Assign ant type
        TYPE = type;
        
        // Set current node
        hereNode = current;
        
        // Set position
        String position = hereNode.getPosition();
        
        // Notify node of presence
        hereNode.setQueen(position, true);
        
        // Add queen's ID to list of ants present at this node
        hereNode.addAnt(ID);
        
        // Set node as visited
        hereNode.setVisited(position, true);
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
        
        // If the queen is not dead...
        if(age < LIFE_SPAN) {
            
            // Consume food
            eat();
        
            // Hatch ant (if appropriate)
            hatch();
        }
    }
    
    /**
     * Responsible for killing the queen
     */
    @Override
    protected void die() {
        
        // Notify node of absence
        hereNode.setQueen(hereNode.getPosition(), false);
        
        // Destory queen
        MODEL.destroyAnt(ID, TYPE);
    }
    
    /**
     * Queen grows older on each turn and dies if maximum life span is reached
     */
    @Override
    protected void age() {
        
        // Grow older
        age++;
        
        // If age reaches maximum life span, then die
        if(age >= LIFE_SPAN)
            die();
    }
    
    /**
     * Queen consumes one unit of food per turn and dies if no food available
     */
    private void eat() {
        
        // Check amount of food available in node
        int foodSupply = hereNode.getFoodAmount();
        
        // If food is available...
        if (foodSupply > 0) {
            
            // Decrease food supply
            foodSupply--;
            
            // Update node's food amount
            hereNode.setFoodAmount(hereNode.getPosition(), foodSupply);
        }
        
        // Else if no food is available...
        else {
            
            // Die
            die();
        }
    }
    
    /**
     * Queen hatches a forager, scout, or soldier on the first turn of each day
     * 
     * Hatch ratio of 2 forager : 1 scout : 1 soldier
     */
    private void hatch() {
        
        // Calculate random number limit (exclusive)
        int limit = FORAGER_FREQ + SCOUT_FREQ + SOLDIER_FREQ;
        
        int random = RandomNumber.get(limit);
        
        // If random number is in first or second quartile...
        if((age % HATCH_RATE) == 0) {

            // Hatch forager
            if(random < FORAGER_FREQ) {
                
                // Hatch forager
                MODEL.createAnt(MODEL.FORAGER, hereNode);
            }
            
            // If random number is in third quartile...
            else if ((random >= FORAGER_FREQ) &&
                     (random < (FORAGER_FREQ + SCOUT_FREQ))) {
                
                // Hatch scout
                MODEL.createAnt(MODEL.SCOUT, hereNode);
            }
            
            // If random number is in fourth quartile...
            else {
                
                // Hatch soldier
                MODEL.createAnt(MODEL.SOLDIER, hereNode);
            }  
        }
    }
}

