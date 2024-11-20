public class FleetManager {
    private AVLTree AVLTree; //Tree for all nodes
    private AVLTree adding_Tree; //Tree for nodes to which the truck can be attached
    private AVLTree loading_Tree; //Tree for nodes where trucks can be loaded
    private AVLTree ready_Tree;//Tree for nodes from which trucks can receive ready commands

    public FleetManager() {
        this.AVLTree = new AVLTree();
        this.adding_Tree = new AVLTree();
        this.ready_Tree = new AVLTree();
        this.loading_Tree = new AVLTree();
    }
    public void createParkingLot(int capacityConstraint, int truckLimit) {
        ParkingLot parkingLot = new ParkingLot(capacityConstraint, truckLimit); // create a new parking lot
        AVLTree.insert(capacityConstraint, parkingLot); // insert it to AVL tree
        adding_Tree.insert(capacityConstraint, parkingLot); //insert it to adding tree also
    }
    public void deleteParkingLot(int capacityConstraint) {
        AVLTree.delete(capacityConstraint);
        adding_Tree.delete(capacityConstraint);
        ready_Tree.delete(capacityConstraint);
        loading_Tree.delete(capacityConstraint);
    }

    public int addTruckToParkingLot(int ID, int capacity) {
        Truck truck = new Truck(ID, capacity, 0); // create new truck
        AVLNode resultNode = adding_Tree.getSmallRoot(capacity, null); // look for exact capacity or the biggest one which is smaller than exact capacity
        if (resultNode == null) // if there is no suitable parking lot
            return -1;
        ParkingLot parkingLot = resultNode.parkingLot;
        parkingLot.addTruck(truck);
        //If parking lot's waiting part is empty, we add a truck here, then it the waiting size became 1
        if (parkingLot.getWaitingSize() == 1) {
            ready_Tree.insert(parkingLot.getCapacityConstraint(), parkingLot); //it has to be inserted to the ready tree
        }
        //If parking lot is full, it can not accept trucks anymore
        if (parkingLot.isFull())
            adding_Tree.delete(parkingLot.getCapacityConstraint()); //we delete from adding tree
        return parkingLot.getCapacityConstraint();
    }

    public String ready (int capacityConstraint) {
        StringBuilder log = new StringBuilder();
        AVLNode resultNode = ready_Tree.getBigRoot(capacityConstraint, null); // look for exact capacity or the smallest one which is bigger than exact capacity
        if (resultNode == null) { //if there is no suitable parking lot exists
            log.append("-1");
            return log.toString();
        }
        ParkingLot parkingLot = resultNode.parkingLot;
        Truck truck = parkingLot.waiting.removeFirst(); // The first truck that arrives will be picked up.
        parkingLot.ready.addLast(truck); // add to the ready part
        log.append(truck.getId()).append(" ").append(parkingLot.getCapacityConstraint());
        //If parking lot's ready part is empty, it means there is no truck to load
        if (parkingLot.getReadySize() == 1) //If it became 1 after adding a truck to ready section
            loading_Tree.insert(parkingLot.getCapacityConstraint(), parkingLot); //It has to be inserted to the loading tree
        if (parkingLot.getWaitingSize() == 0) //if waiting part became empty
            ready_Tree.delete(parkingLot.getCapacityConstraint()); //there is no truck to be commanded ready, so we delete it
        return log.toString();
    }


    public String loadTrucks ( int capacity, int loadAmount){
        StringBuilder log = new StringBuilder();
        AVLNode resultNode = loading_Tree.getBigRoot(capacity, null); // look for exact capacity or the smallest one which is bigger than exact capacity
        if (resultNode == null) { //if there is no suitable parking lot exists
            return "-1";
        }
        ParkingLot parkingLot = resultNode.parkingLot;

        while (loadAmount > 0) {
            // Continue loading trucks as long as there is load amount remaining

            while (loadAmount > 0 && parkingLot.getReadySize() > 0) {
                // While there is still load to be distributed and the current parking lot has ready trucks

                Truck truck = parkingLot.ready.first(); // Get the first ready truck in the parking lot
                int loaded = truck.load(loadAmount, parkingLot.getCapacityConstraint());
                // Load the truck based on the remaining load and the capacity constraint of the parking lot
                loadAmount -= loaded; // Reduce the load amount by the amount loaded onto this truck

                if (truck.isFull()) {
                    truck.resetLoad(); // Reset the load if the truck is fully loaded
                }

                parkingLot.ready.removeFirst(); // Remove the truck from the ready list in the parking lot
                //If there exist an open space at parking lot when we remove one truck
                if (parkingLot.getTruckLimit()-1 == parkingLot.getTotalTruckCount())
                    //Now it became a node which the truck can be attached
                    adding_Tree.insert(parkingLot.getCapacityConstraint(), parkingLot);
                String temp = reassignTruckToParkingLot(truck.getCapacity(), truck.getId(), truck.getLoad());
                // Reassign the truck to a new parking lot based on its updated load and capacity
                log.append(temp).append(" - ");
            }

            if (loadAmount <= 0) {
                break; // Exit the loop if all the load has been distributed
            }
            // Move to the next larger parking lot in the AVL tree to continue loading
            AVLNode temp = loading_Tree.getBigRoot(resultNode.parkingLot.getCapacityConstraint(), null);

            //Since we are all done with this node, we delete it
            loading_Tree.delete(parkingLot.getCapacityConstraint());
            resultNode = temp;


            if (resultNode == null) {
                break; // Exit if there are no more parking lots available in the AVL tree
            }

            parkingLot = resultNode.parkingLot;
        }


        if (log.length() > 0) {
            log.setLength(log.length() - 3);
        }
        return log.length() > 0 ? log.toString() : "-1";
    }

    public String reassignTruckToParkingLot ( int capacity, int ID, int load){
        Truck truck = new Truck(ID, capacity, load); //create new truck with new current capacity
        AVLNode resultNode = adding_Tree.getSmallRoot(capacity - load, null); // look for exact capacity or the biggest one which is smaller than exact capacity
        if (resultNode == null) { // if there is no suitable parking lot
            return truck.getId() + " -1";
        }

        ParkingLot parkingLot = resultNode.parkingLot;
        int c = parkingLot.getCapacityConstraint();
        parkingLot.addTruck(truck);
        //If parking lot's waiting part is empty, we add a truck here, then it the waiting size became 1
        if (parkingLot.getWaitingSize() == 1)
            ready_Tree.insert(parkingLot.getCapacityConstraint(), parkingLot); //it has to be inserted to the ready tree
        //If parking lot is full, it can not accept trucks anymore
        if (parkingLot.isFull())
            adding_Tree.delete(parkingLot.getCapacityConstraint()); //we delete from adding tree
        return truck.getId() + " " + c;
    }


        public String countTrucksOverCapacity ( int capacity){
            return Integer.toString(countTrucksInAVL(AVLTree.root, capacity)); //The result of the counting operation is returned
        }

        private int countTrucksInAVL (AVLNode node,int capacity){
            if (node == null) { // If the node is null, there are no trucks to count, so return 0
                return 0; // If root is null, return 0
            }

            if (node.key > capacity)
                // If the node's key is greater than the given capacity, count trucks in this parking lot
                // and continue the search on both left and right subtrees
                return node.parkingLot.getTotalTruckCount() + countTrucksInAVL(node.right, capacity) + countTrucksInAVL(node.left, capacity);
            // If the node's key is less than or equal to the capacity, continue the search only in the right subtree
            return countTrucksInAVL(node.right, capacity);
        }


    }



