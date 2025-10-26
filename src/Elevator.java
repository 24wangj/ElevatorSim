import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

public class Elevator {
    enum State {
        GOING_UP,
        GOING_DOWN,
        STOPPED
    }

    private final int numFloors;
    private int currentFloor;
    private SortedSet<Integer> upFloors;
    private SortedSet<Integer> downFloors;
    private State state;
    private final int SPEED = 1;
    private final int DOOR_OPEN_TIME = 3;

    /**
     * Constructs a new Elevator instance with a specified number of floors and starting floor.
     */
    public Elevator(int numFloors, int currentFloor) {
        if (numFloors < 2 || currentFloor > numFloors || currentFloor < 1) throw new IllegalArgumentException();

        this.numFloors = numFloors;
        this.currentFloor = currentFloor;
        upFloors = new TreeSet<>();
        downFloors = new TreeSet<>(Collections.reverseOrder());
        state = State.STOPPED;
    }

    /**
     * Adds a floor request to the appropriate queue (up or down) based on the current floor.
     * If the requested floor is the same as the current floor, the elevator doors open.
     */
    public synchronized void requestFloor(int floor) throws IllegalArgumentException {
        if (floor > numFloors || floor < 1) throw new IllegalArgumentException();

        if (floor > currentFloor) {
            upFloors.add(floor);
        } else if (floor < currentFloor) {
            downFloors.add(floor);
        } else {
            openDoors();
        }
    }

    /**
     * Moves the elevator one step in its current direction.
     * If stopped, determines whether to move up or down based on queued requests.
     * Automatically handles stopping at floors, opening doors, and updating state.
     */
    public synchronized void move() {
        if (state == State.STOPPED) {
            if (!upFloors.isEmpty()) state = State.GOING_UP;
            else if (!downFloors.isEmpty()) state = State.GOING_DOWN;
        }

        if (state == State.GOING_UP) {
            try {
                Thread.sleep(SPEED * 1000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (!upFloors.isEmpty()) {
                currentFloor++;
                if (currentFloor == upFloors.getFirst()) {
                    upFloors.remove(currentFloor);
                    openDoors();
                    if (upFloors.isEmpty()) state = State.STOPPED;
                }
            }
        } else if (state == State.GOING_DOWN) {
            try {
                Thread.sleep(SPEED * 1000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (!downFloors.isEmpty()) {
                currentFloor--;
                if (currentFloor == downFloors.getFirst()) {
                    downFloors.remove(currentFloor);
                    openDoors();
                    if (downFloors.isEmpty()) state = State.STOPPED;
                }
            }
        }

        if (state != State.STOPPED) logInfo();
    }

    /**
     * Simulates opening and closing the elevator doors on the current floor.
     */
    public void openDoors() {
        System.out.println("Opening doors on floor: " + currentFloor);
        try {
            Thread.sleep(DOOR_OPEN_TIME * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Closing doors on floor: " + currentFloor);
        System.out.println("==============================");
    }

    public void logInfo() {
        System.out.println("Floor: " + currentFloor + " / " + numFloors);
        System.out.println("Up Floors: " + upFloors);
        System.out.println("Down Floors: " + downFloors);
        System.out.println("==============================");
    }
}