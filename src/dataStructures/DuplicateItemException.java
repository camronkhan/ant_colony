package dataStructures;

/**
 *	Class DuplicateItemException
 *
 *	Generic class to represent the exception that occurs when an attempt is made
 *	to add a duplicate item to a data structure that does not allow duplicates.
 *
 *	Written by Roger West, University of Illinois at Springfield
 */
public class DuplicateItemException extends RuntimeException
{

	/***************
	 *	constructors
	 **************/

	/**
	 *	create a new DuplicateItemException
	 */
	public DuplicateItemException()
	{
		
	}


	/**
	 *	create a new DuplicateItemException that will display the specified
	 *	message if thrown
	 *
	 *	@param msg - the message to be displayed
	 */
	public DuplicateItemException(String msg)
	{
		super(msg);
	}
}