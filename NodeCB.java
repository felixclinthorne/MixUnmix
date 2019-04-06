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
    private NodeD<Character> topOfClipBoard;
    private NodeCB next;

    public NodeCB() {
    }

    public int getClipBoardNumber() {
        return clipBoardNumber;
    }

    public void setClipBoardNumber(int clipBoardNumber) {
        this.clipBoardNumber = clipBoardNumber;
    }

    public NodeD getTopOfClipBoard() {
        return topOfClipBoard;
    }

    public void setTopOfClipBoard(NodeD topOfClipBoard) {
        this.topOfClipBoard = topOfClipBoard;
    }
}
