import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class DCityCalculator {
	
	ArrayList<ArrayList<Integer>> neighborLists;
	ArrayList<HashMap<Integer,Integer>> roadMaps;
	int totalDNodes;
	
	public DCityCalculator(int totalNodes , ArrayList<ArrayList<Integer>> cnodes , ArrayList<HashMap<Integer,Integer>> cNodeEdgeSize){
		this.totalDNodes = totalNodes;
		neighborLists = cnodes; 
		roadMaps = cNodeEdgeSize;
	}
	
	public int findMinValue(int[] list) {
		
		int minIndex = -1;
		int minValue = -1;
		for(int i=1;i<list.length; i++) {
			if(list[i] <= 0) {
				continue;
			}
			if(minValue == -1 || minValue > list[i]) {
				minValue = list[i];
				minIndex = i;
			}
		}
		return minIndex;
	}
	
	public int constructTree(int startId) {
		
		HashSet<Integer> outside = new HashSet<Integer>();
		
		for(int i=0; i <= totalDNodes;i++) {
			outside.add(i);
		}
		
		int lastAddedNode = startId;
		outside.remove(startId);
		
		int[] nodeValue = new int[totalDNodes+1];
		int result = 0;
		while(outside.size() > 0) {
			
			
			for(int neighbor:neighborLists.get(lastAddedNode)) {
				//If there is a road between the chosen outsider
				if(nodeValue[neighbor] > -1) {
					//If there is no value of outsider or new road is shorter then the previous value
					if(nodeValue[neighbor] == 0 || nodeValue[neighbor] > roadMaps.get(lastAddedNode).get(neighbor)) {
						nodeValue[neighbor] = roadMaps.get(lastAddedNode).get(neighbor);
					}
				}
			}
			nodeValue[lastAddedNode] = -1;
				
			lastAddedNode = findMinValue(nodeValue);
			
			
			if(lastAddedNode == -1) {
				return -2;
			}
			
			
			
			result += nodeValue[lastAddedNode];
			outside.remove(lastAddedNode);
			
		}
		return result*2;
	}
}
