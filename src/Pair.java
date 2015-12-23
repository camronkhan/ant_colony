
/**
 * Class Pair
 * 
 * @author Camron Khan
 */
public class Pair<T, U> {
    private final T TEE;
    private final U YOU;
    
    public Pair(T t, U u) {
        TEE = t;
        YOU = u;
    }
    
    public T getT() {
        return TEE;
    }
    
    public U getU() {
        return YOU;
    }
}