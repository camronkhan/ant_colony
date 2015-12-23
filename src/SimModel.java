import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Timer;
import javax.swing.JOptionPane;

/**
 * Class SimModel
 * 
 * Responsible for (1) initializing a node matrix to store simulation data,
 * (2) Handling simulation clock and GUI events, (3) creating/storing ants, and
 * (4) Pushing updates to the simulation view
 * 
 * @author Camron Khan
 */
public class SimModel implements SimulationEventListener, ActionListener {
        
    /**************************************************************************
     * CONSTANTS
     **************************************************************************/

    // Number of rows in grid
    final private int ROWS;

    // Number of columns grid
    final private int COLS;

    // Milliseconds per turn
    final private int MS_PER_TURN;
    
    // Turns per day
    final private int TURNS_PER_DAY;

    // Initial conditions for all nodes
    final private int NUM_SOLDIER_INIT = 0;
    final private int NUM_FORAGER_INIT = 0;
    final private int NUM_SCOUT_INIT = 0;
    final private int NUM_BALA_INIT = 0;
    final private int FOOD_AMOUNT_INIT = 0;
    final private int PHEROMONE_LEVEL_INIT = 0;
    final private boolean VISITED_INIT = false;
    final private boolean QUEEN_INIT = false;

    // Initial conditions for colony entrance
    final private int NUM_SOLDIER_COLONY = 10;
    final private int NUM_FORAGER_COLONY = 50;
    final private int NUM_SCOUT_COLONY = 4;
    final private int FOOD_AMOUNT_COLONY = 1000;

    // Key-value pairs for ant hatches
    final protected Integer QUEEN = 0;
    final protected Integer FORAGER = 1;
    final protected Integer SCOUT = 2;
    final protected Integer SOLDIER = 3;
    final protected Integer BALA = 4;
    

    /**************************************************************************
     * ATTRIBUTES
     **************************************************************************/

    // Graphical user interface (GUI)
    private AntSimGUI gui;

    // View of the model
    private SimView view;

    // Two-dimensional array to store nodes
    private Node[][] grid;
    
    // Simulation timer
    private Timer simTimer;
    
    // Turns elapsed
    private int numTurns;
    
    // Ant ID counter
    private int antID;
    
    // Queen Ant
    private QueenAnt queen;
    
    // HashMap for storing ant ID-type pairs
    private HashMap<Integer, Integer> antTypeMap;
    
    // HashMaps for storing ant ID-object pairs
    private HashMap<Integer, ForagerAnt> foragerMap;
    private HashMap<Integer, ScoutAnt> scoutMap;
    private HashMap<Integer, SoldierAnt> soldierMap;
    private HashMap<Integer, BalaAnt> balaMap;
    
    
    /**************************************************************************
     * CONSTRUCTORS
     **************************************************************************/

    /**
     * Responsible for (1) initializing a node matrix to store simulation data,
     * (2) Handling simulation clock and GUI events, (3) creating/storing ants, and
     * (4) Pushing updates to the simulation view
     * 
     * @param rows      x coordinate
     * @param cols      y coordinate
     * @param delay     Number of milliseconds between timer events
     * @param turns     Number of turns per day
     */
    public SimModel(int rows, int cols, int delay, int turns) {

        // Number of rows and columns
        ROWS = rows;
        COLS = cols;
        
        // Number of milliseconds between simulation clock firings
        MS_PER_TURN = delay;
        
        // Number turn segments per day
        TURNS_PER_DAY = turns;

        // Create a view object to which the model will push updates
        view = new SimView(ROWS, COLS);
        
        // Greate the GUI
        gui = new AntSimGUI();
        
        // Initialize the GUI
        gui.initGUI(view.getContainer());
        
        // Add model as simulation event listener interested in the GUI
        gui.addSimulationEventListener(this);
        
        // Create a simulation timer
        simTimer = new Timer(MS_PER_TURN,this);
        
        // Initialize antID
        antID = 0;
    }


    /**************************************************************************
     * METHODS
     **************************************************************************/   
    
    /************************************
     * Simulation Initialization Methods
     ************************************/
    
