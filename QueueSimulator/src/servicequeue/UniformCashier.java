/**
 * UniformCashier is the parent class of the Cashier class implementing a random number generator.
 * 
 * @author Brandon Belna (bbelna@stetson.edu)
 */
package servicequeue;

import java.util.Random;

public class UniformCashier
{
    private int myMaxTimeService;
    private int myRandom;
    
    /**
     * Constructor for the UniformCashier.
     * @param maxServiceTime Maximum generation time.
     * @param serviceQueue ServiceQueue associated with this UniformCashier.
     */
    public UniformCashier(int maxServiceTime, ServiceQueue serviceQueue)
    {
        myMaxTimeService = maxServiceTime;
        myRandom = 0;
    }
    
    /**
     * Generate a random int bounded by 0 and maxServiceTime.
     * @return Random int bounded by 0 and maxServiceTime.
     */
    public int generateTime()
    {
        myRandom = new Random().nextInt(myMaxTimeService);
        return myRandom;
    }
}
