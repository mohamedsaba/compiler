package Parser;

import lexer.Token;
import lexer.TokenType;

import java.util.List;
import java.util.ArrayList;

public class Parser {

    private final List<Token> tokens;
    private int pos = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    private Token current() {
        return tokens.get(pos);
    }

    private boolean check(TokenType type) {
        return current().type == type;
    }

    private Token consume(TokenType expected) {
        Token t = current();
        if (t.type != expected) {
            throw new RuntimeException(
                "Syntax Error — line " + t.line + ", col " + t.column +
                ": expected " + expected + " but got " + t.type +
                " (\"" + t.lexeme + "\")"
            );
        }
        pos++;
        return t;
    }

    // ── Entry Point ───────────────────────

    public List<ASTNode> parse() {
        List<ASTNode> statements = new ArrayList<>();
        while (!check(TokenType.EOF)) {
            statements.add(parseStatement());
        }
        return statements;
    }

    private ASTNode parseStatement() {
        if (check(TokenType.PRINT)) return parsePrint();
        if (check(TokenType.READ))  return parseRead();
        if (check(TokenType.IDENT)) return parseAssign();

        Token t = current();
        throw new RuntimeException(
            "Syntax Error — line " + t.line +
            ": unexpected token \"" + t.lexeme + "\""
        );
    }


    private ASTNode parseAssign() {
        Token name = consume(TokenType.IDENT);
        consume(TokenType.ASSIGN);
        ASTNode expr = parseExpr();
        consume(TokenType.SEMICOLON);

        return new AssignNode(name.lexeme, expr);
    }


    private ASTNode parsePrint() {
        consume(TokenType.PRINT);
        ASTNode expr = parseExpr();
        consume(TokenType.SEMICOLON);

        return new PrintNode(expr);
    }


    private ASTNode parseRead() {
        consume(TokenType.READ);
        Token name = consume(TokenType.IDENT);
        consume(TokenType.SEMICOLON);

        return new ReadNode(name.lexeme);
    }

    
    private ASTNode parseExpr() {
        ASTNode left = parseTerm();
        while (check(TokenType.PLUS) || check(TokenType.MINUS)) {
            String op = current().lexeme;
            pos++;
            ASTNode right = parseTerm();
            left = new BinaryOp(op, left, right);
        }
        return left;
    }

    
    private ASTNode parseTerm() {
        ASTNode left = parseFactor();
        while (check(TokenType.MUL) || check(TokenType.DIV)) {
            String op = current().lexeme;
            pos++;
            ASTNode right = parseFactor();
            left = new BinaryOp(op, left, right);
        }
        return left;
    }


    private ASTNode parseFactor() {
        Token t = current();

        if (check(TokenType.NUMBER)) {
            pos++;
            return new NumberNode((Double) t.literal);
        }

        if (check(TokenType.IDENT)) {
            pos++;
            return new VariableNode(t.lexeme);
        }

        throw new RuntimeException(
            "Syntax Error — line " + t.line + ", col " + t.column +
            ": unexpected token \"" + t.lexeme + "\""
        );
    }
}
