package cn.inkroom.study.algorithm.tree.binary.avl;

import cn.inkroom.study.algorithm.tree.binary.Node;

/**
 * AVL二叉树
 *
 * @author ink
 * @date 2021/5/20
 */
public class AvlTree<T extends Comparable<? super T>> {
    Node<T> root;
    int size = 0;

    public void insert(T data) {

        this.root = insert(data, root);
    }

    /**
     * @param data 数据
     * @param node 当前节点
     * @return
     */
    private Node<T> insert(T data, Node<T> node) {

        if (node == null) {
            size++;
            return new Node<T>(data);
        }

        int res = data.compareTo(node.getData());
        if (res < 0) {
            node.setLeft(insert(data, node.getLeft()));
        } else if (res > 0) {
            node.setRight(insert(data, node.getRight()));
        }

        return balance(node);
    }

    private int height(Node<T> node) {
        return node == null ? -1 : node.getHeight();
    }

    private Node<T> balance(Node<T> node) {


        if ((height(node.getLeft()) - height(node.getRight())) > 1) {
            if (height(node.getLeft().getLeft()) >= height(node.getLeft().getRight())) {//插入到左节点的左节点，右旋解决
                node = rotateWithLeftChild(node);
            } else//左节点的右节点，先左旋，再右旋
                node = doubleWithLeftChild(node);
        } else if ((height(node.getRight()) - height(node.getLeft())) > 1) {
            if (height(node.getRight().getRight()) >= height(node.getRight().getLeft())) {//右节点的右节点，左旋
                node = rotateWithRightChild(node);
            } else {//右节点的左节点，右旋，再左旋
                node = doubleWithRightChild(node);
            }
        }

        node.setHeight(Math.max(height(node.getLeft()), height(node.getRight())) + 1);
        return node;
    }

    /**
     * 右旋再左旋
     *
     * @param node
     * @return
     */
    private Node<T> doubleWithRightChild(Node<T> node) {
        node.setRight(rotateWithLeftChild(node.getRight()));
        return rotateWithRightChild(node);
    }

    /**
     * 左旋
     *
     * @param node
     * @return
     */
    private Node<T> rotateWithRightChild(Node<T> node) {

        Node<T> nn = node.getRight();
        node.setRight(nn.getLeft());
        nn.setLeft(node);
        nn.setHeight(Math.max(height(nn.getLeft()), height(nn.getRight())) + 1);
        node.setHeight(Math.max(height(node.getLeft()), height(node.getRight())) + 1);
        return nn;
    }

    /**
     * 先左旋，再右旋
     *
     * @param node
     * @return
     */
    private Node<T> doubleWithLeftChild(Node<T> node) {
        node.setLeft(rotateWithRightChild(node.getLeft()));
        return rotateWithLeftChild(node);
    }

    /**
     * 右旋
     *
     * @param k2
     * @return
     */
    private Node<T> rotateWithLeftChild(Node<T> k2) {

        Node<T> k1 = k2.getLeft();
        k2.setLeft(k1.getRight());
        k1.setRight(k2);
        k2.setHeight(Math.max(height(k2.getLeft()), height(k2.getRight())) + 1);
        k1.setHeight(Math.max(height(k1.getLeft()), height(k2.getRight())) + 1);

        return k1;
    }

    private void print() {

        print(root);
    }

    private void print(Node<T> root) {
        if (root == null) return;
        System.out.println(root.getData());
        print(root.getLeft());
        print(root.getRight());


    }

    public static void main(String[] args) {
        AvlTree<Integer> tree = new AvlTree<>();
        for (int i = 0; i < 10; i++) {
            tree.insert(i);
        }
        tree.print();
    }

}
