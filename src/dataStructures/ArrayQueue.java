package dataStructures;

/**
 *	ArrayQueue class
 *
 *	implementation of an array-based queue
 *	the implementation uses a circular array to allow efficient utilization of
 *	the space in the array
 *
 *	Written by Roger West, University of Illinois at Springfield
 *	Adapted from code written by Mark Weiss in Data Structures and Problem
 *	Solving Using Java, 2nd edition, 2002
 */
public class ArrayQueue implements Queue
{

	/************
	 *	constants
	 ***********/
	
	
	/** default array capacity */
	private static final int DEFAULT_CAPACITY = 10;
	
	
	/*************
	 *	attributes
	 ************/
	
	
	/** array to store items */
	protected Object[] theItems;
	
	/** front position in queue */
	protected int front;
	
	/** rear position in queue */
	protected int back;
	
	/** current number of items in queue */
	protected int theSize;


	/***************
	 *	constructors
	 **************/
	

	/**
	 *	return a new, empty ArrayQueue with the default capacity
	 */
	public ArrayQueue()
	{
		// empty this ArrayQueue
		clear();
	}


	/**********
	 *	methods
	 *********/
	

	/**
	 *	method to handle wraparound of array indexes for circular array when
	 *	moving the front or back indexes forward
	 */
	protected int increment(int index)
	{
		// add 1 to index, and wrap around to beginning of array if necessary
		if(++index == theItems.length)
			index = 0;

		return index;
	}


	/**
	 *	double the size of the array
	 */
	protected void doubleArray()
	{
		// create new, double-size array
		Object[] doubledArray = new Object[theItems.length * 2 + 1];
		
		// copy items from current array into doubled array
		for(int i = 0; i < theSize; i++)
		{
			// copy current item
			doubledArray[i] = theItems[front];
			
			// increment front index
			front = increment(front);
		}
		
		// set theArray to the new, doubled array
		theItems = doubledArray;
		
		// reset front and back
		front = 0;
		back = theSize - 1;
	}


	/**************************************
	 *	methods inherited from class Object
	 *************************************/
	

	/**
	 *	return a String representation of the ArrayQueue
	 *
	 *	items are listed from left to right, in comma-delimited fashion,
	 *	with the leftmost item being the item at the front of the queue, and the
	 *	rightmost item being the item at the rear of the queue
	 */
	public String toString()
	{
		String s = "";

		if (!isEmpty())
			for (int i = front, j = 1; j <= theSize; i = increment(i), j++)
			{
				if (i == back)
				{
					s += theItems[i].toString();
					i = -1;
				}
				else
					s += theItems[i].toString() + ", ";
			}
		
		return s;
	}	


	/*****************************************
	 *	methods inherited from interface Queue
	 ****************************************/
	
	
	/**
	 *	return the item at the front of the queue; item is not removed
	 *
	 *	throws UnderflowException if queue is empty
	 */
	public Object getFront()
	{
		// throw exeption if this ArrayQueue is empty
		if(isEmpty())
			throw new UnderflowException("ArrayQueue getFront");

		// return the item at the front of this ArrayQueue
		return theItems[front];
	}


	/**
	 *	remove and return the item at the front of the queue
	 *
	 *	throws UnderflowException if queue is empty
	 */
	public Object dequeue()
	{
		// throw exeption if this ArrayQueue is empty
		if(isEmpty())
			throw new UnderflowException("ArrayQueue dequeue");

		// subtract 1 from size of this ArrayQueue
		theSize--;

		// store object being removed
		Object returnValue = theItems[front];
		
		// logically remove front item by incrementing front
		front = increment(front);

		// return item that was removed
		return returnValue;
	}


	/**
	 *	add the specified item to the rear of the queue
	 */
	public boolean enqueue(Object obj)
	{
		// resize array if full
		if(theSize == theItems.length)
			doubleArray();

		// set back to back + 1, or wwrap around to beginning of array if needed
		back = increment(back);
		
		// store obj at back of array
		theItems[back] = obj;
		
		// add 1 to size of this ArrayQueue
		theSize++;
		
		// item successfully added
		return true;
	}
	
	
	/**********************************************
	 *	methods inherited from interface Collection
	 *********************************************/
	
	
	/**
	 *	add the specified item to the rear of the queue
	 */
	public boolean add(Object obj)
	{
		// invoke enqueue alias method
		return enqueue(obj);
	}


	/**
	 *	remove the item at the front of the queue
	 *	return true if operation is successful
	 *
	 *	throws UnderflowException if queue is empty
	 */
	public boolean remove()
	{
		// invoke dequeue alias method
		dequeue();
		
		// remove successful
		return true;
	}


	/**
	 *	empty the ArrayQueue
	 *
	 *	items are physically removed
	 *	queue will be reset to the default capacity
	 *	size will be set to zero
	 */
	public void clear()
	{
		// create new, empty array with default capacity
		theItems = new Object[DEFAULT_CAPACITY];
		
		// set size to 0
		theSize = 0;
		
		// set front of this ArrayQueue to first array index
		front = 0;
		
		// set back of this ArrayQueue to -1 to indicate no items present
		back = -1;
	}


	/**
	 *	return the number of items in the ArrayQueue
	 */
	public int size()
	{
		return theSize;
	}


	/**
	 *	return true if the ArrayQueue is empty
	 */
	public boolean isEmpty()
	{
		return theSize == 0;
	}


	/**
	 *	return the item at the front of the queue; item is not removed
	 *
	 *	throws UnderflowException if queue is empty
	 */
	public Object get()
	{
		// return item at front of this ArrayQueue
		return getFront();
	}

}