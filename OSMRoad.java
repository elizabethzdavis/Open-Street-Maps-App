//Elizabeth Davis HW 5

public class OSMRoad {//includes all of the road specific data

   private String roadName; // the name of the road
   private OSMNode[] nodes; //Contains the nodes that constitute the road
   //you don't need n because you will be given a value for n
   //public OSMRoad r; //I think this is for the current road
   
   OSMRoad(String name, OSMNode[] nodes) {
      roadName = name;
      this.nodes = nodes; //has to referring to private variable by saying this
   }
   
   OSMNode getNode(int nth) {
      if(nth > nodes.length - 1) { //.length prints number of items
         return null; //null is a special value that means there is no object
      }
      //see if nth is the number or the index 
      return nodes[nth];//assuming its the index
   }

   int getNumNodes() {
      return nodes.length;
   }
   
   String getName() {
      return roadName;
   }
   
   OSMNode intersects(OSMRoad r) {  //comparing r to nodes. They are both objects of the same class
      for(int i = 0; i < this.nodes.length; i++ ) {
         for(int j = 0; j < r.nodes.length; j++) {
              if(this.nodes[i].equals(r.nodes[j])) {
                  return this.nodes[i];
              }
         }
      }
      return null;
   }
}