import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.Locale;
import java.util.HashMap;

public class project3main {

	public static void main(String[] args) {
		
		File inFile = new File(args[0]);
		File outFile = new File(args[1]);
		
		
		Scanner myReader;
		try {
			myReader = new Scanner(inFile).useLocale(Locale.US);
		}catch (FileNotFoundException e) {
			System.out.println("Cannot find input file");
			return;
		}
		
		PrintStream outstream;
		try {
			outFile.createNewFile();
			
			outstream = new PrintStream(outFile);
		}catch (IOException e) {
		    e.printStackTrace();
		    myReader.close();
		    return;
		}
		
		//Reading input data
		
		
		int timeLimit = myReader.nextInt();
		int nodeCount = myReader.nextInt();
		String startNodeName = myReader.next();
		String endNodeName = myReader.next();
		int cNodeCount = Integer.valueOf(endNodeName.substring(1));
		int dNodeCount = nodeCount - cNodeCount; //This value is without leyla's node
		
		
		//To store road values adjacency lists are used due to sparse graphs in inputs.
		ArrayList<ArrayList<Integer>> cNodes = new ArrayList<ArrayList<Integer>>(); // These lists store neighborIds of nodes
		ArrayList<ArrayList<Integer>> dNodes = new ArrayList<ArrayList<Integer>>();
		
		//These hashMaps store road values of road lengths 
		ArrayList<HashMap<Integer,Integer>> cNodeEdgeSize = new ArrayList<HashMap<Integer,Integer>>(); 
		ArrayList<HashMap<Integer,Integer>> dNodeEdgeSize = new ArrayList<HashMap<Integer,Integer>>();
		
		for(int i = 0; i <= cNodeCount;i++) {
			cNodes.add(new ArrayList<Integer>());
		}
		
		for(int i = 0; i <= dNodeCount;i++) {
			dNodes.add(new ArrayList<Integer>());
		}
		
		for(int i = 0; i <= cNodeCount;i++) {
			cNodeEdgeSize.add(new HashMap<Integer,Integer>());
		}
		
		for(int i = 0; i <= dNodeCount;i++) {
			dNodeEdgeSize.add(new HashMap<Integer,Integer>());
		}
		
		myReader.nextLine();
		
		//Building cNodes matrix
		//0th index in cNodes is empty so we start from 1
		for(int i=1;i < cNodeCount;i++) {
			
			String nextLine = myReader.nextLine();
			String[] data = nextLine.split(" ");
			
			String nodeStr = data[0];
			int cNodeId = Integer.valueOf(nodeStr.substring(1));
			
			int totalRoads = (data.length -1 ) / 2;
			
			for(int j=0; j<totalRoads;j++) {
				
				String nodeStr2 = data[2*j+1];
				int cNodeId2 = Integer.valueOf(nodeStr2.substring(1));
				
				if(cNodeId == cNodeId2) {
					continue;
				}
				
				cNodes.get(cNodeId).add(cNodeId2);
				cNodeEdgeSize.get(cNodeId).put(cNodeId2,Integer.valueOf(data[2*j+2]));
			}
		}
		
		//This part is for leyla's node.Leyla's node will have an index at cNodes but also 0th index in dNodes
		String leylaLine = myReader.nextLine();
		String[] leylaData = leylaLine.split(" ");
		
		int leylaRoads = (leylaData.length -1 ) / 2;
			
		for(int j=0; j<leylaRoads;j++) {
			
			String leylaNeighbourStr = leylaData[2*j+1];
			int leylaNeighbourId = Integer.valueOf(leylaNeighbourStr.substring(1));
			
			if(leylaNeighbourId == 0) {
				continue;
			}
			
			if(leylaNeighbourStr.substring(0,1).equals("d")) {
				if(dNodes.get(0).contains(leylaNeighbourId) == false)  {
					dNodes.get(0).add(leylaNeighbourId);  
					dNodes.get(leylaNeighbourId).add(0);
					dNodeEdgeSize.get(0).put(leylaNeighbourId,Integer.valueOf(leylaData[2*j+2]));
					dNodeEdgeSize.get(leylaNeighbourId).put(0,Integer.valueOf(leylaData[2*j+2]));
					continue;
				}
				if(dNodeEdgeSize.get(0).get(leylaNeighbourId) > Integer.valueOf(leylaData[2*j+2])) {
					dNodeEdgeSize.get(0).put(leylaNeighbourId,Integer.valueOf(leylaData[2*j+2]));
					dNodeEdgeSize.get(leylaNeighbourId).put(0,Integer.valueOf(leylaData[2*j+2]));
				}
			}
		}
		
		//Building dNodes matrix
		for(int i=1;i <= dNodeCount;i++) {
			
			String nextLine = myReader.nextLine();
			String[] data = nextLine.split(" ");
			
			String nodeStr = data[0];
			int dNodeId = Integer.valueOf(nodeStr.substring(1));
				
			int totalRoads = (data.length -1 ) / 2;
			
			for(int j=0; j<totalRoads;j++) {
				String nodeStr2 = data[2*j+1];
				int dNodeId2 = Integer.valueOf(nodeStr2.substring(1));
				
				if(dNodeId == dNodeId2) {
					continue;
				}
				
				// In d nodes the same edge between the same nodes may have different values.
				// So we check if it was assigned a value before each time
				if(dNodes.get(dNodeId).contains(dNodeId2) == false) {
					dNodes.get(dNodeId).add(dNodeId2);
					dNodes.get(dNodeId2).add(dNodeId);
					dNodeEdgeSize.get(dNodeId).put(dNodeId2, Integer.valueOf(data[2*j+2]));
					dNodeEdgeSize.get(dNodeId2).put(dNodeId, Integer.valueOf(data[2*j+2]));
				}
				else {
					if(dNodeEdgeSize.get(dNodeId).get(dNodeId2) > Integer.valueOf(data[2*j+2])) {
						dNodeEdgeSize.get(dNodeId).put(dNodeId2, Integer.valueOf(data[2*j+2]));
						dNodeEdgeSize.get(dNodeId2).put(dNodeId, Integer.valueOf(data[2*j+2]));
					}
				}
			}
		}
		
		CCityCalculator ccc = new CCityCalculator(cNodeCount,cNodes,cNodeEdgeSize);
		DCityCalculator dcc = new DCityCalculator(dNodeCount,dNodes,dNodeEdgeSize);
		
		int startId = Integer.valueOf(startNodeName.substring(1));
		int endId = cNodeCount;
		
		ArrayList<String> part1 = ccc.findMinPath(startId, endId);
		
		int part2 = dcc.constructTree(0);
		
		int bestPathLength = ccc.getBestPathLength();
		
		if(part1.isEmpty() == false) {
			if(timeLimit < bestPathLength) {
				for(String nodeStr:part1) {
					outstream.print(nodeStr+" ");
				}
				outstream.println();
				outstream.print("-1");
			}
			if(timeLimit >= bestPathLength) {
				for(String nodeStr:part1) {
					outstream.print(nodeStr+" ");
				}
				outstream.println();
				outstream.print(part2);
			}
		}
		else {
			outstream.println("-1");
			outstream.print("-1");
		}
		
		myReader.close();
		outstream.close();
	}
}
