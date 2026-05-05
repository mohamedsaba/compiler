package Parser;

// Although not in the explicit list, ReadNode is needed for the Parser to work with the existing code.
public class ReadNode extends ASTNode {
    public String variableName;

    ReadNode(String variableName) {
        this.variableName = variableName;
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "ReadNode: " + variableName);
    }
}
