package wordladder;

import java.util.List;

public class ReturnValues {
    private List<String> path;
    private int numVisited;

    ReturnValues(List<String> path, int numVisited) {
        this.path = path;
        this.numVisited = numVisited;
    }

    public List<String> getPath() {
        return path;
    }
    
    public int getNumVisited() {
        return numVisited;
    }
}
