import java.util.ArrayList;
import java.util.ListIterator;

/**
 * Class Node
 * 
 * Encapsulates the basic functionality of the fundamental building blocks of
 * the environment - nodes.
 * 
 * @author Camron Khan
 */
public class Node implements TimeDependent, Comparable<Node> {

        /************
         * Constants
         ************/
        
        // Reference to the simulation model
        private final SimModel MODEL;
        
        // X coordinate
        private final int ROW;
        
        // Y coordinate
        private final int COL;
        
        // Position
        private final String POSITION;
    
    
        /*************
	 * Attributes
	 *************/
        
        // Whether node has been visited by a scout
        private boolean visited;
        
        // Whether the queen is present in node
        private boolean queenPresent;
        
        // Number of ant type in node
        private int numForager;
        private int numScout;
        private int numSoldier;
        private int numBala;
        
        // Amount of food present in node
        private int food;
        
        // Pheromone level present in node
        private int pheromone;
        
        // ArrayList storing the nodes adjacent to this node
        private ArrayList<Node> adjNodes;
        
        // ArrayList storing the visited nodes adjacent to this node
        private ArrayList<Node> visitedAdjNodes;
        
        // ArrayList storing the IDs of ants currently present in this node
        private ArrayList<Integer> antsPresent;
        
        
    	/***************
	 * Constructors
	 ***************/
        
        public Node(SimModel mod, int x, int y) {
            
            // Set initial conditions
            MODEL = mod;
            ROW = x;
            COL = y;
            POSITION = ROW + "," + COL;
            visited = false;
            queenPresent = false;
            numForager = 0;
            numScout = 0;
            numSoldier = 0;
            food = 0;
            pheromone = 0;
            
            // Get the total number of rows and columns in environment
            int rowTotal = MODEL.getRows();
            int colTotal = MODEL.getCols();
            
            // Initialize ArrayLists
            adjNodes = new ArrayList<>();
            visitedAdjNodes = new ArrayList<>();
            antsPresent = new ArrayList<>();
        }
        
        
        /**********
	 * Methods
	 **********/

        /**
         * Responsible for performing actions dependent on the simulation clock
         */
        @Override
        public void performActions() {
           
            // Rescan adjacent nodes to identify all visited nodes
            ArrayList<Node> visitedList = findVisitedAdjacentNodes(adjNodes);
            
            // Update list of visited adjacent nodes
            setVisitedAdjacentNodes(visitedList);
            
            // Reduce the pheromone level in the node by half
            halvePheromoneLevel();
        }
        
        /**
         * Compares the positions of this node to another node
         * 
         * @param otherNode     The other node
         * @return              x=0 if same position; x!=0 if different
         */
        @Override
        public int compareTo(Node otherNode) {
            
            // Get position of this node
            String thisPos = this.getPosition();
            
            // Get position of other node
            String otherPos = otherNode.getPosition();
            
            // Compare the positions
            int val = thisPos.compareTo(otherPos);
            
            // Return the value
            return val;
        }
        
        /**
         * Compares the pheromone level of this node to another node
         * 
         * @param otherNode     The other node
         * @return              The amount of difference between the nodes
         */
        public int comparePL(Node otherNode) {
            
            // Get pheromone level of this node
            int thisPL = this.getPheromoneLevel();
            
            // Get pheromone level of other node
            int otherPL = otherNode.getPheromoneLevel();
            
            // Compare this node's pheromone level to the other node's
            int diff = thisPL - otherPL;
            
            // Return the difference
            return diff;
        }
        
        /**
         * Responsible for getting x coordinate of node
         * 
         * @return      x coordinate of node
         */
        public int getRow() {
            return ROW;
        }
        
        /**
         * Responsible for getting y coordinate of node
         * 
         * @return      y coordinate of node
         */
        public int getCol() {
            return COL;
        }

        /**
         * Responsible for getting grid position of node in "x,y" format
         * 
         * @return      String representation of grid position in "x,y" format
         */
        public String getPosition() {
            return POSITION;
        }
        
        /**
         * Responsible for getting visited status of node
         * 
         * @return      True if visited; false otherwise 
         */
        public boolean getVisited() {
            return visited;
        }
        
