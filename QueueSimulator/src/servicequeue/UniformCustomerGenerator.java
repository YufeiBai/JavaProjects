/**
 * UniformCustomerGenerator is the parent class of the Customer class implementing a random number generator.
 * 
 * @author Brandon Belna (bbelna@stetson.edu)
 */
package servicequeue;

import java.util.Random;

public class UniformCustomerGenerator
{
    private int myRandom;
    private int myMaxGenerationTime;
    
    /**
     * Constructor for the UniformCustomerGenerator.
     * @param maxGenerationTime Maximum generation time.
     * @param serviceQueueManager ServiceQueueManager associated with this UniformCustomerGenerator.
     */
    public UniformCustomerGenerator(int maxGenerationTime, ServiceQueueManager serviceQueueManager)
    {
        myMaxGenerationTime = maxGenerationTime;
        myRandom = 0;
    }
    
    /**
     * Generate a random int bounded by 0 and maxGenerateTime.
     * @return Random int bounded by 0 and maxGenerateTime.
     */
    public int generateTime()
    {
        myRandom = new Random().nextInt(myMaxGenerationTime);
        return myRandom;
    }
}
