import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class CCityCalculator {

	ArrayList<ArrayList<Integer>> neighborLists;
	ArrayList<HashMap<Integer,Integer>> roadMaps;
	int totalCNodes;
	
	int bestPathLength;
	
	public CCityCalculator(int totalNodes , ArrayList<ArrayList<Integer>> cnodes , ArrayList<HashMap<Integer,Integer>> cNodeEdgeSize) {
		this.totalCNodes = totalNodes;
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
	
	//Dijkstra's algorithm is used
	
	public ArrayList<String> findMinPath(int startId,int endId) {
		int[] nodeValue  = new int[totalCNodes+1] ; 
		int[] nodeParent = new int[totalCNodes+1] ; 
		
		int currentValue  = 0;
		int currentId  = startId;
		while(true) {
			for(int i : neighborLists.get(currentId)) {
				if(nodeValue[i]== 0 || nodeValue[i] > currentValue + roadMaps.get(currentId).get(i)) {

					
					nodeValue[i] = currentValue + roadMaps.get(currentId).get(i);
					nodeParent[i] = currentId;
					}
			}
			// currentNode is now permanent.
			nodeValue[currentId] = -1;
			currentId = findMinValue(nodeValue);
			
			if(currentId == -1 || currentId == endId) {
				break;
			}
			currentValue = nodeValue[currentId];
		}
		
		if(currentId == -1) {
			return new ArrayList<String>();
		}
		bestPathLength = nodeValue[endId];
		
		ArrayList<String> result = new ArrayList<String>();
		
		int childId = endId;
		while(childId != startId) {
			result.add("c"+childId);
			childId = nodeParent[childId];
		}
		
		result.add("c"+startId);
		Collections.reverse(result);
		
		return result;
	}
	
	public int getBestPathLength() {
		return bestPathLength;
	}
}
