/**
 * Customer is a basic class which contains information about how long it takes for a
 * customer to be served, how long it will wait, and when it should enter. 
 * 
 * @author Brandon Belna (bbelna@stetson.edu)
 */
package servicequeue;

public class Customer
{
    private long myServiceTime, myEntryTime, myWaitTime;
    
    /**
     * Default constructor. Sets all member data to -1.
     */
    public Customer()
    {
        myServiceTime = -1;
        myWaitTime = -1;
        myEntryTime = System.nanoTime();
    }
    
    /**
     * Sets the service time.
     * @param serviceTime New service time.
     */
    public void setServiceTime(int serviceTime)
    {
        myServiceTime = serviceTime;
    }
    
    /**
     * Sets the entry time.
     * @param entryTime New entry time.
     */
    public void setEntryTime(int entryTime)
    {
        myEntryTime = entryTime;
    }
    
    /**
     * Set the wait time.
     * @param waitTime New wait time.
     */
    public void setWaitTime(int waitTime)
    {
        myWaitTime = waitTime;
    }
    
    /**
     * Get the service time.
     * @return Service time.
     */
    public long getServiceTime()
    {
        return myServiceTime;
    }
    
    /**
     * Get the entry time.
     * @return Entry time.
     */
    public long getEntryTime()
    {
        return myEntryTime;
    }
    
    /**
     * Get the wait time.
     * @return Wait time.
     */
    public long getWaitTime()
    {
        myWaitTime = (System.nanoTime() - myEntryTime)/1000000; // if we haven't been served
        return myWaitTime;
    }
}
