package wordladder;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String args[]) {
        System.out.println(
            "\n" +
            "################################################################################\n" + //
            "#                                                                              #\n" + //
            "#   _       _                  _     _                 _      _                #\n" + //
            "#  ( )  _  ( )                ( )   ( )               ( )    ( )               #\n" + //
            "#  | | ( ) | |   _    _ __   _| |   | |       _ _    _| |   _| |   __   _ __   #\n" + //
            "#  | | | | | | /'_`\\ ( '__)/'_` |   | |  _  /'_` ) /'_` | /'_` | /'__`\\( '__)  #\n" + //
            "#  | (_/ \\_) |( (_) )| |  ( (_| |   | |_( )( (_| |( (_| |( (_| |(  ___/| |     #\n" + //
            "#  `\\___x___/'`\\___/'(_)  `\\__,_)   (____/'`\\__,_)`\\__,_)`\\__,_)`\\____)(_)     #\n" + //
            "#                                                                              #\n" + //
            "################################################################################"
            );
            List<String> listOfWords = Solver.loadTxtToList("../src/dictionary.txt");
        while (true) {
            Scanner scanner = new Scanner(System.in);

            
            String opted;
            do {
                System.out.println("\nAlgorithm:\n(1) UCS\n(2) Greedy BFS\n(3) A*");
                System.out.print("Algorithm: ");
                opted = scanner.nextLine();
                if (!opted.equals("1") && !opted.equals("2") && !opted.equals("3")) {
                    System.out.println("Enter a valid option! (1/2/3)");
                }
            } while (!opted.equals("1") && !opted.equals("2") && !opted.equals("3"));
            
            String startWord;
            String endWord;
            boolean validWord = false;
            do {
                System.out.print("\nEnter the starting word: ");
                startWord = scanner.nextLine();
                startWord = startWord.toLowerCase();
                if (listOfWords.contains(startWord)) {
                    validWord = true;
                } else {
                    System.out.println("\nPlease enter a valid english word!");
                }
            } while (!validWord);

            validWord = false;
            do {
                System.out.print("Enter the end word: ");
                endWord = scanner.nextLine();
                endWord = endWord.toLowerCase();
                if (!listOfWords.contains(endWord)) {
                    System.out.println("\nPlease enter a valid english word!");
                } else if (endWord.length() != startWord.length()) {
                    System.out.println("\nPlease enter a word with the same length as the start word!");
                } 
                else {
                    validWord = true;
                }
            } while (!validWord);

            Solver solver = new Solver(startWord, endWord);
            Solver.EvaluationFunction evaluationFunction;
            if (opted.equals("1")) {
                evaluationFunction = solver.new UCSEvaluationFunction();
            } else if (opted.equals("2")) {
                evaluationFunction = solver.new GreedyBFSEvaluationFunction();
            } else {
                evaluationFunction = solver.new AStarEvaluationFunction();
            }

            long startTime = System.nanoTime();
            ReturnValues result = solver.findPath(listOfWords, evaluationFunction);
            long endTime = System.nanoTime();
            
            List <String> resultPath = result.getPath();
            int numVisited = result.getNumVisited();

            long executionTime = (endTime - startTime) / 1000000;

            int length = resultPath.size();
            if (length != 0) {
                System.out.println("\nPath:\n");
                for (int i = length - 1; i >= 0; i--) {
                    System.out.println(resultPath.get(i));
                }
            } else {
                System.out.println("\nNo Solution");
            }

            System.out.println("\nExecution time: " + executionTime + "ms");
            System.out.println("Number of nodes visited: " + numVisited);

            String cont;
            do {
                System.out.print("\nDo you want to play again? (Y/N) ");
                cont = scanner.nextLine();
                cont = cont.toLowerCase();
                if (cont.equals("n")) {
                    System.out.println(
                        "\n" + 
                        "#############################################################\n" +
                        "#                                                           #\n" +
                        "#   ___                      _     ___                  _   #\n" +
                        "#  (  _`\\                   ( )   (  _`\\               ( )  #\n" +
                        "#  | ( (_)   _      _      _| |   | (_) ) _   _    __  | |  #\n" +
                        "#  | |___  /'_`\\  /'_`\\  /'_` |   |  _ <'( ) ( ) /'__`\\| |  #\n" +
                        "#  | (_, )( (_) )( (_) )( (_| |   | (_) )| (_) |(  ___/| |  #\n" +
                        "#  (____/'`\\___/'`\\___/'`\\__,_)   (____/'`\\__, |`\\____)(_)  #\n" +
                        "#                                        ( )_| |       (_)  #\n" +
                        "#                                        `\\___/'            #\n" +
                        "#                                                           #\n" +
                        "#############################################################\n"
                        );
                    scanner.close();
                    System.exit(0);
                }
            } while (!cont.equals("y") && !cont.equals("n"));
        }
    }
}
