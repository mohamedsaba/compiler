package Parser;

import java.util.ArrayList;
import java.util.List;

/**
 * Task 3: AST Designer
 */

public abstract class ASTNode {
    public abstract void print(String indent);
}

class BinaryOp extends ASTNode {
    String operator;
    ASTNode left;
    ASTNode right;

    BinaryOp(String operator, ASTNode left, ASTNode right) {
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "BinaryOp (" + operator + "):");
        left.print(indent + "  ");
        right.print(indent + "  ");
    }
}

class NumberNode extends ASTNode {
    double value;

    NumberNode(double value) {
        this.value = value;
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "NumberNode: " + value);
    }
}

class VariableNode extends ASTNode {
    String name;

    VariableNode(String name) {
        this.name = name;
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "VariableNode: " + name);
    }
}

class AssignNode extends ASTNode {
    String variableName;
    ASTNode value;

    AssignNode(String variableName, ASTNode value) {
        this.variableName = variableName;
        this.value = value;
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "AssignNode (" + variableName + "):");
        value.print(indent + "  ");
    }
}

class PrintNode extends ASTNode {
    ASTNode expression;

    PrintNode(ASTNode expression) {
        this.expression = expression;
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "PrintNode:");
        expression.print(indent + "  ");
    }
}

// Although not in the explicit list, ReadNode is needed for the Parser to work with the existing code.
class ReadNode extends ASTNode {
    String variableName;

    ReadNode(String variableName) {
        this.variableName = variableName;
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "ReadNode: " + variableName);
    }
}
