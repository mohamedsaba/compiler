package lexer;

public enum TokenType {
    // Single-character tokens
    PLUS, MINUS, MUL, DIV, SEMICOLON,

    // One or two character tokens
    ASSIGN, // :=

    // Literals
    IDENT, NUMBER,

    // Keywords
    PRINT, READ,

    EOF
}
