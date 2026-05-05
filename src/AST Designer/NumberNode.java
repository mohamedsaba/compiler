package Parser;

public class NumberNode extends ASTNode {
    public double value;

    NumberNode(double value) {
        this.value = value;
    }

    @Override
    public void print(String indent) {
        System.out.println(indent + "NumberNode: " + value);
    }
}
