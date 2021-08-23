package cn.inkroom.study.algorithm.tree.binary.avl;

/**
 * AVL二叉树
 * @param <T>
 */
public class AvlNode<T>{

    int height;
     T data;
     AvlNode<T> left;
     AvlNode<T> right;

    public AvlNode(AvlNode<T> left, AvlNode<T> right) {
        this.left = left;
        this.right = right;
    }

    public AvlNode(T data, AvlNode<T> left, AvlNode<T> right) {
        this.data = data;
        this.left = left;
        this.right = right;
    }

    public AvlNode(T data) {
        this.data = data;
    }

    public AvlNode(int height, T data, AvlNode<T> left, AvlNode<T> right) {
        this.height = height;
        this.data = data;
        this.left = left;
        this.right = right;
    }

    public AvlNode(int height, T data) {
        this.height = height;
        this.data = data;
    }

    public AvlNode() {
    }

    @Override
    public String toString() {
        return data.toString();
    }
}
