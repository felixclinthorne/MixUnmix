public class clipBdLinkedList {
    private NodeCB top;
    private NodeCB tail;

    /**
     *
     */
    public clipBdLinkedList() {
        tail = top = null;
    }

    /**
     *
     * @return
     */
    public int size() {
        int nodeCounter = 0;
        NodeCB temp;
        temp = this.top;

        while(temp != null) {
            nodeCounter++;
            temp = temp.getNext();
        }
        return nodeCounter;
    }

    /**
     *
     * @param num
     * @param clipBoardMessage
     */
    public void add(int num, String clipBoardMessage) {
        NodeCB newNode = new NodeCB();
        newNode.setClipBoardNumber(num);
        for (int i = 0; i < clipBoardMessage.length(); i++) {
            newNode.getMyLinkedList().add(clipBoardMessage.charAt(i));
        }

        if (size() == 0) {
            top = newNode;
            tail = top;
        } else if (size() == 1) {
            tail = newNode;
            top.setNext(tail);
        } else {
            tail.setNext(newNode);
            tail = newNode;
        }
    }

    /**
     *
     * @param num
     * @return
     */
    public NodeCB get(int num) {
        NodeCB temp;
        temp = top;

        while(temp != null) {
            if(temp.getClipBoardNumber() == num) {
                return temp;
            }
            temp = temp.getNext();
        }
        throw new IllegalArgumentException();
    }
}
