/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataStructures;

/**
 *
 * @author nmq687
 */
public class FinalExam {
    
    /** Constants */
    
    // The return value if list does not contain the searched-for object
    private static final ListNode NOT_FOUND = null;
    
    /** Attributes */
    
    // The current number of items in the list
    private int theSize;
    
    // Reference to the list's head node
    private ListNode head;
    
    // Reference to the list's tail node
    private ListNode tail;
    
    /** Methods */
    
    // Returns true if the specified item is in the list
    public boolean contains(Object obj) {
        
        // Return true if findPos() does not return a null value
        return findPos(obj) != NOT_FOUND;
    }
    
    /** Helper Class: findPos(Object obj) */
    
    // Returns a reference to the node at the position of the specified item
    private ListNode findPos(Object obj) {
        
        // Traverse from first to last node in the LinkedList
        for(ListNode node = head.next; node != tail; node = node.next) {
            
            // If searching for a null item
            if(obj == null) {
                
                // Compare null items with '==' operator
                if(node.theItem == null) {
                    
                    // Return reference to node at position of specified null item
                    return node;
                }
            }
            
            // Else if the specified item is non-null, use the equals method
            else if(obj.equals(node.theItem)) {
                
                // Return reference to node at position of specified  non-null item
                return node;
            }
        }
        
        // If the item is not in the LinkedList, return null
        return NOT_FOUND;
    }
    
    /** Nested Classes: ListNode */
    
    private static class ListNode {
        
        /** Attributes */
    
        // The data item
        Object theItem;

        // Reference to the next node in list
        ListNode next;

        // Reference to the previous node in list
        ListNode previous;

        /** Constructors */

        // Constructs a ListNode
        public ListNode(Object item, ListNode prevNode, ListNode nextNode) {
            theItem = item;
            previous = prevNode;
            next = nextNode;
        }
    }
}
