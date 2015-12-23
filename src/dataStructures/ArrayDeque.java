package dataStructures;

/**
 *	ArrayDeque class
 *
 *	implementation of an array-based deque
 *	the implementation uses a circular array to allow efficient utilization of
 *	the space in the array
 *
 *	Written by Roger West, University of Illinois at Springfield
 *	Adapted from code written by Mark Weiss in Data Structures and Problem
 *	Solving Using Java, 2nd edition, 2002
 */
public class ArrayDeque extends ArrayQueue implements Deque
{

	/**********
	 *	methods
	 *********/
	

	/**
	 *	method to handle wraparound of array indexes for circular array when
	 *	moving the front or back indexes backward
	 */
	private int decrement(int index)
	{
		// decrement index and wraparound to end of array if necessary
		if (--index < 0)
			index = theItems.length - 1;
		
		return index;
	}
	
	
	/*****************************************
	 *	methods inherited from interface Deque
	 ****************************************/
	
	
	/**
	 *	add the specified item to the front of the deque
	 */
	public void addFront(Object obj)
	{
		// resize array if full
		if (theSize == theItems.length)
			doubleArray();
		
		// set front to front - 1, or wrap around to end of array if needed
		front = decrement(front);
		
		// place obj in array at front index
		theItems[front] = obj;
		
		// add 1 to size of this ArrayDeque
		theSize++;
	}


	/**
	 *	add the specified item to the back of the deque
	 */
	public void addBack(Object obj)
	{
		// invoke ArrayQueue method for adding an item
		enqueue(obj);
	}


	/**
	 *	remove and return the item at the front of the deque
	 *
	 *	throws UnderflowException if deque is empty
	 */
	public Object removeFront()
	{
		// invoke ArrayQueue method for removing an item
		return dequeue();
	}


	/**
	 *	remove and return the item at the rear of the deque
	 *
	 *	throws UnderflowException if deque is empty
	 */
	public Object removeBack()
	{
		// throw exception if this ArrayDeque is empty
		if (isEmpty())
			throw new UnderflowException("Empty Deque");
		
		// store old value at back of this ArrayDeque
		Object returnValue = theItems[back];
		
		// subtract 1 from size of this ArrayDeque
		theSize--;
		
		// set back to back - 1, or wrap around to end of array if needed
		back = decrement(back);

		return returnValue;
	}


	/**
	 *	return the item at the rear of the queue; item is not removed
	 *
	 *	throws UnderflowException if queue is empty
	 */
	public Object getBack()
	{
		// throw exception if this ArrayDeque is empty
		if (isEmpty())
			throw new UnderflowException("Empty Deque");
		
		// return the item at the back of this ArrayDeque
		return theItems[back];
	}

}