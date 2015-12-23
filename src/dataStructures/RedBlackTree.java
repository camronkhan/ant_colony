package dataStructures;

/**
 *	RedBlackTree class
 *	
 *	implementation of a rooted tree in which each node of the tree can have a
 *	maximum of 2 child nodes, and the items in the tree are stored according to
 *	the following ordering rule:
 *		1)	items < a given item, X, will be located in X's left subtree
 *		2)	items > a given item, X, will be located in X's right subtree
 *		3)	duplicate items are not allowed
 *
 *	the tree also has balancing rules:
 *		1)	each node is colored either black or red
 *		2)	the root node is always black
 *		3)	there cannot be 2 consecutive red nodes in any path down the tree
 *		4)	all paths down the tree from the root must contain the same number of black nodes
 *
 *	Written by Roger West, University of Illinois at Springfield
 *	Adapted from code written by Mark Weiss in Data Structures and Problem
 *	Solving Using Java, 2nd edition, 2002
 */
public class RedBlackTree implements BinarySearchTree
{
	
	/************
	 *	constants
	 ***********/
	
	/** designates black node */
	private static final int BLACK = 1;
	
	/** designates red node */
    private static final int RED = 0;
    
	
	/*************
	 *	attributes
	 ************/
	
	/** used to avoid problems when tree is empty
	 *	header's right child is the root node
	 */
	private RedBlackNode header;

	/** number of items in tree */
	protected int theSize;
	
	/** current number of modifications to tree */
	protected int modCount;

	
	/** sentinel nodes */
	
	/** current node */
	private static RedBlackNode current;
	
	/** current's parent */
    private static RedBlackNode parent;
    
    /** current's grandparent */
    private static RedBlackNode grand;
    
    /** current's great-grandparent */
    private static RedBlackNode great;
    
    /** used for null reference */
    private static RedBlackNode nullNode;


	/**
	 *	static initializer for nullNode
	 *	allows nullNode to be used in a similar fashion to the null object
	 */
	static
	{
		// create nullNode; nullNode has no item
		nullNode = new RedBlackNode(null);
		
		// nullNode not linked to any other nodes
		nullNode.left = nullNode.right = nullNode;
	}

	
	/***************
	 *	constructors
	 **************/

	/**
	 *	create a new, empty RedBlackTree
	 */
	public RedBlackTree()
	{
		// empty this RedBlackTree
		clear();
	}
	
	
	/**
	 *	create a new RedBlackTree with the specified item in the root node
	 */
	public RedBlackTree(Comparable obj)
	{
		// empty this RedBlackTree
		clear();
		
		// create root node and set header to reference root node
		header.right = new RedBlackNode(obj);
		
		// add 1 to size of this RedBlackTree
		theSize++;
	}
	
	
	// methods
	
