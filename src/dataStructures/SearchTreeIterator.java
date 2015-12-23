package dataStructures;

/**
 *	Interface SearchTreeIterator
 *
 *	Encapsulates the most basic functionality for traversing a search tree.
 *
 *	Written by Roger West, University of Illinois at Springfield
 */
public interface SearchTreeIterator extends TreeIterator
{
	/**
	 *	Return the depth of this SearchTreeIterator in the traversal.
	 */
	public int depth();
	
	
	/**
	 *	Remove the subtree whose root item is the current item in the traversal.
	 */
	public boolean removeSubtree();
}