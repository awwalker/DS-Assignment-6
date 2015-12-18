import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * CollisionList class stores a list of collisions. The organization of this list is
 * based on the zip code associated with a given collision. This organization simplifies 
 * processing of collisions that occur within a particular zip code. 
 * @author Joanna K.
 */

public class CollisionList {
	//change to hashmap/treeset?
	//try just priority queues
	//zip is the key and then the object for that zip is the ziplist object
	
	//private ArrayList<ZipCodeList> list;
	private HashMap< String , ZipCodeList > list;
	/**
	 * Creates an empty CollisionList object. 
	 */
	public CollisionList() {
		//list = new ArrayList<ZipCodeList> ();
		list = new HashMap< String, ZipCodeList >();
	}
	
	
	/**
	 * Adds a particular record to this CollisionList object.
	 * The record should consist of 21 string entries in the following order:
	 * date
	 * time
	 * borough
	 * zip
	 * lattitude^
	 * longitude ^
	 * on street name^
	 * cross street name ^
	 * personsInjured
	 * personsKilled
	 * pedestriansInjured
	 * pedestriansKilled
	 * cyclistsInjured
	 * cyclistsKilled
	 * motoristsInjured
	 * motoristsKilled
	 * contributing factor vehicle 1^
	 * contributing factor vehicle 2^
	 * uniqueKey
	 * vehicleCode1
	 * vehicleCode2
	 * The entries indicated with ^ are not used. 
	 * 
	 * @param record an list of string describing a particular collision (see above
	 * for order of entries) 
	 * @return true if the record was added to this CollisionList object, false if any 
	 * problem occurred and the record was not added 
	 */
	public boolean add ( ArrayList<String> record ) {
		
		try{
			Collision col = new Collision(record);
			ZipCodeList tmp = new ZipCodeList(col);
			String zip = col.getZip();
			if( list.containsKey(zip) ){
				list.put(zip, list.get(zip).add(col));
			}
			else{
				list.put(zip, tmp);
			}
		}
		catch(IllegalArgumentException ex){
			return false;
			
		}

		return true; //return true to indicate that the object was added
		
	}
	
	/**
	 * Determines k zip codes with most collisions in this CollisionList object. 
	 * @param k number of zip codes with the highest number of collisions
	 * @return a string formatted as 
	 *     zip  numOfCollisions
	 *  one per line, that contains k zip codes with the highest number of collisions
	 */
	public String getZipCodesWithMostCollisions (int k) {

		PriorityQueue<ZipCodeList> queue = 
				new PriorityQueue<ZipCodeList>(100, new CompareByNumOfCollisionsDescending() );
		
		for(ZipCodeList zipList : list.values() ){
			queue.add(zipList);
		}
		
		StringBuffer result = new StringBuffer();
		
		int count = 0;
		
		for (int i = 0; i < k && i < queue.size() && count < queue.size(); i++ ) {
			
			ZipCodeList current = queue.peek();
			int numCollisions = current.getTotalNumOfCollisions();
			String zip = current.getZip();
			do{
				queue.poll();
				result.append(String.format("    %5s  %5d collisions\n", zip,
					numCollisions));
				count++;
			}
			while( count + 1 < queue.size() && numCollisions == queue.peek().getTotalNumOfCollisions() );
		}
		return result.toString();
	}
	
