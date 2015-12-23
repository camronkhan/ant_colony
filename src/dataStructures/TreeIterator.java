package dataStructures;

/**
 *	interface TreeIterator
 *
 *	encapsulates the most basic functionality for traversing a tree
 *
 *	Written by Roger West, University of Illinois at Springfield
 */
public interface TreeIterator extends Iterator
{
	/**
	 *	add the specified item at the current position of the traversal
	 */
	public boolean add(Object obj);


	/**
	 *	remove the current item in the traversal
	 */
	public boolean remove();
}





