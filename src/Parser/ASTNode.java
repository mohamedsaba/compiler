package Parser;

import java.util.*;

public abstract class ASTNode {
    NodeType type;
    Object value;
    List<ASTNode> children;

    ASTNode() {}

    ASTNode(NodeType type) {
        this.type = type;
        this.children = new ArrayList<>();
    }

    public abstract void print(String indent);
}

enum NodeType {
    BINARY,
    UNARY,
    LITERAL,
    VARIABLE,
    ASSIGN
}
