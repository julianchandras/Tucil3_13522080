package wordladder;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private String word;
    private int cost;
    private int depth;
    private Node parent;

    Node() {}

    Node(String word, int depth, Node parent) {
        this.word = word;
        this.depth = depth;
        this.parent = parent;
    }

    Node(String word, int cost, int depth, Node parent) {
        this.word = word;
        this.cost = cost;
        this.depth = depth;
        this.parent = parent;
    }

    public String getWord() {
        return word;
    }

    public int getCost() {
        return cost;
    }

    public int getDepth() {
        return depth;
    }

    public Node getParent() {
        return parent;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public List<Node> findAdjacent(List<String> listOfWords) {
        int length = listOfWords.size();
        List<Node> adjacentList = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            String tempWord = listOfWords.get(i);
            if (isAdjacent(tempWord)) {
                Node newNode = new Node(tempWord, this.depth + 1, this);
                adjacentList.add(newNode);
            }
        }
        return adjacentList;
    }

    public List<Node> reconstructPath() {
        Node currentNode = this;
        List<Node> path = new ArrayList<>();
        while (currentNode != null) {
            path.add(currentNode);
            currentNode = currentNode.parent;
        }

        return path;
    }

    private boolean isAdjacent(String word2) {
        // Check if words have the same length
        if (this.word.length() != word2.length()) {
            return false;
        }

        int diffCount = 0;
        for (int i = 0; i < this.word.length(); i++) {
            if (this.word.charAt(i) != word2.charAt(i)) {
                diffCount++;
                if (diffCount > 1) {
                    return false; // More than one difference found
                }
            }
        }
        return diffCount == 1; // Exactly one difference found
    }
}