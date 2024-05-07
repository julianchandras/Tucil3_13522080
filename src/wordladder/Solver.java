package wordladder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class Solver {
    /**
     * interface EvaluationFunction
     */
    public interface EvaluationFunction {
        public abstract int calculateCost(Node node);        
    }

    /**
     * implementation of EvaluationFunction for Uniform Cost Search
     */
    class UCSEvaluationFunction implements EvaluationFunction {
        @Override
        public int calculateCost(Node node) {
            return node.getDepth();
        }
    }

    /**
     * implementation of EvaluationFunction for Greedy Best First Search involving heuristic
     */
    class GreedyBFSEvaluationFunction implements EvaluationFunction {
        @Override
        public int calculateCost(Node node) {
            String word = node.getWord();
            
            int numberOfDifferentChars = 0;
            for (int i = 0; i < word.length(); i++) {
                if (word.charAt(i) != endWord.charAt(i)) {
                    numberOfDifferentChars++;
                }
            }
            return numberOfDifferentChars;
        }
    }

    /**
     * implementation of EvaluationFunction for AStar, combining UCS evaluation function and Greedy BFS heuristic
     */
    class AStarEvaluationFunction implements EvaluationFunction {
        @Override
        public int calculateCost(Node node) {
            UCSEvaluationFunction ucsEvaluationFunction = new UCSEvaluationFunction();
            GreedyBFSEvaluationFunction greedyBFSEvaluationFunction = new GreedyBFSEvaluationFunction();

            return ucsEvaluationFunction.calculateCost(node) + greedyBFSEvaluationFunction.calculateCost(node);
        }
    }

    private String startWord;
    private String endWord;

    public Solver(String startWord, String endWord) {
        this.startWord = startWord;
        this.endWord = endWord;
    }

    /**
     * 
     * @param listOfWords list of words from the dictionary used in current game
     * @param evaluationFunction evaluation function unique to the opted algorithm
     * @return resulting path from the route planning algorithm
     */
    public ReturnValues findPath(List<String> listOfWords, EvaluationFunction evaluationFunction) {
        PrioQueue openList = new PrioQueue();
        List<Node> closedList = new ArrayList<>();

        Node startNode = new Node(this.startWord, 0, 0, null);
        openList.enqueue(startNode);

        Node currentNode = new Node();
        
        boolean found = false;
        while (!found && !openList.getBuffer().isEmpty()) {
            currentNode = openList.dequeue();
            closedList.add(currentNode);
            if (currentNode.getWord().equals(endWord)) {
                found = true;
            } else {
                List<Node> childNodes = new ArrayList<>();
                childNodes = currentNode.findAdjacent(listOfWords);
                childNodes.forEach(node -> {
                    node.setCost(evaluationFunction.calculateCost(node));
                });

                /* if a node with the same position as successor is in the OPEN list which has a lower f than successor, skip this successor
                   if a node with the same position as successor is in the CLOSED list which has a lower f than successor, skip this successor
                   otherwise, add  the node to the open list
                   ref: https://www.geeksforgeeks.org/a-search-algorithm/ */

                if (evaluationFunction instanceof GreedyBFSEvaluationFunction) {
                    openList.getBuffer().clear();
                }
                for (Node childNode: childNodes) {
                    if (!isWordInOpenListWithLowerEvalFunc(openList, childNode) && !isWordInClosedListWithLowerEvalFunc(closedList, childNode)) {
                        openList.enqueue(childNode);
                    }
                }
            }
        }

        List<String> result = new ArrayList<>();
        int numVisited = 0;
        if (found) {
            result = (currentNode.reconstructPath()).stream().map(Node::getWord).collect(Collectors.toList());
        }
        numVisited = closedList.size();
        return new ReturnValues(result, numVisited);
    }

    /**
     * 
     * @param closedList closed list of the route planning algorithm
     * @param succesor successor of current node being expanded
     * @return true if there exist a node with the same position as successor is in the CLOSED list which has a lower f than successor
     */
    private static boolean isWordInClosedListWithLowerEvalFunc(List<Node> closedList, Node succesor) {
        if (closedList.isEmpty()) {
            return false;
        }
        for (Node element: closedList) {
            if (element.getWord().equals(succesor.getWord())) {
                if (succesor.getCost() >= element.getCost()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 
     * @param openList open list of the route planning algorithm
     * @param succesor successor of current node being expanded
     * @return true if there exist a node with the same position as successor is in the OPEN list which has a lower f than successor
     */
    private static boolean isWordInOpenListWithLowerEvalFunc(PrioQueue openList, Node succesor) {
        if (openList.getBuffer().isEmpty()) {
            return false;
        }
        List<Node> buffer = openList.getBuffer();
        for (Node element: buffer) {
            if (element.getWord().equals(succesor.getWord())) {
                if (succesor.getCost() >= element.getCost()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 
     * @param filename the file with list of words (dictionary) to be used in the game
     * @return list of words that will be used in the game
     */
    public static List<String> loadTxtToList(String filename) {
        List<String> words = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                words.add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return words;
    }
}