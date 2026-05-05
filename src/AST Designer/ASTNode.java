package Parser;

public class ASTNode extends ASTNode {
    String name;

    ASTNode(String name) {
        this.name = name;
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "VariableNode: " + name);
    }
}