    /**
     * Initialize SimModel with "Normal Setup" values
     */
    public void initSimModel() {

        // Create a two-dimensional array of nodes to implement grid
        grid = new Node[ROWS][COLS];
        
        // Initialize nodes
        createNodes(ROWS, COLS);
        
        // Create HashMaps to store ants
        antTypeMap = new HashMap<>();
        scoutMap = new HashMap<>();
        foragerMap = new HashMap<>();
        soldierMap = new HashMap<>();
        balaMap = new HashMap<>();

        // Initialize colony entrance
        initColonyEntrance(ROWS, COLS);
    }
    
    /**
     * Initializes the colony entrance
     * 
     * @param row   x coordinate
     * @param col   y coordinate
     */
    private void initColonyEntrance(int row, int col) {
        // Identify center node on x axis
        int x = row/2 + 1;

        // Identify center node on y axis
        int y = col/2 + 1;
        
        // Rename center node
        Node centerNode = grid[x][y];
        
        // Get position of center node
        String posCenterNode = getPosition(x, y);
        
        // Create queen
        createAnt(QUEEN, centerNode);
        
        // Create foragers
        for(int i = 0; i < NUM_FORAGER_COLONY; i++)
            createAnt(FORAGER, centerNode);
        
        //Create scouts
        for(int i = 0; i < NUM_SCOUT_COLONY; i++)
            createAnt(SCOUT, centerNode);
        
        // Create soldiers
        for(int i = 0; i < NUM_SOLDIER_COLONY; i++)
            createAnt(SOLDIER, centerNode);

        // Set initial food amount
        centerNode.setFoodAmount(posCenterNode, FOOD_AMOUNT_COLONY);

        // Set initial pheromone level
        centerNode.setPheromoneLevel(posCenterNode, PHEROMONE_LEVEL_INIT);

        // Reveal nodes surrounding colony entrance
        showAdjacentNodes(centerNode);
    }    
    
    
    /***************************
     * Simulation Clock Methods
     ***************************/
    
    /**
     * Responsible for advancing the simulation clock in continuous fashion
     */
    private void run() {
        
        // Start the simulation clock
        simTimer.start();
    }
    
    /**
     * Responsible for advancing the simulation clock in stepwise fashion
     */
    private void step() {
        
        // Stop timer
        simTimer.stop();
        
        // Create an action event to pass to the simulation clock listeners
        ActionEvent timerEvent = new ActionEvent(simTimer,
                                                 ActionEvent.ACTION_PERFORMED,
                                                 "Step");
        
        // Broadcast the event
        this.actionPerformed(timerEvent);
    }
    
    
    /*************************
     * Event Handling Methods
     *************************/
    
    /**
     * Responds to buttons pressed on the GUI
     * 
     * @param simEvent    The SimulationEvent which occurred (button pressed)
     */
    @Override
    public void simulationEventOccurred(SimulationEvent simEvent) {

        if (simEvent.getEventType() == SimulationEvent.NORMAL_SETUP_EVENT) { 

            // Initialize SimModel
            initSimModel();
        }

        else if (simEvent.getEventType() == SimulationEvent.QUEEN_TEST_EVENT) {
            
            createAnt(BALA, getNode(0, 0));
        }

        else if (simEvent.getEventType() == SimulationEvent.SCOUT_TEST_EVENT) {

            createAnt(SCOUT, getNode(14, 14));

        }

        else if (simEvent.getEventType() == SimulationEvent.FORAGER_TEST_EVENT) {

            createAnt(FORAGER, getNode(14, 14));
        }

        else if (simEvent.getEventType() == SimulationEvent.SOLDIER_TEST_EVENT) {
                
            createAnt(SOLDIER, getNode(3, 3));
        }

        else if (simEvent.getEventType() == SimulationEvent.RUN_EVENT) {

            run();

        }

        else if (simEvent.getEventType() == SimulationEvent.STEP_EVENT) {

            step();

        }

        else {

            // invalid event occurred - probably will never happen
            System.out.println("Invalid Entry!");
        }
    }
    
    /**
     * Responds to increments in the simulation clock
     * 
     * @param action    The ActionEvent which occurred (timer incremented)
     */
    @Override
    public void actionPerformed(ActionEvent action) {
        
        // Update number of turns elapsed
        numTurns++;
        
        // Broadcast time change to handlers
        guiHandler();
        nodeHandler();
        scoutHandler();
        foragerHandler();
        soldierHandler();
        balaHandler();
        queenHandler();
    }
    