        /**
         * Responsible for setting visited status of node
         * 
         * @param pos   String representation of grid position in "x,y" format
         * @param v     True if visited; false otherwise 
         */
        public void setVisited(String pos, boolean v) {
            
            // Set status of node's visibility / accessibility
            visited = v;
            
            // Notify model
            MODEL.setVisited(pos, visited);
        }
        
        /**
         * Responsible for getting status of queen's presence in node
         * 
         * @return      True if present; false otherwise
         */
        public boolean getQueen() {
            return queenPresent;
        }
        
        /**
         * Responsible for setting status of queen's presence in node
         * 
         * @param pos   String representation of grid position in "x,y" format
         * @param q     True if present; false otherwise
         */
        public void setQueen(String pos, boolean q) {
            
            // Set new status of queen's presence
            queenPresent = q;
            
            // Notify model
            MODEL.setQueen(pos, q);
        }
        
        /**
         * Responsible for getting number of foragers present in node
         * 
         * @return      Number of foragers present in node
         */
        public int getNumForager() {
            return numForager;
        }
        
        /**
         * Responsible for setting number of foragers present in node
         * 
         * @param pos   String representation of grid position in "x,y" format
         * @param f     Number of foragers present in node
         */
        public void setNumForager(String pos, int f) {
            
            // Set new forager number
            numForager = f;
            
            // Notify model
            MODEL.setForagerCount(pos, numForager);
        }
        
        /**
         * Responsible for getting number of scouts present in node
         * 
         * @return      Number of scouts present in node
         */
        public int getNumScout() {
            return numScout;
        }
        
        /**
         * Responsible for setting number of scouts present in node
         * 
         * @param pos   String representation of grid position in "x,y" format
         * @param s     Number of scouts present in node
         */
        public void setNumScout(String pos, int s) {
            
            // Set new scout number
            numScout = s;
            
            // Notify model
            MODEL.setScoutCount(pos, numScout);
        }
        
        /**
         * Responsible for getting number of soldiers present in node
         * 
         * @return      Number of soldiers present in node
         */
        public int getNumSoldier() {
            return numSoldier;
        }
        
        /**
         * Responsible for setting number of soldiers present in node
         * 
         * @param pos   String representation of grid position in "x,y" format
         * @param s     Number of soldiers present in node
         */
        public void setNumSoldier(String pos, int s) {
            
            // Set new soldier number
            numSoldier = s;
            
            // Notify model
            MODEL.setSoldierCount(pos, numSoldier);
        }
        
        /**
         * Responsible for getting number of balas present in node
         * 
         * @return      Number of balas present in node
         */
        public int getNumBala() {
            return numBala;
        }
        
        /**
         * Responsible for setting number of balas present in node
         * 
         * @param pos   String representation of grid position in "x,y" format
         * @param b     Number of balas present in node
         */
        public void setNumBala(String pos, int b) {
            
            // Set new bala number
            numBala = b;
            
            // Notify model
            MODEL.setBalaCount(pos, numBala);
        }
        
        /**
         * Responsible for getting amount of food present in node
         * 
         * @return      Amount of food present in node
         */
        public int getFoodAmount() {
            return food;
        }
        
        /**
         * Responsible for setting amount of food present in node
         * 
         * @param pos   String representation of grid position in "x,y" format
         * @param f     Amount of food present in node
         */
        public void setFoodAmount(String pos, int f) {
            
            // Set new food amount
            food = f;
            
            // Notify model
            MODEL.setFoodAmount(pos, food);
        }
        
        /**
         * Responsible for getting pheromone level present in node
         * 
         * @return      Pheromone level present in node
         */
        public int getPheromoneLevel() {
            return pheromone;
        }
        
        /**
         * Responsible for setting pheromone level present in node
         * 
         * @param pos   String representation of grid position in "x,y" format
         * @param p     Pheromone level present in node
         */
        public void setPheromoneLevel(String pos, int p) {
            
            // Set new pheromone level in node
            pheromone = p;
            
            // Notify model
            MODEL.setPheromoneLevel(pos, pheromone);
        }
        
        /**
         * Responsible for getting nodes adjacent to current node
         * 
         * @return      A list of nodes adjacent to current node
         */
        public ArrayList<Node> getAdjacentNodes() {
            return adjNodes;
        }
        
