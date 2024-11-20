public class AVLNode {
    int key;                       // Capacity constraint of parking lot
    ParkingLot parkingLot;        // relevant parking lot
    AVLNode left, right;          // Right and left child nodes
    int height;                   // height of node
    AVLNode parent;

    public AVLNode(int key, ParkingLot parkingLot) {
        this.key = key;
        this.parkingLot = parkingLot;
        this.height = 1; //The new node initially has a height of 1
        this.left = null;
        this.right = null;
        this.parent = null;
    }
}
