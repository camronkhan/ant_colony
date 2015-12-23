/**
 * Abstract Class Ant
 * 
 * Encapsulates the most basic features of an ant at the greatest level of
 * generalization
 * 
 * @author Camron Khan
 */
public abstract class Ant {
    
    /************
     * Constants
     ************/
    
    // Reference to simulation model
    protected SimModel MODEL;
    
    // Unique interger ID
    protected Integer ID;
    
    // Ant type
    protected Integer TYPE;
    
    
    /*************
     * Attributes
     *************/
    
    // Current node
    protected Node hereNode;
    
    // Age
    protected int age;
    
    
    /**********
     * Methods
     **********/
    
    /**
     * Responsible for killing the ant
     */
    protected abstract void die();
    
    /**
     * Responsible for maintaining the age of the ant and ensuring that the ant
     * does not grow older than its maximum allotted lifespan
     */
    protected abstract void age();
}

