import java.io.Serializable;

public class NodeD<E> implements Serializable {
    public E data;
    public NodeD next;
    public NodeD prev;

    public NodeD() {
        super();
    }

    public NodeD(E data, NodeD next, NodeD prev) {
        this.data = data;
        this.next = next;
        this.prev = prev;
    }

    public E getData() {
        return data;
    }

    public void setData(E data2) {
        this.data = data2;
    }

    public NodeD getNext() {
        return next;
    }

    public void setNext(NodeD next) {
        this.next = next;
    }

    public NodeD getPrev() {
        return prev;
    }

    public void setPrev(NodeD prev) {
        this.prev = prev;
    }
}
