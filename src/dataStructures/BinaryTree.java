package dataStructures;

/**
 *	BinaryTree class
 *	
 *	implementation of a rooted tree in which each node of the tree can have a
 *	maximum of 2 child nodes
 *
 *	Written by Roger West, University of Illinois at Springfield
 */
public class BinaryTree implements Tree
{

	/*************
	 *	attributes
	 ************/
	
	/** root node */
	private BinaryNode root;
	
	/** number of items in tree */
	private int theSize;
	
	/** current number of modifications to tree */
	private int modCount;


	/***************
	 *	constructors
	 **************/
	

	/**
	 *	create a new, empty BinaryTree
	 */
	public BinaryTree()
	{
		// empty this BinaryTree
		clear();
	}
	
	/**
	 *	create a new BinaryTree with the specified item in the root node
	 */
	public BinaryTree(Object obj)
	{
		// empty this BinaryTree
		clear();
		
		// create root node to store obj
		root = new BinaryNode(obj, null, null);
	}


	/**********
	 *	methods
	 *********/
	
	/**
	 *	return the item in the root node
	 */
	public Object getRootItem()
	{
		// throw exception if this BinaryTree is empty
		if (isEmpty())
			throw new NoSuchElementException("Tree is empty");
		
		// return item in root node
		return root.theItem;
	}
	
	
	/**
	 *	return the height of the specified node
	 *	returns -1 if node is null
	 */
	protected int height(BinaryNode node)
	{
		if (node == null)
		{
			// null nodes have no height
			return -1;
		}
		else
		{
			// height of node is height of deepest subtree + 1
			return 1 + Math.max(height(node.left), height(node.right));
		}
	}
	
	
	/**
	 *	return a BinaryNode that contains the specified item
	 *		returns the first such BinaryNode encountered using a level-order
	 *		traversal
	 *
	 *		returns null if item is not in the tree
	 */
	protected BinaryNode getNode(Object obj)
	{
		// create level-order iterator for searching
		BinaryTreeIterator itr = levelOrderIterator();
		
		// temp node storage
		BinaryNode node;
		
		// traverse this BinaryTree
		while (itr.hasNext())
		{
			// retrieve node at current position in traversal
			node = itr.getCurrentNode();
			
			if (node.theItem.equals(obj))
			{
				// item found; return node containing item
				return node;
			}
			
			// item not yet found; continue traversal
			itr.next();
		}
		
		// item not in this BinaryTree
		return null;
	}
	
	
	/**
	 *	Add the specified item to this BinaryTree as the left child of the first
	 *	existing instance of the specified parent item in this BinaryTree, as
	 *	determined using a level-order traversal.
	 *
	 *	If the specified parent item does not exist in this BinaryTree, the new
	 *	item is added at the first available branch obtained using a level-order
	 *	traversal.
	 *
	 *	@param - obj		the new item to be added
	 *	@param - parent		the item to which the new item should be added
	 */
	public boolean addLeft(Object obj, Object parent)
	{
		// new node with new item
		BinaryNode childNode = new BinaryNode(obj);
		
		// node to which new item will be linked
		BinaryNode parentNode = getNode(parent);
		
		if (parentNode == null)
		{
			// parent item doesn't exist; add obj using default add method
			add(obj);
		}
		else
		{
			// parent node's left child becomes new node's left child
			childNode.left = parentNode.left;
			
			// new node becomes parent node's new left child
			parentNode.left = childNode;
			
			// add 1 to size of this BinaryTree
			theSize++;
			
			// indicate this BinaryTree has been modified
			modCount++;
		}
		
		// add successful
		return true;
	}
	
	
	/**
	 *	Add the specified item to this BinaryTree as the right child of the first
	 *	existing instance of the specified parent item in this BinaryTree, as
	 *	determined using a level-order traversal.
	 *
	 *	If the specified parent item does not exist in this BinaryTree, the new
	 *	item is added at the first available branch obtained using a level-order
	 *	traversal.
	 *
	 *	@param - obj		the new item to be added
	 *	@param - parent		the item to which the new item should be added
	 */
	public boolean addRight(Object obj, Object parent)
	{
		// new node with new item
		BinaryNode childNode = new BinaryNode(obj);
		
		// node to which new item will be linked
		BinaryNode parentNode = getNode(parent);
		
		if (parentNode == null)
		{
			// parent item doesn't exist; add obj using default add method
			add(obj);
		}
		else
		{
			// parent node's right child becomes new node's right child
			childNode.right = parentNode.right;
			
			// new node becomes parent node's new right child
			parentNode.right = childNode;
			
			// add 1 to size of this BinaryTree
			theSize++;
			
			// indicate this BinaryTree has been modified
			modCount++;
		}
		
		// add successful
		return true;
	}
	
	
	/**************************************
	 *	methods inherited from class Object
	 *************************************/
	
	
	/**
	 *	return a String representation of this BinaryTree
	 */
	public String toString()
	{
		if (isEmpty())
			return "";
		else
			return root.toString() + "\n" + toString(root, 1);
	}
	
	
	/**
	 *	return a representation of this BinaryTree in a format similar to that
	 *	found when browsing the contents of a file system using a GUI-based
	 *	component such as Windows Explorer
	 */
	private String toString(BinaryNode node, int level)
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


