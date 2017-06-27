
/**
 * A button listener to implement reflection with.
 * 
 * @author Brandon Belna (bbelna@stetson.edu)
 */

import java.lang.reflect.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.lang.IllegalAccessException;

public class Button extends MouseAdapter
{
	private Object myController;
	private Method[] myMethods;
	private Object[] myArguments;
	
	/**
	 * Constructor for our ButtonListener.
	 * @param controller The controller handling the game.
	 * @param methods Which methods to invoke upon requested actions.
	 * @param args The arguments to pass through.
	 */
	public Button(Object controller, Method[] methods, Object[] args)
	{
		myController = controller;
		myMethods = methods;
		myArguments = args;
	}
	
	/**
	 * Mouse released method invoker. Calls myMethods[0].
	 */
	public void mouseReleased(MouseEvent event)
	{
		Method releaseMethod;
		Object controller;
		Object[] arguments;
		
		releaseMethod = this.getMethods()[0];
		controller = this.getController();
		arguments = this.getArguments();
		
		try
		{
			releaseMethod.invoke(controller, arguments);
		}
		catch(InvocationTargetException exception)
		{
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
	 * Mouse entered method invoker. Calls myMethods[1].
	 */
	public void mouseEntered(MouseEvent event)
	{
		if (this.getMethods().length > 1)
		{
			Method enterMethod;
			Object controller;
			Object[] arguments;
			
			enterMethod = this.getMethods()[1];
			controller = this.getController();
			arguments = this.getArguments();
			
			try
			{
				enterMethod.invoke(controller, arguments);
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
	}
	
	/**
	 * Mouse exited method invoker. Calls myMethods[2].
	 */
	public void mouseExited(MouseEvent event)
	{
		if (this.getMethods().length > 2)
		{
			Method exitMethod;
			Object controller;
			
			exitMethod = this.getMethods()[2];
			controller = this.getController();
			
			try
			{
				exitMethod.invoke(controller, null);
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
	}
	
	/**
	 * Return the methods associated with this listener.
	 * @return Methods associated with this listener.
	 */
	protected Method[] getMethods()
	{
		return myMethods;
	}
	
	/**
	 * Set the methods for this listener.
	 * @param methods New methods to set for listener.
	 */
	protected void setMethod(Method[] methods)
	{
		myMethods = methods;
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