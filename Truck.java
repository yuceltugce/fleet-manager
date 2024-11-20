class Truck {
    private int id;                // Truck ID
    private int capacity;          // Truck's total capacity
    private int load;              // Truck's current load

    public Truck(int id, int capacity, int load) {
        this.id = id;
        this.capacity = capacity;
        this.load = load;
    }

    public int getId() {
        return id;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getLoad() {
        return load;
    }

    public boolean isFull() {

        return load == capacity; // Is truck is full?
    }

    public int load(int amount, int capacityConstraint) {
        int loaded = Math.min(capacityConstraint, amount);
        load += loaded; // Adding load
        return loaded;
    }

    public int resetLoad() {
        load = 0;
        return load;
    }
}