	/****************************************
	 *	methods inherited from interface Tree
	 ***************************************/
	
	
	/**
	 *	return the height of the tree
	 */
	public int height()
	{
		// height of tree == height of root node
		return height(root);
	}


	/**
	 *	return the first occurrence of the specified item
	 *	level-order traversal is used to search for the item
	 *
	 *	returns null if specified item doesn't exist
	 */
	public Object get(Object obj)
	{
		// create level-order iterator for searching
		BinaryTreeIterator itr = levelOrderIterator();
		
		// temp node storage
		BinaryNode node;
		
		// traverse this BinaryTree
		while (itr.hasNext())
		{
			// retrieve node at current position in traversal
			node = itr.getCurrentNode();
			
			if (node.theItem.equals(obj))
			{
				// item found; return item
				return node.theItem;
			}
			
			// item not yet found; continue traversal
			itr.next();
		}
		
		// item not in this BinaryTree
		return null;
	}
	
	
	/**
	 *	remove the first occurrence of the specified object from the tree, as
	 *	determined by performing a level-order traversal
	 *
	 *	returns true if removal was successful, false otherwise
	 *	throws NoSuchElementException if tree is empty
	 */
	public boolean remove(Object obj)
	{
		// throw exception if this BinaryTree is empty
		if (isEmpty())
			throw new NoSuchElementException("Tree is empty");
		
		// create level-order iterator for searching
		BinaryTreeIterator itr = levelOrderIterator();
		
		// temp node storage
		BinaryNode node;
		
		// traverse this BinaryTree
		while (itr.hasNext())
		{
			// retrieve node at current position in traversal
			node = itr.getCurrentNode();
			
			if (node.theItem.equals(obj))
			{
				// item found - remove it
				itr.remove();
				
				// remove successful
				return true;
			}
			
			// item not yet found; continue traversal
			itr.next();
		}
		
		// either item not in this BinaryTree, or remove failed
		return false;
	}
	
	
	/**
	 *	return an iterator for traversing the tree in a preorder fashion
	 */
	public BinaryTreeIterator preOrderIterator()
	{
		return new PreOrderIterator(this);
	}
	
	
	/**
	 *	return an iterator for traversing the tree in a postorder fashion
	 */
	public BinaryTreeIterator postOrderIterator()
	{
		return new PostOrderIterator(this);
	}
	
	
	/**
	 *	return an iterator for traversing the tree in an inorder fashion
	 */
	public BinaryTreeIterator inOrderIterator()
	{
		return new InOrderIterator(this);
	}
	
	
	/**
	 *	return an iterator for traversing the tree in a level-order fashion
	 */
	public BinaryTreeIterator levelOrderIterator()
	{
		return new LevelOrderIterator(this);
	}
	
	
	/*********************************************************
	 *	methods inherited from interface TraversableCollection
	 ********************************************************/
	
	
	/**
	 *	return default iterator for this BinaryTree
	 *	default iterator uses a level-order traversal
	 */
	public Iterator iterator()
	{
		return new LevelOrderIterator(this);
	}
	
	
	/**
	 *	return whether the specified object exists in the tree
	 *	level-order traversal is used to search for the item
	 */
	public boolean contains(Object obj)
	{
		// create level-order iterator for searching
		BinaryTreeIterator itr = levelOrderIterator();
		
		// temp node storage
		BinaryNode node;
		
		// traverse this BinaryTree
		while (itr.hasNext())
		{
			// retrieve node at current position in traversal
			node = itr.getCurrentNode();
			
			if (node.theItem.equals(obj))
			{
				// item found
				return true;
			}
			
			// item not yet found; continue traversal
			itr.next();
		}
		
		// item not in this BinaryTree
		return false;
	}


