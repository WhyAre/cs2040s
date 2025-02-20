public class AVLTree<T extends Comparable<T>> {
    private TreeNode<T> root;

    AVLTree() {
        root = null;
    }

    void validate() {
        if (root == null) {
            return;
        }

        root.validate();
    }

    public void insert(T key) {
        if (root == null) {
            root = new TreeNode<>(key);
            return;
        }

        root = root.insert(key);
    }

    public void print() {
        if (root == null) {
            System.out.println("Empty tree");
        }

        var out = new StringBuilder();
        this.root.formatTree(0, out);
        System.out.println(out);
    }

}

class TreeNode<T extends Comparable<T>> {
    private TreeNode<T> left;
    private TreeNode<T> right;
    private TreeNode<T> parent;
    private int height;

    private final T key;

    TreeNode(T key) {
        this(null, null, null, 0, key);
    }

    TreeNode(TreeNode<T> left, TreeNode<T> right, TreeNode<T> parent, int height, T key) {
        this.left = left;
        this.right = right;
        this.parent = parent;

        this.height = height;
        this.key = key;
    }

    static <T extends Comparable<T>> int getHeight(TreeNode<T> n) {
        if (n == null) {
            return -1;
        }

        return n.height;
    }

    private static <T extends Comparable<T>> void setParent(TreeNode<T> n, TreeNode<T> parent) {
        if (n == null) {
            return;
        }

        n.parent = parent;
    }

    int computeHeight() {
        var leftHeight = (left != null) ? left.computeHeight() : -1;
        var rightHeight = (right != null) ? right.computeHeight() : -1;

        return Math.max(leftHeight, rightHeight) + 1;
    }

    /**
     * Computes the balance factor of a noe
     */
    int computeBf() {
        return getHeight(this.right) - getHeight(this.left);
    }

    void computeNewHeight() {
        this.height = Math.max(getHeight(this.left), getHeight(this.right)) + 1;
    }

    /**
     * Rotates right around this
     *
     * @return new root of subtree after rotation
     */
    TreeNode<T> rotateRight() {
        var newRoot = this.left;

        assert newRoot != null : "New root is null";

        this.left = newRoot.right;
        setParent(this.left, this);

        newRoot.right = this;
        this.parent = newRoot;

        // Update heights
        this.computeNewHeight();
        newRoot.computeNewHeight();

        return newRoot;
    }

    /**
     * Rotates left around this
     *
     * @return new root of subtree after rotation
     */
    TreeNode<T> rotateLeft() {
        var newRoot = this.right;

        assert newRoot != null : "new root is null";

        this.right = newRoot.left;
        setParent(this.right, this);

        newRoot.left = this;
        this.parent = newRoot;

        // Update heights
        this.computeNewHeight();
        newRoot.computeNewHeight();

        return newRoot;
    }

    /**
     * Inserts key at subtree rooted at this
     *
     * @param key key to insert
     * @return new root of the subtree after insertion (and possibly rotation)
     */
    TreeNode<T> insert(T key) {
        int cmp = key.compareTo(this.key);

        if (cmp == 0) {
            return this;
        } else if (cmp < 0) {
            this.left = (this.left == null) ? new TreeNode<>(key) : this.left.insert(key);
            assert this.left != null : "left child is null";
            this.left.parent = this;
        } else {
            this.right = (this.right == null) ? new TreeNode<>(key) : this.right.insert(key);
            assert this.right != null : "right child is null";
            this.right.parent = this;
        }

        // Update height
        this.computeNewHeight();

        return this.balance();
    }

    TreeNode<T> balance() {
        var bf = this.computeBf();

        var newRoot = this;
        if (bf >= 2) {
            // Unbalanced to the right
            assert this.right != null : "right child is null";
            var rightChild = this.right;
            var childBf = rightChild.computeBf();

            if (childBf < 0) {
                // Right child is left heavy (RL case)
                this.right = rightChild.rotateRight();
                this.right.parent = this;
            }

            // Right child is balanced/right heavy (RR case)
            newRoot = this.rotateLeft();
        } else if (bf <= -2) {
            // Unbalanced to the left
            assert this.left != null : "left child is null";
            var leftChild = this.left;
            var childBf = leftChild.computeBf();

            if (childBf > 0) {
                // Left child is right heavy (LR case)
                this.left = leftChild.rotateLeft();
                this.left.parent = this;
            }

            // Left child is balanced/left heavy (LL case)
            newRoot = this.rotateRight();
        }

        return newRoot;
    }

    void formatTree(int depth, StringBuilder sb) {
        var str = "%s%s%s\n".formatted("-".repeat(depth), (depth == 0) ? "" : " ", this.toString());
        sb.append(str);

        if (this.left != null) {
            this.left.formatTree(depth + 1, sb);
        }

        if (this.right != null) {
            this.right.formatTree(depth + 1, sb);
        }
    }

    void validate() {
        assert height == computeHeight() : "height doesn't match";

        var bf = computeBf();
        assert bf >= -1 : "bf smaller than -1";
        assert bf <= 1 : "bf greater than 1";

        if (left != null) {
            assert left.parent == this : "left parent pointer is wrong";
            assert left.key.compareTo(key) < 0 : "left key is larger";

            left.validate();
        }

        if (right != null) {
            assert right.parent == this : "right parent pointer is wrong";
            assert right.key.compareTo(key) > 0 : "right key is smaller";

            right.validate();
        }
    }

    @Override
    public String toString() {
        return "TreeNode[key=%s, height=%d]".formatted(this.key.toString(), this.height);
    }

}