	/**
	 * Determines k zip codes with least collisions in this CollisionList object. 
	 * @param k number of zip codes with the lowest number of collisions
	 * @return a string formatted as 
	 *     zip  numOfCollisions
	 *  one per line, that contains k zip codes with the lowest number of collisions
	 */
	public String getZipCodesWithLeastCollisions (int k) {

		CompareByNumOfCollisionsAscending comp = new CompareByNumOfCollisionsAscending();
		PriorityQueue<ZipCodeList> queue = 
				new PriorityQueue<ZipCodeList>(100, comp ) ;
		
		for( ZipCodeList zip : list.values() ){
			queue.add(zip);
		}
		
		StringBuffer result = new StringBuffer();
		
		
		int count = 0;
		
		for (int i = 0; i < k && i < queue.size() && count < queue.size(); i++ ) {
			ZipCodeList current = queue.peek();
			String zip = current.getZip();
			int numCollisions = current.getTotalNumOfCollisions();
			
			do{
				queue.poll();
				result.append(String.format("    %5s  %5d collisions\n", zip,
					numCollisions));
				count++;
			}
			while( count + 1 < queue.size() && numCollisions == queue.peek().getTotalNumOfCollisions());
		}

		return result.toString();
	}
	

	
	/**
	 * Determines k zip codes with most number of collisions involving 
	 * cyclists in this CollisionList object. 
	 * @param k number of zip codes with the highest number of collisions that involved cyclists 
	 * @return a string formatted as 
	 *     zip  numOfCycliststHurt  (numOfCyclists killed) 
	 *  one per line, that contains k zip codes with the highest number of injured cyclists 
	 */
	public String getZipCodesWithMostCyclistIncidents ( int k ) {
/*
		@SuppressWarnings("unchecked")
		ArrayList<ZipCodeList> sortedList = (ArrayList<ZipCodeList>) list.clone();
		//sort the list 
		
		Collections.sort(sortedList, comp );
*/
		CompareByNumOfCyclistsIncidentsDescending comp = new CompareByNumOfCyclistsIncidentsDescending() ;
		PriorityQueue<ZipCodeList> queue = 
				new PriorityQueue<ZipCodeList>(100, comp );
		
		for(ZipCodeList zip : list.values() ){
			queue.add(zip);
		}
		
		StringBuffer result = new StringBuffer();
		
		int inj = 0, killed = 0;
		int count = 0;
		
		for (int i = 0; i < k && i < queue.size() && count< queue.size(); i++ ) {
			ZipCodeList current = queue.peek();
			String zip = current.getZip();
			killed = current.getTotalNumOfCyclistsKilled();
			inj = current.getTotalNumOfCyclistsInjured();
			do { 
				queue.poll();
				result.append( String.format("    %5s  %5d (%3d killed ) cyclists hurt\n", zip,
						inj + killed, killed ));
					count++;
			}
			while ( count+1 < queue.size() 
					&& 0 == comp.compare(current, queue.peek() ) ) ;
		}
		
		return result.toString();
	}
	
	
	/**
	 * Determines k zip codes with most number of injured and killed persons. 
	 * @param k number of zip codes with the highest number of injured and killed persons
	 * @return a string formatted as 
	 *     zip  numOfPersonsHurt  (numOfPersons killed) 
	 *  one per line, that contains k zip codes with the highest number of injured persons 
	 */
	public String getZipCodesWithMostPersonIncidents ( int k ) {
		/*
		@SuppressWarnings("unchecked")
		ArrayList<ZipCodeList> sortedList = (ArrayList<ZipCodeList>) list.clone();
		//sort the list 

		Collections.sort(sortedList, comp );
		*/
		CompareByNumOfPersonsIncidentsDescending comp = new CompareByNumOfPersonsIncidentsDescending() ;
		PriorityQueue<ZipCodeList> queue = new PriorityQueue<ZipCodeList>(100, comp) ;
		StringBuffer result = new StringBuffer();
		
		int inj = 0, killed = 0;
		int count = 0;
		
		for (int i = 0; i < k && i < queue.size() && count< queue.size(); i++ ) {
			ZipCodeList current = queue.peek();
			String zip = current.getZip();
			inj = current.getTotalNumOfPersonsInjured();
			killed = current.getTotalNumOfPersonsKilled();
			do { 
				queue.poll();
				result.append( String.format("    %5s  %5d (%3d killed ) persons hurt\n", zip,
						inj + killed, killed ));
					count++;
			}
			while ( count+1 < queue.size() 
					&& 0 == comp.compare(current, queue.peek() ) ) ;
		}
		
		return result.toString();
	}
	

