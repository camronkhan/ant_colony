package dataStructures;

/**
 *	class HashSet
 *
 *	Implementation of a set in which a HashMap is used to store items.
 *
 *	Duplicate items are not allowed.
 *
 *	Written by Roger West, University of Illinois at Springfield
 */
public class HashSet implements Set
{
	
	/*************
	 *	attributes
	 ************/
	
	/** the underlying HashMap containing the items */
	private HashMap hashMap;
	
	
	/***************
	 *	constructors
	 **************/
	
	/**
	 *	create a new HashSet using a HashMap with default values for load
	 *	factor, table sizes, and hashing function
	 */
	public HashSet()
	{
		// create new HashMap to support this HashSet
		hashMap = new HashMap();
	}
	
	
	/**
	 *	return the specified item from this HashSet
	 *
	 *	returns null if the specified item is not in this HashSet
	 */
	public Object get(Object obj)
	{
		// invoke HashMap get method
		return hashMap.get(obj);
	}
	
	
	/***************************************
	 *	methods inherited from interface Set
	 **************************************/
	
	/**
	 *	remove the specified item from this HashSet
	 *
	 *	returns true if removal was successful, false otherwise
	 */
	public boolean remove(Object obj)
	{
		// invoke HashMap remove method
		return hashMap.remove(obj);
	}
	
	
	/**********************************************
	 *	methods inherited from interface Collection
	 *********************************************/
	
	/**
	 *	add the specified item to this HashSet
	 *
	 *	if an attempt is made to add a duplicate item, this method will make no
	 *	modifications to this HashSet
	 *
	 *	returns true if the add was successful, false otherwise
	 */
	public boolean add(Object obj)
	{
		if (!hashMap.containsValue(obj))
		{
			// obj not already in this HashSet, so OK to add it
			hashMap.add(obj, obj);
			
			// add successful
			return true;
		}
		
		// obj already exists in this HashSet
		return false;
	}


	/**
	 *	remove an item from this HashSet
	 *
	 *	the item that is removed will be the first item in the first cell in the
	 *	underlying HashMap that contains one or more items
	 *
	 *	returns true if the remove was successful, false otherwise
	 */
	public boolean remove()
	{
		// use a MapIterator to get to first item, if it exists
		MapIterator mitr = hashMap.mapIterator();
		
		// get key for first item; will be null if HashSet is empty
		Object firstKey = mitr.getCurrentKey();
		
		if (firstKey == null)
		{
			// HashSet is empty
			return false;
		}
		
		// remove first item
		hashMap.remove(firstKey);
		
		// remove successful
		return true;
	}


	/**
	 *	empty this HashSet
	 */
	public void clear()
	{
		// empty the underlying HashMap supporting this HashSet
		hashMap.clear();
	}


	/**
	 *	return the number of items in this HashSet
	 */
	public int size()
	{
		return hashMap.size();
	}


	/**
	 *	return true if this HashSet contains 0 items, or false if this HashSet
	 *	contains at least 1 item
	 */
	public boolean isEmpty()
	{
		return hashMap.isEmpty();
	}


	/**
	 *	return the next available item from the Collection
	 *
	 *	the item that is returned will be the first item in the first cell in the
	 *	underlying HashMap that contains one or more items
	 *
	 *	returns null if this HashSet is empty
	 */
	public Object get()
	{
		// retrieve list of values
		List values = hashMap.valueList();
		
		if (values.isEmpty())
		{
			// this HashSet is empty
			return null;
		}
		
		// get first value, which also serves as a key in the underlying HashMap
		Object obj = values.get(0);
		
		// return item in underlying HashMap associated with key
		return hashMap.get(obj);
	}
	
	
	/*********************************************************
	 *	methods inherited from interface TraversableCollection
	 ********************************************************/
	
	/**
	 *	return an Iterator for this HashSet
	 */
	public Iterator iterator()
	{
		// return iterator capable of traversing items only (not keys)
		return hashMap.iterator();
	}
	
	
	/**
	 *	return whether this HashSet contains the specified item
	 */
	public boolean contains(Object obj)
	{
		return hashMap.containsValue(obj);
	}
	
	
	/**************************************
	 *	methods inherited from class Object
	 *************************************/
	
	/**
	 *	return a String representation of this HashSet
	 */
	public String toString()
	{
		String result = "";
		
		for (Iterator itr = iterator(); itr.hasNext(); )
		{
			result += itr.getCurrent().toString() + "\n";
			itr.next();
		}
		
		return result;
	}
}