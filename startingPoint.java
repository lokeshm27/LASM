import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class startingPoint {
	static Logger logger = Logger.getLogger("lasm.startingPoint");
	static FileHandler handler;
	static MyFormatter formatter = new MyFormatter();
	static HashMap<Integer, String[]> program = new HashMap<Integer, String[]>();
	static int[] locCtr = {0x000, 0x000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000};
	static int maxBlocks = 10;

	public static void main(String args[]) {
		try {
			handler = new FileHandler("C:\\LASM\\log.html");
			handler.setFormatter(formatter);
			logger.addHandler(handler);
			logger.setUseParentHandlers(false);
			logger.info("lasm.startingPoint.main : Logger initialized.");
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
			program = new HashMap<Integer, String[]>();
		}
	}
	
	@SuppressWarnings("resource")
	static public void prepare(String fileName){
		File file = new File(fileName);
		String line;
		int programLenght = 0x0000;
		int lineNum = 0;
		try {
			logger.info("lasm.startingPoint.prepare : Reading input file : " + fileName);
			BufferedReader buff = new BufferedReader(new FileReader(file));
			while((line = buff.readLine()) != null){
				lineNum++;
				line = line.replaceAll("\\s+"," ");
				if(line.startsWith(";") || line.startsWith("\\t*\\s*;")){
					logger.info("lasm.startingPoint.prepare : line " + Integer.toString(lineNum) + " : Comment Line. Skipping.");
					continue;
				}
				String fields[] = line.split(" ");
				switch(fields.length){
					case 0 : 
						logger.info("lasm.startingPoint.prepare : line " + Integer.toString(lineNum) + ": Empty line.");
						break;
					
					case 1 : 
						////TODO
						break;
						
					case 2 : 
						////TODO
						break;
						
					case 3 :
						////TODO
						break;
						
					default :
				}
			}
		} catch (FileNotFoundException e) {
			logger.severe("lasm.startingPoint.prepare : input file : " + fileName + " doesn't exist");
			System.out.println("Error : Input file not found.");
			return;
		} catch (IOException e) {
			logger.severe("lasm.startingPoint.prepare : IOException while reading file : " + fileName);
			logger.log(Level.SEVERE, "Stack Trace : ", e);
			System.out.println("Error : Can't read input file : " + fileName);
			return;
		}
		
		
		//passOne(fileName);
	}
	
	static public void passOne(String fileName){
		////TODO
		//passTwo(fileName);
	}
	
	static public void passTwo(String fileName){
		////TODO
	}
}
