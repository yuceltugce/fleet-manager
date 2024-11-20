public class AVLTree {
    public AVLNode root; // Root node of the AVL tree
    AVLTree(){
        root = null;
    }

    // Returns the height of the given node; if null, height is 0
    public static int height(AVLNode node) {
        if (node == null)
            return 0;
        return node.height;
    }

    // Calculates and returns the balance factor of the given node
    public static int getBalance(AVLNode node) {
        if (node == null)
            return 0;
        return height(node.left) - height(node.right);
    }

    // Performs a right rotation around the given node y
    public static AVLNode rightRotate(AVLNode y) {
        AVLNode x = y.left; // Set x as left child of y
        AVLNode T2 = x.right; // Store right child of x

        x.right = y; // Perform rotation
        y.left = T2;

        // Update parent pointers after rotation
        x.parent = y.parent;
        y.parent = x;
        if (T2 != null) T2.parent = y;

        // Update heights
        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;

        return x; // Return new root after rotation
    }

    // Performs a left rotation around the given node x
    public static AVLNode leftRotate(AVLNode x) {
        AVLNode y = x.right; // Set y as right child of x
        AVLNode T2 = y.left; // Store left child of y

        y.left = x; // Perform rotation
        x.right = T2;

        // Update parent pointers after rotation
        y.parent = x.parent;
        x.parent = y;
        if (T2 != null) T2.parent = x;

        // Update heights
        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;

        return y; // Return new root after rotation
    }
    public void insert(int key, ParkingLot parkinglot){
        root = real_insert(root, key, parkinglot);
    }
    // Inserts a node with the specified key and parkingLot data into the AVL tree
    private static AVLNode real_insert(AVLNode node, int key, ParkingLot parkingLot) {
        // Base case: if current node is null, create a new node
        if (node == null)
            return new AVLNode(key, parkingLot);

        // Recursively insert key in the left or right subtree
        if (key < node.key) {
            AVLNode leftChild = real_insert(node.left, key, parkingLot);
            node.left = leftChild;
            leftChild.parent = node; // Set parent of left child
        } else if (key > node.key) {
            AVLNode rightChild = real_insert(node.right, key, parkingLot);
            node.right = rightChild;
            rightChild.parent = node; // Set parent of right child
        } else {
            return node; // Duplicate keys are not allowed
        }

        // Update height and balance factor of current node
        node.height = 1 + Math.max(height(node.left), height(node.right));
        int balance = getBalance(node);

        // Perform necessary rotations to balance the tree
        if (balance > 1 && key < node.left.key)
            return rightRotate(node);
        if (balance < -1 && key > node.right.key)
            return leftRotate(node);
        if (balance > 1 && key > node.left.key) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }
        if (balance < -1 && key < node.right.key) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node; // Return the unchanged node pointer
    }

    public void delete(int key){
        root = real_deleteNode(root, key);
    }

    // Deletes the node with the specified key and returns the new root of the subtree
    private AVLNode real_deleteNode(AVLNode root, int key) {
        if (root == null)
            return root;

        // Locate the node to be deleted in the left or right subtree
        if (key < root.key) {
            root.left = real_deleteNode(root.left, key);
        } else if (key > root.key) {
            root.right = real_deleteNode(root.right, key);
        } else {
            // Node found; handle deletion for nodes with one or no child
            if ((root.left == null) || (root.right == null)) {
                AVLNode temp = (root.left != null) ? root.left : root.right;

                if (temp == null) { // Case when node has no children
                    if (root.parent != null) {
                        if (root.parent.left == root)
                            root.parent.left = null;
                        else
                            root.parent.right = null;
                    }
                    return null;
                } else { // Node has one child
                    temp.parent = root.parent;
                    if (root.parent != null) {
                        if (root.parent.left == root)
                            root.parent.left = temp;
                        else
                            root.parent.right = temp;
                    }
                    root = temp;
                }
            } else {
                // Node has two children, replace it with its inorder successor
                AVLNode temp = minValueNode(root.right);
                root.parkingLot = temp.parkingLot;
                root.key = temp.key;
                root.right = real_deleteNode(root.right, temp.key);
            }
        }

        if (root == null)
            return null;

        // Update height and balance factor of current node
        root.height = 1 + Math.max(height(root.left), height(root.right));
        int balance = getBalance(root);

        // Perform rotations if necessary to balance the tree
        if (balance > 1 && getBalance(root.left) >= 0) return rightRotate(root);
        if (balance < -1 && getBalance(root.right) <= 0) return leftRotate(root);
        if (balance > 1 && getBalance(root.left) < 0) {
            root.left = leftRotate(root.left);
            return rightRotate(root);
        }
        if (balance < -1 && getBalance(root.right) > 0) {
            root.right = rightRotate(root.right);
            return leftRotate(root);
        }

        return root; // Return the updated root of the subtree
    }

    // Finds and returns the node with the minimum key value in a subtree
    public static AVLNode minValueNode(AVLNode node) {
        AVLNode current = node;
        while (current.left != null) current = current.left; // Go to the leftmost node
        return current;
    }

    // Finds and returns the node with the maximum key value in a subtree
    public static AVLNode maxValueNode(AVLNode node) {
        AVLNode current = node;
        while (current.right != null) {
            current = current.right; // Go to the rightmost node
        }
        return current;
    }
    public AVLNode getBigRoot(int val, AVLNode optimal){
        return getBigRoot(root, val, optimal);
    }

    // Finds the closest node with a key greater than or equal to the specified value
    private AVLNode getBigRoot(AVLNode node, int val, AVLNode optimal) {
        if (node == null) {
            return optimal;
        }
        if (node.key >= val) {
            optimal = node;
            return getBigRoot(node.left, val, optimal);
        } else {
            return getBigRoot(node.right, val, optimal);
        }
    }

    public AVLNode getSmallRoot(int val, AVLNode optimal){
        return getSmallRoot(root, val, optimal);
    }
    // Finds the closest node with a key less than or equal to the specified value
    private AVLNode getSmallRoot(AVLNode node, int val, AVLNode optimal) {
        if (node == null) {
            return optimal;
        }
        if (node.key > val) {
            return getSmallRoot(node.left, val, optimal);
        } else {
            optimal = node;
            return getSmallRoot(node.right, val, optimal);
        }
    }
}
