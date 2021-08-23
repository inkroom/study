package cn.inkroom.study.algorithm.tree.binary;

/**
 * 二叉查找树
 *
 * @author ink
 * @date 2021/5/19
 */
public class BinarySearchTree<T extends Comparable<? super T>> {

    private Node<T> root;

    public boolean contains(T data) {
        return contains(data, root).getData().compareTo(data) == 0;
    }

    public void remove(T data) {
        root = remove(data, root);
    }

    private Node<T> remove(T data, Node<T> node) {

        if (node == null) return null;
        int res = data.compareTo(node.getData());
        if (res == 0) {
            //删除当前节点
            if (node.getLeft() == null && node.getRight() == null) {
                return null;
            }
            if (node.getRight() == null || node.getLeft() == null) {//只有一个节点
                return node.getLeft() != null ? node.getLeft() : node.getRight();//哪个节点有值返回哪个
            }
            //有两个叶子,找到其右子树最小值替换当前节点，并删除最小值所在节点
            if (node.getRight().getLeft()==null){//右节点就是右子树最小的值
                node.setData(node.getRight().getData());
                node.setRight(null);
            }else {
                Node<T> min = removeMin(node.getRight());
                node.setData(min.getData());
            }

//            Node<T> min = findMin(node.getRight());
//            node.setData(min.getData());
//            node.setRight(remove(min.getData(), node.getRight()));
        } else if (res < 0) {
            node.setLeft(remove(data, node.getLeft()));
        } else
            node.setRight(remove(data, node.getRight()));
        return node;
    }

    /**
     * 删除树中最小的子节点，并返回被删除的节点
     *
     * @param node 当前节点一定有左子树
     *
     * @return 找到的最小的节点
     */
    private Node<T> removeMin(Node<T> node) {
        Node<T> now = node;
        Node<T> left = now.getLeft();
        while (left.getLeft() != null) {
            now = left;
            left = left.getLeft();
        }
        now.setLeft(null);
        now.setRight(left.getRight());
        return left;
    }

    public void insert(T data) {
        if (root == null) {
            root = new Node<>(data);
            return;
        }
        Node<T> near = contains(data, root);
        int res = data.compareTo(near.getData());
        if (res == 0) {
            near.setData(data);
        } else if (res < 0) {
            near.setLeft(new Node<T>(data));
        } else {
            near.setRight(new Node<T>(data));
        }

    }

    /**
     * 递归查找数据
     *
     * @param data
     * @param node
     * @return 如果找到数据所在节点，则返回该节点;否则返回找到的最后一个节点，因此需要调用方再判断一次是否相等
     */
    private Node<T> contains(T data, Node<T> node) {
        int res = data.compareTo(node.getData());
        if (res == 0) {
            return node;
        }
        if (res < 0) {//往左查找
            if (node.getLeft() == null) {//左边已经没有了
                return node;
            }
            return contains(data, node.getLeft());
        }
        if (node.getRight() == null) return node;
        return contains(data, node.getRight());

    }

    public Node<T> findMin() {
        return findMin(root);
    }

    public Node<T> findMax() {
        Node<T> now = root;
        while (now.getRight() != null) {
            now = now.getRight();
        }
        return now;
    }

    private Node<T> findMin(Node<T> node) {
        if (node.getLeft() != null) return findMin(node.getLeft());
        return node;
    }


    public static void main(String[] args) {

        //构建一棵树
        BinarySearchTree<Integer> tree = new BinarySearchTree<>();
        tree.insert(6);
        tree.insert(2);
        tree.insert(1);
        tree.insert(4);
        tree.insert(3);
        tree.insert(8);
        tree.insert(5);
//删除4
        tree.remove(4);

        //删除2
        tree.insert(4);
        tree.remove(2);

        System.out.println(1);
    }

}
