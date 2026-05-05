package Parser;

public class PrintNode extends ASTNode {
    public ASTNode expression;

    PrintNode(ASTNode expression) {
        this.expression = expression;
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "PrintNode:");
        expression.print(indent + "  ");
    }
}
