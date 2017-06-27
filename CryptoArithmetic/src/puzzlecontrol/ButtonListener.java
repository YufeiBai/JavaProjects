/**
 * A button listener to implement reflection with.
 * 
 * @author Brandon Belna (bbelna@stetson.edu)
 */
package puzzlecontrol;

import java.lang.reflect.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.lang.IllegalAccessException;

public class ButtonListener extends MouseAdapter
{
    private Object myController;
    private Method myMethod;
    private Object[] myArguments;
    
    /**
     * Constructor for our ButtonListener.
     * @param controller The controller handling the game.
     * @param methods Which methods to invoke upon requested actions.
     * @param args The arguments to pass through.
     */
    public ButtonListener(Object controller, Method method, Object[] args)
    {
        myController = controller;
        myMethod = method;
        myArguments = args;
    }
    
    /**
     * Mouse released method invoker. Calls myMethod.
     */
    public void mouseReleased(MouseEvent event)
    {
        Method releaseMethod;
        Object controller;
        Object[] arguments;
        
        releaseMethod = this.getMethod();
        controller = this.getController();
        arguments = this.getArguments();
        
        try
        {
            releaseMethod.invoke(controller, arguments);
        }
        catch(InvocationTargetException exception)
        {
            System.out.println("InvocationTargetException");
        }
        catch(IllegalAccessException exception)
        {
            System.out.println("IllegalAccessException");
        }
        catch(IllegalArgumentException exception)
        {
            System.out.println("IllegalArgumentException");
        }
    }
    
    /**
     * Return the methods associated with this listener.
     * @return Methods associated with this listener.
     */
    protected Method getMethod()
    {
        return myMethod;
    }
    
    /**
     * Set the methods for this listener.
     * @param methods New methods to set for listener.
     */
    protected void setMethod(Method method)
    {
        myMethod = method;
    }

    /**
     * Get the controller of this listener.
     * @return Controller of listener.
     */
    protected Object getController()
    {
        return myController;
    }
    
    /**
     * Set the controller of this listener.
     * @param controller The new controller of the listener.
     */
    protected void setController(Object controller)
    {
        myController = controller;
    }
    
    /**
     * Get the arguments associated with this listener.
     * @return The arguments of the listener.
     */
    protected Object[] getArguments()
    {
        return myArguments;
    }
    
    /**
     * Set the arguments associated with this listener.
     * @param arguments The new arguments for this listener.
     */
    protected void setArguments(Object[] arguments)
    {
        myArguments = arguments;
    }
}