	/**
	 *	compare the specified item with the item in the specified node
	 *
	 *	returns 1 if node is the header node
	 *	returns value < 0 if obj < node item
	 *	returns value == 0 if obj.equals(node item)
	 *	returns value > 0 if obj > node item
	 */
	private final int compare(Comparable obj, RedBlackNode node)
	{
		if(node == header)
			return 1;
		else
			return obj.compareTo(node.theItem);
	}
    
	
	/**
	 *	internal method to restore balance at the node containing the specified item
	 *	ensures all red-black tree rules will be preserved
	 */
	private void balance(Comparable obj)
	{
		// swap colors of parent and children
		current.color = RED;
		current.left.color = BLACK;
		current.right.color = BLACK;

		if(parent.color == RED)
		{
			// 2 consecutive red nodes
			grand.color = RED;
			if((compare(obj, grand) < 0) != (compare(obj, parent) < 0))
			{
				// begin double rotation
				parent = rotate(obj, grand);
			}
			
			// do single rotation, or 2nd rotation of double rotation
			current = rotate(obj, great);
			
			// set color to BLACK
			current.color = BLACK;
		}
		
		// Make sure root is black
		header.right.color = BLACK;
	}

	
	/**
	 *	internal method to rotate the node containing the specified item around the 
	 *	node's parent
	 *
	 *	called by the balance method
	 */
	private RedBlackNode rotate(Comparable item, RedBlackNode parent)
	{
		if(compare(item, parent) < 0)
		{
			// item is in parent's left subtree
			if (compare(item, parent.left) < 0)
			{
				// item is in left subtree of parent's left child; do case 1 rotation
				parent.left = caseOneRotation(parent.left);
			}				
			else
			{
				// item is in right subtree of parent's left child; do case 4 rotation
				parent.left = caseFourRotation(parent.left);
			}	
			
			return parent.left;
		}
		else
		{
			// item is in parent's right subtree
			if (compare(item, parent.right) < 0)
			{
				// item is in left subtree of parent's right child; do case 1 rotation
				parent.right = caseOneRotation(parent.right);
			}
			else
			{
				// item is in right subtree of parent's right child; do case 4 rotation
				parent.right = caseFourRotation(parent.right);
			}
			
			return parent.right;
		}
	}
	
	
	/**
	 *	internal method for performing a Case 1 rotation
	 */
	private RedBlackNode caseOneRotation(RedBlackNode k2)
	{
		RedBlackNode k1 = k2.left;
		k2.left = k1.right;
		k1.right = k2;
		
		return k1;
	}
	
	
	/**
	 *	internal method for performing a Case 2 double rotation
	 */
	private RedBlackNode caseTwoRotation(RedBlackNode k3)
	{
		k3.left = caseFourRotation(k3.left);
        return caseOneRotation(k3);
	}
	
	
	/**
	 *	internal method for performing a Case 3 double rotation
	 */
	private RedBlackNode caseThreeRotation(RedBlackNode k3)
	{
		k3.right = caseOneRotation(k3.right);
        return caseFourRotation(k3);
	}
	
	
	/**
	 *	internal method for performing a Case 4 rotation
	 */
	private RedBlackNode caseFourRotation(RedBlackNode k2)
	{
		RedBlackNode k1 = k2.right;
		k2.right = k1.left;
		k1.left = k2;
		
		return k1;
	}
	
	
	// methods inherited from class BinarySearchTree
	
	/**
	 *	return the item in the root node
	 */
	public Comparable getRootItem()
	{
		// throw exception if this RedBlackTree is empty
		if (isEmpty())
			throw new NoSuchElementException("Tree is empty");
		
		// return item in root node
		return header.right.theItem;
	}
	
	
	/**
	 *	return the height of the specified node
	 *	returns -1 if node is null
	 */
	protected int height(RedBlackNode node)
	{
		return node == nullNode ? -1 : node.height;
	}
	
	
	/**
	 *	return the minimum item in the tree
	 *
	 *	throws NoSuchElementException if tree is empty
	 */
	public Comparable getMinItem()
	{
		// throw exception if this RedBlackTree is empty
		if (isEmpty())
			throw new NoSuchElementException("Tree is empty");
		
		// find minimum item and return it
		return getMinNode(header.right).theItem;
	}
	
	
	/**
	 *	beginning at the specified node, return the node containing the
	 *	minimum item
	 *
	 *	returns null if there is no minimum item
	 */
	protected RedBlackNode getMinNode(RedBlackNode node)
    {
		if(node != nullNode)
		{
			// iteratively branch left to find node containing min item
			while(node.left != nullNode)
				node = node.left;
		}

		return node;
    }
    
    
    /**
	 *	return the maximum item in the tree
	 *
	 *	throws NoSuchElementException if tree is empty
	 */
	public Comparable getMaxItem()
	{
		// throw exception if this RedBlackTree is empty
		if (isEmpty())
			throw new NoSuchElementException("Tree is empty");
		
		// find maximum item and return it
		return getMaxNode(header.right).theItem;
	}
    
    
	/**
	 *	beginning at the specified node, return the node containing the
	 *	maximum item
	 *
	 *	returns null if there is no maximum item
	 */
	protected RedBlackNode getMaxNode(RedBlackNode node)
    {
		if(node != nullNode)
		{
			// iteratively branch right to find node containing min item
			while(node.right != nullNode)
				node = node.right;
		}

		return node;
    }
    
    
    /**
     *	remove the minimum item from the tree
     *
     *	returns true if removal was successful, false otherwise
	 *	throws NoSuchElementException if tree is empty
     */
    public boolean removeMinItem()
    {
    	// throw exception if this RedBlackTree is empty
    	if (isEmpty())
			throw new NoSuchElementException("Tree is empty");
		
		// find min item and remove it
    	remove(getMinItem());
    	
    	// subtract 1 from size of this RedBlackTree
    	theSize--;
    	
    	// indicate a modification has been made
		modCount++;
    	
    	// removal successful
    	return true;
    }


