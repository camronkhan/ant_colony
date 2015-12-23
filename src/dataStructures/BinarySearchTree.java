package dataStructures;

/**
 *	BinarySearchTree interface
 *	
 *	Encapsulates the basic functionality of an ordered tree in which each node
 *	of the tree can have a maximum of 2 child nodes, and the items in the tree
 *	are stored according to the following ordering rule:
 *		1)	items < a given item, X, will be located in X's left subtree
 *		2)	items > a given item, X, will be located in X's right subtree
 *		3)	duplicate items are not allowed
 *
 *	Written by Roger West, University of Illinois at Springfield
 */
public interface BinarySearchTree extends OrderedTree
{

	/**
	 *	return the minimum item in the tree
	 */
	public Comparable getMinItem();
	
	
	/**
	 *	return the maximum item in the tree
	 */
	public Comparable getMaxItem();
	
	
	/**
	 *	remove the minimum item from the tree
	 */
	public boolean removeMinItem();
	
	
	/**
	 *	remove the maximum item from the tree
	 */
	public boolean removeMaxItem();
}