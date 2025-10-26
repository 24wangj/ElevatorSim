
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Elevator elevator = new Elevator(10, 1);

        System.out.print("Enter floor: ");

        Thread scannerThread = new Thread(() -> {
            while (true) {
                String line = scanner.nextLine();
                try {
                    int floor = Integer.parseInt(line);
                    try {
                        elevator.requestFloor(floor);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Floor out of range");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input");
                }
            }
        });

        Thread elevatorThread = new Thread(() -> {
            while (true) {
                elevator.move();
            }
        });

        scannerThread.start();
        elevatorThread.start();
    }
}