/**
 * Ant Colony Simulation
 * 
 * @author Camron Khan
 */
public class Driver {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        // Set initial conditions
        final int ROW = 27;
        final int COL = 27;
        final int MS_PER_TURN = 1000;
        final int TURNS_PER_DAY = 10;
        
        // Create simulation model
        SimModel model = new SimModel(ROW, COL, MS_PER_TURN, TURNS_PER_DAY);
    }
}
