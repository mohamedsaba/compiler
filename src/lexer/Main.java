package lexer;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        String source = "read x;\n" +
                        "y := x + 10;\n" +
                        "print y;";
        
        System.out.println("Scanning source:\n" + source + "\n");
        
        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.scanTokens();
        
        for (Token token : tokens) {
            System.out.println(token);
        }
    }
}
