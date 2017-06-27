/**
 * The Cashier class is a thread which takes Customers from a given ServiceQueue, and serves them.
 * 
 * @author Brandon Belna (bbelna@stetson.edu)
 */
package servicequeue;

public class Cashier extends UniformCashier implements Runnable 
{
    private ServiceQueue myServiceQueue;
    private Thread myThread;
    private boolean mySuspend, myKillSignal;
    private long mySuspendTime, myIdleTime;
    
    /**
     * Constructor for a Cashier thread.
     * @param maxServiceTime The maximum service time of a customer.
     * @param serviceQueue The ServiceQueue associated with this cashier.
     */
    public Cashier(int maxServiceTime, ServiceQueue serviceQueue)
    {
        super(maxServiceTime, serviceQueue);
        myServiceQueue = serviceQueue;
        myThread = new Thread(this);
        myKillSignal = false;
        myIdleTime = 0;
        mySuspendTime = -1;
        mySuspend = false;
    }
    
    /**
     * Serve a customer. Waits for a random amount of time bounded by
     * the max service time entered by the user, and then removes the first customer
     * from the associated ServiceQueue.
     */
    public void serveCustomer()
    {
        int myTime = this.generateTime();
        try
        {
            Thread.sleep(myTime);
        } 
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        Customer customerOff = myServiceQueue.serveCustomer();
        myServiceQueue.addWaitTime(customerOff.getWaitTime() - myTime);
        customerOff.setServiceTime(myTime);
        myServiceQueue.addServiceTime(customerOff.getServiceTime());
        System.gc();
    }
    
    /**
     * Start the cashier.
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
     * The method for the thread.
     */
    @Override
    public void run()
    {
        while (true)
        {
            try
            {
                this.waitWhileSuspended();
            } 
            catch (InterruptedException e)
            {
                System.out.println("Thread suspended.");
            }
            
            if (myServiceQueue.size() == 0 && mySuspendTime == -1)
            {
                mySuspendTime = System.nanoTime();
            }
            
            if (myKillSignal) break;
            
            if (myServiceQueue.size() != 0)
            {
                this.serveCustomer();
                if (mySuspendTime > 0)
                {
                    long addToIdle = System.nanoTime() - mySuspendTime;
                    myServiceQueue.addIdleTime(addToIdle/1000000);
                    myIdleTime = 0;
                    mySuspendTime = -1;
                }
            }
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
     * Suspend the thread.
     */
    public void suspend()
    {
        mySuspend = true;
    }
    
    /**
     * Get our current thread status.
     * @return Current thread status.
     */
    public boolean getStatus()
    {
        return mySuspend;
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
     * Get how long the thread was idle for.
     * @return Idle time.
     */
    public long getIdleTime()
    {
        return myIdleTime;
    }
    
    /**
     * Resume the thread.
     */
    public synchronized void resume()
    {
        mySuspend = false;
        this.notify();
    }
}
