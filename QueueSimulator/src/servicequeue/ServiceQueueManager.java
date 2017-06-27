/**
 * ServiceQueueManager is the accessor for all the individual ServiceQueues. It is primarily
 * responsible for managing ServiceQueues and enabling or disabling them.
 * 
 * @author Brandon Belna (bbelna@stetson.edu)
 */
package servicequeue;

public class ServiceQueueManager 
{
    public static final int MAX_NUMBER_OF_QUEUES = 5;
    private int myQueueCount, myTotalServed;
    private ServiceQueue[] myServiceQueues;
    private Cashier[] myCashiers;
    private float myTotalWaitTime, myTotalServiceTime, myTotalIdleTime;
    private float myAverageWaitTime, myAverageServiceTime, myAverageIdleTime;
    
    public ServiceQueueManager()
    {
        myServiceQueues = new ServiceQueue[MAX_NUMBER_OF_QUEUES];
    }
    
    public ServiceQueueManager(Cashier[] cashiers)
    {
        this();
        myCashiers = cashiers;
        myTotalWaitTime = 0;
        myTotalServiceTime = 0;
        myTotalIdleTime = 0;
        myTotalServed = 0;
        myAverageWaitTime = 0;
        myAverageServiceTime = 0;
        myAverageIdleTime = 0;
    }
    
    public ServiceQueue determineShortestQueue()
    {
        // find the min of the lengths of the service queues
        int currentMin = 0;
        for (int i = 0; i < myQueueCount; i++)
        {
            if (myServiceQueues[i].size() < myServiceQueues[currentMin].size())
            {
                currentMin = i;
            }
        }
        return myServiceQueues[currentMin];
    }
    
    public synchronized void computeStatistics()
    {
        this.resetStatistics();
        for (int i = 0; i < this.getQueueCount(); i++)
        {
            myTotalIdleTime += myServiceQueues[i].getIdleTime();
            myTotalServiceTime += myServiceQueues[i].getServiceTime();
            myTotalWaitTime += myServiceQueues[i].getWaitTime();
            myTotalServed += myServiceQueues[i].getCustomersServed();
        }
    }
    
    public void resetStatistics()
    {
        myTotalIdleTime = 0;
        myTotalServiceTime = 0;
        myTotalWaitTime = 0;
        myTotalServed = 0;
    }
    
    public void sleepAllCashiers()
    {
        //System.out.println("Sleeping cashiers...");
        for (int i = 0; i < myCashiers.length; i++)
        {
            myCashiers[i].suspend();
        }
    }
    
    public float getTotalWaitTime()
    {
        return myTotalWaitTime;
    }
    
    public float getTotalServiceTime()
    {
        return myTotalServiceTime;
    }
    
    public float getTotalIdleTime()
    {
        return myTotalIdleTime;
    }
    
    public float getAverageWaitTime()
    {
        myAverageWaitTime = myTotalServed == 0? 0 : myTotalWaitTime/myTotalServed;
        return myAverageWaitTime;
    }
    
    public float getAverageServiceTime()
    {
        myAverageServiceTime = myTotalServed == 0? 0 : myTotalServiceTime/myTotalServed;
        return myAverageServiceTime;
    }
    
    public float getAverageIdleTime()
    {
        myAverageIdleTime = myTotalServed == 0? 0 : myTotalIdleTime/myTotalServed;
        return myAverageIdleTime;
    }
    
    public synchronized boolean generateQueues(int numberOfQueues)
    {
        for (int i = 0; i < numberOfQueues; i++)
        {
            myServiceQueues[myQueueCount] = new ServiceQueue();
            myQueueCount++;
        }
        return true;
    }
    
    public synchronized boolean cleanQueues()
    {
        for (int i = 0; i < ServiceQueueManager.MAX_NUMBER_OF_QUEUES; i++)
        {
            myServiceQueues[i] = null;
        }
        myQueueCount = 0;
        return true;
    }
    
    public void setCashiers(Cashier[] cashiers)
    {
        myCashiers = cashiers;
    }
    
    public int getQueueCount()
    {
        return myQueueCount;
    }
    
    public ServiceQueue getQueue(int i)
    {
        return myServiceQueues[i];
    }
    
    public int getCustomersServed()
    {
        return myTotalServed;
    }
}