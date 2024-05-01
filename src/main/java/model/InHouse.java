package model;

public class InHouse extends Part{
    int machineId;


    /**
     * This method is a constructor that takes in InHouse data with a machine ID.
     * @param id The auto-generated ID
     * @param name The name
     * @param price The price
     * @param stock The inventory
     * @param min The minimum
     * @param max The maximum
     * @param machineId The machine ID
     */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    /**
     * This method sets the machine ID.
     * @param machineId sets the machine ID
     */
    public void setMachine(int machineId) {
        this.machineId = machineId;
    }

    /**
     * This method gets the machine ID.
     * @return Returns the machine ID
     */
    public int getMachineId() {
        return machineId;
    }

}