	/**
	 * Computes percentage of total collisions in this CollisionList object that involved one
	 * of the following vehicle types: taxi, bus, bicycle, truck, fire truck and ambulance. 
	 * @return a string containing the results of the computation 
	 */
	public String getVehicleTypeStats ( ) {
		String result = new String();
		
		int taxi = 0;
		int bus = 0;
		int bicycle = 0;
		int fireTruck = 0;
		int ambulance = 0;
		
		int totalNumOfCollisions = 0;
		
		for (ZipCodeList l : list.values() ) {
			totalNumOfCollisions += l.getTotalNumOfCollisions(); 
			for ( Collision c : l ) { 
				if (c.getVehicleCode1().equalsIgnoreCase("taxi") || 
						c.getVehicleCode2().equalsIgnoreCase("taxi")) taxi++;
				if (c.getVehicleCode1().equalsIgnoreCase("bus") ||
						c.getVehicleCode2().equalsIgnoreCase("bus")) bus++;
				if (c.getVehicleCode1().equalsIgnoreCase("bicycle") ||
						c.getVehicleCode2().equalsIgnoreCase("bicycle")) bicycle++;
				if (c.getVehicleCode1().equalsIgnoreCase("fire truck") ||
						c.getVehicleCode2().equalsIgnoreCase("fire truck")) fireTruck++;
				if (c.getVehicleCode1().equalsIgnoreCase("ambulance") ||
						c.getVehicleCode2().equalsIgnoreCase("ambulance")) ambulance++;
			}
		}

		//create a string object with results
		result += String.format("    %-11s %5.2f%%\n", "taxi", (float)(taxi)/totalNumOfCollisions*100);
		result += String.format("    %-11s %5.2f%%\n", "bus", (float)(bus)/totalNumOfCollisions*100);
		result += String.format("    %-11s %5.2f%%\n", "bicycle", (float)(bicycle)/totalNumOfCollisions*100);
		result += String.format("    %-11s %5.2f%%\n", "fire truck", (float)(fireTruck)/totalNumOfCollisions*100);
		result += String.format("    %-11s %5.2f%%\n", "ambulance", (float)(ambulance)/totalNumOfCollisions*100);
		
		return result;
	}
	
	/**
	 * Computes percentage of total collisions in this CollisionList object that occured within 
	 * a particular hour. The collisions are placed into bins of 1 hour intervals.  
	 * @return a string containing the results of the computation 
	 */
	public String getHourlyStats ( ) { 
		StringBuffer result = new StringBuffer() ;
		
		//counter for each hour
		int [] hourlyCount = new int [24]; 
		
		String hour = "", time = ""; 
		StringBuffer bar; 
		int totalNumOfCollisions = 0; 
		
		for (ZipCodeList l : list.values() ) {
			totalNumOfCollisions += l.getTotalNumOfCollisions(); 
			for ( Collision c : l ) { 
				try { 
					//extract the hour from the time entry 
					time = c.getTime();
					hour = time.substring(0,time.indexOf(':')).trim();
					//increment counter for that hour
					hourlyCount[Integer.parseInt(hour)]++;
				} catch (IndexOutOfBoundsException e) {
					//ignore incorrectly formed times 
				} catch (NumberFormatException e ) {
					//ignore incorrectly formed times 
				}
			}
		}
		
		for (int i = 0; i < 24; i++ ) {
			//determine number of "bars" to be printed for visual representation of 
			//the histogram 
			int numOfBars = (int)(((double)hourlyCount[i]/totalNumOfCollisions) * 240);
			bar = new StringBuffer(numOfBars);
			for (int j = 0; j < numOfBars; j++)
				bar.append("|");
			result.append(String.format("%3d h  %5.1f%% %s%n", 
					i, 100.0*hourlyCount[i]/totalNumOfCollisions, bar.toString() ));
		}
		
		return result.toString();
	}
	
}


/*
 * Comparator class for comparing two @see ZipCodeList objects based on the
 * number of collisions occurring in each. The resulting order is ascending. 
 * @author Joanna K. 
 *
 */
