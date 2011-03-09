package functionalTests.multiactivities.scc2;

import java.util.Date;

import org.apache.log4j.Logger;

/**
 * Use this to start an SCC search.
 * @author Izso
 *
 */
public class GraphMaster {

	public static void main(String[] args) {
		Logger logger = Logger.getLogger("SCC");
		
		if (args.length<3) {
			printUsage(); 
			return;
		}
		
		int workerCount = Integer.parseInt(args[0]);
		
		Deployer deployer = new Deployer();
		GraphWorker[] workers;
		if (args.length==4) {
		    workers = deployer.createAndDeploy(workerCount, args[3].split(";"), args[2].startsWith("MA"));
		} else {
		    workers = deployer.createAndDeploy(workerCount, args[2].startsWith("MA"));
		}
		if (workers==null) {
			logger.error("Failed to create workers!");
			return;
		}
		logger.info("Created "+workerCount+" workers.");
		
		/*DataManager data = new DataManager(workers);
		data.loadAndDistribute(args[1]);
		logger.info("Loaded graph from "+args[1]);*/
		Date before = new Date();
		int x = 0;
		for (GraphWorker gw : workers) {
		    gw.loadEdges(workers, args[1], x);
		    x++;
		}
		
		int numNodes = 0;
		for (GraphWorker gw : workers) {
		    numNodes += gw.getOwnedNodesCount();
		}
		System.out.println(new Date().getTime()-before.getTime());
		Executer executer = new Executer(workers);
		logger.info("Starting executer.");
		//-1 == infinite branches
		executer.runAlgorithm(-1, numNodes);
		
		logger.info("Killing all workers and exiting.");
		deployer.killAll();
		System.exit(0);
	}

	private static void printUsage() {
		System.out.println("Usage:");
		System.out.println("<Number of workers> <Path to graph> <Mode for workers: 'MA' or 'SA'>");
	}
	
	

}