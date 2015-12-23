import java.util.HashMap;

/**
 * Class SimView
 * 
 * Encapsulates the basic functionality of a View
 * 
 * @author Camron Khan
 */
public class SimView {
        
        /************
	 * Constants
	 ************/
         
        /*************
	 * Attributes
	 *************/
        
        private HashMap<String, NodeView> nodeViewMap;
        private ColonyView container;
         
        
    	/***************
	 * Constructors
	 ***************/
        
        public SimView(int row, int col) {
            
            // Creates HashMap to store view of inidivual nodes in colony
            nodeViewMap = new HashMap<>();
            
            // Creates a container to hold the individual node views
            container = new ColonyView(row, col);
            
            // Initializse the HashMap with key-value pairs
            initMap(row, col);
        }
        
        
        /**********
	 * Methods
	 **********/
        
        /**
         * Adds the views of the nodes in the colony to the HashMap
         * 
         * @param row   x coordinate
         * @param col   y coordinate
         */
        private void initMap(int row, int col) {
            for(int x = 0; x < row; x++) {
                for(int y = 0; y < col; y++) {
                    
                    // Creates view of nodes
                    NodeView nodeView = new NodeView();
                    
                    // Create a key for the node view
                    String pos = createKey(x, y);
                    
                    // Set the node view's ID
                    nodeView.setID(pos);
                    
                    // Adds the node view to the HashMap
                    nodeViewMap.put(pos, nodeView);
                    
                    // Adds instance to the 
                    container.addColonyNodeView(nodeView, x, y);
                }
            }
        }
        
        /**
         * Returns a string representation of item's key
         * 
         * @param row   x coordinate
         * @param col   y coordinate
         * @return      String representation of the item's key
         */
        private String createKey(int row, int col) {
            return row + "," + col;
        }
        
        /**
         * Return a node stored in the HashMap given its position
         * 
         * @param pos   Grid position in "x,y" format
         * @return      NodeView object
         */
        private NodeView getNodeView(String pos) {
            return nodeViewMap.get(pos);
        }
             
        /**
         * Get method to retrieve container object
         * 
         * @return  ColonyView container object
         */
        public ColonyView getContainer() {
            return container;
        }
        
        /**
         * Reveal the node view on GUI
         * 
         * @param pos   Grid position in "x,y" format
         */
        public void showNode(String pos) {
            getNodeView(pos).showNode();
        }
        
        /**
         * Hide the node view on GUI
         * 
         * @param pos   Grid position in "x,y" format
         */
        public void hideNode(String pos) {
            getNodeView(pos).hideNode();
        }
        
        /**
         * Responsible for setting queen presence and displaying icon
         * 
         * @param pos   Grid position in "x,y" format
         * @param q     True if queen present; false otherwise
         */
        public void setQueen(String pos, boolean q) {
            
            // Create reference to NodeView object
            NodeView nv = getNodeView(pos);
            
            // Set presence or absence
            nv.setQueen(q);
            
            // If present display, icon
            if(q)
                nv.showQueenIcon();
            
            // Otherwise, hide icon
            else
                nv.hideQueenIcon();
        }
        
        /**
         * Responsible for setting forager count and displaying icon
         * 
         * @param pos   Grid position in "x,y" format
         * @param num   Number of forager ants
         */
        public void setForager(String pos, int num) {
            
            // Create reference to NodeView object
            NodeView nv = getNodeView(pos);
            
            // Set count
            nv.setForagerCount(num);
            
            // If number > 0, then display icon
            if(num > 0)
                nv.showForagerIcon();
            
            // Otherwise, hide icon
            else
                nv.hideForagerIcon();
        }
        
        /**
         * Responsible for setting scout count and displaying icon
         * 
         * @param pos   Grid position in "x,y" format
         * @param num   Number of scout ants
         */
        public void setScout(String pos, int num) {
            
            // Create reference to NodeView object
            NodeView nv = getNodeView(pos);
            
            // Set count
            nv.setScoutCount(num);
            
            // If number > 0, then display icon
            if(num > 0)
                nv.showScoutIcon();
            
            // Otherwise, hide icon
            else
                nv.hideScoutIcon();
        }
        
        /**
         * Responsible for setting soldier count and displaying icon
         * 
         * @param pos   Grid position in "x,y" format
         * @param num   Number of soldier ants
         */
        public void setSoldier(String pos, int num) {
            
            // Create reference to NodeView object
            NodeView nv = getNodeView(pos);
            
            // Set count
            nv.setSoldierCount(num);
            
            // If number > 0, then display icon
            if(num > 0)
                nv.showSoldierIcon();
            
            // Otherwise, hide icon
            else
                nv.hideSoldierIcon();
        }
        
        /**
         * Responsible for setting bala count and displaying icon
         * 
         * @param pos   Grid position in "x,y" format
         * @param num   Number of bala ants
         */
        public void setBala(String pos, int num) {
            
            // Create reference to NodeView object
            NodeView nv = getNodeView(pos);
            
            // Set bala count
            nv.setBalaCount(num);
            
            // If number > 0, then display icon
            if(num > 0)
                nv.showBalaIcon();
            
            // Otherwise, hide icon
            else
                nv.hideBalaIcon();
        }
        
        /**
         * Set amount of food in node
         * 
         * @param pos   Grid position in "x,y" format
         * @param num   Amount of food available
         */
        public void setFoodAmount(String pos, int num) {
            getNodeView(pos).setFoodAmount(num);
        }
        
        /**
         * Set pheromone level in node
         * 
         * @param pos   Grid position in "x,y" format
         * @param num   Pheromone level present
         */
        public void setPheromoneLevel(String pos, int level) {
            getNodeView(pos).setPheromoneLevel(level);
        }
}
         
        
        
