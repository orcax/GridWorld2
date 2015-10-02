package edu.rudcs.gridworld.util;

import java.util.LinkedList;
import java.util.List;

public class TreeNode<T> {

    private T data;
    private TreeNode<T> parent;
    private List<TreeNode<T>> children;

    public TreeNode(T data) {
        this.data = data;
        this.parent = null;
        this.children = new LinkedList<TreeNode<T>>();
    }

    public List<TreeNode<T>> getChildren() {
        return this.children;
    }

    public TreeNode<T> getChild(T data) {
        if (this.data.equals(data)) {
            return this;
        }
        for (TreeNode<T> child : children) {
            TreeNode<T> result = child.getChild(data);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    public void addChild(TreeNode<T> node) {
        node.setParent(this);
        this.children.add(node);
    }

    public void removeChild(TreeNode<T> node) {
        children.remove(node);
    }

    public TreeNode<T> getParent() {
        return this.parent;
    }

    public void setParent(TreeNode<T> node) {
        this.parent = node;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getDepth(T data) {
        return depth(data, 0);
    }

    private int depth(T data, int d) {
        if (this.data.equals(data)) {
            return d;
        }
        for (TreeNode<T> child : children) {
            int tmp = child.depth(data, d + 1);
            if (tmp > 0) {
                return tmp;
            }
        }
        return -1;
    }

}
