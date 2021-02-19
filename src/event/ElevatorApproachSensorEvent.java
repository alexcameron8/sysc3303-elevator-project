package event;

public class ElevatorApproachSensorEvent extends Event {
    private int car;
    private int floor;
    private DirectionType direction;

    /**
     * Create elevator arrive event
     * @param c - int car number
     * @param dir - DirectionType where it is going (UP/DOWN)
     */
    public ElevatorApproachSensorEvent(int c, DirectionType dir) {
        super("", EventType.ELEVATOR_APPROACH_SENSOR);
        car = c;
        direction = dir;
    }

    public int getCar() {
            return car;
        }
        public int getFloor() {
            return floor;
        }

        public DirectionType getDirection() {
            return direction;
        }
    }


