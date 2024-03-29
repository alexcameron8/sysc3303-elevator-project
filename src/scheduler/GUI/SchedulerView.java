package scheduler.GUI;

import elevator.ElevatorSubsystem;
import event.DirectionType;
import event.ElevatorButtonPressEvent;
import event.ElevatorTripUpdateEvent;
import event.FloorButtonPressEvent;
import floor.FloorSubsystem;
import main.Configuration;
import scheduler.ElevatorJobState;
import scheduler.ElevatorStatus;
import scheduler.ElevatorTripUpdate;
import scheduler.Scheduler;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
/**
 * The View that displays all the scheduler data in a JFrame.
 * @author: Alex Cameron
 *
 */
public class SchedulerView extends JFrame implements SchedulerViewListener {

    private SchedulerController sc;
    private ElevatorSubsystem elevatorSubsystem;
    private FloorSubsystem floorSubsystem;
    private Scheduler schedulerModel;
    private CarView[] carViews;
    private NotificationView notificationView;
    private ElevatorInfoView[] elevatorInfoViews;
    private JPanel carPanel, elevatorInfoPanel, inputFilePanel;
    private JPanel[] floors, carDirections;
    private JLabel directionLabel;
    private JSplitPane splitPane;
    private Color RED = new Color(249,65,68);
    private Color ORANGE = new Color(248,132,74);
    private Color YELLOW = new Color(249,199,79);
    private Color BLUE = new Color(15,141,176);
    private Color GREEN = new Color(82,230,109);
    private Color PURPLE = new Color(134,117,214);

    /**
     * Initializes all the view components within the SchedulerView Frame
     * @param schedulerModel - The model associated with the SchedulerView
     */
    public SchedulerView(Scheduler schedulerModel){
        super("ElevatorSubsystem");
        this.setLayout(new BorderLayout());
        Toolkit tk = Toolkit.getDefaultToolkit();
        int xSize = ((int) tk.getScreenSize().getWidth());
        int ySize = ((int) tk.getScreenSize().getHeight());
        this.setSize(xSize,ySize); //set size to max screen
        this.setMinimumSize(new Dimension(xSize-100,ySize-100));
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setResizable(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.schedulerModel = schedulerModel;

        //initialize Scheduler Controller
        sc = new SchedulerController(this, schedulerModel);

        //set listeners
        schedulerModel.addSchedulerViewListeners(this);
        //initialize the
        initTitleBar();
        //initialize the floors and cars
        initFloors();
        initCars();
        directionLabel = new JLabel(); //init direction label
        //initialize car info view
        initElevatorInfoViews();
        //initialize input file view
        initInputFileView();
        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, elevatorInfoPanel, inputFilePanel);
        splitPane.setOneTouchExpandable(true);
        splitPane.setResizeWeight(0.3);
        this.add(splitPane,BorderLayout.CENTER);
        //initialize notification view
        this.notificationView = new NotificationView();
        this.add(notificationView, BorderLayout.LINE_END);
    }
    
