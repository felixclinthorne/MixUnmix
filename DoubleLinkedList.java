/**********************************************
 * This is the Double Linked List structure that will hold the data
 * for mixing and unmixing.
 *
 * Created by Tim Bomers and Matt Hendrick
 * CIS 163
 *
 * @param <E> Generic type for the nodes within
 **********************************************/
public class DoubleLinkedList<E>  {
	protected NodeD<E> top;      // The first NodeD<E> in the list

    // This instance variable is not required, however if you
    // find it useful, then you are welcome to use it
	protected NodeD<E> cursor;  // The current NodeD<E> in the list

	public DoubleLinkedList() {
		top = null;
		cursor = null;
	}

	public E get(int position) {
		cursor = top;
		for (int i = 0; i < position; i++)
			cursor = cursor.getNext();
		return cursor.getData();

	}

    /**
     * A method to add an element at the end of this doubly linked list.
     *
     * @param e The element to be added to the doubly linked list
     */
	public void add(E e) {

		//creates a new NodeD that contain the desired element
	    NodeD<E> newNode = new NodeD<E>();
		newNode.setData(e);

		//if the size of the double linked list is 0,
		//makes "top" equal to  "newNode"
	    if (this.size() == 0) {
	        this.top = newNode;

        }
	    //If the size of the double linked list is 1,
		//Attaches the newNode to "top"
	    else if (this.size() == 1) {
	        newNode.setPrev(this.top);
	        this.top.setNext(newNode);
        }
	    //If the size of the double linked list is > 1,
		//Moves "cursor" to the last element in the list and
		//attaches "newNode" to "cursor"
	    else {
	        setCursor(this.size() - 1);
	        newNode.setPrev(this.cursor);
	        cursor.setNext(newNode);
        }
    }

	public void add(int index, E e) {

		//creates a new NodeD that contain the desired element
		NodeD<E> newNode = new NodeD<E>();
		newNode.setData(e);

		NodeD<E> tempPrevious = new NodeD<E>();
		if (cursor.getPrev() != null) {
			setCursor(index-1);
			tempPrevious = cursor;
		}
		NodeD<E> tempNext = new NodeD<E>();
		if (cursor.getNext() != null) {
			setCursor(index);
			tempPrevious = cursor;
		}

		newNode.setNext(tempNext);
		newNode.setPrev(tempPrevious);
		tempNext.setPrev(newNode);
		tempPrevious.setNext(newNode);
	}

	/**
	 * This will remove a given element from the doubly linked list at position index
	 *
	 * @param index The location of the element to remove
	 * @return E For the element that is contained in the removed node
	 */
	public E remove(int index) {
		//If the array size is 0, do nothing
		if (this.size() == 0) {
			return null;
		}
		//Set the element to where the cursor is
		setCursor(index);
		E element = cursor.getData();

		//Sets the values of the temporary nodes
		NodeD<E> tempPrevious = new NodeD<E>();
		if (cursor.getPrev() != null) {
			tempPrevious = cursor.getPrev();
		}
		NodeD<E> tempNext = new NodeD<E>();
		if (cursor.getNext() != null) {
			tempNext = cursor.getNext();
		}

		//Removes the first element and sets the previous to null
		if (index == 0) {
			this.top = tempNext;
			tempNext.setPrev(null);
		}

		//Removes the element and attaches the elements before and after to one another
		tempPrevious.setNext(tempNext);
		tempNext.setPrev(tempPrevious);
		cursor = this.top;

		return element;
	}

	/**
	 * Private helper method to loop through the message and set the index as directed
	 *
	 * @param index Where the cursor is focused
	 */
	private void setCursor(int index) {
		cursor = this.top;
		for (int i = 0; i < index; i++) {
			cursor = cursor.getNext();
		}
	}

    /**
     * Method to increment the size of a doubly linked list
     *
     * @return the size of the doubly linked list
     */
    public int size() {
	    cursor = this.top;
	    int nodeCounter = 0;
	    while (cursor != null) {
	        nodeCounter++;
	        cursor = cursor.getNext();
        }
	    return nodeCounter;
    }

    /**
     * Prints out the string of data
     *
     * @return
     */
	public String toString() {
		String retVal = "";
		NodeD<E> cur = top;

		while (cur != null) {
			retVal += cur.getData();
			cur = cur.getNext();
		}

		return retVal;
	}
}
