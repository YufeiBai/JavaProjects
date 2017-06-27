/**
 * ServiceQueue is an extension of LinkedList which implements Customer specific methods.
 * 
 * @author Brandon Belna (bbelna@stetson.edu)
 */
package servicequeue;

import java.util.LinkedList;

@SuppressWarnings("serial")
public class ServiceQueue extends LinkedList<Customer>
{
    private int myCustomersServed;
    private boolean myStatus;
    private float myIdleTime, myServiceTime, myWaitTime;
    
    /**
     * Constructor for a ServiceQueue.
     */
    public ServiceQueue()
    {
        myCustomersServed = 0;
        myStatus = true;
        myIdleTime = 0;
        myServiceTime = 0;
        myWaitTime = 0;
    }
    
    /**
     * Insert a customer into the ServiceQueue.
     * @param customer The customer to insert.
     */
    public synchronized void insertCustomer(Customer customer)
    {
        this.addFirst(customer);
    }
    
    /**
     * Remove a customer from the ServiceQueue.
     * @return The customer to remove.
     */
    public synchronized Customer serveCustomer()
    {
        myCustomersServed++;
        return this.removeLast();
    }
    
    /**
     * Set the status of the LinkedList.
     * @param newStatus New status of the LinkedList.
     */
    public void setStatus(boolean newStatus)
    {
        myStatus = newStatus;
    }
    
    /**
     * Get the status of this LinkedList.
     * @return Status of the LinkedList.
     */
    public boolean getStatus()
    {
        return myStatus;
    }
    
    /**
     * Add idle time to this queue.
     * @param addition Idle time to add.
     */
    public void addIdleTime(float addition)
    {
        myIdleTime += addition;
    }
    
    /**
     * Add service time to this queue.
     * @param addition Service time to add.
     */
    public void addServiceTime(float addition)
    {
        myServiceTime += addition;
    }
    
    /**
     * Add wait time to this queue.
     * @param addition Time to add.
     */
    public void addWaitTime(float addition)
    {
        myWaitTime += addition;
    }
    
    /**
     * Get amount of customers served.
     * @return Number of customers served.
     */
    public int getCustomersServed()
    {
        return myCustomersServed;
    }
    
    /**
     * Reset statistics being totalled by this queue.
     */
    public synchronized void resetStatistics()
    {
        myIdleTime = 0;
        myWaitTime = 0;
        myServiceTime = 0;
        myCustomersServed = 0;
    }
    
    /**
     * Get our idle time.
     * @return Idle time.
     */
    public float getIdleTime()
    {
        return myIdleTime;
    }
    
    /**
     * Get our service time.
     * @return Service time.
     */
    public float getServiceTime()
    {
        return myServiceTime;
    }
    
    /**
     * Get our wait time.
     * @return Wait time.
     */
    public float getWaitTime()
    {
        return myWaitTime;
    }
}
