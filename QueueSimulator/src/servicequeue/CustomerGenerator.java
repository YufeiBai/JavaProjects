/**
 * CustomerGenerator generates customers at random intervals.
 * 
 * @author Brandon Belna (bbelna@stetson.edu)
 */
package servicequeue;

public class CustomerGenerator extends UniformCustomerGenerator implements Runnable
{
    private ServiceQueueManager myManager;
    private Thread myThread;
    private int myCustomerCount, myMaxCustomers;
    private boolean mySuspend, myKillSignal, myFinished;
    
    /**
     * CustomerGenerator constructor.
     * @param maxGenerationTime Max time for each customer to generate.
     * @param maxCustomers How many customers to generate.
     * @param serviceQueueManager The corresponding ServiceQueue.
     */
    public CustomerGenerator(int maxGenerationTime, int maxCustomers, ServiceQueueManager serviceQueueManager)
    {
        super(maxGenerationTime, serviceQueueManager);
        myManager = serviceQueueManager;
        myThread = new Thread(this);
        mySuspend = false;
        myKillSignal = false;
        myMaxCustomers = maxCustomers;
        myCustomerCount = 0;
        myFinished = false;
    }
    
    /**
     * The method for the thread.
     */
    @Override
    public void run()
    {
        while (myCustomerCount < myMaxCustomers)
        {
            try
            {
                this.waitWhileSuspended();
            } 
            catch (InterruptedException e1)
            {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            if (myKillSignal) break; // ABANDON SHIP!
            int generateTime = this.generateTime();
            try
            {
                
                Thread.sleep(generateTime);
            } 
            catch (InterruptedException e)
            {
                System.out.println("Thread interrupted");
            }
            this.insertCustomer();
            myCustomerCount++;
        }
        if (myCustomerCount == myMaxCustomers)
        {
            int numOfQueues = myManager.getQueueCount();
            for (int i = 0; i < numOfQueues; i++) 
            {
                while (myManager.getQueue(i).size() != 0)
                {
                    try
                    {
                        Thread.sleep(0);
                    } catch (InterruptedException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            myManager.sleepAllCashiers(); // and sleep
            myFinished = true; // tell the controller that we're done
        }
    }
    
    /**
     * Insert a customer into the queue.
     */
    public synchronized void insertCustomer()
    {
        Customer newCustomer = new Customer();
        myManager.determineShortestQueue().insertCustomer(newCustomer);
        //System.out.println("Served Customer #" + myCustomerCount + ".");
    }
    
    /**
     * Start the thread.
     */
    public void start()
    {
        try
        {
            myThread.start();
        }
        catch(IllegalThreadStateException e)
        {
            System.out.println("Thread already started");
        }
    }
    
    /**
     * When we are told to suspend, keep waiting while we are.
     * @throws InterruptedException
     */
    private synchronized void waitWhileSuspended() throws InterruptedException
    {
        while (mySuspend)
        {
            this.wait();
        }
    }
    
    /**
     * Kill the thread.
     */
    public void kill()
    {
        myKillSignal = true;
        if (mySuspend == true) this.resume();
    }
    
    /**
     * Suspend the thread.
     */
    public void suspend()
    {
        mySuspend = true;
    }
    
    /**
     * Resume the thread.
     */
    public synchronized void resume()
    {
        mySuspend = false;
        this.notify();
    }
    
    /**
     * Get whether or not the generator is complete.
     * @return The status of the generator's completeness.
     */
    public boolean getFinished()
    {
        return myFinished;
    }
}
