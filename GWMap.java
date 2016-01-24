//Elizabeth Davis HW 5

public class GWMap {
   
   OSMNode[] nodes; //we now want to set the nodes 
   OSMRoad[] roads; //Set the roads into it
   OSMNode[] bounds; //has the bounds
   double lon, lat;
   String[] data;
   String fileName = "";
   GWMap(String fileName) { //The constructor. This will call the OSMParse file
      //We want to take the fileName and compare to the OSM file in directory and create a string
      //array like in our OSM Parse that will have all of the data
      //It will have all of the nodes and roads
      //System.out.println(file[0]);
      data = OSMParse.getOsmData(fileName);
      populateNodes(data);
      populateRoads(data);
      populateBounds(data);
      //System.out.println(file[0]);

   } 
   
   int numNodes() {
      //using the object and the OSMParse method we are using the numNodes method to find the nuber of nodes
      //System.out.println(data[0]);
      int numberNodes = OSMParse.numEntries(data, "node ");
         return numberNodes; 
   }
   
   
   OSMNode getNode(int nth) {
      //Validate it and then return the node at the nth index 
      //Like OSMRoad
      if(nth > nodes.length - 1) { //.length prints number of items
         return null; //null is a special value that means there is no object
      }
      //see if nth is the number or the index 
      return nodes[nth]; //assuming its the index   
   }
  
   int numRoads() {
      return roads.length;
   }
  
  
   OSMRoad getRoad(int nth) {
   
      if(nth > roads.length - 1) {
         return null;
      }

      return roads[nth];
   
   }
   
   OSMNode[] getBounds() {
   
      return bounds;
   } 
   
   OSMRoad[] getRoads() {
      return roads;
   }

   
   void populateNodes(String[] osm) { //get node ids and see if they're traffic signals 
      //create an array of all of the osm nodes
      int numNodes = OSMParse.numEntries(data, "node ");
      nodes = new OSMNode[numNodes];
      long id = 0;
         for(int i = 0; i < numNodes; i++) { 
             int lineOfIdx = OSMParse.nthEntry(data, "node ", i);
             lon = Double.parseDouble(OSMParse.getAttribute(data[lineOfIdx], "lon"));   
             lat = Double.parseDouble(OSMParse.getAttribute(data[lineOfIdx], "lat"));
             id = Long.valueOf(OSMParse.getAttribute(data[lineOfIdx], "id"));
             nodes[i] = new OSMNode(lon, lat, id);   
               if(data[lineOfIdx + 1].indexOf("<tag ") != -1) {
                  if((OSMParse.getAttribute(data[lineOfIdx+1], "k").equals("highway")) && (OSMParse.getAttribute(data[lineOfIdx+1], "v").equals("traffic_signals"))) {
                     nodes[i].setAsSignal(); 
                     //System.out.println("Signal" + id); 
                  }
               } 
         }
    //loop through and get the nodes that loops through numNodes times and get lon lat and id      
    //Then use our getAttribute method for lat and lon into doubles and id into a long
 
    //Look to see if it has a tag "traffic_signal" and highway and tag for the nth entries    
    //Find tag. if getAttribute "k" = highway and getAttribute "v" = traffic signal, then 
   
   } 
   
   
   boolean isRoad(int wayIdx) { //decides if it is a road that matters
         //System.out.println("wayIdx = " + wayIdx);
         boolean isValidRoad = false;
         boolean hasName = false;
        int beginningWay = OSMParse.nthEntry(data, "way ", wayIdx);
        //System.out.println("beginningWay = " + beginningWay);
        int endingWay = OSMParse.nthEntry(data, "/way", wayIdx);
        //System.out.println("endingWay = " + endingWay);
        for(int i = beginningWay; i <= endingWay; i++) {
            if(data[i].indexOf("<tag ") != -1) {
                     if((OSMParse.getAttribute(data[i], "k").equals("highway"))) {
                          if(!OSMParse.getAttribute(data[i], "v").equals("footway") && 
                          !OSMParse.getAttribute(data[i], "v").equals("service") && 
                          !OSMParse.getAttribute(data[i], "v").equals("pedestrian")){ 
                              isValidRoad = true;
                          }    
                     }
                     if((OSMParse.getAttribute(data[i], "k").equals("name"))) {
                        hasName = true;
                     }
            } 
        }   
         return isValidRoad && hasName;
   }
   
