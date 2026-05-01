package lexer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static lexer.TokenType.*;

public class Lexer {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private int start = 0;
    private int current = 0;
    private int line = 1;
    private int column = 1;
    private int tokenStartColumn = 1;

    private static final Map<String, TokenType> keywords;

    static {
        keywords = new HashMap<>();
        keywords.put("print", PRINT);
        keywords.put("read", READ);
    }

    public Lexer(String source) {
        this.source = source;
    }

    /**
     * Optional: Helper to create a Lexer from a file path.
     */
    public static Lexer fromFile(String path) throws java.io.IOException {
        String content = java.nio.file.Files.readString(java.nio.file.Paths.get(path));
        return new Lexer(content);
    }

    public List<Token> scanTokens() {
        while (!isAtEnd()) {
            start = current;
            tokenStartColumn = column;
            scanToken();
        }

        tokens.add(new Token(EOF, "", null, line, column));
        return tokens;
    }

    private void scanToken() {
        char c = advance();
        switch (c) {
            case '+': addToken(PLUS); break;
            case '-': addToken(MINUS); break;
            case '*': addToken(MUL); break;
            case '/': addToken(DIV); break;
            case ';': addToken(SEMICOLON); break;
            case ':':
                if (match('=')) {
                    addToken(ASSIGN);
                } else {
                    reportError(line, column, "Unexpected character: ':' - Expected '=' after ':'");
                }
                break;
            case ' ':
            case '\r':
            case '\t':
                // Ignore whitespace.
                break;
            case '\n':
                line++;
                column = 1;
                break;
            default:
                if (isDigit(c)) {
                    number();
                } else if (isAlpha(c)) {
                    identifier();
                } else {
                    reportError(line, column, "Unexpected character: " + c);
                }
                break;
        }
    }

    private void identifier() {
        while (isAlphaNumeric(peek())) advance();

        String text = source.substring(start, current);
        TokenType type = keywords.get(text);
        if (type == null) type = IDENT;
        addToken(type);
    }

    private void number() {
        while (isDigit(peek())) advance();

        addToken(NUMBER, Double.parseDouble(source.substring(start, current)));
    }

    private boolean match(char expected) {
        if (isAtEnd()) return false;
        if (source.charAt(current) != expected) return false;

        current++;
        column++;
        return true;
    }

    private char peek() {
        if (isAtEnd()) return '\0';
        return source.charAt(current);
    }

    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') ||
               (c >= 'A' && c <= 'Z') ||
                c == '_';
    }

    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean isAtEnd() {
        return current >= source.length();
    }

    private char advance() {
        column++;
        return source.charAt(current++);
    }

    private void addToken(TokenType type) {
        addToken(type, null);
    }

    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line, tokenStartColumn));
    }

    private void reportError(int line, int col, String message) {
        System.err.printf("[line %d, column %d] Error: %s%n", line, col, message);
    }
}
