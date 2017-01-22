import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class startingPoint {
	static Logger logger = Logger.getLogger("lasm.startingPoint");
	static FileHandler handler;
	static MyFormatter formatter = new MyFormatter();
	static HashMap<Integer, String[]> program = new HashMap<Integer, String[]>();

	public static void main(String args[]) {
		try {
			handler = new FileHandler("C:\\LASM\\log.html");
			handler.setFormatter(formatter);
			logger.addHandler(handler);
			logger.setUseParentHandlers(false);
			logger.info("lasm.statingPoint.main : Logger initialized.");
		} catch (Exception e) {
			logger.warning("lasm.statingPoint.main : Exception : " + e.getMessage());
			logger.log(Level.WARNING, "Stack Trace : ", e );
			System.out.println("Warning : Can't write into log file. Continuing without logging.");
		}
		
		if(args.length == 0){
			logger.severe("lasm.startingPoint.main : No input file.");
			System.out.println("Error : No input file.");
			return;
		}
		for(String fileName : args){
			logger.info("lasm.startingPoint.main : Processing file : " + fileName);
			prepare(fileName);
			passOne(fileName);
			passTwo(fileName);
			program = new HashMap<Integer, String[]>();
		}
	}
	
	static public void prepare(String fileName){
		////TODO
	}
	
	static public void passOne(String fileName){
		////TODO
	}
	
	static public void passTwo(String fileName){
		////TODO
	}
}
