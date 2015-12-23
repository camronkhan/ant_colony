package dataStructures;

/**
 *	Deque (double-ended queue) interface
 *
 *	Encapsulates the basic functionality for a deque
 *	items may only be added/removed to the front/rear of the deque
 *
 *	Written by Roger West, University of Illinois at Springfield
 */
public interface Deque extends Collection
{

	/**
	 *	add the specified item to the front of the deque
	 */
	public void addFront(Object obj);


	/**
	 *	add the specified item to the rear of the deque
	 */
	public void addBack(Object obj);


	/**
	 *	remove and return the item at the front of the deque
	 */
	public Object removeFront();


	/**
	 *	remove and return the item at the rear of the deque
	 */
	public Object removeBack();


	/**
	 *	return the item at the front of the deque; the item is not removed
	 */
	public Object getFront();


	/**
	 *	return the item at the rear of the deque; the item is not removed
	 */
	public Object getBack();

}





