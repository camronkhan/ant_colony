package dataStructures;


/**
 *	AVLTree class
 *	
 *	implementation of a rooted tree in which each node of the tree can have a
 *	maximum of 2 child nodes, and the items in the tree are stored according to
 *	the following ordering rule:
 *		1)	items < a given item, X, will be located in X's left subtree
 *		2)	items > a given item, X, will be located in X's right subtree
 *		3)	duplicate items are not allowed
 *
 *	the tree also has a balancing rule:
 *		for any given node, X, the heights of X's left and right subtrees can
 *		differ by no more than 1
 *
 *	Written by Roger West, University of Illinois at Springfield
 *	Adapted from code written by Mark Weiss in Data Structures and Problem
 *	Solving Using Java, 2nd edition, 2002
 */
public class AVLTree implements BinarySearchTree
{

	/*************
	 *	attributes
	 ************/
	
	/** root node */
	private AVLNode root;
	
	/** number of items in tree */
	protected int theSize;
	
	/** current number of modifications to tree */
	protected int modCount;


	/***************
	 *	constructors
	 **************/

	/**
	 *	create a new, empty AVLTree
	 */
	public AVLTree()
	{
		clear();
	}
	
	/**
	 *	create a new AVLTree with the specified item in the root node
	 */
	public AVLTree(Comparable obj)
	{
		clear();
		root = add(obj, root);
	}
	
	
	/**********
	 *	methods
	 *********/
	
	/**
	 *	Fix a case 1 imbalance
	 */
	private AVLNode caseOneRotation(AVLNode k2)
	{
		AVLNode k1 = k2.left;
		k2.left = k1.right;
		k1.right = k2;
		k2.height = Math.max(height(k2.left), height(k2.right)) + 1;
		k1.height = Math.max(height(k1.left), k2.height) + 1;
		
		return k1;
	}
	
	
	/**
	 *	Fix a case 2 imbalance
	 */
	private AVLNode caseTwoRotation(AVLNode k3)
	{
		k3.left = caseFourRotation(k3.left);
        return caseOneRotation(k3);
	}
	
	
	/**
	 *	Fix a case 3 imbalance
	 */
	private AVLNode caseThreeRotation(AVLNode k3)
	{
		k3.right = caseOneRotation(k3.right);
        return caseFourRotation(k3);
	}
	
	
	/**
	 *	Fix a case 4 imbalance
	 */
	private AVLNode caseFourRotation(AVLNode k2)
	{
		AVLNode k1 = k2.right;
		k2.right = k1.left;
		k1.left = k2;
		k2.height = Math.max(height(k2.left), height(k2.right)) + 1;
		k1.height = Math.max(height(k1.right), k2.height) + 1;
		
		return k1;
	}
	
	
	/************************************************
	 *	methods inherited from class BinarySearchTree
	 ***********************************************/
	