    /**
     * Responsible for broadcasting simulation clock updates to GUI
     */
    private void guiHandler() {
        
        // Convert simulation time to string
        String t = getTime();
        
        // Set the GUI's time label
        gui.setTime(t);
    }
    
    /**
     * Responsible for broadcasting simulation clock updates to nodes
     */
    private void nodeHandler() {
        
        // For each node in the grid...
        for(int x = 0; x < ROWS; x++) {
            for(int y = 0; y < COLS; y++) {
                
                // Notify the node that time has elapsed
                grid[x][y].performActions();
            }
        }
    }
    
    /**
     * Responsible for broadcasting simulation clock updates to the queen
     */
    private void queenHandler() {
        
        // Notify the queen that time has elapsed
        queen.performActions();
    }
    
    /**
     * Responsible for broadcasting simulation clock updates to scouts
     */
    private void scoutHandler() {

        // Create iterator
        Iterator<Map.Entry<Integer, ScoutAnt>> iterator =
                                                scoutMap.entrySet().iterator();
        
        // While items remain in the HashMap...
        while(iterator.hasNext()) {
            
            // Get the key-value pair
            Map.Entry<Integer, ScoutAnt> pair = iterator.next();
            
            // Get ant
            ScoutAnt ant = pair.getValue();
            
            // Notify the ant that time has elapsed
            ant.performActions();
        }
    }
    
    /**
     * Responsible for broadcasting simulation clock updates to foragers
     */
    private void foragerHandler() {

        // Create iterator
        Iterator<Map.Entry<Integer, ForagerAnt>> iterator =
                                               foragerMap.entrySet().iterator();
        
        // While items remain in the HashMap...
        while(iterator.hasNext()) {
            
            // Get the key-value pair
            Map.Entry<Integer, ForagerAnt> pair = iterator.next();
            
            // Get ant
            ForagerAnt ant = pair.getValue();
            
            // Notify the ant that time has elapsed
            ant.performActions();
        }
    }
    
    /**
     * Responsible for broadcasting simulation clock updates to soldiers
     */
    private void soldierHandler() {

        // Create iterator
        Iterator<Map.Entry<Integer, SoldierAnt>> iterator =
                                               soldierMap.entrySet().iterator();
        
        // While items remain in the HashMap...
        while(iterator.hasNext()) {
            
            // Get the key-value pair
            Map.Entry<Integer, SoldierAnt> pair = iterator.next();
            
            // Get ant
            SoldierAnt ant = pair.getValue();
            
            // Notify the ant that time has elapsed
            ant.performActions();
        }    
    }
    
    /**
     * Responsible for broadcasting simulation clock updates to balas
     */
    private void balaHandler() {
        
        // Create iterator
        Iterator<Map.Entry<Integer, BalaAnt>> iterator =
                                                  balaMap.entrySet().iterator();
        
        // While items remain in the HashMap...
        while(iterator.hasNext()) {
            
            // Get the key-value pair
            Map.Entry<Integer, BalaAnt> pair = iterator.next();
            
            // Get ant
            BalaAnt ant = pair.getValue();
            
            // Notify the ant that time has elapsed
            ant.performActions();
        }
        
        // Randomly determine if Bala will be generated
        int random = RandomNumber.get(100);
        
        // There is a 3% chance that a bala will be generated
        if(random < 3) {
            
            // Balas enter environment in top-left corner (0,0)
            createAnt(BALA, getNode(0,0));
        }
    }
    
    
    /**********************************
     * Object Create & Destroy Methods
     **********************************/
    