   int countRoads() { //returns the number of roads there are that matter
      int numWays = OSMParse.numEntries(data, "way ");
      //System.out.println("numWays = " + numWays);
      int count = 0;
      for(int i = 0; i < numWays; i++ ) {
         if(isRoad(i)) {
            count ++;
         }
      }
         //System.out.println("count = " + count);
         return count;
   } 
   
   String getRoadName(int wayIdx) {
      int beginningWay = OSMParse.nthEntry(data, "way ", wayIdx);
      int endingWay = OSMParse.nthEntry(data, "/way", wayIdx);
      String name = "";
        for(int i = beginningWay; i < endingWay; i++) {
            if(data[i].indexOf("<tag ") != -1) {
               if(OSMParse.getAttribute(data[i], "k").equals("name")){
                  name = OSMParse.getAttribute(data[i], "v");
               }
            }
         }         
         return name;
   }
   
   int countNodesinWay(int wayIdx) {
      int beginningWay = OSMParse.nthEntry(data, "way ", wayIdx);
      int endingWay = OSMParse.nthEntry(data, "/way", wayIdx);
      int countNodes = 0;
        for(int i = beginningWay; i < endingWay; i++) {
            if(data[i+1].indexOf("<nd ") != -1) { 
               countNodes ++;
            }
        }  
        return countNodes;
   }
   
   private OSMNode findNodebyId(long id) {
      //search node array for every time through, calls getId and if matches, then returns node
      for(int i = 0; i < nodes.length; i++) {
         if(nodes[i].getId() == id) {
            return nodes[i];
         }
      }
      return null;
   }    
   
   OSMNode[] nodesInRoad(int wayIdx) {
      int beginningWay = OSMParse.nthEntry(data, "way ", wayIdx);
      int endingWay = OSMParse.nthEntry(data, "/way", wayIdx);
      OSMNode[] n = new OSMNode[countNodesinWay(wayIdx)];
      int nx = 0; //node index
        for(int i = beginningWay; i < endingWay; i++) {
            if(data[i+1].indexOf("<nd ") != -1) { 
               long id = Long.valueOf(OSMParse.getAttribute(data[i+1], "ref"));
               n[nx] = findNodebyId(id);
               nx++;
            }   
        
        }
         return n;
   } 
   
   
   void populateRoads(String[] osm) { //see if a way is a highway and name  
      //looking for <nd and <ref 
      //When we look to see if there is a way, we see how many nodes there are within that road 
      //Each <nd contains an id of the node. So we need to find the object of that node that has a corresponding id
      //We now want to go through that number of ways to see which ones are ones we care about
      roads = new OSMRoad[countRoads()];
      int numWays = OSMParse.numEntries(data, "way ");
      int rx = 0; //road index
      for(int i = 0; i < numWays; i++ ) {
         if(isRoad(i)) {
            String name = getRoadName(i);
            OSMNode[] nodes = nodesInRoad(i);
            roads[rx] = new OSMRoad(name, nodes);   
            rx++;      
         }
       
      } 
    }
    
   void populateBounds(String[] osm) {
      int lineOfBounds = OSMParse.nthEntry(data, "bounds ", 0);
      double maxLon = Double.parseDouble(OSMParse.getAttribute(osm[lineOfBounds], "maxlon"));
      double maxLat = Double.parseDouble(OSMParse.getAttribute(osm[lineOfBounds], "maxlat"));
      double minlon = Double.parseDouble(OSMParse.getAttribute(osm[lineOfBounds], "minlon"));
      double minlat = Double.parseDouble(OSMParse.getAttribute(osm[lineOfBounds], "minlat")); 
      OSMNode minBound = new OSMNode(minlon, minlat, 1);
      OSMNode maxBound = new OSMNode(maxLon, maxLat, 2);
      bounds = new OSMNode[2];
      bounds[1] = minBound;
      bounds[0] = maxBound;
   } 
   

}