    /**
     *	remove the maximum item from the tree
     *
     *	returns true if removal was successful, false otherwise
	 *	throws NoSuchElementException if tree is empty
     */
    public boolean removeMaxItem()
    {
    	// throw exception if this RedBlackTree is empty
    	if (isEmpty())
			throw new NoSuchElementException("Tree is empty");
		
		// find max item and remove it
    	remove(getMaxItem());
    	
    	// subtract 1 from size of this RedBlackTree
    	theSize--;
    	
    	// indicate a modification has been made
		modCount++;
    	
    	// removal successful
    	return true;
    }
    
    
    /***********************************************
     *	methods inherited from interface OrderedTree
     **********************************************/
    
    /**
	 *	return the height of the tree
	 */
	public int height()
	{
		return height(header.right);
	}
	
	
	/**
	 *	return the specified item
	 *
	 *	returns null if specified item doesn't exist
	 */
	public Comparable get(Comparable obj)
	{
		// start search at root node
		RedBlackNode node = header.right;
		
		// branch left or right, as needed, to find item
		while (node != nullNode)
		{
			if (obj.compareTo(node.theItem) < 0)
			{
				// branch left
				node = node.left;
			}	
			else if (obj.compareTo(node.theItem) > 0)
			{
				// branch right
				node = node.right;
			}
			else
			{
				// item found
				return node.theItem;
			}
		}
		
		// item is not in this RedBlackTree
		return null;
	}
	