    /**
     * Responsible for creating the nodes in the grid
     * 
     * @param row   x coordinate
     * @param col   y coordinate
     */
    private void createNodes(int row, int col) {
        
        // Create nodes
        for(int x = 0; x < row; x++) {
            for(int y = 0; y < col; y++) {

                // Creates a node with (x,y) grid position and reference to
                // the simulation view
                grid[x][y] = new Node(this, x, y);

                // Alias for new node
                Node newNode = grid[x][y];
                
                // Get position of node
                String pos = getPosition(x, y);

                // Set visited==false
                newNode.setVisited(pos, VISITED_INIT);

                // Set queenPresent==false
                newNode.setQueen(pos, QUEEN_INIT);

                // Set initial number of foragers
                newNode.setNumForager(pos, NUM_FORAGER_INIT);

                // Set initial number of scouts
                newNode.setNumScout(pos, NUM_SCOUT_INIT);

                // Set initial number of soldiers
                newNode.setNumSoldier(pos, NUM_SOLDIER_INIT);

                // Set initial number of balas
                newNode.setNumBala(pos, NUM_BALA_INIT);

                // Set initial food amount
                newNode.setFoodAmount(pos, FOOD_AMOUNT_INIT);

                // Set initial pheromone level
                newNode.setPheromoneLevel(pos, PHEROMONE_LEVEL_INIT);
            }
        }
        
        // ArrayLists to store lists of adjacent and visited-adjacent nodes
        ArrayList<Node> adj;
        ArrayList<Node> vst;
        
        // For each node created, set adjacent and visited-adjacent nodes
        for(int x = 0; x < row; x++) {
            for(int y = 0; y < col; y++) {
                
                // Identify nodes adjacent to current node
                adj = grid[x][y].findAdjacentNodes(ROWS, COLS);
                
                // Set adjacent nodes
                grid[x][y].setAdjacentNodes(adj);
                
                // Identify visted nodes adjacent to current node
                vst = grid[x][y].findVisitedAdjacentNodes(adj);
                
                // Set visited adjacent nodes
                grid[x][y].setVisitedAdjacentNodes(vst);
            }
        }
    }
    
    /**
     * Responsible for creating ants
     * 
     * @param antKey                Indicates type of ant to be created
     * @param initialPosition       Initial Node where ant will be located
     */
    protected void createAnt(Integer antType, Node initialNode) {
        
        // Create queen
        if(antType.intValue() == 0) {
            
            // Create Integer containing antID to pass to constructor
            Integer id = new Integer(antID);
            
            // Create queen
            queen = new QueenAnt(this, id, antType, initialNode);
            
            // Add ID and type to antTypeMap
            antTypeMap.put(id, antType);
            
            // Increment antID counter
            antID++;
        }
        
        // Create forager
        if(antType.intValue() == 1) {
            
            // Create Integer containing antID to pass to constructor
            Integer id = new Integer(antID);
            
            // Create ant
            ForagerAnt ant = new ForagerAnt(this, id, antType, initialNode);
            
            // Add ID and type to antTypeMap
            antTypeMap.put(id, antType);
            
            // Add ant to appropriate HashMap
            foragerMap.put(id, ant);
            
            // Increment AntID counter
            antID++;
        }
        
        // Create scout
        if(antType.intValue() == 2) {
            
            // Create Integer containing antID to pass to constructor
            Integer id = new Integer(antID);
            
            // Create ant
            ScoutAnt ant = new ScoutAnt(this, id, antType, initialNode);
            
            // Add ID and type to antTypeMap
            antTypeMap.put(id, antType);
            
            // Add ant to appropriate HashMap
            scoutMap.put(id, ant);
            
            // Increment AntID counter
            antID++;
        }
        
        // Create soldier
        if(antType.intValue() == 3) {
            
            // Create Integer containing antID to pass to constructor
            Integer id = new Integer(antID);
            
            // Create ant
            SoldierAnt ant = new SoldierAnt(this, id, antType, initialNode);
            
            // Add ID and type to antTypeMap
            antTypeMap.put(id, antType);
            
            // Add ant to appropriate HashMap
            soldierMap.put(id, ant);
            
            // Increment AntID counter
            antID++;
        }
        
        // Create bala
        if(antType.intValue() == 4) {
            
            // Create Integer containing antID to pass to constructor
            Integer id = new Integer(antID);
            
            // Create ant
            BalaAnt ant = new BalaAnt(this, id, antType, initialNode);
            
            // Add ID and type to antTypeMap
            antTypeMap.put(id, antType);
            
            // Add ant to appropriate HashMap
            balaMap.put(id, ant);
            
            // Increment AntID counter
            antID++;
        }
    }
    
