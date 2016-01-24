//Elizabeth Davis HW 7

import java.lang.Math;

public class GWDirections {
   
   GWMap map;

   public GWDirections(GWMap m) { //Every constructor initializes the object
      //Saving the map as part of the GWDirections object
      this.map = m; 
      
   }
   
   public GWMap getMap() {
      return map;
   }
   
   private static double cosineValue(OSMNode a, OSMNode b) { //Returns the distance in miles
      double latA = a.getLat();
      double latB = b.getLat();
      double avgLat = (latA + latB) / 2;    
      avgLat = Math.toRadians(avgLat);
      double cosine = Math.cos(avgLat);    
      
      return cosine; 
   }

   
   public static double distance(OSMNode a, OSMNode b) { //Returns the distance in miles
      double cosine = cosineValue(a, b);
      //System.out.println("cosine = " + cosine);
      double distance = 0;
      double latA = a.getLat();
      //System.out.println("latA = " + latA);
      double lonA = a.getLon();
      //System.out.println("lonA = " + lonA);
      double latB = b.getLat();
      //System.out.println("latB = " + latB);
      double lonB = b.getLon();
      //System.out.println("lonB = " + lonB);
      double distanceLat = (latA - latB) * 69.172; //Number of miles vertically 
      double distanceLon = (lonA - lonB) * 69.172 * cosine;
      distance = Math.sqrt((distanceLat * distanceLat) + (distanceLon * distanceLon));
      
      return distance;
         
   }
   
   public static OSMNode closerNode(OSMNode a, OSMNode b, OSMNode dest) {
      double distA = distance(a, dest);
      double distB = distance(b, dest);
      if(distA < distB) {
         return a;
      }
      return b;
   }
   
   public double totalDistance(OSMNode[] ns) { 
      double tDist = 0;
      double length = ns.length;
      for(int i = 0; i < length - 1; i++) {
         tDist += distance(ns[i], ns[i+1]);
         //System.out.println("tDist = " + tDist);
      }
      return tDist;
   }
      
   private boolean isInRoad(OSMRoad r, OSMNode n) {
      int numNodes = r.getNumNodes();
      for(int i = 0; i < numNodes; i++ ) {
         OSMNode node = r.getNode(i);
         if(node == n) {
            return true;
         }
      }   
      return false;
   } 
   
   private int getNumNodesRoads(OSMNode n) {
    //Go through roads[] and find if roads[i] == n save it and return an array of those roads
      //Make an array of the roads
      OSMRoad[] roads = map.getRoads();
      int numRoads = roads.length;
      int countRoads = 0;
      for(int i = 0; i < numRoads; i++) {
         if(isInRoad(roads[i], n)) {
            countRoads ++;
         }
      }
        return countRoads;            
   } 
   
   public OSMRoad[] getNodesRoads(OSMNode n) {
      //Go through roads[] and find if roads[i] == n save it and return an array of those roads
      //Make an array of the roads
      OSMRoad[] roads = map.getRoads();
      //For each road go through the different nodes to see if node n is there
      int numRoads = roads.length; //total roads
      int numberofRoads = getNumNodesRoads(n); //number of roads that matter
      //System.out.println("numberofRoads = " + numberofRoads);
      OSMRoad[] nodesRoads = new OSMRoad[numberofRoads]; //For the new OSMRoads[]
      int countRoads = 0;
      for(int i = 0; i < numRoads; i++) {
         if(isInRoad(roads[i], n)) {
             nodesRoads[countRoads] = roads[i];
             countRoads ++;
         }
      }
      return nodesRoads;
   }

   
   public OSMNode[] getAdjacentNodes(OSMRoad r, OSMNode n) {
      //System.out.println(n.getId());
      //System.out.println(r.getName());
      //First we need to make sure the node is IN the road 
      if(!isInRoad(r, n)) {
         return null;
      } 
      //Find which index the desired node is at
      OSMNode[] surroundingNodes = new OSMNode[2];
      OSMNode[] allNodes = new OSMNode[r.getNumNodes()];
      int nodeN = 0;
      for(int i = 0; i < r.getNumNodes(); i++) {
         allNodes[i] = r.getNode(i);
         if(allNodes[i] == n) {
            nodeN = i;
         }
      }
      //Look for the node before it and the node after it
      if(nodeN == 0) {
         surroundingNodes[0] = null;
      }
      else {
         surroundingNodes[0] = allNodes[nodeN - 1];
      }
      if(nodeN == r.getNumNodes() - 1) {
         surroundingNodes[1] = null;
      }
      else {
         surroundingNodes[1] = allNodes[nodeN + 1];
      }
      return surroundingNodes;
   }
   
   private OSMNode nextNode(OSMNode a, OSMNode b) {
      OSMRoad[] roads = getNodesRoads(a);
      int numRoads = roads.length;
      OSMNode[] nextNodes = new OSMNode[numRoads];
      for(int i = 0; i < numRoads; i++) { //Looking for each node
         OSMNode[] adjacent = getAdjacentNodes(roads[i], a);
         OSMNode one = adjacent[0];
         OSMNode two = adjacent[1];
         if(one == null) {
            nextNodes[i] = two;
         }
         else if(two == null) {
            nextNodes[i] = one;
         }
         else{
            nextNodes[i] = closerNode(one, two, b);
         }
      }
      OSMNode next = nextNodes[0];
      for(int j = 0; j < nextNodes.length - 1; j++) {
         next = closerNode(next, nextNodes[j + 1], b); 
      }
         return next;
   }
   
   private int numNodesinDirections(OSMNode a, OSMNode b) {
      int countNodes = 1; 
      OSMNode next = a;
      while(next != b) {
         next = nextNode(next, b);
         countNodes++;  
      }
      return countNodes;
   }
   
   public OSMNode[] getDirections(OSMNode a, OSMNode b) {
      int numNodes = numNodesinDirections(a,b);
      OSMNode[] directions = new OSMNode[numNodes]; 
      directions[0] = a;
      directions[numNodes - 1] = b;
      for(int i = 1; i < numNodes; i++) {
         directions[i] = nextNode(directions[i-1],b);
      }
      return directions;
   }

}