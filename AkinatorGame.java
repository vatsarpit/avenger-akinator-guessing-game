import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

class TreeNode {
    String data;
    TreeNode yesNode;
    TreeNode noNode;

    public TreeNode(String data) {
        this.data = data;
    }
}

public class AvengerAkinator {
    public static void main(String[] args) {
        TreeNode root = buildTreeFromFile("text.txt");

        if (root != null) {
            Scanner scanner = new Scanner(System.in);
            playGame(root, scanner);
            scanner.close();
        } else {
            System.out.println("Error reading the tree from the file.");
        }
    }

    private static TreeNode buildTreeFromFile(String fileName) {
        try {
            File file = new File(fileName);
            Scanner scanner = new Scanner(file);

            // Read the first line as the root question
            String rootQuestion = scanner.nextLine();
            TreeNode root = new TreeNode(rootQuestion);

            // Read the rest of the lines as character names and questions
            while (scanner.hasNextLine()) {
                String characterName = scanner.nextLine();
                String yesQuestion = scanner.nextLine();
                String noQuestion = scanner.nextLine();

                TreeNode characterNode = new TreeNode(characterName);
                characterNode.yesNode = new TreeNode(yesQuestion);
                characterNode.noNode = new TreeNode(noQuestion);

                // Add the character node to the tree
                addNodeToTree(root, characterNode);
            }

            scanner.close();
            return root;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void addNodeToTree(TreeNode root, TreeNode newNode) {
        if (root.yesNode == null) {
            root.yesNode = newNode;
        } else if (root.noNode == null) {
            root.noNode = newNode;
        } else {
            // Randomly choose to go left or right to balance the tree
            if (Math.random() < 0.5) {
                addNodeToTree(root.yesNode, newNode);
            } else {
                addNodeToTree(root.noNode, newNode);
            }
        }
    }

    private static void playGame(TreeNode node, Scanner scanner) {
        System.out.println(node.data);

        if (node.yesNode == null && node.noNode == null) {
            // Leaf node, guess made
            System.out.println("I guessed it! Is it correct? (yes/no): ");
            String answer = scanner.next().toLowerCase();

            if (answer.equals("yes")) {
                System.out.println("Awesome! I win!");
            } else if (answer.equals("no")) {
                // Expand the tree
                System.out.println("Oops! What character were you thinking of? ");
                String newCharacter = scanner.next();
                System.out.println("Please provide a yes/no question to distinguish " + newCharacter + " from " + node.data);
                String newQuestion = scanner.next();
                System.out.println("If the answer for " + newCharacter + " is yes, what would be the correct response?");
                String newAnswerYes = scanner.next();
                System.out.println("If the answer for " + newCharacter + " is no, what would be the correct response?");
                String newAnswerNo = scanner.next();

                // Update the tree
                node.data = newQuestion;
                node.yesNode = new TreeNode(newAnswerYes);
                node.noNode = new TreeNode(newAnswerNo);
            }
        } else {
            // Non-leaf node, continue asking questions
            System.out.print("Enter your answer (yes/no): ");
            String answer = scanner.next().toLowerCase();

            if (answer.equals("yes")) {
                playGame(node.yesNode, scanner);
            } else if (answer.equals("no")) {
                playGame(node.noNode, scanner);
            } else {
                System.out.println("Invalid input. Please enter 'yes' or 'no'.");
                playGame(node, scanner);
            }
        }
    }
}
