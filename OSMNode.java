//Elizabeth Davis HW 5

public class OSMNode {

   private double lat;
   private double lon;
   private long identifier; //long is another data storage
   private boolean trafficSignal = false;

   OSMNode(long id) {
      identifier = id;
      double lontitude = 0.0;
      double latitude = 0.0;
   }
      
   OSMNode(double longitude, double latitude, long id) {
      //assigning the value the parameter to the variable   
      lon = longitude;
      lat = latitude;      
      identifier = id; 
   }
   
   boolean equals(OSMNode n) {
      //got to check each thing
      /*
      if(this.identifier != n.getId()) {
         return false;
      }
      //this is something that makes sure we are referring to the current private variables of that object
      if(this.lat != n.getLat()){
         return false;
      }
      
      if(this.lon != n.getLon()) {
         return false;
      }
      
      return(trafficSignal == n.trafficSignal);  */
      
      if(this.identifier == n.getId()) {
         return true;
      }  
      
      return false; 
   }
   
   double getLon() {
      return lon;
   }
   
   double getLat() {
      return lat;
   }
   
   long getId() {
      return identifier;
   }
   
   boolean isSignal() {
      return trafficSignal;
   }
   
   void setAsSignal() {
      trafficSignal = true;
   
   } 

}