

class ParkingLot {
    private int capacityConstraint; // capacity constraint for parking lot
    private int truckLimit;         // truck limit for parking lot
    public SinglyLinkedList<Truck> waiting; // trucks at waiting part
    public SinglyLinkedList<Truck> ready;   // trucks at ready part

    //constructor for parking lot
    public ParkingLot(int capacityConstraint, int truckLimit) {
        this.capacityConstraint = capacityConstraint;
        this.truckLimit = truckLimit;
        this.waiting = new SinglyLinkedList<>();
        this.ready = new SinglyLinkedList<>();
    }


    public int getCapacityConstraint() {
        return capacityConstraint;
    }

    public int getReadySize() {
        return ready.size();
    }

    public int getWaitingSize(){
        return waiting.size();
    }

    public int getTotalTruckCount() {
        return waiting.size() + ready.size();
    }

    public boolean isFull() {
        return getTotalTruckCount() >= truckLimit;
    }

    //It adds truck to the waiting part
    public boolean addTruck(Truck truck) {
        if (!isFull()) {
            waiting.addLast(truck); // Truck is added to the waiting part
            return true;
        }
        return false; // If waiting part is full, truck is not added
    }
    public int getTruckLimit(){
        return truckLimit;
    }
}