	/**
	 *	return the item in the root node
	 */
	public Comparable getRootItem()
	{
		if (isEmpty())
			throw new NoSuchElementException("Tree is empty");
		
		return root.theItem;
	}
	
	
	/**
	 *	return the height of the specified node
	 *	returns -1 if node is null
	 */
	protected int height(AVLNode node)
	{
		return node == null ? -1 : node.height;
	}
	
	
	/**
	 *	return the minimum item in the tree
	 *
	 *	throws NoSuchElementException if tree is empty
	 */
	public Comparable getMinItem()
	{
		if (isEmpty())
			throw new NoSuchElementException("Tree is empty");
		
		return getMinNode(root).theItem;
	}
		
	
	/**
	 *	beginning at the specified node, return the node containing the
	 *	minimum item
	 *
	 *	returns null if there is no minimum item
	 */
	protected AVLNode getMinNode(AVLNode node)
    {
		if(node != null)
			while(node.left != null)
				node = node.left;

		return node;
    }
    
    
    /**
	 *	return the maximum item in the tree
	 *
	 *	throws NoSuchElementException if tree is empty
	 */
	public Comparable getMaxItem()
	{
		if (isEmpty())
			throw new NoSuchElementException("Tree is empty");
		
		return getMaxNode(root).theItem;
	}
    
    
	/**
	 *	beginning at the specified node, return the node containing the
	 *	maximum item
	 *
	 *	returns null if there is no maximum item
	 */
	protected AVLNode getMaxNode(AVLNode node)
    {
		if(node != null)
			while(node.right != null)
				node = node.right;

		return node;
    }
    
    
    /**
     *	internal, recursive method for adding an item to the tree
     *
     *	throws DuplicateItemException if item already exists in tree
     */
    protected AVLNode add(Comparable obj, AVLNode node)
    {
		if(node == null)
			node = new AVLNode(obj);
		else if(obj.compareTo(node.theItem) < 0)
		{
			node.left = add(obj, node.left);
			if (height(node.left) - height(node.right) > 1)
				if (obj.compareTo(node.left.theItem) < 0)
				{
					// fix case 1 imbalance
					node = caseOneRotation(node);
				}
				else
				{
					// fix case 2 imbalance
					node = caseTwoRotation(node);
				}
		}			
		else if(obj.compareTo(node.theItem) > 0)
		{
			node.right = add(obj, node.right);
			if (height(node.right) - height(node.left) > 1)
				if (obj.compareTo(node.right.theItem) > 0)
				{
					// fix case 4 imbalance
					node = caseFourRotation(node);
				}
				else
				{
					// fix case 3 imbalance
					node = caseThreeRotation(node);
				}
		}			
		else
		{
			// Duplicate item
			throw new DuplicateItemException(obj.toString());
		}

		// set height of new node
		node.height = Math.max(height(node.left), height(node.right)) + 1;

		return node;
    }
	
	
	/**
	 *	internal, recursive method to remove an item
	 *	search for item begins at the specified node
	 */
	protected AVLNode remove(Comparable obj, AVLNode node)
    {
		if(node == null)
			throw new NoSuchElementException("Item does not exist");

		if(obj.compareTo(node.theItem) < 0)
		{
			// branch left
			node.left = remove(obj, node.left);

			// rebalance, if necessary
			if (Math.abs(height(node.right) - height(node.left)) > 1)
				if (height(node.right.left) <= height(node.right.right))
					node = caseFourRotation(node);
				else
					node = caseThreeRotation(node);
		}
		else if(obj.compareTo(node.theItem) > 0)
		{
			// branch right
			node.right = remove(obj, node.right);

			// rebalance, if necessary
			if (Math.abs(height(node.right) - height(node.left)) > 1)
				if (height(node.left.right) <= height(node.left.left))
					node = caseOneRotation(node);
				else
					node = caseTwoRotation(node);
		}
		else if(node.left != null && node.right != null)
		{
			// node found; has 2 children
			node.theItem = getMinNode(node.right).theItem;
			node.right = removeMinNode(node.right);

			// rebalance, if necessary
			if (Math.abs(height(node.right) - height(node.left)) > 1)
				if (height(node.left.right) <= height(node.left.left))
					node = caseOneRotation(node);
				else
					node = caseTwoRotation(node);
		}
		else
		{
			// node found; has 0 or 1 child
			node = (node.left != null) ? node.left : node.right;
		}

		// update height
		if (node != null)
			node.height = Math.max(height(node.left), height(node.right)) + 1;

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
    	if (isEmpty())
			throw new NoSuchElementException("Tree is empty");
		
    	root = removeMinNode(root);
    	
    	// update size
    	theSize--;
		modCount++;
    	
    	return true;
    }
    
    
    /**
     *	internal, recursive method for removing the minimum item
     *
     *	throws NoSuchElementException if the item does not exist
     */
    protected AVLNode removeMinNode(AVLNode node)
    {
		if(node == null )
			throw new NoSuchElementException("Item does not exist");
		else if(node.left != null)
		{
			// left child exists
			node.left = removeMinNode(node.left);

			// update height
			if (node.left == null && node.right == null)
				node.height = 0;
			else
				node.height = Math.max(height(node.left), height(node.right)) + 1;
			
			// rebalance, if necessary
			if (Math.abs(height(node.right) - height(node.left)) > 1)
				if (height(node.right.left) <= height(node.right.right))
					node = caseFourRotation(node);
				else
					node = caseThreeRotation(node);
			
			return node;
		}
		else
		{
			// node has no left child
			
			// update height
			if (node.left == null && node.right == null)
				node.height = 0;
			else
				node.height = Math.max(height(node.left), height(node.right)) + 1;
			
			// rebalance, if necessary
			if (Math.abs(height(node.right) - height(node.left)) > 1)
				if (height(node.right.left) <= height(node.right.right))
					node = caseFourRotation(node);
				else
					node = caseThreeRotation(node);
			
			return node.right;
		}
    }
    
    
    /**
     *	remove the maximum item from the tree
     *
     *	returns true if removal was successful, false otherwise
	 *	throws NoSuchElementException if tree is empty
     */
    public boolean removeMaxItem()
    {
    	if (isEmpty())
			throw new NoSuchElementException("Tree is empty");
		
    	root = removeMaxNode(root);
    	
    	// update size
    	theSize--;
		modCount++;
    	
    	return true;
    }
    
    
    /**
     *	internal, recursive method for removing the maximum item
     *
     *	throws NoSuchElementException if the item does not exist
     */
    protected AVLNode removeMaxNode(AVLNode node)
    {
		if(node == null )
			throw new NoSuchElementException("Item does not exist");
		else if(node.right != null)
		{
			// right child exists
			node.right = removeMaxNode(node.right);
			
			// update height
			if (node.left == null && node.right == null)
				node.height = 0;
			else
				node.height = Math.max(height(node.left), height(node.right)) + 1;
			
			// rebalance, if necessary
			if (Math.abs(height(node.right) - height(node.left)) > 1)
			{
				if (height(node.left.right) <= height(node.left.left))
					node = caseOneRotation(node);
				else
					node = caseTwoRotation(node);
			}
			
			return node;
		}
		else
		{
			// node has no right child
			
			// update height
			if (node.left == null && node.right == null)
				node.height = 0;
			else
				node.height = Math.max(height(node.left), height(node.right)) + 1;
			
			// rebalance, if necessary
			if (Math.abs(height(node.right) - height(node.left)) > 1)
			{
				if (height(node.left.right) <= height(node.left.left))
					node = caseOneRotation(node);
				else
					node = caseTwoRotation(node);
			}
			
			return node.left;
		}
    }
	
	
	/***********************************************
	 *	methods inherited from interface OrderedTree
	 **********************************************/
	
	
	/**
	 *	return the height of the tree
	 */
	public int height()
	{
		return height(root);
	}
	
	
	/**
	 *	return the specified item
	 *
	 *	returns null if specified item doesn't exist
	 */
	public Comparable get(Comparable obj)
	{
		AVLNode node = root;
		
		while (node != null)
		{
			if (obj.compareTo(node.theItem) < 0)
				node = node.left;
			else if (obj.compareTo(node.theItem) > 0)
				node = node.right;
			else
				return node.theItem;
		}
		
		return null;
	}
	
	
	/**
	 *	remove the specified object from the tree
	 *
	 *	returns true if removal was successful, false otherwise
	 *	throws NoSuchElementException if tree is empty
	 */
	public boolean remove(Comparable obj)
	{
		if (isEmpty())
			throw new NoSuchElementException("Tree is empty");
		
		root = remove(obj, root);
		
		// update size
		theSize--;
		modCount++;
		
		return true;
	}
	
	
	/**
	 *	return an iterator for traversing the tree in a preorder fashion
	 */
	public AVLTreeIterator preOrderIterator()
	{
		return new PreOrderIterator(this);
	}
	
	
	/**
	 *	return an iterator for traversing the tree in a postorder fashion
	 */
	public AVLTreeIterator postOrderIterator()
	{
		return new PostOrderIterator(this);
	}
	
	
	/**
	 *	return an iterator for traversing the tree in an inorder fashion
	 *	this iterator will return the items in ascending order
	 */
	public AVLTreeIterator inOrderIterator()
	{
		return new InOrderIterator(this);
	}
	
	
	/**
	 *	return an iterator for traversing the tree in a level-order fashion
	 */
	public AVLTreeIterator levelOrderIterator()
	{
		return new LevelOrderIterator(this);
	}
	
	
	/****************************************************************
	 *	methods inherited from interface TraversableOrderedCollection
	 ***************************************************************/
	
	
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


