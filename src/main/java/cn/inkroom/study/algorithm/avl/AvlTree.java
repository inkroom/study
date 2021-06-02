package cn.inkroom.study.algorithm.avl;

/**
 * AVL二叉树
 * @author ink
 * @date 2021/5/20
 */
public class AvlTree<T extends Comparable<? super T>> {
    AvlNode<T> root;

    public void insert(T data){

       this.root = insert(data,root);
    }

    /**
     *
     * @param data 数据
     * @param node 当前节点
     * @return
     */
    private AvlNode<T> insert(T data, AvlNode<T> node) {

        if (node==null) return new AvlNode<>(0,data);

        int res = data.compareTo(node.data);
        if (res<0){
            node.left = insert(data, node.left);
        }else if(res>0){
            node.right = insert(data, node.right);
        }

        return balance(node);
    }

    private int height(AvlNode<T> node){
        return node == null? -1: node.height;
    }

    private AvlNode<T> balance(AvlNode<T> node) {


        if (height(node.left) - height(node.right) > 1){
            if (height(node.left.left) >= height(node.left.right)){
                node= rotateWithLeftChild(node);
            }else
                node = doubleWithLeftChild(node);
        }else if (height(node.right)-height(node.left)>1){
            if (height(node.right.right)>=height(node.right.left)){
                node = rotateWithRightChild(node);
            }else {
                node = doubleWithRightChild(node);
            }
        }

        node.height =  Math.max(height(node.left),height(node.right))+1;
        return node;
    }

    private AvlNode<T> doubleWithRightChild(AvlNode<T> node) {
        return null;
    }

    private AvlNode<T> rotateWithRightChild(AvlNode<T> node) {
        return null;
    }

    private AvlNode<T> doubleWithLeftChild(AvlNode<T> node) {
        return null;
    }

    private AvlNode<T> rotateWithLeftChild(AvlNode<T> k2) {

        AvlNode<T> k1 = k2.left;
        k2.left = k1.right;
        k1.right  = k2;
        k2.height = Math.max(height(k2.left),height(k2.right))+1;
        k1.height = Math.max(height(k1.left),k2.height) + 1;


            return k1;
    }


}
