import java.util.Random;

/**
 * Class RandomNumber
 * 
 * Encapsulates the basic functionality of a random number generator
 * 
 * Mimics a static class in order to prevent seed duplication by...
 * (1) Declaring the class as final, which prevents the extension of the class
 * (2) Declaring the constructor as private, which prevents any client code from
 *     instantiating any instances of this class
 * 
 * The get() method should be used by any object that exhibits
 * (approximately) random behavior
 * 
 * @author Camron Khan
 */
public final class RandomNumber {
    
    /*************
     * Attributes
     *************/
    
    // Create a pseduorandom number generator
    private static final Random random = new Random();
    
    
    /***************
     * Constructors
     ***************/
    
    /**
     * Mimic a static class by declaring the constructor to be private, thus
     * preventing client code from instantiating any instances of this class
     */
    private RandomNumber() {
        System.out.println("Do not create instances of RandomNumber!");
    }
    
    
    /**********
     * Methods
     **********/
    
    /**
     * Returns the next pseudorandom number in the sequence between zero (0)
     * inclusive and max exclusive
     * 
     * @param max       The bound on the number to be returned; must be positive
     * @return          The next pseudorandom number in the sequence between
     *                  zero (0) inclusive and max exclusive
     */
    public static int get(int max) {
        return random.nextInt(max);
    }
}
