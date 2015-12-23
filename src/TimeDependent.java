/**
 * Interface TimeDependent
 * 
 * Encapsulates the basic functionality required to perform actions based on
 * increments in the simulation clock
 * 
 * Any class that performs actions over time must implement this interface
 * 
 * @author Camron Khan
 */
public interface TimeDependent {
    
    /**
     * Respond to an increment in the simulation clock
     */
    public void performActions();
}