        /**
         * Responsible for setting the list of nodes adjacent to current node
         * 
         * @param adjList   ArrayList of adjacent nodes
         */
        public void setAdjacentNodes(ArrayList<Node> adjList) {
            adjNodes = adjList;
        }
        
        /**
         * Establishes an ArrayList of nodes that are adjacent to the current
         * node within the bounds of the grid
         * 
         * @param rows  The total number of rows in the environment
         * @param cols  The total number of columns in the environment
         * @return      An ArrayList of nodes adjacent to current node
         */
        public ArrayList<Node> findAdjacentNodes(int rows, int cols) {
            
            // Get total number of rows and columns in grid
            int rowTotal = rows;
            int colTotal = cols;

            // Create ArrayList to store set of adjacent nodes
            ArrayList<Node> adjList = new ArrayList<>();

            // Get surrounding node positions and store in ArrayList
            for(int i = -1; i <= 1; i++) {
                for(int j = -1; j <= 1; j++) {
                    int x = ROW + i;
                    int y = COL + j;
                    
                    // If node is within grid bounds and not current node...
                    if( ((x >= 0 && x < rowTotal) && (y >= 0 && y < colTotal))
                        &&
                        !((x == ROW) && (y == COL)) 
                      ) {

                        // Create Node object to store node instance
                        Node node = MODEL.getNode(x, y);
                        
                        // Add the node instance to ArrayList
                        adjList.add(node);
                    }
                }
            }
            
            // Return the ArrayList
            return adjList;
        }
        
        /**
         * Responsible for getting visited nodes adjacent to current node
         * 
         * @return      A list of visited nodes adjacent to current node
         */
        public ArrayList<Node> getVisitedAdjacentNodes() {
            return visitedAdjNodes;
        }
        
        /**
         * Responsible for setting visited nodes adjacent to current node
         * 
         * @param visitedAdjList    ArrayList of visited adjacent nodes
         */
        public void setVisitedAdjacentNodes(ArrayList<Node> visitedAdjList) {
            visitedAdjNodes = visitedAdjList;
        }
        
        /**
         * Establishes an ArrayList of visited nodes that are adjacent to the
         * current node within the bounds of the grid
         * 
         * @param adj   An ArrayList of nodes adjacent to current node
         * @return      An ArrayList of visited nodes adjacent to current node
         */
        public ArrayList<Node> findVisitedAdjacentNodes(ArrayList<Node> adj) {
            
            // Create an ArrayList to store adjacent nodes
            ArrayList<Node> adjacent = adj;
        
            // Create an ArrayList to store visited adjacent nodes
            ArrayList<Node> visitedList = new ArrayList<>();
            
            // Create an iterator
            ListIterator<Node> iterator = adjacent.listIterator();
            
            // While nodes remain in the list...
            while(iterator.hasNext()) {
                
                // Get the next node in the list
                Node nextNode = iterator.next();
                
                // If the node has been visited...
                if(nextNode.getVisited()) {
                    
                    // Add the node to the list of vistited adjacent nodes
                    visitedList.add(nextNode);
                }
            }
            
            // Return ArrayList
            return visitedList;
        }
        
        /**
         * Responsible getting a list of ID's of ants currently in node
         * 
         * @return  Integer ID's of ants currently in node
         */
        public ArrayList<Integer> getAntsPresent() {
            return antsPresent;
        }
        
        /**
         * Responsible for adding an ant's ID to the list of current members
         * 
         * @param id    Integer ID of ant to be added
         */
        public void addAnt(Integer id) {
            antsPresent.add(id);
        }
        
        /**
         * Responsible for removing an ant's ID from the list of current members
         * 
         * @param id    Integer ID of ant to be removed
         */
        public void removeAnt(Integer id) {
            antsPresent.remove(id);
        }

        /**
         * Responsible for reducing the pheromone level in the node by half
         */
        private void halvePheromoneLevel() {

            // Get current pheromone level
            int level = getPheromoneLevel();
            
            // Decrease pheromone level by half
            level = level / 2;
                        
            // Set new pheromone level
            setPheromoneLevel(POSITION, level);
        }
}