    /**
     * Responsible for removing ants from applicable HashMap
     * 
     * @param ID            Unique ant ID
     * @param antType       Type of ant to be removed
     */
    protected void destroyAnt(Integer ID, int antType) {
        
        // Remove ant from antTypeMap
        antTypeMap.remove(ID);
        
        // GAME OVER
        if(antType == QUEEN) {
            
            // Stop the simulation timer
            simTimer.stop();
            
            System.out.println("Queen has died");
            
           JOptionPane.showMessageDialog(null, "The Queen has died - Click OK to exit", "GAME OVER", JOptionPane.INFORMATION_MESSAGE);
            
            //if (result == JOptionPane.YES_OPTION)
                //System.exit(0);
            
            System.exit(0);
        }
        
        // Destroy forager
        if(antType == FORAGER) {
            
            // Remove ant from appropriate HashMap
            foragerMap.remove(ID);
        }
        
        // Destroy scout
        if(antType == SCOUT) {
            
            // Remove ant from appropriate HashMap
            scoutMap.remove(ID);
        }
        
        // Destroy soldier
        if(antType == SOLDIER) {
            
            // Remove ant from appropriate HashMap
            soldierMap.remove(ID);
        }
        
        // Destroy bala
        if(antType == BALA) {
            
            // Remove ant from appropriate HashMap
            balaMap.remove(ID);
        }
    }
    
    
    /******************************
     * Push Model --> View Methods
     ******************************/
    
    /**
     * Responsible for revealing nodes on GUI
     * 
     * @param pos   String representation of grid position in "x,y" format
     */
    public void showNode(String pos) {
        view.showNode(pos);
    }
    
    /**
     * Responsible for hiding nodes on GUI
     * 
     * @param pos   String representation of grid position in "x,y" format
     */
    public void hideNode(String pos) {
        view.hideNode(pos);
    }
    
    /**
     * Responsible for setting a node's visited status
     * 
     * @param pos   String representation of the node's position
     * @param v     True if visited; false otherwise
     */
    public void setVisited(String pos, boolean v) {
        
        boolean visited = v;
        
        if(visited)
                view.showNode(pos);
            else
                view.hideNode(pos);
    }
    
    /**
     * Responsible for revealing adjacent nodes on GUI
     * 
     * @param current   The current node
     */
    public void showAdjacentNodes(Node currentNode) {
        
        // Create an ArrayList to store the set of adjacent nodes
        ArrayList arrLst = currentNode.getAdjacentNodes();
        
        // Create a ListIterator to iterate through ArrayList
        ListIterator<Node> lstItr = arrLst.listIterator();
        
        // While nodes remain in the ArrayList...
        while(lstItr.hasNext()) {
            
            Node tempNode = lstItr.next();
            
            // Get the position of each node
            String pos = tempNode.getPosition();
            
            // Reaveal each node
            tempNode.setVisited(pos, true);
        }
        
    }
    /**
     * Responsible for setting queen's presence or absence on GUI
     * 
     * @param pos   String representation of grid position in "x,y" format
     * @param q     True if queen present; false otherwise
     */
    public void setQueen(String pos, boolean q) {
        view.setQueen(pos, q);
    }
    
    /**
     * Responsible for setting number of foragers to display in node on GUI
     * 
     * @param pos   String representation of grid position in "x,y" format
     * @param num   Number of forager ants
     */
    public void setForagerCount(String pos, int num) {
        view.setForager(pos, num);
    }
    
    /**
     * Responsible for setting number of scouts to display in node on GUI
     * 
     * @param pos   String representation of grid position in "x,y" format
     * @param num   Number of scout ants
     */
    public void setScoutCount(String pos, int num) {
        view.setScout(pos, num);
    }
    
    /**
    * Responsible for setting number of soldiers to display in node on GUI
    * 
    * @param pos   String representation of grid position in "x,y" format
    * @param num   Number of soldier ants
    */
    public void setSoldierCount(String pos, int num) {
         view.setSoldier(pos, num);
    }   
    
    /**
     * Responsible for setting number of balas to display in node on GUI
     * 
     * @param pos   String representation of grid position in "x,y" format
     * @param num   Number of bala ants
     */
    public void setBalaCount(String pos, int num) {
        view.setBala(pos, num);
    }
    