	/**********************************************
	 *	methods inherited from interface Collection
	 *********************************************/
	
	
	/**
	 *	add the specified Object to the tree
	 *	the item will be added at the first available branch obtained using a
	 *	level-order traversal
	 */
	public boolean add(Object obj)
	{
		if (root == null)
		{
			// tree is empty; add item as root node
			root = new BinaryNode(obj);
		}
		else
		{
			// traverse to find insertion point
			
			// create level-order iterator for searching
			BinaryTreeIterator itr = levelOrderIterator();
			
			// temp node storage
			BinaryNode node;
			
			// traverse this BinaryTree
			while (itr.hasNext())
			{
				// retrieve node at current position in traversal
				node = itr.getCurrentNode();
			
				if (node.left == null)
				{
					// add item as left child of current node
					node.left = new BinaryNode(obj);
					
					// exit traversal
					break;
				}
				else if (node.right == null)
				{
					// add item as right child of current node
					node.right = new BinaryNode(obj);
					
					// exit traversal
					break;
				}
				else
				{
					// current node already has 2 children, so keep traversing
					itr.next();
				}
			}
		}
		
		// add 1 to size of this BinaryTree
		theSize++;
		
		// indicate this BinaryTree has been modified
		modCount++;
		
		// add successful
		return true;
	}


	/**
	 *	remove the first item in the BinaryTree
	 *	by default, the first item is considered to be the root item
	 *	the new root will be:
	 *		1) the root's right child, if it exists
	 *		2) the root's left child if the root has no right child
	 */
	public boolean remove()
	{
		// throw exception if this BinaryTree is empty
		if (isEmpty())
			throw new NoSuchElementException("Tree is empty");
		
		// create level-order iterator to carry out removal
		BinaryTreeIterator itr = levelOrderIterator();
		
		// remove root item
		return itr.remove();
	}


