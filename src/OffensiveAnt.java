import java.util.ArrayList;
import java.util.HashMap;

/**
 * Abstract Class OffensiveAnt
 * 
 * Encapsulates the basic functionality of an ant that attacks other ants
 * 
 * @author Camron Khan
 */
public abstract class OffensiveAnt extends MobileAnt {
    
    /**
     * Responsible for checking if there is potential prey in the current node
     * 
     * @param currentNode       The current node
     * @return                  True if prey present; false otherwise
     */
    protected abstract boolean preyCheck(Node currentNode);
    
    /**
     * Responsible for selecting the actual prey object that the offensive ant
     * will attack
     * 
     * @param currentNode       The current node
     * @param idList            List of IDs of ants in a node
     * @param typeMap           A mapping of ant ID-Type values
     * @return                  A pair of ID-Type values of the prey to be attacked
     */
    protected abstract Pair findPrey(Node currentNode,
                                     ArrayList<Integer> idList,
                                     HashMap<Integer, Integer> typeMap);
    
    /**
     * Responsible for performing the attack
     * 
     * @param prey      The ID-Type values of the prey object to be attacked
     */
    protected abstract void attack(Pair<Integer, Integer> prey);
}
