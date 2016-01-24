public class GWDirectionsMain {
	// We're counting "close enough" as equal here
	public static boolean doubleIsEqual(double a, double b) {
		final double threshold = 0.000000001;
		if (Math.abs(a - b) < threshold) return true;
		return false;
	}

	public static void testDistance(GWDirections dir) {
		OSMNode a, b, c, d;

		a = new OSMNode(-77.0488, 38.896, 12345); //lon, lat, id
         //System.out.println("A = " + a);
		b = new OSMNode(-77.0588, 38.896, 23456);
         //System.out.println("B = " + b);
		c = new OSMNode(-77.0588, 38.886, 34678);
         //System.out.println("C = " + c);
		d = new OSMNode(-77.0488, 38.886, 45768);
         //System.out.println("D = " + d);

		assert(doubleIsEqual(GWDirections.distance(a, b), 0.5383566744550649));
            //System.out.println(GWDirections.distance(a, b));
            //System.out.println("");

		assert(doubleIsEqual(GWDirections.distance(a, d), 0.6917199999998623));
            //System.out.println(GWDirections.distance(a, d));
            //System.out.println("");
            
		assert(doubleIsEqual(GWDirections.distance(a, c), 0.8765530658695381));
            //System.out.println(GWDirections.distance(a, c));
            //System.out.println("");


		assert(GWDirections.closerNode(a, d, c) == d);
         //System.out.println(GWDirections.closerNode(a, d, c)); //Should be d
		assert(GWDirections.closerNode(b, d, a) == b);
         //System.out.println(GWDirections.closerNode(b, d, a)); //Should be b
      

		assert(doubleIsEqual(dir.totalDistance(new OSMNode[]{a, b, c, d}), 1.768509146821097));
            /*double one = GWDirections.distance(a, b);
            System.out.println(one);
            double two = GWDirections.distance(b, c);
            System.out.println(one + two);
            double three = GWDirections.distance(c, d);            
            System.out.println(one + two + three);
            System.out.println(GWDirections.totalDistance(new OSMNode[]{a, b, c, d})); */          
		assert(doubleIsEqual(dir.totalDistance(new OSMNode[]{a, c, b, d}), 2.4448261317389384)); 
	}

	public static void testRoadAdjacency(GWDirections d, long nodeId, String[] rNames, long[][] nIds) {
		OSMRoad[] roads;
		OSMNode[] nodes;
		OSMNode n;
		GWMap m = d.getMap();
		
		n = new OSMNode(0);
		for (int i = 0 ; i < m.numNodes() ; i++) {
			if (m.getNode(i).getId() == nodeId) {
				n = m.getNode(i);
			}
		}
		assert(n.getId() != 0);

		roads = d.getNodesRoads(n);
		if (roads == null) return;
		for (int i = 0 ; i < roads.length ; i++) {
			assert(rNames[i].equals(roads[i].getName()));
         //System.out.println("roads[i] = " + roads[i]);
			nodes = d.getAdjacentNodes(roads[i], n);

			for (int j = 0 ; j < 2 ; j++) {
				long id;
				if (nodes[j] != null) {
					id = nodes[j].getId();
               //System.out.println("id = " + id);
               
				} else {
					id = 0;
               //System.out.println("id = " + id);
       
				}
            //System.out.println("nIds[i][j] = " + nIds[i][j]);
				assert(id == nIds[i][j]);
			}
		}
	} 

	public static void testDirections(String fileName, long aId, long bId, long[] pathIds, double distance) {
		GWMap m = new GWMap(fileName);
		GWDirections d = new GWDirections(m);
		OSMNode a, b;
		OSMNode[] nodes;

		b = a = m.getNode(0);
		for (int i = 0 ; i < m.numNodes() ; i++) {
			OSMNode n = m.getNode(i);

			if (n.getId() == aId) {
				a = n;
			}
			if (n.getId() == bId) {
				b = n;
			}
		}

		nodes = d.getDirections(a, b);
		assert(nodes.length == pathIds.length);
      //System.out.println(nodes.length);
      //System.out.println(pathIds.length);
		for (int i = 0 ; i < nodes.length ; i++) {
         //System.out.println("nodes is " + nodes[i].getId());
         //System.out.println("paths is " + pathIds[i]);
			assert(nodes[i].getId() == pathIds[i]);
		}
		assert(doubleIsEqual(d.totalDistance(nodes), distance));
	}




	public static void main(String[] args) {
		GWMap m = new GWMap("OSMTest.osm");
		GWDirections d = new GWDirections(m);
      OSMNode nodeTester = m.getNode(6);
      d.getNodesRoads(nodeTester);
      
		long [] nodes = {49741710L, 49741717L, 49777696L};
		String[][] nodesRoads = {{"21st Street Northwest"}, 
					 {"21st Street Northwest"}, 
					 {"22nd Street Northwest", "H Street Northwest"}};
		long [][][] adjNodeIds = {
			{{3099885755L, 633923936L}},
			{{0L, 49741715L}},
			{{2451171135L,291675370L}, {291675380L, 49777690L}}};


		testDistance(d);

		for (int i = 0 ; i < nodesRoads.length ; i++) {
			testRoadAdjacency(d, nodes[i], nodesRoads[i], adjNodeIds[i]);
		} 

		testDirections("OSMTest.osm", 49799834L, 1443727088L, 
			       new long[]{49799834L, 49762264L, 804599379L, 291675382L, 2451171135L, 49777696L, 291675370L, 1443727088L}, 0.2308672887300257);
		testDirections("OSMTest.osm", 49799834L, 2383566163L, 
			       new long[]{49799834L, 49762264L, 804599379L, 291675382L, 2451171135L, 49777696L, 49777690L, 49741695L, 2383566163L}, 0.3208488500693274);
		testDirections("SEASatGWU.osm", 49799834L, 1443727088L, 
			       new long[]{49799834L, 49762264L, 804599379L, 291675382L, 2451171135L, 49777696L, 291675370L, 1443727088L}, 0.2308672887300257);
		testDirections("SEASatGWU.osm", 49799834L, 2383566163L, 
			       new long[]{49799834L, 49762264L, 1381307996L, 291675253L, 49741695L, 2383566163L}, 0.3218549525319201);
		testDirections("SEASatGWU.osm", 49762184L, 1381210497L, 
			       new long[]{49762184L, 641127623L, 641127622L, 641127621L, 49762331L, 49762286L, 49777712L, 49793670L, 1443727088L, 291675365L, 49793629L, 49741703L, 1381210497L}, 0.5040507030491289);

   }
}