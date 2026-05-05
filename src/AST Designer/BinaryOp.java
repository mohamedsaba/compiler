package Parser;

/**
 * Task 3: AST Designer
 */



 public  class BinaryOp extends ASTNode {
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