	/**
	 *	remove the specified object from the tree
	 *
	 *	returns true if removal was successful, false otherwise
	 *	throws NoSuchElementException if tree is empty
	 *
	 *	all red-black tree rules are preserved
	 */
	public boolean remove(Comparable obj)
	{
		// throw exception if this RedBlackTree is empty
		if (isEmpty())
			throw new NoSuchElementException("Tree is empty");
		
		// initialize sibling reference
		RedBlackNode sibling = nullNode;
		
		
		// Step 1 - the root
		// special case - check if root node has item to remove
		if (obj.compareTo(header.right.theItem) == 0)
		{
			if(header.right.left != nullNode && header.right.right != nullNode)
			{
				// Two children
				header.right.theItem = getMinNode(header.right.right).theItem;
				obj = header.right.theItem;
			}
			else if (header.right.left == nullNode && header.right.right == nullNode)
			{
				// leaf node
				header.right = nullNode;
			}
			else
			{
				// one child
				if (header.right.left != nullNode)
					header.right = header.right.left;
				else
					header.right = header.right.right;
			}
		}

		if (header.right.left.color == BLACK && header.right.right.color == BLACK)
		{
			// root has 2 black children
			header.right.color = RED;
			great = header.right;
			grand = header.right;
			parent = header.right;
			current = compare(obj, header.right) < 0 ? header.right.left : header.right.right;
			sibling = (current == parent.left ? parent.right : parent.left);
		}
		else
		{
			// root has at least 1 red child
			current = parent = grand = header.right;
		}
		
		// Step 2 - Main Case
		while (current != nullNode)
		{
			if (current.left.color == BLACK && current.right.color == BLACK)
			{
				// subcase 2.1
				if (sibling.left.color == BLACK && sibling.right.color == BLACK)
				{
					// subcase 2.1A
					parent.color = (parent.color == BLACK ? RED : BLACK);
					current.color = (current.color == BLACK ? RED : BLACK);
					sibling.color = (sibling.color == BLACK ? RED : BLACK);
				}
				else if (current == parent.left && sibling.left.color == RED)
				{
					// subcase 2.1B
					grand = caseThreeRotation(parent);
					current.color = RED;
					parent.color = BLACK;
				}
				else if (current == parent.right && sibling.right.color == RED)
				{
					// subcase 2.1B_M (mirror image of 2.1B)
					grand = caseTwoRotation(parent);
					current.color = RED;
					parent.color = BLACK;
				}
				else if (current == parent.left && sibling.right.color == RED)
				{
					// subcase 2.1C
					current.color = RED;
					parent.color = BLACK;
					sibling.color = RED;
					sibling.right.color = BLACK;
					grand = caseFourRotation(parent);
				}
				else if (current == parent.right && sibling.left.color == RED)
				{
					// subcase 2.1C_M (mirror image of 2.1C)
					current.color = RED;
					parent.color = BLACK;
					sibling.color = RED;
					sibling.left.color = BLACK;
					grand = caseOneRotation(parent);
				}

				// update sentinels
				great = grand;
				grand = parent;
				parent = current;
				current = compare(obj, current) < 0 ? current.left : current.right;
				sibling = (current == parent.left ? parent.right : parent.left);
			}
			else
			{
				// subcase 2.2 - fall through
				great = grand;
				grand = parent;
				parent = current;
				current = compare(obj, current) < 0 ? current.left : current.right;
				sibling = (current == parent.left ? parent.right : parent.left);
			
				if (current.color == BLACK)
				{
					// new current node is black
					if (current == parent.left)
						grand = caseFourRotation(parent);
					else
						grand = caseOneRotation(parent);
						
					parent.color = RED;
					grand.color = BLACK;
				}
			}
			
			// Step 3 - remove node
			if (obj.compareTo(current.theItem) == 0)
			{
				if(current.left != nullNode && current.right != nullNode)
				{
					// two children
					
					current.theItem = getMinNode(current.right).theItem;
					obj = current.theItem;
				}
				else if (current.left == nullNode && current.right == nullNode)
				{
					// leaf node
					
					if (current == parent.left)
					{
						parent.left = nullNode;
						current = nullNode;
					}
					else
					{
						parent.right = nullNode;
						current = nullNode;
					}
				}
				else
				{
					// one child
					
					if (current == parent.left)
					{
						// the one child is the left child, which is a leaf
						if (current.left != nullNode && current.left.left == nullNode && current.left.right == nullNode)
						{
							current.theItem = current.left.theItem;
							current.left = nullNode;
							break;
						}
						else	// the one child is the left child, which is not a leaf
						{
							parent.left = (current.left != nullNode) ? current.left : current.right;
							current = parent.left;
						}
					}
					else
					{
						// the one child is the right child, which is a leaf
						if (current.right != nullNode && current.right.left == nullNode && current.right.right == nullNode)
						{
							current.theItem = current.right.theItem;
							current.right = nullNode;
							break;
						}
						else	// the one child is the right child, which is not a leaf
						{
							parent.right = (current.left != nullNode) ? current.left : current.right;
							current = parent.right;
						}
					}
				}
			}
		}
		
		// Step 4 - make sure root is black
		header.right.color = BLACK;
		
		// update size
		theSize--;
		
		// update modCount
		modCount++;
		
		// removal successful
		return true;
	}
	
	
	/**
	 *	return an iterator for traversing the tree in a preorder fashion
	 */
	public RedBlackTreeIterator preOrderIterator()
	{
		return new PreOrderIterator(this);
	}
	
	
	/**
	 *	return an iterator for traversing the tree in a postorder fashion
	 */
	public RedBlackTreeIterator postOrderIterator()
	{
		return new PostOrderIterator(this);
	}
	
	
	/**
	 *	return an iterator for traversing the tree in an inorder fashion
	 *	this iterator will return the items in ascending order
	 */
	public RedBlackTreeIterator inOrderIterator()
	{
		return new InOrderIterator(this);
	}
	
	
	/**
	 *	return an iterator for traversing the tree in a level-order fashion
	 */
	public RedBlackTreeIterator levelOrderIterator()
	{
		return new LevelOrderIterator(this);
	}
	
	
	// methods inherited from interface TraversableOrderedCollection
	
