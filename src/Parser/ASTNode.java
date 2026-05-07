package Parser;

import java.util.*;

public abstract class ASTNode {
    public NodeType type;
    public Object value;
    public List<ASTNode> children;

    public ASTNode() {}

    public ASTNode(NodeType type) {
        this.type = type;
        this.children = new ArrayList<>();
    }

    public void print(String indent) {
        String label;
        switch (type) {
            case ASSIGN:  label = "ASSIGN(" + value + ")"; break;
            case BINARY:  label = "BINARY(" + value + ")"; break;
            case UNARY:   label = "UNARY(" + value + ")";  break;
            case LITERAL: label = "LITERAL(" + formatNumber(value) + ")"; break;
            case VARIABLE:label = "VARIABLE(" + value + ")"; break;
            default:      label = type + "(" + value + ")"; break;
        }
        System.out.println(indent + label);
        if (children != null) {
            for (int i = 0; i < children.size(); i++) {
                boolean last = (i == children.size() - 1);
                String branch = last ? "`-- " : "|-- ";
                String childIndent = indent + (last ? "    " : "|   ");
                System.out.print(indent + branch);
                children.get(i).printWithoutIndent(childIndent);
            }
        }
    }

    private void printWithoutIndent(String indent) {
        String label;
        switch (type) {
            case ASSIGN:  label = "ASSIGN(" + value + ")"; break;
            case BINARY:  label = "BINARY(" + value + ")"; break;
            case UNARY:   label = "UNARY(" + value + ")";  break;
            case LITERAL: label = "LITERAL(" + formatNumber(value) + ")"; break;
            case VARIABLE:label = "VARIABLE(" + value + ")"; break;
            default:      label = type + "(" + value + ")"; break;
        }
        System.out.println(label);
        if (children != null) {
            for (int i = 0; i < children.size(); i++) {
                boolean last = (i == children.size() - 1);
                String branch = last ? "`-- " : "|-- ";
                String childIndent = indent + (last ? "    " : "|   ");
                System.out.print(indent + branch);
                children.get(i).printWithoutIndent(childIndent);
            }
        }
    }
     private String formatNumber(Object val) {
        if (val instanceof Double) {
            double d = (Double) val;
            if (d == Math.floor(d) && !Double.isInfinite(d))
                return String.valueOf((long) d);
        }
        return String.valueOf(val);
    }

}
