/*****************************************************
 * This will store nodes in various clipboards to be used in pasting
 * to the original message to further scramble it.
 *
 * Created by Tim Bomers and Matt Hendrick
 * CIS 163
 *
 *****************************************************/
public class NodeCB {
    private int clipBoardNumber;
    private DoubleLinkedList<Character> myLinkedList;
    private NodeCB next;

    public NodeCB() {
    }

    public int getClipBoardNumber() {
        return clipBoardNumber;
    }

    public void setClipBoardNumber(int clipBoardNumber) {
        this.clipBoardNumber = clipBoardNumber;
    }

    public NodeCB getNext() {
        return next;
    }

    public void setNext(NodeCB nextNode) {
        this.next = nextNode;
    }

    public DoubleLinkedList getMyLinkedList() {
        return myLinkedList;
    }
}