	/**
	 *	return default iterator for this tree
	 *	default iterator uses an inorder traversal
	 */
	public OrderedIterator iterator()
	{
		return new InOrderIterator(this);
	}
	
	
	/**
	 *	return whether the specified object exists in the tree
	 */
	public boolean contains(Comparable obj)
	{
		return (get(obj) != null);
	}
	
	
	// methods inherited from interface Collection
	
	
	/**
	 *	add the specified item to the tree
	 *	the item will be added such that the ordering of the tree is preserved
	 */
	public boolean add(Comparable obj)
	{
		// initalize sentinels
		current = parent = grand = header;
		
		// set item of nullNode
		nullNode.theItem = obj;

		// search for insertion point
		while (compare(obj, current) != 0)
		{
			// advance sentinels
			great = grand;
			grand = parent;
			parent = current;
			current = compare(obj, current) < 0 ? current.left : current.right;

			// fix balance if 2 red children
			if (current.left.color == RED && current.right.color == RED)
				balance(obj);
        }
        
		// insertion fails if item already present
		if (current != nullNode)
			throw new DuplicateItemException(obj.toString());
		
		// create new node
		current = new RedBlackNode(obj, nullNode, nullNode);

		// attach to parent
		if (compare(obj, parent) < 0)
			parent.left = current;
		else
			parent.right = current;

		// fix rule violations
		balance(obj);

		// update size
		theSize++;
		
		// indicate a modification has been made
		modCount++;
		
		// add successful
		return true;
	}
	
	
	/**
	 *	remove the first item in the RedBlackTree
	 *	by default, the first item is considered to be the root item
	 */
	public boolean remove()
	{
		// throw exception if this RedBlackTree is empty
		if (isEmpty())
			throw new NoSuchElementException("Tree is empty");
		
		// remove root item
		return remove(header.right.theItem);
	}
	
	
	/**
	 *	make the tree empty
	 */
	public void clear()
	{
		// reset header node
		header = new RedBlackNode(null);
		header.left = header.right = nullNode;
		
		// reset size to 0
		theSize = 0;
		
		// emptying tree counts as a modification
		modCount++;
	}
	
	
	/**
	 *	return the current size of the tree
	 */
	public int size()
	{
		return theSize;
	}
	
	
	/**
	 *	return true if the tree is empty
	 */
	public boolean isEmpty()
	{
		return theSize == 0;
	}
	
	
	/**
	 *	by default, return the item in the root node
	 *
	 *	throws NoSuchElementException if tree is empty
	 */
	public Comparable get()
	{
		// throw exception if this RedBlackTree is empty
		if (isEmpty())
			throw new NoSuchElementException("Empty Tree");
		
		// return item in root node
		return getRootItem();
	}
	
	
	public String toString()
	{
		if (isEmpty())
			return "*** empty tree ***";
		else
			return header.right.toString() + "\n" + toString(header.right, 1);
	}
	
	
	public String toString(RedBlackNode node, int level)
	{
		String s = "";
		
		if (node.left != nullNode)
		{
			for (int i = 1; i < 3 * level; i++)
				s += " ";
			
			s += node.left.toString() + "\n" + toString(node.left, level + 1);
		}
		else
		{
			for (int i = 1; i < 3 * level; i++)
				s += " ";
			
			s += "*\n";
		}
		
		if (node.right != nullNode)
		{
			for (int i = 1; i < 3 * level; i++)
				s += " ";
			
			s += node.right.toString() + "\n" + toString(node.right, level + 1);
		}
		else
		{
			for (int i = 1; i < 3 * level; i++)
				s += " ";
			
			s += "*\n";
		}
		
		return s;
	}
	

	/****************
	 *	inner classes
	 ***************/

	/**
	 *	inner abstract class RedBlackTreeIterator
	 *	encapsulates the basic functionality of an iterator for an RedBlackTree
	 */
	protected abstract class RedBlackTreeIterator implements OrderedIterator
	{
		
		/*************
		 *	attributes
		 ************/
		
		/** the RedBlackTree that is being iterated */
		protected RedBlackTree theTree;
		
		/** number of tree modifications the iterator is aware of */
		protected int expectedModCount;
		
		
		/***************
		 *	constructors
		 **************/
		
		/**
		 *	create a new RedBlackTreeIterator to traverse the specified RedBlackTree
		 */
		public RedBlackTreeIterator(RedBlackTree tree)
		{
			// set RedBlackTree being traversed
 			theTree = tree;
 			
 			// sync mod counts of RedBlackTree and this RedBlackTreeIterator
 			expectedModCount = modCount; 			
 		}
		
		
		/**********
		 *	methods
		 *********/
 		
 		/**
 		 *	return the current RedBlackNode object
 		 *	only accessible within RedBlackTreeIterator and its subclasses
 		 */
 		protected abstract RedBlackNode getCurrentNode();
 		
 		
 		// methods inherited from interface OrderedIterator
 		
 		/**
 		 *	return the item in the current node
 		 *	throws a NoSuchElementException if current node is null
 		 */
 		public Comparable getCurrent()
 		{
 			// retrieve current node in traversal
 			RedBlackNode node = getCurrentNode();
 			
 			if (node == nullNode)
 			{
 				// node is invalid
 				throw new NoSuchElementException("invalid node");
 			}	
 			else
 			{
 				// return item in current node of traversal
 				return node.theItem;
 			}
 		}

		
		/**
		 *	return true if there are more nodes to traverse
		 */
		public abstract boolean hasNext();


