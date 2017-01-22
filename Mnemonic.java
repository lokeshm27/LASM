import java.io.Serializable;

public class Mnemonic implements Serializable{
	
	private static final long serialVersionUID = 1L;
	public String mnemonic;
	public String opcode;
	public int format;
	public int operands;
	public boolean isReg;
	public boolean builtIn;
	
	Mnemonic(String mnemonic, String opcode, int format, int operands, boolean isReg, boolean builtIn){
		this.mnemonic = mnemonic;
		this.opcode = opcode;
		this.format = format;
		this.operands = operands;
		this.isReg = isReg;
		this.builtIn = builtIn;
	}
}