	/**
	 *	make the tree empty
	 */
	public void clear()
	{
		// set root to null to indicate no tree is present
		root = null;
		
		// set size of this BinaryTree to 0
		theSize = 0;
		
		// indicate this BinaryTree has been modified
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
	public Object get()
	{
		// return item in root node of this BinaryTree
		return getRootItem();
	}
	
	
	/****************
	 *	inner classes
	 ***************/

	private abstract class BinaryTreeIterator implements TreeIterator
	{
		
		/*************
		 *	attributes
		 ************/
		
		/** the BinaryTree that is being iterated */
		protected BinaryTree theTree;
		
		/** number of tree modifications the iterator is aware of */
		protected int expectedModCount;
		
		
		/***************
		 *	constructors
		 **************/
		
		/**
		 *	create a new BinaryTreeIterator to traverse the specified BinaryTree
		 */
		public BinaryTreeIterator(BinaryTree tree)
		{
			// set reference to BinaryTree being traversed
 			theTree = tree;
 			
 			// sync mo count of this BinaryTreeIterator with mod count of BinaryTree
 			expectedModCount = modCount;
 		}
		
		
		/**********
		 *	methods
		 *********/
 		
 		/**
 		 *	return the current BinaryNode object
 		 *	only accessible within BinaryTreeIterator and its subclasses
 		 */
 		protected abstract BinaryNode getCurrentNode();
 		
 		
 		/**
 		 *	remove the specified node from the tree
 		 *	the node is only logically removed - the item in the node is replaced
 		 *	with the item in the node's left child
 		 *	items are shifted up from the left until a leaf node is reached; at the
 		 *	end of this shifting the leaf node will be removed
 		 *
 		 *	returns reference to the parent of the node that is removed
 		 *
 		 *	throws NoSuchElementException if tree is empty
 		 */
 		protected BinaryNode remove(BinaryNode node)
 		{	
 			// empty tree
 			if (node == null)
				throw new NoSuchElementException("Tree is empty");
			
			// get parent of node to remove
			BinaryNode parent = getParentNode(node);

			// shift items up from left
			while (node.left != null)
			{
				node.theItem = node.left.theItem;
				parent = node;
				node = node.left;
			}
			
			// physically remove the node
			if (parent == null)
			{
				// root node is removed, and root node had no left child
				root = node.right;
			}
			else if (parent.right == node)
			{
				// node to be removed is parent's right child
				parent.right = node.right;
			}
			else
			{
				// node to be removed is parent's left child
				parent.left = node.right;
			}
			
			return parent;
 		}
 		
 		
 		/**
 	 	 *	return the specified node's parent node
 	 	 *
 	 	 *	returns null if node == root node
 	 	 */
 		protected BinaryNode getParentNode(BinaryNode node)
 		{
 			if (node == root)
 			{
 				// root node has no parent
 				return null;
 			}
 			else
 			{
 				// create level-order iterator for searching
 				BinaryTreeIterator itr = BinaryTree.this.levelOrderIterator();
 				
 				// temp node storage
 				BinaryNode currentNode;
 			
 				// traverse this BinaryTree
 				while (itr.hasNext())
 				{
 					// retrieve node at current position in traversal
 					currentNode = itr.getCurrentNode();
 					
 					if (currentNode.left == node || currentNode.right == node)
 					{
 						// parent node found; return it
 						return currentNode;
 					}
 				
 					// parent node not yet found; continue traversal
 					itr.next();
 				}
 			}
 		
 			// node is not in this BinaryTree
 			return null;
 		}
 		
 		
 		/********************************************
 		 *	methods inherited from interface Iterator
 		 *******************************************/
 		
 		/**
 		 *	return the item in the current node
 		 *	throws a NoSuchElementException if current node is null
 		 */
 		public Object getCurrent()
 		{
 			// retrieve current node in traversal
 			BinaryNode node = getCurrentNode();
 			
 			if (node == null)
 			{
 				// node is invalid
 				throw new NoSuchElementException("invalid node");
 			}
 			else
 			{
 				// node is valid; return item in node
 				return node.theItem;
 			}
 		}


		/**
		 *	add the specified object to the tree
		 *
		 *	the item is inserted as the current node's left child if the height
		 *	of the current node's left subtree <= height of the current node's
		 *	right subtree; otherwise the item is inserted as the current node's
		 *	right child
		 *
		 *	the root node cannot be added to the tree using an iterator, since
		 *	a TreeIterator is invalid for an empty tree
		 */
		public boolean add(Object obj)
		{
			// throw exception if BinaryTree has been modified outside this BinaryTreeIterator
			if(expectedModCount != modCount)
				throw new ConcurrentModificationException();
			
			// retrieve current node in traversal
			BinaryNode current = getCurrentNode();
			
			// create new node containing obj
			BinaryNode newNode = new BinaryNode(obj);
			
			// add new node to shortest subtree of BinaryTree
			if (BinaryTree.this.height(current.left) <= BinaryTree.this.height(current.right))
			{
				// new node's left child is current node's old left child
				newNode.left = current.left;
				
				// current node's new left child is new node
				current.left = newNode;
			}
			else
			{
				// new node's right child is current node's old right child
				newNode.right = current.right;
				
				// current node's new right child is new node
				current.right = newNode;
			}
			
			// add 1 to size of BinaryTree
			theSize++;
			
			// indicate BinaryTree has been modifies
			modCount++;
			
			// sync mod counts of BinaryTree and this BinaryTreeIterator
			expectedModCount++;
			
			// add successful
			return true;
		}


		/**
		 *	remove the current item from the tree
		 */
		public abstract boolean remove();
		
		
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
	 *	traverses a BinaryTree in preorder fashion:
	 *		parent --> left child --> right child
	 */
	private class PreOrderIterator extends BinaryTreeIterator
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
		 *	create a new PreOrderIterator for the specified BinaryTree
		 *	iteration begins with the root node
		 */
		public PreOrderIterator(BinaryTree tree)
		{
			// invoke constructor of superclass, BinaryTreeIterator
 			super(tree);
 			
 			// initialize stack
 			s = new ArrayStack();
 			
 			// if root exists, add root node to stack
 			if (theTree.root != null)
				s.push(theTree.root);
 		}
 		
 		
 		/**************************************************
 		 *	methods inherited from class BinaryTreeIterator
 		 *************************************************/
 		
 		/**
 		 *	return the current BinaryNode in the iteration
 		 *	return null if the stack is empty
 		 */
 		protected BinaryNode getCurrentNode()
 		{
 			// if no items left to traverse, return null
 			if (s.isEmpty())
 				return null;
 			
 			// return current BinaryNode
 			return (BinaryNode)s.peek();
 		}
 		
 		
 		/********************************************
 		 *	methods inherited from interface Iterator
 		 *******************************************/
 		
 		/**
 		 *	return true if there are more nodes to traverse
 		 */
 		public boolean hasNext()
 		{
 			// throw exception if BinaryTree is modified outside this PreOrderIterator
 			if(expectedModCount != modCount)
				throw new ConcurrentModificationException();
			
			// if there are items left to traverse, stack will not be empty
 			return !s.isEmpty();
 		}
 		
 		
 		/**
 		 *	advance to the next node in the traversal
 		 */
		public void next()
		{
			// set current node to the node on top of the stack
			BinaryNode current = (BinaryNode)s.pop();
		
			// push right child onto stack, if it exists
			if (current.right != null)
				s.push(current.right);
		
			// push left child onto stack, if it exists
			if (current.left != null)
				s.push(current.left);
		}


		/**
		 *	add the specified object to the tree
		 */
		public boolean add(Object obj)
		{
			// invoke add method of superclass, BinaryTreeIterator
			return super.add(obj);
		}


		/**
	 	 *	remove the current item
	 	 *
	 	 *	following this operation:
	 	 *		1) the current item will be the next item that would have been
	 	 *		   processed next, according to the rules for the preorder traversal,
	 	 *		   had the current item not been removed
	 	 *		2) the next call to next() will return the next unprocessed item
	 	 *		   according to the preorder traversal; however, no guarantees are
	 	 *		   made regarding subsequent calls to next(), since removal of an
	 	 *		   item changes the structure of the tree
	 	 */
		public boolean remove()
		{
			// throw exception is BinaryTree modified outside this PreOrderIterator
			if(expectedModCount != modCount)
				throw new ConcurrentModificationException();
			
			// retrieve current node in traversal
			BinaryNode node = getCurrentNode();
			
			// temp storage for current item
			Object temp = node.theItem;
			
			// remove current node using superclass remove method
			super.remove(node);

			// update stack to be in sync with BinaryTree
			if (!s.isEmpty())
			{
				if (temp.equals(((BinaryNode)s.peek()).theItem))
				{
					// remove from stack the node removed from the BinaryTree
					s.pop();
				}
			}
			
			// subtract 1 from size of BinaryTree
			theSize--;
			
			// indicate BinaryTree has been modified
			modCount++;
			
			// sync mod counts of BinaryTree and this PreOrderIterator
			expectedModCount++;
			
			// remove successful
			return true;
		}
	}
	
	
	/**
	 *	inner class PostOrderIterator
	 *
	 *	traverses a BinaryTree in postorder fashion:
	 *		left child --> right child --> parent
	 */
	private class PostOrderIterator extends BinaryTreeIterator
	{
		
		/*************
		 *	attributes
		 ************/
		
		/** stores nodes yet to be traversed */
		protected Stack s;
		
		/** stores current BinaryNode */
		protected BinaryNode current;
		
		
		/***************
		 *	constructors
		 **************/
		
		/**
		 *	create a new PostOrderIterator for the specified BinaryTree
		 *	iteration begins with the root node
		 */
		public PostOrderIterator(BinaryTree tree)
		{
			// invoke constructor of superclass, BinaryTreeIterator
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
 		
 		
 		/**************************************************
 		 *	methods inherited from class BinaryTreeIterator
 		 *************************************************/
 		
 		/**
 		 *	return the current BinaryNode
 		 */
 		protected BinaryNode getCurrentNode()
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
 			// throw exception if BinaryTree modified outside this PostOrderIterator
 			if(expectedModCount != modCount)
				throw new ConcurrentModificationException();
			
			// if next item exists, current will be non-null
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
		
			// empty stack and null current node means traversal has gone past last item
			if (s.isEmpty())
			{
				// throw exception if current node is null
				if (current == null)
					throw new NoSuchElementException();
				
				// set current node to null to mark end of traversal
				current = null;
				
				// exit method
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
					// set current node
					current = cnode.node;
					
					// exit method
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
		 *	add the specified object to the tree
		 */
		public boolean add(Object obj)
		{
			// invoke superclass add method
			return super.add(obj);
		}


		/**
	 	 *	remove the current item
	 	 *
	 	 *	following this operation:
	 	 *		1) the current item will be the next item that would have been
	 	 *		   processed next, according to the rules for the postorder traversal,
	 	 *		   had the current item not been removed
	 	 *		2) the next call to next() will return the next unprocessed item
	 	 *		   according to the postorder traversal; however, no guarantees are
	 	 *		   made regarding subsequent calls to next(), since removal of an
	 	 *		   item changes the structure of the tree
	 	 */
		public boolean remove()
		{
			// throw exception if BinaryTree modified outside this PostOrderIterator
			if(expectedModCount != modCount)
				throw new ConcurrentModificationException();
			
			// invoke superclass remove method
			super.remove(getCurrentNode());

			// ensure current item is the next item in the traversal
			next();

			// subtract 1 from size of BinaryTree			
			theSize--;
			
			// indicate BinaryTree has been modified
			modCount++;
			
			// sync mod counts of BinaryTree and this PostOrderIterator
			expectedModCount++;
			
			// remove successful
			return true;
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
			
			/** stores the BinaryNode */
			BinaryNode node;
			
			/** # of times popped off the Stack */
			int timesPopped;
		
		
			/***************
			 *	constructors
			 **************/
		
			/**
		 	 * create a new ItrNode that contains the specified BinaryNode
		 	 * timesPopped = 0
		 	 */
			ItrNode(BinaryNode theNode)
			{
				// store BinaryNode
				node = theNode;
				
				// initialize times popped of stack
				timesPopped = 0;
			}
			
			
			/**********
			 *	methods
			 *********/
			
			/**
			 *	return a String representation of the ItrNode
		 	 *	by default, this method simply returns the String representation of
		 	 *	the data item in the BinaryNode
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
	 *	traverses a BinaryTree in inorder fashion:
	 *		left child --> parent --> right child
	 */
	private class InOrderIterator extends PostOrderIterator
	{
		
		/***************
		 *	constructors
		 **************/
		
		/**
		 *	create a new InOrderIterator for the specified BinaryTree
		 *	iteration begins with the root node
		 */
		public InOrderIterator(BinaryTree tree)
		{
			// invoke constructor of superclass, PostOrderIterator
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
 		
 		
 		/**************************************************
 		 *	methods inherited from class BinaryTreeIterator
 		 *************************************************/
 		
 		/**
 		 *	return the current BinaryNode
 		 */
 		protected BinaryNode getCurrentNode()
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
 			// throw exception if BinaryTree modified outside this InOrderIterator
 			if(expectedModCount != modCount)
				throw new ConcurrentModificationException();
			
			// if next item exists, current will be non-null
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
		
			// empty stack and null current node means traversal has gone past last item
			if (s.isEmpty())
			{
				// throw exception if current node is null
				if (current == null)
					throw new NoSuchElementException();
				
				// set current node to null to mark end of traversal
				current = null;
				
				// exit method
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
					// set current node
					current = cnode.node;
					
					// push current node's right child onto stack, if it exists
					if (cnode.node.right != null)
						s.push(new ItrNode(cnode.node.right));
					
					// exit method
					return;
				}
			
				// 1st time cnode has been popped; push it back onto the stack
				s.push(cnode);
				
				// push cnode's left child onto stack, if it exists
				if (cnode.node.left != null)
					s.push(new ItrNode(cnode.node.left));
			}
		}


		/**
		 *	add the specified object to the tree
		 */
		public boolean add(Object obj)
		{
			// invoke superclass add method
			return super.add(obj);
		}


		/**
	 	 *	remove the current item
	 	 *
	 	 *	following this operation:
	 	 *		1) the current item will be the next item that would have been
	 	 *		   processed next, according to the rules for the inorder traversal,
	 	 *		   had the current item not been removed
	 	 *		2) the next call to next() will return the next unprocessed item
	 	 *		   according to the inorder traversal; however, no guarantees are
	 	 *		   made regarding subsequent calls to next(), since removal of an
	 	 *		   item changes the structure of the tree
	 	 */
		public boolean remove()
		{
			// invoke superclass remove method
			return super.remove();
		}
	}
	
	
	/**
	 *	inner class LevelOrderIterator
	 *
	 *	traverses a BinaryTree in level-order fashion:
	 *		one level at a time, beginning with root and proceeding down the tree
	 *		at each level, traverse from left to right
	 */
	private class LevelOrderIterator extends BinaryTreeIterator
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
		 *	create a new LevelOrderIterator for the specified BinaryTree
		 *	iteration begins with the root node
		 */
		public LevelOrderIterator(BinaryTree tree)
		{
			// invoke constructor of superclass, BinaryTreeIterator
 			super(tree);
 			
 			// initialize queue
 			q = new ArrayQueue();
 			
 			// if root exists, add root node to queue
 			if (theTree.root != null)
				q.enqueue(theTree.root);
 		}
 		
 		
 		/**************************************************
 		 *	methods inherited from class BinaryTreeIterator
 		 *************************************************/
 		
 		/**
 		 *	return the current BinaryNode in the iteration
 		 *	return null if the queue is empty
 		 */		
 		protected BinaryNode getCurrentNode()
 		{
 			// no current node if queue is empty
 			if (q.isEmpty())
 				return null;
 			
 			// return node at front of queue
 			return (BinaryNode)q.getFront();
 		}
 		
 		
 		/********************************************
 		 *	methods inherited from interface Iterator
 		 *******************************************/
 		
 		/**
 		 *	return true if there are more nodes to traverse
 		 */
 		public boolean hasNext()
 		{
 			// throw exception if BinaryTree modified outside this LevelOrderIterator
 			if(expectedModCount != modCount)
				throw new ConcurrentModificationException();
			
			// if next item exists, queue will not be empty
 			return !q.isEmpty();
 		}
 		
 		
 		/**
 		 *	advance to the next node in the traversal
 		 */
		public void next()
		{
			// set current node to first node in queue
			BinaryNode current = (BinaryNode)q.dequeue();

			// enqueue current node's left child, if it exists
			if (current.left != null)
				q.enqueue(current.left);
			
			// enqueue current node's right child, if it exists
			if (current.right != null)
				q.enqueue(current.right);
		}


		/**
		 *	add the specified object to the tree
		 */
		public boolean add(Object obj)
		{
			// invoke superclass add method
			return super.add(obj);
		}


		/**
	 	 *	remove the current item
	 	 *
	 	 *	following this operation:
	 	 *		1) the current item will be the next item that would have been
	 	 *		   processed next, according to the rules for the level-order
	 	 *		   traversal, had the current item not been removed
	 	 *		2) the next call to next() will return the next unprocessed item
	 	 *		   according to the level-order traversal; however, no guarantees
	 	 *		   are made regarding subsequent calls to next(), since removal of
	 	 *		   an item changes the structure of the tree
	 	 */
		public boolean remove()
		{
			// throw exception if BinaryTree modified outside this LevelOrderIterator
			if(expectedModCount != modCount)
				throw new ConcurrentModificationException();
			
			// retrieve current node
			BinaryNode node = getCurrentNode();
			
			// store item in node to be removed
			Object temp = node.theItem;
			
			// invoke superclass remove method to remove node
			super.remove(node);
			
			// update queue to be in sync with BinaryTree
			if (!q.isEmpty())
			{
				if (temp.equals(((BinaryNode)q.getFront()).theItem))
				{
					// remove from queue the node removed from the BinaryTree
					q.dequeue();
				}
			}
			
			// subtract 1 from size of BinaryTree
			theSize--;
			
			// indicate BinaryTree has been modified
			modCount++;
			
			// sync mod counts of BinaryTree and this LevelOrderIterator
			expectedModCount++;
			
			// remove successful
			return true;
		}
	}


	/**
	 *	nested class BinaryNode
	 *	stores a data item and references to left and right child nodes
	 */
	private static class BinaryNode
	{

		/*************
		 *	attributes
		 ************/
		
		/** data item */
		Object theItem;
		
		/** left child */
		BinaryNode left;
		
		/** right child */
		BinaryNode right;


		/***************
		 *	constructors
		 **************/

		/**
		 *	create a new BinaryNode containing the specified item
		 *	left and right child nodes will be null
		 */
		public BinaryNode(Object obj)
		{
			theItem = obj;
			left = null;
			right = null;
		}

		
		/**
		 *	create a new BinaryNode containing the specified item, with the
		 *	specified left and right child nodes
		 */
		public BinaryNode(Object item, BinaryNode leftChild, BinaryNode rightChild)
		{
			theItem = item;
			left = leftChild;
			right = rightChild;
		}
		
		
		/**********
		 *	methods
		 *********/
		
		/**
		 *	return a String representation of a BinaryNode
		 *	by default, this method simply returns the String representation of
		 *	the data item in the node
		 */
		public String toString()
		{
			return theItem.toString();
		}

	}

}