		/**
 		 *	advance to the next node in the traversal
 		 */
		public abstract void next();
		
	}
	
	
	/**
	 *	inner class PreOrderIterator
	 *
	 *	traverses an RedBlackTree in preorder fashion:
	 *		parent --> left child --> right child
	 */
	protected class PreOrderIterator extends RedBlackTreeIterator
	{
		
		/*************
		 *	attributes
		 ************/
		
		/** stores nodes yet to be traversed */
		protected Stack s;

		
		/***************
		 *	constructors
		 **************/
		
		/**
		 *	create a new PreOrderIterator for the specified RedBlackTree
		 *	iteration begins with the root node
		 */
		public PreOrderIterator(RedBlackTree tree)
		{
			// invoke constructor of superclass, BinarySearchTree
 			super(tree);
 			
 			// initialize stack
 			s = new ArrayStack();
 			
 			// if root exists, add root node to stack
 			if (theTree.header.right != nullNode)
				s.push(theTree.header.right);
 		}
 		
 		
 		/****************************************************
 		 *	methods inherited from class RedBlackTreeIterator
 		 ***************************************************/
 		
 		/**
 		 *	return the current RedBlackNode in the iteration
 		 *	return null if the stack is empty
 		 */
 		protected RedBlackNode getCurrentNode()
 		{
 			if (s.isEmpty())
 			{
 				// stack is empty, so there is no current node
 				return nullNode;
 			}
 			
 			// return current node of traversal
 			return (RedBlackNode)s.peek();
 		}
 		
 		
 		/********************************************
 		 *	methods inherited from interface Iterator
 		 *******************************************/
 		
 		/**
 		 *	return true if there are more nodes to traverse
 		 */
 		public boolean hasNext()
 		{
 			// throw exception if RedBlackTree modified outside this PreOrderIterator
 			if(expectedModCount != modCount)
				throw new ConcurrentModificationException();
			
			// if more items to traverse, stack will not be empty
 			return !s.isEmpty();
 		}
 		
 		
 		/**
 		 *	advance to the next node in the traversal
 		 */
		public void next()
		{
			// set current node to the node on top of the stack
			RedBlackNode current = (RedBlackNode)s.pop();
		
			// push right child onto stack
			if (current.right != nullNode)
				s.push(current.right);
		
			// push left child onto stack
			if (current.left != nullNode)
				s.push(current.left);
		}
	}
	
	
	/**
	 *	inner class PostOrderIterator
	 *
	 *	traverses an RedBlackTree in postorder fashion:
	 *		left child --> right child --> parent
	 */
	protected class PostOrderIterator extends RedBlackTreeIterator
	{
		
		/*************
		 *	attributes
		 ************/
		
		/** stores nodes yet to be traversed */
		protected Stack s;
		
		/** stores current RedBlackNode */
		protected RedBlackNode current;
		
		
		/***************
		 *	constructors
		 **************/
		
		/**
		 *	create a new PostOrderIterator for the specified RedBlackTree
		 *	iteration begins with the root node
		 */
		public PostOrderIterator(RedBlackTree tree)
		{
			// invoke constructor of superclass, BinarySearchTree
 			super(tree);
 			
 			// initialize stack
 			s = new ArrayStack();
 			
 			// if root exists, add root node to stack
 			if (theTree.header.right != nullNode)
 			{
				s.push(new ItrNode(theTree.header.right));
				
				// need to prime stack
				next();
			}
 		}
 		
 		
 		/****************************************************
 		 *	methods inherited from class RedBlackTreeIterator
 		 ***************************************************/
 		
 		/**
 		 *	return the current RedBlackNode
 		 */
 		protected RedBlackNode getCurrentNode()
 		{
 			return current;
 		}
 		
 		
 		/********************************************
 		 *	methods inherited from interface Iterator
 		 *******************************************/
 		