    /**
     * This method initializes the title bar with the Title and Legend which describes the different colours'
     * associated to each panel colour in the elevator system view.
     */
    public void initTitleBar(){
    	//init title bar
        JPanel titleBar = new JPanel();
        titleBar.setBackground(Color.darkGray);
        titleBar.setLayout(new BoxLayout(titleBar, BoxLayout.PAGE_AXIS));
        JLabel titleLabel = new JLabel("SYSC3303 Final Project - Group 7: Elevator System.");
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 40));
        titleLabel.setForeground(Color.white);
        titleLabel.setBackground(Color.WHITE);
        titleBar.add(titleLabel);
        this.add(titleBar, BorderLayout.PAGE_START);
    }

    /**
     * Initializes the floors to be displayed in the elevator system component.
     */
    public void initFloors(){
        JPanel floorPanel = new JPanel(); //floor panel
        floorPanel.setLayout(new BoxLayout(floorPanel, BoxLayout.PAGE_AXIS));
        floors = new JPanel[Configuration.NUM_FLOORS+1]; //The rows for floors and floor header
        JLabel[] floorLabels = new JLabel[Configuration.NUM_FLOORS+1];

        //Creating floor header
        JPanel floorHeader = new JPanel();
        floorHeader.setBorder(BorderFactory.createLineBorder(Color.black));
        floorHeader.add(new JLabel("Floors:"));
        floorHeader.setFont(new Font("Serif", Font.BOLD, 14));
        floorHeader.setBackground(Color.lightGray);
        floorPanel.add(floorHeader);

        //Initializing all panels for each floor
        for(int i=Configuration.NUM_FLOORS; i>=1 ;i--){
            floors[i] = new JPanel();
            floors[i].setBorder(BorderFactory.createLineBorder(Color.black));
            floorLabels[i] = new JLabel("Floor " + i);
            floorLabels[i].setFont(new Font("Serif", Font.BOLD, 14));
            floors[i].add(floorLabels[i]);
            floors[i].setBackground(Color.WHITE);
            floorPanel.add(floors[i]);
        }

        //Direction label
        JPanel directionFloorPanel = new JPanel();
        JLabel directionFloorLabel = new JLabel("Direction:");
        directionFloorPanel.setBackground(Color.white);
        directionFloorPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        directionFloorLabel.setFont(new Font("Serif", Font.BOLD, 14));
        directionFloorPanel.add(directionFloorLabel);
        floorPanel.add(directionFloorPanel);

        carPanel = new JPanel();
        carPanel.setLayout(new FlowLayout());
        carPanel.add(floorPanel);
    }
    /**
     * Initializes the car panels with the number of cars and floors for each elevator car.
     */
    public void initCars(){
        JPanel[] cars = new JPanel[Configuration.NUM_CARS]; //panel for each car
        carViews = new CarView[Configuration.NUM_CARS]; //car views for each car which contain the floors
        JButton[] carLabelButtons = new JButton[Configuration.NUM_CARS]; //labels at top representing each car
        carDirections = new JPanel[Configuration.NUM_CARS]; //Direction panels for each car with icons specifying direction

        //create car labels for each elevator car
        for(int i=0; i< Configuration.NUM_CARS;i++){
            if(i==0){
                carPanel.add(Box.createRigidArea(new Dimension(20,0))); //adding white space between views
            }
            cars[i] = new JPanel();
            cars[i].setLayout(new BoxLayout(cars[i],BoxLayout.PAGE_AXIS));
            cars[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
            cars[i].setBackground(Color.lightGray);
            JPanel carLabelPanel = new JPanel();
            carLabelButtons[i] = new JButton("Car " + i);
            carLabelButtons[i].setFont(new Font("Serif", Font.BOLD, 14));
            carLabelButtons[i].setFocusPainted(false);
            carLabelButtons[i].setBackground(Color.lightGray);
            carLabelPanel.add(carLabelButtons[i]);
            cars[i].add(carLabelPanel);
            carPanel.add(cars[i]);

            if(i!=Configuration.NUM_CARS-1){
                carPanel.add(Box.createRigidArea(new Dimension(20,0))); //adding white space between views
            }

            //actionlisteners for car buttons
            carLabelButtons[i].addActionListener(sc);
            carLabelButtons[i].setActionCommand("" +i);
        }

        //setting up carviews which represent each floor for each elevator car
        for(int i = 0; i<cars.length;i++){
            carViews[i] = new CarView(i); //create new Car view
            carViews[i].createFloors(cars[i]); //init all floors
        }

        //setup direction panels
        for(int i = 0; i<cars.length;i++){
            carDirections[i] = new JPanel();
            carDirections[i].add(new JLabel("IDLE"));
            carDirections[i].setFont(new Font("Serif", Font.BOLD, 14));
            cars[i].add(carDirections[i]);
        }

        this.add(carPanel,BorderLayout.LINE_START);
    }
    
    /**
     * Initializes the information views for each elevator car.
     */
    public void initElevatorInfoViews(){
        elevatorInfoPanel = new JPanel();
        elevatorInfoPanel.setLayout(new FlowLayout());
        elevatorInfoViews = new ElevatorInfoView[Configuration.NUM_CARS];
        for(int i=0; i<Configuration.NUM_CARS; i++){
            elevatorInfoViews[i] = new ElevatorInfoView();
            elevatorInfoPanel.add(elevatorInfoViews[i]);

            if(i!=Configuration.NUM_CARS-1){
                elevatorInfoPanel.add(Box.createRigidArea(new Dimension(20,0))); //adding white space between views
            }
        }
    }
    /**
     * Initialize the Input File and Configuration File View.  
     */
    public void initInputFileView(){
        inputFilePanel = new InputFileView();
    }
    /**
     * Getter method for the NotificationView
     * @return The NotificationView
     */
    public NotificationView getNotificationView(){
        return notificationView;
    }
    /**
     * Getter method to retrieve the ElevatorInfoView with a specified car ID.
     * @param carID - The elevator car ID
     * @return The ElevatorInfoView for the specified car ID
     */
    public ElevatorInfoView getElevatorInfoView(int carID) {
        return elevatorInfoViews[carID];
    }
    
    /**
     * Sets the direction label for the specified elevator car.
     * @param directionType - The Directiontype in the JPanel elevator car
     * @param panel - The panel which the Directiontype is inputed in.
     */
    public void setDirectionLabel(DirectionType directionType, JPanel panel){
        if(directionType==DirectionType.DOWN){
            directionLabel = new JLabel("DOWN");
        }else if(directionType==DirectionType.UP){
            directionLabel = new JLabel("UP");
        }else {
            directionLabel = new JLabel("STOPPED");
        }
        directionLabel.setFont(new Font("Serif", Font.BOLD, 14));
        panel.removeAll();
        panel.revalidate();
        panel.repaint();
        panel.add(directionLabel);
        panel.repaint();
    }

    @Override
    public void handleElevatorStatusUpdate(ElevatorTripUpdateEvent e) {
        if(e.getUpdate() == ElevatorTripUpdate.CONTINUE){ //updates the elevator car ID panel with green for continuing
            carViews[e.getCar()].setFloor(e.getApproachingFloor(),GREEN);
        }else{ //update the elevator car ID panel with red for stopping
            carViews[e.getCar()].setFloor(e.getApproachingFloor(),RED); 
        }
    }

    @Override
    public void handleFloorButtonPressUpdate(FloorButtonPressEvent e){
    	Color color = floors[e.getFloor()].getBackground();
    	
    	if(color == Color.BLUE && (e.getDirection() == DirectionType.DOWN)) {
    		floors[e.getFloor()].setBackground(Color.CYAN); //if there is an existing UP or DOWN request change floor panel to cyan
    	}else if(color == Color.yellow && (e.getDirection() == DirectionType.UP)) {
    		floors[e.getFloor()].setBackground(Color.CYAN); //if there is an existing UP or DOWN request change floor panel to cyan
    	}else if(e.getDirection() == DirectionType.DOWN) {
    		floors[e.getFloor()].setBackground(YELLOW); //if there is a floor request then the floor panel is yellow
    	}else {
            floors[e.getFloor()].setBackground(BLUE); //if there is a floor request UP then the floor panel is blue
    	}
        this.revalidate();
        this.repaint();
    }

    @Override
    public void handleElevatorButtonPressUpdate(ElevatorButtonPressEvent e){
    	Color color = floors[e.getState().getFloorNum()].getBackground();
    	if(color == Color.CYAN && (e.getDirection() == DirectionType.DOWN)) {
    		floors[e.getState().getFloorNum()].setBackground(BLUE); //There were UP and DOWN requests and now there is now only an UP request
    	}else if(color == Color.CYAN && (e.getDirection() == DirectionType.UP)){
    		floors[e.getState().getFloorNum()].setBackground(YELLOW); //There were UP and DOWN requests and now there is now only a down request
    	}else{
    		floors[e.getState().getFloorNum()].setBackground(Color.WHITE); //reset floor panel that previously requested a pickup
    	}
    }

    @Override
    public void handleElevatorStateUpdate(ElevatorStatus elevatorStatus){ //update the CarInfoView with the data from elevatorStatus of the specified car
        elevatorInfoViews[elevatorStatus.getId()].setCarInfo(elevatorStatus);

        if(elevatorStatus.getStatus() == ElevatorJobState.IDLE){ //if elevator state is idle then set the carview to red
            carViews[elevatorStatus.getId()].setFloor(elevatorStatus.getLocation(),RED);
        }

        if(elevatorStatus.isFaulty()){ //if the elevator is in fault state 
        	if(elevatorStatus.getStatus() == ElevatorJobState.DOOR_STUCK) { //door is stuck set to purple
                carViews[elevatorStatus.getId()].setFloor(elevatorStatus.getLocation(),PURPLE);
        	}else { //any critical fault (arrival sensor or motor fail) then set orange (Out of service)
                carViews[elevatorStatus.getId()].setFloor(elevatorStatus.getLocation(),ORANGE);	
        	}
        }

        //set Elevator car direction
        setDirectionLabel(elevatorStatus.getDirection(),carDirections[elevatorStatus.getId()]);
    }
}
