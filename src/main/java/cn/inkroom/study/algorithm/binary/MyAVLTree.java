package com.bcyunqian;

/**
 * @author inkbox
 * @date 2021/8/23
 */
public class MyAVLTree<T extends Comparable> {


    private Node<T> root;


    public void insert(T t) {
        root = insert(t, root);
    }

    private Node<T> insert(T t, Node<T> node) {
        if (t == null) return null;
        if (node == null) {
            return new Node<>(t);
        }

        int r = t.compareTo(node.data);
        if (r < 0) {//添加到左子树
            node.left = insert(t, node.left);
            if (height(node.left) - height(node.right) > 1) {
                if (t.compareTo(node.left.data) < 0) { //高度不正确，需要右旋
                    node = rightRotate(node);
                }else{//左子树的右子树，需要先右旋，再左旋
                    

                }
            }

        } else {//添加到右子树
            node.right = insert(t, node.right);
            if (height(node.right) - height(node.left) > 1) {
                if (t.compareTo(node.right.data) < 0){//左旋
                    node =  leftRotate(node);
                }
            }

        }


    }

    private Node<T> leftRotate(Node<T> node) {

        Node<T> newNode = node.right;
        newNode.left = node;
        node.right = newNode.left;

        node.height = Math.max(height(node.left), height(node.right)) + 1;
        newNode.height = Math.max(height(newNode.left), height(newNode.right)) + 1;
        return newNode;
    }

    private Node<T> rightRotate(Node<T> node) {

        Node<T> newNode = node.left;
        newNode.right = node;
        node.left = newNode.right;

        //两个的顺序应该不能换吧？
        node.height = Math.max(height(node.left), height(node.right)) + 1;
        newNode.height = Math.max(height(newNode.left), height(newNode.right)) + 1;


        return newNode;
    }

    private int height(Node<T> node) {
        return node == null ? -1 : node.height;
    }

    private class Node<T> {
        private T data;
        private Node<T> left;
        private Node<T> right;
        private int height;

        public Node(T data) {
            this.data = data;
        }


    }
}
