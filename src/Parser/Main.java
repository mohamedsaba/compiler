package Parser;

import lexer.Lexer;
import lexer.Token;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the code to compile (type 'END' on a new line to finish):");

        StringBuilder sourceBuilder = new StringBuilder();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.equals("END")) {
                break;
            }
            sourceBuilder.append(line).append("\n");
        }
        String source = sourceBuilder.toString();

        try {
            Lexer lexer = new Lexer(source);
            List<Token> tokens = lexer.scanTokens();

            Parser parser = new Parser(tokens);
            List<ASTNode> ast = parser.parse();

            System.out.println("--- Abstract Syntax Tree (AST) ---");
            for (ASTNode node : ast) {
                node.print("");
                System.out.println(node.value); 
            }

            System.out.println("\n--- Output ---");
            Interpreter.Interpreter interpreter = new Interpreter.Interpreter(scanner);
            interpreter.interpret(ast);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}