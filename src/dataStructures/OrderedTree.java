package dataStructures;

/**
 *	OrderedTree interface
 *	Encapsulates the basic functionality for a tree data structure in which the
 *	items are stored according to some ordering rule
 *
 *	Written by Roger West, University of Illinois at Springfield
 */
public interface OrderedTree extends TraversableOrderedCollection
{

	/**
	 *	return the height of the tree
	 *	the height of the tree is defined as the length of the path from the
	 *	root to the deepest leaf
	 */
	public int height();


	/**
	 *	return the first occurrence of the specified item
	 *
	 *	returns null if specified item doesn't exist
	 */
	public Comparable get(Comparable obj);

}