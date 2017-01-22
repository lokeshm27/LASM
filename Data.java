import java.io.Serializable;
import java.util.HashMap;

public class Data implements Serializable {
	private static final long serialVersionUID = 1L;

	private HashMap<String, Mnemonic> Opcodes = new HashMap<String, Mnemonic>();
	private HashMap<String, Boolean> Directives = new HashMap<String, Boolean>();

	public boolean isOpcode(String mnemonic) {
		if (Opcodes.containsKey(mnemonic)) {
			return true;
		}
		return false;
	}

	public boolean isDirective(String name) {
		if (Directives.containsKey(name)) {
			return true;
		}
		return false;
	}

	public int getFormat(String mnemonic){
		return Opcodes.get(mnemonic).format;
	}
	
	public int getOperands(String mnemonic){
		return Opcodes.get(mnemonic).operands;
	}
	
	public int getOpcode(String mnemonic){
		return Integer.parseInt(Opcodes.get(mnemonic).opcode);
	}
	
	public boolean isRegisterOnly(String mnemonic){
		return Opcodes.get(mnemonic).isReg;
	}
}