class CompareByNumOfCollisionsAscending implements Comparator <ZipCodeList> {

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(ZipCodeList arg0, ZipCodeList arg1) {
		return arg0.getTotalNumOfCollisions() - arg1.getTotalNumOfCollisions();
	}
	
}


/*
 * Comparator class for comparing two @see ZipCodeList objects based on the
 * number of collisions occurring in each. The resulting order is descending. 
 * @author Joanna K. 
 *
 */
class CompareByNumOfCollisionsDescending implements Comparator <ZipCodeList> {

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(ZipCodeList arg0, ZipCodeList arg1) {
		return arg1.getTotalNumOfCollisions() - arg0.getTotalNumOfCollisions();
	}
	
}

/*
 * Comparator class for comparing two @see ZipCodeList objects based on the
 * number of injured persons. The resulting order is descending. Ties are resolved
 * based on the number of killed persons. 
 * @author Joanna K. 
 *
 */
class CompareByNumOfPersonsIncidentsDescending implements Comparator <ZipCodeList> {

	@Override
	public int compare(ZipCodeList arg0, ZipCodeList arg1) {
		int diff = ( arg1.getTotalNumOfPersonsInjured() + arg1.getTotalNumOfPersonsKilled()) 
				- ( arg0.getTotalNumOfPersonsInjured() + arg0.getTotalNumOfPersonsKilled()) ; 

		if (diff != 0 ) 
			return diff;
		else return ( arg1.getTotalNumOfPersonsKilled() - arg0.getTotalNumOfPersonsKilled() );
	}
	
}

/*
 * Comparator class for comparing two @see ZipCodeList objects based on the
 * number of injured persons. The resulting order is ascending. Ties are resolved
 * based on the number of killed persons. 
 * @author Joanna K. 
 *
 */
class CompareByNumOfPersonsIncidentsAscending implements Comparator <ZipCodeList> {

	@Override
	public int compare(ZipCodeList arg0, ZipCodeList arg1) {
		int diff = - ( arg1.getTotalNumOfPersonsInjured() + arg1.getTotalNumOfPersonsKilled()) 
				+ ( arg0.getTotalNumOfPersonsInjured() + arg0.getTotalNumOfPersonsKilled()) ; 

		if (diff != 0 ) 
			return diff;
		else return ( -arg1.getTotalNumOfPersonsKilled() + arg0.getTotalNumOfPersonsKilled() );
	}
	
}

/*
 * Comparator class for comparing two @see ZipCodeList objects based on the
 * number of injured cyclists. The resulting order is descending. Ties are resolved
 * based on the number of killed cyclists. 
 * @author Joanna K. 
 *
 */
class CompareByNumOfCyclistsIncidentsDescending implements Comparator <ZipCodeList> {

	@Override
	public int compare(ZipCodeList arg0, ZipCodeList arg1) {
		int diff = ( arg1.getTotalNumOfCyclistsInjured() + arg1.getTotalNumOfCyclistsKilled()) 
				- ( arg0.getTotalNumOfCyclistsInjured() + arg0.getTotalNumOfCyclistsKilled()) ; 

		if (diff != 0 ) 
			return diff;
		else return ( arg1.getTotalNumOfCyclistsKilled() - arg0.getTotalNumOfCyclistsKilled() );
	}
	
}

/*
 * Comparator class for comparing two @see ZipCodeList objects based on the
 * number of injured cyclists. The resulting order is ascending. Ties are resolved
 * based on the number of killed cyclists. 
 * @author Joanna K. 
 *
 */
class CompareByNumOfCyclistsIncidentsAscending implements Comparator <ZipCodeList> {

	@Override
	public int compare(ZipCodeList arg0, ZipCodeList arg1) {
		int diff = - ( arg1.getTotalNumOfCyclistsInjured() + arg1.getTotalNumOfCyclistsKilled()) 
				+ ( arg0.getTotalNumOfCyclistsInjured() + arg0.getTotalNumOfCyclistsKilled()) ; 

		if (diff != 0 ) 
			return diff;
		else return ( -arg1.getTotalNumOfCyclistsKilled() + arg0.getTotalNumOfCyclistsKilled() );
	}
	
}