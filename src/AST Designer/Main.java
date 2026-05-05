package lexer;

import Parser.Parser;
import Parser.ASTNode;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String source = "read x;\n" +
                        "y := x + 10;\n" +
                        "print y;";

        try {
            Lexer lexer = new Lexer(source);
            List<Token> tokens = lexer.scanTokens();

            Parser parser = new Parser(tokens);
            List<ASTNode> ast = parser.parse();

            System.out.println("--- Abstract Syntax Tree (AST) ---");
            for (ASTNode node : ast) {
                node.print("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
