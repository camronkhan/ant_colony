package dataStructures;

/**
 *	ArrayStack class
 *
 *	implementation of an array-based stack
 *
 *	Written by Roger West, University of Illinois at Springfield
 *	Adapted from code written by Mark Weiss in Data Structures and Problem
 *	Solving Using Java, 2nd edition, 2002
 */
public class ArrayStack implements Stack
{

	/*************
	 *	constants
	 ************/
	
	/** default array capacity */
	private static final int DEFAULT_CAPACITY = 10;
	
	
	/*************
	 *	attributes
	 ************/
	
	/** array to store items */
	private Object[] theItems;
	
	/** position of top item */
	private int topOfStack;
	
	/** current number of items in stack */
	private int theSize;


	/***************
	 *	constructors
	 **************/
	

	/**
	 *	return a new, empty ArrayStack with the default capacity
	 */
	public  ArrayStack()
	{
		// empty this ArrayStack
		clear();
	}


	/**********
	 *	methods
	 *********/
	

	/**
	 *	double the size of the array
	 */
	private void doubleArray()
	{
		// backup items
		Object[] old = theItems;
		
		// create new array
		theItems = new Object[theItems.length * 2 + 1];
		
		// copy items from backup into new array
		for(int i = 0; i < theSize; i++)
			theItems[i] = old[i];
	}
	
	
	/**************************************
	 *	methods inherited from class Object
	 *************************************/
	

	/**
	 *	return a String representation of the ArrayStack
	 *
	 *	items are listed in a single column, with the item at the top of the column
	 *	representing the top of the stack
	 */
	public String toString()
	{
		String s = "";
		
		for (int i = theSize - 1; i >= 0; i--)
		{
			if (i == 0)
				s += theItems[i].toString();
			else
				s += theItems[i].toString() + "\n";
		}
		
		return s;
	}
	
	
	/*****************************************
	 *	methods inherited from interface Stack
	 ****************************************/
	
	
	/**
	 *	return the item at the top of the stack; the item is not removed
	 *
	 *	throws UnderflowException if stack is empty
	 */
	public Object peek()
	{
		// throw exception if this ArrayStack is empty
		if(isEmpty())
			throw new UnderflowException("ArrayStack top");

		// return item at top of this ArrayStack
		return theItems[topOfStack];
	}


	/**
	 *	remove the item at the top of the stack
	 *
	 *	throws UnderflowException if stack is empty
	 */
	public Object pop()
	{
		// throw exception if this ArrayStack is empty
		if(isEmpty())
			throw new UnderflowException("ArrayStack pop");

		// subtract 1 from size of this ArrayStack
		theSize--;

		// return item at top of this ArrayStack and set top of this ArrayStack to topOfStack - 1
		return theItems[topOfStack--];
	}


	/**
	 *	add the specified item to the top of the stack
	 */
	public boolean push(Object obj)
	{
		// resize array if full
		if(topOfStack + 1 == theItems.length)
			doubleArray();

		// increment top of stack by 1, and store obj at that array index
		theItems[++topOfStack] = obj;
		
		// add 1 to size of this ArrayStack
		theSize++;
		
		// item successfully added
		return true;
	}
	
	
	/**********************************************
	 *	methods inherited from interface Collection
	 *********************************************/
	
	
	/**
	 *	add the specified item to the top of the stack
	 */
	public boolean add(Object obj)
	{
		// invoke push alias method
		push(obj);
		
		// add successful
		return true;
	}


	/**
	 *	remove the item at the top of the stack
	 *
	 *	throws UnderflowException if stack is empty
	 */
	public boolean remove()
	{
		// invoke pop alias method
		pop();
		
		// remove successful
		return true;
	}


	/**
	 *	empty the ArrayStack
	 *
	 *	items are physically removed
	 *	stack will be reset to the default capacity
	 *	size will be set to zero
	 */
	public void clear()
	{
		// create new, empty array with default capacity
		theItems = new Object[DEFAULT_CAPACITY];
		
		// set top of this ArrayStack to -1 to indicate empty stack
		topOfStack = -1;
		
		// set size to 0
		theSize = 0;
	}


	/**
	 *	return the number of items in the ArrayStack
	 */
	public int size()
	{
		return theSize;
	}


	/**
	 *	return true if the ArrayStack is empty
	 */
	public boolean isEmpty()
	{
		return theSize == 0;
	}


	/**
	 *	return the item at the top of the stack; the item is not removed
	 *
	 *	throws UnderflowException if stack is empty
	 */
	public Object get()
	{
		// invoke peek alias method
		return peek();
	}

}