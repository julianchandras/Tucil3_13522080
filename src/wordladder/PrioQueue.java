package wordladder;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
* Priority Queue to store the node with the cost in ascending order.
*/
class PrioQueue {
    private List<Node> buffer = new ArrayList<>();

    public List<Node> getBuffer() {
        return buffer;
    }

    public void enqueue(Node q) {
        int idx = Collections.binarySearch(buffer, q, (a, b) -> a.getCost() - b.getCost());

        // if q is not in buffer, idx = (-(insertion point) - 1). Therefore insertion point = - (idx + 1)
        if (idx < 0) {
            idx = -(idx + 1);
        }

        buffer.add(idx, q);
    }

    public Node dequeue() {
        Node firstNode = buffer.get(0);
        buffer.remove(0);
        return firstNode;
    }
}
