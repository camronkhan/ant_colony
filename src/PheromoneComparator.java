import java.util.Comparator;

/**
 * Class PheromoneComparator
 * 
 * Encapsulates the basic functionality for comparing the pheromone level
 * in two different nodes
 * 
 * @author Camron Khan
 */
public class PheromoneComparator implements Comparator<Node> {
    
    /**
     * Compares the pheromone level in the first node to that in the second
     * 
     * @param firstNode     First node to be compared
     * @param secondNode    Second node against which the first will be compared
     * @return              The difference between the pheromone level in the nodes
     *                      Difference = First - Second
     */
    @Override
    public int compare(Node firstNode, Node secondNode) {
        
        // Difference = First - Second
        int diff = firstNode.getPheromoneLevel() - secondNode.getPheromoneLevel();
        
        // Return the difference
        return diff;
    }
}
