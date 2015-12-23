package dataStructures;

/**
 *	OrderedList interface
 *	Encapsulates the basic functionality for an ordered list: a data structure
 *	in which:
 *		1) each item is associated with a particular position, or index, in the list
 *		2) the items are stored in an ordered fashion
 *
 *	Written by Roger West, University of Illinois at Springfield
 */
public interface OrderedList extends TraversableOrderedCollection
{

	/**
	 *	return the item at the specified index
	 */
	public Comparable get(int index);


	/**
	 *	insert the specified item at the specified index
	 */
	public boolean add(int index, Comparable obj);


	/**
	 *	replace the item at the specified index with the specified item
	 */
	public Comparable set(int index, Comparable obj);


	/**
	 *	remove the item at the specified index
	 */
	public boolean remove(int index);


	/**
	 *	return an iterator specialized for traversing a List
	 *	traversal begins at the specified index
	 */
	public ListIterator listIterator(int index);


	/**
	 *	return the index of the specified item
	 */
	public int indexOf(Comparable obj);
	
	
	/**
	 *	remove the specified item from the List
	 */
	public boolean remove(Comparable obj);

}