 		/**
 		 *	return true if there are more nodes to traverse
 		 */
 		public boolean hasNext()
 		{
 			// throw exception if RedBlackTree modified outside this PreOrderIterator
 			if(expectedModCount != modCount)
				throw new ConcurrentModificationException();
			
			// if more items to traverse, current node will not be nullNode
 			return current != nullNode;
 		}
 		
 		
 		/**
 		 *	advance to the next node in the traversal
 		 *	throws NoSuchElementException if attempt is made to traverse to a
 		 *	non-existent node
 		 */
		public void next()
		{
			// stores current ItrNode
			ItrNode cnode;
		
			// if the stack is empty and the current node is null, throw exception
			if (s.isEmpty())
			{
				// throw exception if current node is null
				if (current == nullNode)
					throw new NoSuchElementException();
				
				// set current node to nullNode to indicate end of traversal
				current = nullNode;
				
				return;
			}
		
			// process the stack
			for ( ; ; )
			{
				// get the top node on the stack
				cnode = (ItrNode)s.pop();
			
				// if this is the 3rd time cnode has been popped, ready to process cnode
				if (++cnode.timesPopped == 3)
				{
					current = cnode.node;
					return;
				}
			
				// not ready to process cnode yet, so push it back onto the stack
				s.push(cnode);
			
				if (cnode.timesPopped == 1)
				{
					// first time cnode is popped; proceed to left child
					if (cnode.node.left != nullNode)
						s.push(new ItrNode(cnode.node.left));
				}
				else
				{
					// 2nd time cnode is popped; proceed to right child
					if (cnode.node.right != nullNode)
						s.push(new ItrNode(cnode.node.right));
				}
			}
		}
		
		
		/**
	 	 * inner class ItrNode
	 	 * represents a node that maintains the number of times it has been popped
	 	 * off the stack
	 	 */
		protected class ItrNode
		{
		
			/*************
			 *	attributes
			 ************/
			
			/** stores the RedBlackNode */
			RedBlackNode node;
			
			/** # of times popped off the Stack */
			int timesPopped;
		
		
			/****************
			 *	 constructors
			 ***************/
		
			/**
		 	 * create a new ItrNode that contains the specified RedBlackNode
		 	 * timesPopped = 0
		 	 */
			ItrNode(RedBlackNode theNode)
			{
				node = theNode;
				timesPopped = 0;
			}
			
			
			/**********
			 *	methods
			 *********/
			
			/**
			 *	return a String representation of the ItrNode
		 	 *	by default, this method simply returns the String representation of
		 	 *	the data item in the RedBlackNode
		 	 */
		 	public String toString()
		 	{
		 		return node.theItem.toString();
		 	}
		}
	}
	
	
	/**
	 *	inner class InOrderIterator
	 *
	 *	traverses an RedBlackTree in inorder fashion:
	 *		left child --> parent --> right child
	 */
	protected class InOrderIterator extends PostOrderIterator
	{
		
		/***************
		 *	constructors
		 **************/
		
		/**
		 *	create a new InOrderIterator for the specified RedBlackTree
		 *	iteration begins with the root node
		 */
		public InOrderIterator(RedBlackTree tree)
		{
			// invoke constructor of superclass, BinarySearchTree
 			super(tree);
 			
 			// initialize stack
 			s = new ArrayStack();
 			
 			// if root exists, add root node to stack
 			if (theTree.header.right != nullNode)
 			{
				s.push(new ItrNode(theTree.header.right));
				
				// need to prime stack
				next();
			}
 		}
 		
 		
 		/****************************************************
 		 *	methods inherited from class RedBlackTreeIterator
 		 ***************************************************/
 		
 		/**
 		 *	return the current RedBlackNode
 		 */
 		protected RedBlackNode getCurrentNode()
 		{
 			return current;
 		}
 		
 		
 		/********************************************
 		 *	methods inherited from interface Iterator
 		 *******************************************/
 		
