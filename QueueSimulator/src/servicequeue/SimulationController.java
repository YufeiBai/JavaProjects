/**
 * The SimulationController is the heart of the queue simulator. SimulationController parses and manages all
 * information entered by the user, handles button events, updates the view, and starts or stops simulations.
 * 
 * @author Brandon Belna (bbelna@stetson.edu)
 */
package servicequeue;

import javax.swing.JOptionPane;

public class SimulationController implements Runnable
{
    private SimulationView myView;
    private ServiceQueueManager myModel;
    private Cashier[] myCashiers;
    private CustomerGenerator myGenerator;
    private Thread myThread;
    private int myCashierCount, myMaxServiceTime, myMaxGenerateTime, myNumberOfCustomers;
    private boolean mySuspend, myKillSignal, myHasRun, mySuspendSignal;
    
    /**
     * Entry point of the program.
     * @param args Arguments passed through command line.
     */
    public static void main(String[] args)
    {
        new SimulationController();
    }
    
    /**
     * Constructor for the SimulationController.
     */
    public SimulationController()
    {
        myView = new SimulationView(this);
        myModel = new ServiceQueueManager();
        myCashierCount = -1;
        myMaxServiceTime = -1;
        myMaxGenerateTime = -1;
        myNumberOfCustomers = -1;
        myThread = new Thread(this);
        mySuspend = false;
        myKillSignal = false;
        myHasRun = false;
    }
    
    /**
     * Start a simulation.
     */
    public synchronized void startSimulation()
    {   
        if (myHasRun) // kill old threads
        {
            //this.kill();
            for (int i = 0; i < myCashiers.length; i++)
            {
                myCashiers[i].kill();
            }
            myGenerator.kill();
        }
        // so, we're starting anew
        // set our variables
        if (myView.getCashierCount() == -1 || myView.getMaxServiceTime() == -1 
                || myView.getMaxGenerateTime() == -1 || myView.getCustomerCount() == -1)
        {
            JOptionPane.showMessageDialog(null, "Invalid value(s) entered for simulation!");
            return;
        }
        myView.switchGoPauseText();
        myCashierCount = myView.getCashierCount();
        myMaxServiceTime = myView.getMaxServiceTime();
        myMaxGenerateTime = myView.getMaxGenerateTime(); 
        myNumberOfCustomers = myView.getCustomerCount();
        
        // set up cashiers
        myCashiers = new Cashier[myCashierCount];
        
        // flush out 
        myModel.cleanQueues();
        myModel.generateQueues(myCashierCount);
        
        for (int i = 0; i < myCashierCount; i++)
        {
            myCashiers[i] = new Cashier(myMaxServiceTime, myModel.getQueue(i));
            myCashiers[i].start();
        }
        // start the view
        myView.updateView(true);
        myModel.setCashiers(myCashiers);
        System.gc();
                
        // customer generator
        myGenerator = new CustomerGenerator(myMaxGenerateTime, myNumberOfCustomers, myModel);
        myGenerator.start();
        
        myModel.resetStatistics();
        if (!myHasRun) 
        {
            this.start();
            myHasRun = true;
        }
        else
        {
            this.resume();
        }
    }
    
    /**
     * The method called when the Go!/Pause button is pressed.
     */
    public void goAndPause()
    {
        if (myView.getGoPauseText() == SimulationView.GO_TEXT)
        {
            // resuming simulation?
            if (myCashierCount == myView.getCashierCount() && myMaxServiceTime == myView.getMaxServiceTime()
                    && myMaxGenerateTime == myView.getMaxGenerateTime() && myNumberOfCustomers == myView.getCustomerCount())
            {
                this.resumeSimulation();
                myView.switchGoPauseText();
            }
            else
            {
                // starting new simulation
                this.startSimulation();
            }
        }
        else
        {
            mySuspendSignal = true;
            myView.switchGoPauseText();
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
                e.printStackTrace();
            }
            if (myKillSignal) 
            {
                myHasRun = false;
                myKillSignal = false;
                break;
            }
            if (mySuspendSignal) this.suspendSimulation();
            if (!mySuspendSignal) this.updateView();
        }
    }
    
    /**
     * Updates the view.
     */
    public void updateView()
    {
        // we need to suspend the generator, otherwise if spawn times are low enough, the customergenerator
        // will spawn faster than updateview can complete, causing OutOfBounds exceptions.
        synchronized (myGenerator)
        {
            myView.updateView(false);
        }
        try
        {
            Thread.sleep(5);
        } catch (InterruptedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (myGenerator.getFinished() && myCashierCount != -1)
        {
            myView.updateView(false);
            this.suspendSimulation();
            myView.switchGoPauseText();
            myCashierCount = -1;
            JOptionPane.showMessageDialog(null, "Simulation Complete!", "Complete!", JOptionPane.PLAIN_MESSAGE);
        }
    }
    
    /**
     * Resumes the thread.
     */
    public synchronized void resume()
    {
        mySuspend = false;
        mySuspendSignal = false;
        this.notify();
    }
    
    /**
     * Suspend the thread.
     */
    public void suspend()
    {
        mySuspend = true;
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
    public synchronized void waitWhileSuspended() throws InterruptedException
    {
        while (mySuspend)
        {
            this.wait();
        }
    }
    
    /**
     * Get the cashier count.
     * @return Number of cashiers.
     */
    public int getCashierCount()
    {
        return myCashierCount;
    }
    
    /**
     * Get the max generate time.
     * @return Maximum generate time.
     */
    public int getMaxGenerateTime()
    {
        return myMaxGenerateTime;
    }
    
    /**
     * Get the customer count.
     * @return Customer count.
     */
    public int getCustomerCount()
    {
        return myNumberOfCustomers;
    }
    
    /**
     * Get the max service time.
     * @return Max service time.
     */
    public int getMaxServiceTime()
    {
        return myMaxServiceTime;
    }
    
    /**
     * Get the number of queues.
     * @return Number of queues.
     */
    public int getQueues()
    {
        if (myModel == null) return 0;
        return myModel.getQueueCount();
    }
    
    /**
     * Get the queue size of the ith queue.
     * @param i Index of the queue.
     * @return Size of the given queue.
     */
    public int getQueueSize(int i)
    {
        return myModel.getQueue(i).size();
    }
    
    /**
     * Get the ServiceQueueManager being used by this controller.
     * @return ServiceQueueManager utilized by the controller.
     */
    public ServiceQueueManager getModel()
    {
        return myModel;
    }
    
    /**
     * Suspend the simulation.
     */
    public void suspendSimulation()
    {
        this.suspend();
        myGenerator.suspend();
        for (int i = 0; i < myCashiers.length; i++)
        {
            myCashiers[i].suspend();
        }
    }
    
    /**
     * Resume the simulation.
     */
    public void resumeSimulation()
    {
        mySuspendSignal = false;
        this.resume();
        for (int i = 0; i < myCashiers.length; i++)
        {
            myCashiers[i].resume();
        }
        myGenerator.resume();
    }
    
    /**
     * Set the cashier statistics text.
     * @param i Index of the cashier's statistics set.
     */
    public void setCashierView(Integer i)
    {
        myView.setCurrentCashier(i);
        this.updateView();
    }
}