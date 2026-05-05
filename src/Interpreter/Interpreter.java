package Interpreter;

import Parser.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class Interpreter {

    // =============================================
    // Symbol Table: بتخزن المتغيرات وقيمها
    // مثال: x=10, y=20
    // =============================================
    private final Map<String, Double> symbolTable = new HashMap<>();

    // Scanner عشان نقرأ من المستخدم في حالة read
    private final Scanner scanner = new Scanner(System.in);

    // =============================================
    // نقطة الدخول الرئيسية
    // بتاخد List<ASTNode> (البرنامج كامل)
    // =============================================
    public void interpret(List<ASTNode> statements) {
        for (ASTNode statement : statements) {
            execute(statement);
        }
    }

    // =============================================
    // execute: بتنفذ أي statement
    // مش بترجع قيمة (void)
    // =============================================
    private void execute(ASTNode node) {
        if (node instanceof AssignNode) {
            executeAssign((AssignNode) node);

        } else if (node instanceof PrintNode) {
            executePrint((PrintNode) node);

        } else if (node instanceof ReadNode) {
            executeRead((ReadNode) node);

        } else {
            throw new RuntimeError("Unknown statement type: " + node.getClass().getSimpleName());
        }
    }

    // =============================================
    // evaluate: بتحسب قيمة أي expression
    // بترجع Double
    // =============================================
    private double evaluate(ASTNode node) {

        // رقم مباشر: مثال 42
        if (node instanceof NumberNode) {
            return ((NumberNode) node).value;
        }

        // متغير: مثال x
        if (node instanceof VariableNode) {
            String name = ((VariableNode) node).name;
            if (!symbolTable.containsKey(name)) {
                throw new RuntimeError("Undefined variable: '" + name + "'");
            }
            return symbolTable.get(name);
        }

        // عملية حسابية: مثال x + 5، a * b
        if (node instanceof BinaryOp) {
            return evaluateBinaryOp((BinaryOp) node);
        }

        // عملية سالبة: مثال -x
        if (node instanceof UnaryOpNode) {
            return evaluateUnaryOp((UnaryOpNode) node);
        }

        throw new RuntimeError("Cannot evaluate node: " + node.getClass().getSimpleName());
    }

    // =============================================
    // تنفيذ الـ Assignment: x := 10
    // =============================================
    private void executeAssign(AssignNode node) {
        double value = evaluate(node.expression);
        symbolTable.put(node.variableName, value);
        // Debug (اختياري): System.out.println("[DEBUG] " + node.variableName + " = " + value);
    }

    // =============================================
    // تنفيذ الـ Print: print x
    // =============================================
    private void executePrint(PrintNode node) {
        double value = evaluate(node.expression);

        // لو الرقم integer اطبعه من غير فاصلة
        if (value == Math.floor(value) && !Double.isInfinite(value)) {
            System.out.println((long) value);
        } else {
            System.out.println(value);
        }
    }

    // =============================================
    // تنفيذ الـ Read: read x
    // بيقرأ من المستخدم ويخزن في المتغير
    // =============================================
    private void executeRead(ReadNode node) {
        System.out.print("Enter value for " + node.variableName + ": ");
        try {
            double value = Double.parseDouble(scanner.nextLine().trim());
            symbolTable.put(node.variableName, value);
        } catch (NumberFormatException e) {
            throw new RuntimeError("Invalid number entered for variable: " + node.variableName);
        }
    }

    // =============================================
    // حساب العمليات الحسابية
    // =============================================
    private double evaluateBinaryOp(BinaryOpNode node) {
        double left  = evaluate(node.left);
        double right = evaluate(node.right);

        switch (node.operator) {
            case "+": return left + right;
            case "-": return left - right;
            case "*": return left * right;
            case "/":
                if (right == 0) {
                    throw new RuntimeError("Division by zero!");
                }
                return left / right;
            default:
                throw new RuntimeError("Unknown operator: " + node.operator);
        }
    }

    // =============================================
    // حساب العمليات الأحادية (Unary)
    // مثال: -x أو +x
    // =============================================
    private double evaluateUnaryOp(UnaryOpNode node) {
        double value = evaluate(node.operand);
        switch (node.operator) {
            case "-": return -value;
            case "+": return +value;
            default:
                throw new RuntimeError("Unknown unary operator: " + node.operator);
        }
    }

    // =============================================
    // Symbol Table: للـ Testing والـ Debugging
    // Developer #5 ممكن يستخدمها في الـ Tests
    // =============================================
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

    // =============================================
    // RuntimeError: Exception خاص بالـ Interpreter
    // =============================================
    public static class RuntimeError extends RuntimeException {
        public RuntimeError(String message) {
            super("[Runtime Error] " + message);
        }
    }
}