import java.io.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {

        File file1 = new File(args[0]);
        Scanner scanner1 = new Scanner(file1);

        BufferedWriter writer = new BufferedWriter(new FileWriter(args[1]));
        String line1;
        String[] temp;
        FleetManager fleetManager = new FleetManager();
        while(scanner1.hasNextLine()) {
            line1 = scanner1.nextLine();
            temp = line1.split(" ");
            if (temp[0].equals("create_parking_lot")){
                fleetManager.createParkingLot(Integer.parseInt(temp[1]), Integer.parseInt(temp[2]));
            }
            if (temp[0].equals("add_truck")){
                writer.write(Integer.toString(fleetManager.addTruckToParkingLot(Integer.parseInt(temp[1]), Integer.parseInt(temp[2]))));
                writer.newLine();
            }
            if(temp[0].equals("ready")){
                writer.write(fleetManager.ready(Integer.parseInt(temp[1])));
                writer.newLine();
            }
            if(temp[0].equals("load")){
                writer.write(fleetManager.loadTrucks(Integer.parseInt(temp[1]),Integer.parseInt(temp[2])));
                writer.newLine();
            }
            if(temp[0].equals("delete_parking_lot"))
                fleetManager.deleteParkingLot(Integer.parseInt(temp[1]));

            if(temp[0].equals("count")) {
                writer.write(fleetManager.countTrucksOverCapacity(Integer.parseInt(temp[1])));
                writer.newLine();
            }
        }
        writer.close();
        scanner1.close();
    }
}