    /**
     * Responsible for setting amount of food to display in node on GUI
     * 
     * @param pos   String representation of grid position in "x,y" format
     * @param num   Amount of food available
     */
    public void setFoodAmount(String pos, int num) {
        view.setFoodAmount(pos, num);
    }
        
    /**
    * Responsible for setting pheromone level to display in node on GUI
    * 
    * @param pos   String representation of grid position in "x,y" format
    * @param num   Pheromone level present
    */
    public void setPheromoneLevel(String pos, int num) {
        view.setPheromoneLevel(pos, num);
    }


    /*****************************
     * Accessor & Mutator Methods
     *****************************/
    
    /**
     * Get the number of rows in the grid
     * 
     * @return      Number of rows in the grid
     */
    public int getRows() {
        return ROWS;
    }
    
    /**
     * Get the number of columns in the grid
     * 
     * @return      Number of columns in the grid
     */
    public int getCols() {
        return COLS;
    }
    
    /**
     * Get the position of a node given its row and column locations
     * 
     * @param row   x-coordinate
     * @param col   y-coordinate
     * @return      String representation of position
     */
    public String getPosition(int row, int col) {
        return row + "," + col;
    }

    /**
     * Get a String representation of the current time
     * 
     * @return      String representation of the current time
     */
    public String getTime() {
        
        // Number of days elapsed
        int days = numTurns / TURNS_PER_DAY;

        // Remainder of turns withn a particular day that have elapsed
        int turns = numTurns % TURNS_PER_DAY;

        // Return #days, #turns
        return days + " days, " + turns + " turns";
    }

    /**
     * Retrieves the container for the node views
     * 
     * @return  ColonyView container object
     */
    public ColonyView getContainer() {
        return view.getContainer();
    }
    
    /**
     * Returns a node given its position
     * 
     * @param row       x coordinate
     * @param col       y coordinate
     * @return          Node at position (x,y)
     */
    public Node getNode(int row, int col) {
        return grid[row][col];
    }
    
    /**
     * Returns the queen
     * 
     * @return      The queen
     */
    public QueenAnt getQueen() {
        return queen;
    }
    
    /**
     * Returns a forager ant given its ant ID
     * 
     * @param ID        Unique integer ant ID
     * @return          The forager
     */
    public ForagerAnt getForager(Integer ID) {
        
        // Get the ant from its map
        ForagerAnt forager = foragerMap.get(ID);
        
        return forager;
    }
    
    /**
     * Returns a scout ant given its ant ID
     * 
     * @param ID        Unique integer ant ID
     * @return          The scout
     */
    public ScoutAnt getScout(Integer ID) {
        
        // Get the ant from its map
        ScoutAnt scout = scoutMap.get(ID);
        
        return scout;
    }
    
    /**
     * Returns a soldier ant given its ant ID
     * 
     * @param ID        Unique integer ant ID
     * @return          The soldier
     */
    public SoldierAnt getSoldier(Integer ID) {
        
        // Get the ant from its map
        SoldierAnt soldier = soldierMap.get(ID);
        
        return soldier;
    }
    
    /**
     * Returns a bala ant given its ant ID
     * 
     * @param ID        Unique integer ant ID
     * @return          The bala
     */
    public BalaAnt getBala(Integer ID) {
        
        // Get the ant from its map
        BalaAnt bala = balaMap.get(ID);
        
        return bala;
    }
    
    /**
     * Returns a HashMap storing ant ID-object pairs. Each map can be used to
     * get a particular ant object given its unique ID.
     * 
     * @param type      The ant type
     * @return          HashMap storing ant ID-object pairs
     */
    public HashMap getAntMap(Integer antType) {
        
        // If ant type is forager
        if(antType.intValue() == FORAGER.intValue())
            return foragerMap;
        
        // If ant type is scout
        else if(antType.intValue() == SCOUT.intValue())
            return scoutMap;
        
        // If ant type is soldier
        else if(antType.intValue() == SOLDIER.intValue())
            return soldierMap;
        
        // If ant type is bala
        else
            return balaMap;
    }
    
    /**
     * Returns a HashMap storing ant ID-type pairs. The map can be used to get
     * an ant's type given its unique ID.
     * 
     * @return      The ant type
     */
    public HashMap getAntTypes() {
        return antTypeMap;
    }
}