 		/**
 		 *	return true if there are more nodes to traverse
 		 */
 		public boolean hasNext()
 		{
 			// throw exception if RedBlackTree modified outside this InOrderIterator
 			if(expectedModCount != modCount)
				throw new ConcurrentModificationException();
			
			// if more items to traverse, current node will not be nullNode
 			return current != nullNode;
 		}
 		
 		
 		/**
 		 *	advance to the next node in the traversal
 		 *	throws NoSuchElementException if attempt is made to traverse to a
 		 *	non-existent node
 		 */
		public void next()
		{
			// stores current ItrNode
			ItrNode cnode;
		
			// if the stack is empty and the current node is null, throw exception
			if (s.isEmpty())
			{
				if (current == nullNode)
					throw new NoSuchElementException();
				
				current = nullNode;
				return;
			}
		
			// process the stack
			for ( ; ; )
			{
				// get the top node on the stack
				cnode = (ItrNode)s.pop();
			
				if (++cnode.timesPopped == 2)
				{
					// 2nd time cnode has been popped; ready to process cnode; push right child onto stack
					current = cnode.node;
					if (cnode.node.right != nullNode)
						s.push(new ItrNode(cnode.node.right));
					
					return;
				}
			
				// 1st time cnode has been popped; push it back onto the stack and push left child onto stack
				s.push(cnode);
				if (cnode.node.left != nullNode)
					s.push(new ItrNode(cnode.node.left));
			}
		}
	}
	
	
	/**
	 *	inner class LevelOrderIterator
	 *
	 *	traverses an RedBlackTree in level-order fashion:
	 *		one level at a time, beginning with root and proceeding down the tree
	 *		at each level, traverse from left to right
	 */
	protected class LevelOrderIterator extends RedBlackTreeIterator
	{
		
		/*************
		 *	attributes
		 ************/
		
		/** stores nodes yet to be traversed */
		private ArrayQueue q;
		
		
		/***************
		 *	constructors
		 **************/
		
		/**
		 *	create a new LevelOrderIterator for the specified RedBlackTree
		 *	iteration begins with the root node
		 */
		public LevelOrderIterator(RedBlackTree tree)
		{
			// invoke constructor of superclass, BinarySearchTree
 			super(tree);

			// initialize queue
 			q = new ArrayQueue();
 			
 			// if root exists, add root node to queue
 			if (theTree.header.right != nullNode)
				q.enqueue(theTree.header.right);
 		}
 		
 		
 		/****************************************************
 		 *	methods inherited from class RedBlackTreeIterator
 		 ***************************************************/
 		
 		/**
 		 *	return the current RedBlackNode in the iteration
 		 *	return null if the queue is empty
 		 */		
 		protected RedBlackNode getCurrentNode()
 		{
 			if (q.isEmpty())
 				return nullNode;
 			
 			return (RedBlackNode)q.getFront();
 		}
 		
 		
 		/********************************************
 		 *	methods inherited from interface Iterator
 		 *******************************************/
 		
 		/**
 		 *	return true if there are more nodes to traverse
 		 */
 		public boolean hasNext()
 		{
 			// throw exception if RedBlackTree modified outside this LevelOrderIterator
 			if(expectedModCount != modCount)
				throw new ConcurrentModificationException();
			
			// if more items in traversal, queue will not be empty
 			return !q.isEmpty();
 		}
 		
 		
 		/**
 		 *	advance to the next node in the traversal
 		 */
		public void next()
		{
			// retrieve next node from queue
			RedBlackNode current = (RedBlackNode)q.dequeue();

			// enqueue current node's left child, if it exists
			if (current.left != nullNode)
				q.enqueue(current.left);
			
			// enqueue current node's right child, if it exists
			if (current.right != nullNode)
				q.enqueue(current.right);
		}
	}
	
	
	/**
	 *	nested class RedBlackNode
	 *	stores a data item, references to left and right child nodes, height, and color
	 */
	private static class RedBlackNode
	{

		/*************
		 *	attributes
		 ************/
		
		/** data item */
		Comparable theItem;
		
		/** left child */
		RedBlackNode left;
		
		/** right child */
		RedBlackNode right;
		
		/** node height */
		int height;
		
		/** node color */
		int color;


		/***************
		 *	constructors
		 **************/

		/**
		 *	create a new RedBlackNode containing the specified item
		 *	left and right child nodes will be null
		 */
		public RedBlackNode(Comparable obj)
		{
			theItem = obj;
			left = nullNode;
			right = nullNode;
			color = RedBlackTree.BLACK;
		}

		
		/**
		 *	create a new RedBlackNode containing the specified item, with the
		 *	specified left and right child nodes
		 */
		public RedBlackNode(Comparable obj, RedBlackNode leftChild, RedBlackNode rightChild)
		{
			theItem = obj;
			left = leftChild;
			right = rightChild;
			color = RedBlackTree.BLACK;
		}
		
		
		public String toString()
		{
			return theItem.toString() + "(" + (color == RedBlackTree.BLACK ? "b" : "r") + ")";
		}

	}
}