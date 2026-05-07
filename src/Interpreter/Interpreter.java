package Interpreter;

import Parser.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class Interpreter {

    // =============================================
    // Symbol Table
    // =============================================
    private final Map<String, Double> symbolTable = new HashMap<>();

    // Scanner for read
    private final Scanner scanner;

    public Interpreter(Scanner scanner) {
        this.scanner = scanner;
    }

    // =============================================
    // interpret
    // =============================================
    public void interpret(List<ASTNode> statements) {
        for (ASTNode statement : statements) {
            execute(statement);
        }
    }

    // =============================================
    // execute
    // =============================================
    private void execute(ASTNode node) {
        if (node.type == NodeType.ASSIGN) {
            executeAssign(node);
        } else if (node.type == NodeType.UNARY && "print".equals(node.value)) {
            executePrint(node);
        } else if (node.type == NodeType.UNARY && "read".equals(node.value)) {
            executeRead(node);
        } else {
            throw new RuntimeError("Unknown statement type: " + node.type);
        }
    }

    // =============================================
    // evaluate
    // =============================================
    private double evaluate(ASTNode node) {
        if (node.type == NodeType.LITERAL) {
            return (Double) node.value;
        }

        if (node.type == NodeType.VARIABLE) {
            String name = (String) node.value;
            if (!symbolTable.containsKey(name)) {
                throw new RuntimeError("Undefined variable: '" + name + "'");
            }
            return symbolTable.get(name);
        }

        if (node.type == NodeType.BINARY) {
            return evaluateBinaryOp(node);
        }

        if (node.type == NodeType.UNARY && ("-".equals(node.value) || "+".equals(node.value))) {
            return evaluateUnaryOp(node);
        }

        throw new RuntimeError("Cannot evaluate node of type: " + node.type);
    }

    private void executeAssign(ASTNode node) {
        String varName = (String) node.value;
        double value = evaluate(node.children.get(0));
        symbolTable.put(varName, value);
    }

    private void executePrint(ASTNode node) {
        double value = evaluate(node.children.get(0));

        if (value == Math.floor(value) && !Double.isInfinite(value)) {
            System.out.println((long) value);
        } else {
            System.out.println(value);
        }
    }

    private void executeRead(ASTNode node) {
        ASTNode varNode = node.children.get(0);
        String varName = (String) varNode.value;
        System.out.print("Enter value for " + varName + ": ");
        try {
            double value = Double.parseDouble(scanner.nextLine().trim());
            symbolTable.put(varName, value);
        } catch (NumberFormatException e) {
            throw new RuntimeError("Invalid number entered for variable: " + varName);
        }
    }

    private double evaluateBinaryOp(ASTNode node) {
        double left  = evaluate(node.children.get(0));
        double right = evaluate(node.children.get(1));
        String operator = (String) node.value;

        switch (operator) {
            case "+": return left + right;
            case "-": return left - right;
            case "*": return left * right;
            case "/":
                if (right == 0) {
                    throw new RuntimeError("Division by zero!");
                }
                return left / right;
            default:
                throw new RuntimeError("Unknown operator: " + operator);
        }
    }

    private double evaluateUnaryOp(ASTNode node) {
        double value = evaluate(node.children.get(0));
        String operator = (String) node.value;
        switch (operator) {
            case "-": return -value;
            case "+": return +value;
            default:
                throw new RuntimeError("Unknown unary operator: " + operator);
        }
    }

    public Map<String, Double> getSymbolTable() {
        return symbolTable;
    }

    public boolean hasVariable(String name) {
        return symbolTable.containsKey(name);
    }

    public double getVariable(String name) {
        if (!symbolTable.containsKey(name)) {
            throw new RuntimeError("Variable not found: " + name);
        }
        return symbolTable.get(name);
    }

    public static class RuntimeError extends RuntimeException {
        public RuntimeError(String message) {
            super("[Runtime Error] " + message);
        }
    }
}