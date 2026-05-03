package Parser;

import java.util.*;

class ASTNode {
    NodeType type;
    Object value;
    List<ASTNode> children;

    ASTNode(NodeType type) {
        this.type = type;
        this.children = new ArrayList<>();
    }
}

enum NodeType {
    BINARY,
    UNARY,
    LITERAL,
    VARIABLE,
    ASSIGN
}
