package Parser;

public class AssignNode extends ASTNode {
    public String variableName;
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
