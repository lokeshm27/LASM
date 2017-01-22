import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
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
	static Data dat = new Data();
	static File data = new File("C:\\LASM\\Bin\\data.dat");
	
	public static void main(String args[]) {
		try {
			handler = new FileHandler("C:\\LASM\\log.html");
			handler.setFormatter(formatter);
			logger.addHandler(handler);
			logger.setUseParentHandlers(false);
			logger.setLevel(Level.ALL);
			logger.info("lasm.startingPoint.main : Logger initialized.");
		} catch (Exception e) {
			logger.warning("lasm.statingPoint.main : Exception : " + e.getMessage());
			logger.log(Level.WARNING, "Stack Trace : ", e );
			System.out.println("Warning : Can't write into log file. Continuing without logging.");
		}
		
		deserial();
		
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
		int programLength = 0;
		int lineNum = 0;
		try {
			logger.info("lasm.startingPoint.prepare : Reading input file : " + fileName);
			BufferedReader buff = new BufferedReader(new FileReader(file));
			while((line = buff.readLine()) != null){
				lineNum++;
				line = line.replaceAll("\\s+"," ");
				line = line.trim();
				line = line.toLowerCase();
				if(line.startsWith(";") || line.startsWith(".")){
					logger.info("lasm.startingPoint.prepare : line " + Integer.toString(lineNum) + " : Comment Line. Skipping.");
					continue;
				}
				
				if(line.contains("%empty%")){
					logger.warning("lasm.startingPoint.prepare : line " + Integer.toString(lineNum) + " : contains internal empty token. Skipping file.");
					System.out.println("Error : Invalid token : %empty%");
					return;
				}
				logger.info("lasm.startingPoint.prepare : Processing line : " + line);
				String fields[] = line.split(" ");
				switch(fields.length){
					case 0 : 
						logger.info("lasm.startingPoint.prepare : line " + Integer.toString(lineNum) + ": Empty line.");
						break;
					
					case 1 :
						logger.info("lasm.startingPoint.prepare : line " + Integer.toString(lineNum) + ": field 1 present.");
						if(dat.isOpcode(fields[0])){
							logger.info("lasm.startingPoint.prepare : line " + Integer.toString(lineNum) + ": field 1 - Mnemonic.");
							programLength++;
							String[] temp = {"%empty%", fields[0], "%empty%"}; 
							program.put(programLength, temp);
						} else if(dat.isDirective(fields[0])){
							logger.info("lasm.startingPoint.prepare : line " + Integer.toString(lineNum) + ": field 1 - Directive.");
							programLength++;
							String[] temp = {"%empty%", fields[0], "%empty%"}; 
							program.put(programLength, temp);
						} else {
							logger.severe("lasm.startingPoint.prepare : line " + Integer.toString(lineNum) + ": Syntax Error. field 1 - Unknown.");
							System.out.println("Error : line " + Integer.toString(lineNum) + " : Syntax error.");
							return; 
						}
						break;
						
					case 2 : 
						logger.info("lasm.startingPoint.prepare : line " + Integer.toString(lineNum) + ": field 1 2 present.");
						if(dat.isOpcode(fields[0])){
							logger.info("lasm.startingPoint.prepare : line " + Integer.toString(lineNum) + ": field 1 - Mnemonic. field 2 - Operands.");
							programLength++;
							String[] temp = {"%empty%", fields[0], fields[1]};
							program.put(programLength, temp);
						}else if(dat.isDirective(fields[0])){
							logger.info("lasm.startingPoint.prepare : line " + Integer.toString(lineNum) + ": field 1 - Directive. field 2 - Operands.");
							programLength++;
							String[] temp = {"%empty%", fields[0], fields[1]};
							program.put(programLength, temp);
						}else if(dat.isOpcode(fields[1])){
							logger.info("lasm.startingPoint.prepare : line " + Integer.toString(lineNum) + ": field 1 - Label. field 2 - Mnemonic.");
							programLength++;
							String[] temp = {fields[0], fields[1], "%empty%"};
							program.put(programLength, temp);
						}else if(dat.isDirective(fields[1])){
							logger.info("lasm.startingPoint.prepare : line " + Integer.toString(lineNum) + ": field 1 - Label. field 2 - Directive.");
							programLength++;
							String[] temp = {fields[0], fields[1], "%empty%"};
							program.put(programLength, temp);
						}else{
							logger.severe("lasm.startingPoint.prepare : line " + Integer.toString(lineNum) + ": Syntax Error. field 1 - Unknown.");
							System.out.println("Error : Line " + Integer.toString(lineNum) + ": Syntax error.");
							return;
						}
						break;
						
					case 3 :
						logger.info("lasm.startingPoint.prepare : line " + Integer.toString(lineNum) + ": field 1 2 3 present. Assuming fields 1 - Label, 2 - Mnemonic, 3 - Operands.");
						programLength++;
						String[] temp = {fields[0], fields[1], fields[2]};
						program.put(programLength, temp);
						break;
						
					default :
						logger.severe("lasm.startingPoint.prepare : line " + Integer.toString(lineNum) + ": More than 3 fields. ");
						System.out.println("Error : Line " + lineNum + ": Invalid Syntax.");
				}
			}
			if(programLength == 0){
				logger.warning("lasm.startingPoint.prepare : " + fileName + " : No Program lines. Skipping");
				System.out.println("Error : " + fileName + ": No statements to assemble.");
				return;
			}
			logger.fine("lasm.startingPoint.prepare : " + fileName + " prepared successfully.");

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
	
	private static void deserial() {
		if(!data.exists()){
			logger.severe("lasm.startingPoint.deserial : data file not found. Exiting Job.!");
			System.out.println("Fatal Error : Data file not found. Refer Mannual for More information." );
			System.exit(-1);
		}
		
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(data));
			dat = (Data) ois.readObject();
			ois.close();
			logger.fine("lasm.startingPoint.deserial : data file loaded.");
		} catch (Exception e) {
			logger.severe("lasm.startingPoint.deserial : Exception while loading data file.");
			logger.log(Level.SEVERE, "Stack Trace : ", e);
			System.out.println("Fatal Error : Data file can't be loaded. Refer Mannual for More information.");
			System.exit(-1);
		}
	}
}
