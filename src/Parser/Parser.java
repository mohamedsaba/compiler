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

        ASTNode node = new ASTNode(NodeType.ASSIGN);
        node.value = name.lexeme;
        node.children.add(expr);
        return node;
    }


    private ASTNode parsePrint() {
        consume(TokenType.PRINT);
        ASTNode expr = parseExpr();
        consume(TokenType.SEMICOLON);

        ASTNode node = new ASTNode(NodeType.UNARY);
        node.value = "print";
        node.children.add(expr);
        return node;
    }


    private ASTNode parseRead() {
        consume(TokenType.READ);
        Token name = consume(TokenType.IDENT);
        consume(TokenType.SEMICOLON);

        ASTNode var = new ASTNode(NodeType.VARIABLE);
        var.value = name.lexeme;

        ASTNode node = new ASTNode(NodeType.UNARY);
        node.value = "read";
        node.children.add(var);
        return node;
    }

    
    private ASTNode parseExpr() {
        ASTNode left = parseTerm();
        while (check(TokenType.PLUS) || check(TokenType.MINUS)) {
            String op = current().lexeme;
            pos++;
            ASTNode right = parseTerm();

            ASTNode node = new ASTNode(NodeType.BINARY);
            node.value = op;
            node.children.add(left);
            node.children.add(right);
            left = node;
        }
        return left;
    }

    
    private ASTNode parseTerm() {
        ASTNode left = parseFactor();
        while (check(TokenType.MUL) || check(TokenType.DIV)) {
            String op = current().lexeme;
            pos++;
            ASTNode right = parseFactor();

            ASTNode node = new ASTNode(NodeType.BINARY);
            node.value = op;
            node.children.add(left);
            node.children.add(right);
            left = node;
        }
        return left;
    }


    private ASTNode parseFactor() {
        Token t = current();

        if (check(TokenType.NUMBER)) {
            pos++;
            ASTNode node = new ASTNode(NodeType.LITERAL);
            node.value = (Double) t.literal;
            return node;
        }

        if (check(TokenType.IDENT)) {
            pos++;
            ASTNode node = new ASTNode(NodeType.VARIABLE);
            node.value = t.lexeme;
            return node;
        }

        throw new RuntimeException(
            "Syntax Error — line " + t.line + ", col " + t.column +
            ": unexpected token \"" + t.lexeme + "\""
        );
    }
}