	/**********************************************
	 *	methods inherited from interface Collection
	 *********************************************/
	
	
	/**
	 *	add the specified item to the tree
	 *	the item will be added such that the ordering of the tree is preserved
	 */
	public boolean add(Comparable obj)
	{
		root = add(obj, root);
		
		// update size
		theSize++;
		modCount++;
		
		return true;
	}


	/**
	 *	remove the first item in the AVLTree
	 *	by default, the first item is considered to be the root item
	 */
	public boolean remove()
	{	
		if (isEmpty())
			throw new NoSuchElementException("Tree is empty");
		
		// remove root item
		return remove(root.theItem);
	}
	
	
	/**
	 *	make the tree empty
	 */
	public void clear()
	{
		root = null;
		theSize = 0;
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
		if (isEmpty())
			throw new NoSuchElementException("Empty Tree");
		
		return getRootItem();
	}
	
	
	/**
	 *	return a String representation of this AVLTree
	 */
	public String toString()
	{
		if (isEmpty())
			return "";
		else
			return root.toString() + "\n" + toString(root, 1);
	}
	
	
	/**
	 *	return a representation of this AVLTree in a format similar to that
	 *	found when browsing the contents of a file system using a GUI-based
	 *	component such as Windows Explorer
	 */
	private String toString(AVLNode node, int level)
	{
		String s = "";
		
		if (node.left != null)
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
		
		if (node.right != null)
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
	 *	inner abstract class AVLTreeIterator
	 *	encapsulates the basic functionality of an iterator for an AVLTree
	 */
	protected abstract class AVLTreeIterator implements OrderedIterator
	{
		
		/*************
	 	 *	attributes
	 	 ************/
	 	
	 	/** the AVLTree that is being iterated */
		protected AVLTree theTree;
		
		/** number of tree modifications the iterator is aware of */
		protected int expectedModCount;
		
		
		/***************
	 	 *	constructors
	 	 **************/
		
		/**
		 *	create a new AVLTreeIterator to traverse the specified AVLTree
		 */
		public AVLTreeIterator(AVLTree tree)
		{
 			theTree = tree;
 			expectedModCount = modCount; 			
 		}
 		
 		
		/**********
	 	 *	methods
	 	 *********/
 		
 		/**
 		 *	return the current AVLNode object
 		 *	only accessible within AVLTreeIterator and its subclasses
 		 */
 		protected abstract AVLNode getCurrentNode();
 		
 		
 		/***************************************************
 		 *	methods inherited from interface OrderedIterator
 		 **************************************************/
 		
 		
 		/**
 		 *	return the item in the current node
 		 *	throws a NoSuchElementException if current node is null
 		 */
 		public Comparable getCurrent()
 		{
 			AVLNode node = getCurrentNode();
 			if (node == null)
 				throw new NoSuchElementException("invalid node");
 			else
 				return node.theItem;
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
	 *	traverses an AVLTree in preorder fashion:
	 *		parent --> left child --> right child
	 */
	protected class PreOrderIterator extends AVLTreeIterator
	{
		
		/*************
	 	 *	attributes
	 	 ************/
	 	
	 	/**	stores nodes yet to be traversed */
		protected Stack s;

		
		/***************
	 	 *	constructors
	 	 **************/
		
		/**
		 *	create a new PreOrderIterator for the specified AVLTree
		 *	iteration begins with the root node
		 */
		public PreOrderIterator(AVLTree tree)
		{
 			super(tree);
 			
 			// initialize stack
 			s = new ArrayStack();
 			
 			// if root exists, add root node to stack
 			if (theTree.root != null)
				s.push(theTree.root);
 		}
 		
 		
 		/***********************************************
 		 *	methods inherited from class AVLTreeIterator
 		 **********************************************/
 		
 		
 		/**
 		 *	return the current AVLNode in the iteration
 		 *	return null if the stack is empty
 		 */
 		protected AVLNode getCurrentNode()
 		{
 			if (s.isEmpty())
 				return null;
 				
 			return (AVLNode)s.peek();
 		}
 		
 		
 		/********************************************
 		 *	methods inherited from interface Iterator
 		 *******************************************/
 		
 		
 		/**
 		 *	return true if there are more nodes to traverse
 		 */
 		public boolean hasNext()
 		{
 			if(expectedModCount != modCount)
				throw new ConcurrentModificationException();
			
 			return !s.isEmpty();
 		}
 		
 		
 		/**
 		 *	advance to the next node in the traversal
 		 */
		public void next()
		{
			// set current node to the node on top of the stack
			AVLNode current = (AVLNode)s.pop();
		
			// push right child onto stack
			if (current.right != null)
				s.push(current.right);
		
			// push left child onto stack
			if (current.left != null)
				s.push(current.left);
		}
	}
	
	
	/**
	 *	inner class PostOrderIterator
	 *
	 *	traverses an AVLTree in postorder fashion:
	 *		left child --> right child --> parent
	 */
	protected class PostOrderIterator extends AVLTreeIterator
	{
		
		/*************
	 	 *	attributes
	 	 ************/
	 	
	 	
	 	/** stores nodes yet to be traversed */
		protected Stack s;
		
		/** stores current AVLNode */
		protected AVLNode current;
		
		
		/***************
	 	 *	constructors
	 	 **************/
	 	
		
		/**
		 *	create a new PostOrderIterator for the specified AVLTree
		 *	iteration begins with the root node
		 */
		public PostOrderIterator(AVLTree tree)
		{
 			super(tree);
 			
 			// initialize stack
 			s = new ArrayStack();
 			
 			// if root exists, add root node to stack
 			if (theTree.root != null)
 			{
				s.push(new ItrNode(theTree.root));
				
				// need to prime stack
				next();
			}
 		}
 		
 		
 		/***********************************************
 		 *	methods inherited from class AVLTreeIterator
 		 **********************************************/
 		
 		
 		/**
 		 *	return the current AVLNode
 		 */
 		protected AVLNode getCurrentNode()
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
 			if(expectedModCount != modCount)
				throw new ConcurrentModificationException();
			
 			return current != null;
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
				if (current == null)
					throw new NoSuchElementException();
				
				current = null;
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
					if (cnode.node.left != null)
						s.push(new ItrNode(cnode.node.left));
				}
				else
				{
					// 2nd time cnode is popped; proceed to right child
					if (cnode.node.right != null)
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
	 	 	
	 	 	/** stores the AVLNode */
			AVLNode node;
			
			/** # of times popped off the Stack */
			int timesPopped;
		
		
			/***************
	 	 	 *	constructors
	 	 	 **************/
		
			/**
		 	 * create a new ItrNode that contains the specified AVLNode
		 	 * timesPopped = 0
		 	 */
			ItrNode(AVLNode theNode)
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
		 	 *	the data item in the AVLNode
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
	 *	traverses an AVLTree in inorder fashion:
	 *		left child --> parent --> right child
	 */
	protected class InOrderIterator extends PostOrderIterator
	{
		
		/***************
	 	 *	constructors
	 	 **************/
		
		/**
		 *	create a new InOrderIterator for the specified AVLTree
		 *	iteration begins with the root node
		 */
		public InOrderIterator(AVLTree tree)
		{
 			super(tree);
 			
 			// initialize stack
 			s = new ArrayStack();
 			
 			// if root exists, add root node to stack
 			if (theTree.root != null)
 			{
				s.push(new ItrNode(theTree.root));
				
				// need to prime stack
				next();
			}
 		}
 		
 		
 		/***********************************************
 		 *	methods inherited from class AVLTreeIterator
 		 **********************************************/
 		
 		
 		/**
 		 *	return the current AVLNode
 		 */
 		protected AVLNode getCurrentNode()
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
 			if(expectedModCount != modCount)
				throw new ConcurrentModificationException();
			
 			return current != null;
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
				if (current == null)
					throw new NoSuchElementException();
				
				current = null;
				return;
			}
		
			// process the stack
			for ( ; ; )
			{
				// get the top node on the stack
				cnode = (ItrNode)s.pop();
			
				// 2nd time cnode has been popped; ready to process cnode; push right child onto stack
				if (++cnode.timesPopped == 2)
				{
					current = cnode.node;
					if (cnode.node.right != null)
						s.push(new ItrNode(cnode.node.right));
					
					return;
				}
			
				// 1st time cnode has been popped; push it back onto the stack and push left child onto stack
				s.push(cnode);
				if (cnode.node.left != null)
					s.push(new ItrNode(cnode.node.left));
			}
		}
	}
	
	
	/**
	 *	inner class LevelOrderIterator
	 *
	 *	traverses an AVLTree in level-order fashion:
	 *		one level at a time, beginning with root and proceeding down the tree
	 *		at each level, traverse from left to right
	 */
	protected class LevelOrderIterator extends AVLTreeIterator
	{
		
		/*************
		 *	attributes
		 ************/
		
		/** stores nodes yet to be traversed */
		private LinkedQueue q;
		
		
		/***************
		 *	constructors
		 **************/
		
		
		/**
		 *	create a new LevelOrderIterator for the specified AVLTree
		 *	iteration begins with the root node
		 */
		public LevelOrderIterator(AVLTree tree)
		{
 			super(tree);

			// initialize queue
 			q = new LinkedQueue();
 			
 			// if root exists, add root node to queue
 			if (theTree.root != null)
				q.enqueue(theTree.root);
 		}
 		
 		
 		/***********************************************
 		 *	methods inherited from class AVLTreeIterator
 		 **********************************************/
 		
 		
 		/**
 		 *	return the current AVLNode in the iteration
 		 *	return null if the queue is empty
 		 */		
 		protected AVLNode getCurrentNode()
 		{
 			if (q.isEmpty())
 				return null;
 			
 			return (AVLNode)q.getFront();
 		}
 		
 		
 		/********************************************
 		 *	methods inherited from interface Iterator
 		 *******************************************/
 		
 		
 		/**
 		 *	return true if there are more nodes to traverse
 		 */
 		public boolean hasNext()
 		{
 			if(expectedModCount != modCount)
				throw new ConcurrentModificationException();
			
 			return !q.isEmpty();
 		}
 		
 		
 		/**
 		 *	advance to the next node in the traversal
 		 */
		public void next()
		{
			AVLNode current = (AVLNode)q.dequeue();

			// enqueue current node's left child, if it exists
			if (current.left != null)
				q.enqueue(current.left);
			
			// enqueue current node's right child, if it exists
			if (current.right != null)
				q.enqueue(current.right);
		}
	}
	
	
	/**
	 *	nested class AVLNode
	 *	stores a data item, references to left and right child nodes, and height
	 */
	private static class AVLNode
	{

		/*************
		 *	attributes
		 ************/
		
		/** data item */
		Comparable theItem;
		
		/** left child */
		AVLNode left;
		
		/** right child */
		AVLNode right;
		
		/** node height */
		int height;


		/***************
		 *	constructors
		 **************/
		

		/**
		 *	create a new AVLNode containing the specified item
		 *	left and right child nodes will be null
		 */
		public AVLNode(Comparable obj)
		{
			theItem = obj;
			left = null;
			right = null;
		}

		
		/**
		 *	create a new AVLNode containing the specified item, with the
		 *	specified left and right child nodes
		 */
		public AVLNode(Comparable obj, AVLNode leftChild, AVLNode rightChild)
		{
			theItem = obj;
			left = leftChild;
			right = rightChild;
		}
		
		
		/**
		 *	return a String representation of an AVLNode
		 *	the format is:
		 *		item(<node height>)
		 *
		 *	the height has been included for testing purposes; it should be removed
		 *	when this class is packaged as a reusable component
		 */
		public String toString()
		{
			return theItem.toString() + "(" + height + ")";
		}

	}

}