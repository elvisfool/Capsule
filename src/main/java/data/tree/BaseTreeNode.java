package data.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BaseTreeNode<ID, Value> implements Tree.TreeNode<ID, Value> {
    
    protected ID    id;
    protected Value value;
    
    private Map<ID, BaseTreeNode<ID, Value>> children;
    
    public BaseTreeNode() {
    }
    
    public BaseTreeNode(ID id, Value value) {
        this.id = id;
        this.value = value;
    }
    
    public BaseTreeNode(ID id, Value value, List<BaseTreeNode<ID, Value>> children) {
        this.id = id;
        this.value = value;
        this.children = children.stream().collect(Collectors.toMap(BaseTreeNode::getId, Function.identity()));
    }
    
    protected void traversal(Consumer<BaseTreeNode<ID, Value>> consumer) {
        consumer.accept(this);
        getChildren().forEach(child -> traversal(consumer));
    }
    
    @Override
    public <R extends Tree.TreeNode<ID, Value>> R getOrAddChild(ID id) {
        return null;
    }
    
    @Override
    public ID getId() {
        return id;
    }
    
    @Override
    public Value getValue() {
        return value;
    }
    
    @Override
    public List<BaseTreeNode<ID, Value>> getChildren() {
        return hasChildren() ? new ArrayList<>(children.values()) : new ArrayList<>();
    }
    
    @Override
    public boolean hasChildren() {
        return this.children != null && !this.children.isEmpty();
    }
    
    @Override
    public String toString() {
        List<ID> childrenIds = getChildren().stream().map(BaseTreeNode::getId).collect(Collectors.toList());
        return "BinaryTreeNode{" + "id=" + id + ", value=" + value + ", children=" + childrenIds + ")}";
    }
    
    protected void flatString(StringBuilder stringBuilder, String indent) {
        stringBuilder.append(indent).append(this.toString()).append(System.lineSeparator());
        indent = "    " + indent;
        
        for(BaseTreeNode<ID, Value> childNode : this.getChildren()) {
            childNode.flatString(stringBuilder, indent);
        }
    }
    
}