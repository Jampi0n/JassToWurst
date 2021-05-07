
public class JassToWurst extends RuleSet{
	
	public JassToWurst()
	{
		super();
		this.addBlock("if", "else", "elseif", "endif");
		this.addBlock("function", "endfunction");
		//this.addBlock("globals", "endglobals");
		this.addBlock("loop", "endloop");
		this.addConversion("endif", "", true, true);
		this.addConversion("endloop", "", true, true);
		this.addConversion("endfunction", "", true, true);
		this.addConversion("elseif(.+?(?=then))then", "else if$1", true, true);
		this.addConversion("function (.+?(?=takes))takes nothing returns nothing", "function $1()", true, true);
		this.addConversion("function (.+?(?=takes))takes nothing returns (.*)", "function $1() returns $2", true, true);
		this.addConversion("function (.+?(?=takes))takes (.+?(?=returns))returns nothing", "function $1($2)", true, true);
		this.addConversion("function (.+?(?=takes))takes (.+?(?=returns))returns (.*)", "function $1($2) returns $3", true, true);
		this.addConversion("if(.+?(?=then))then", "if$1", true, true);
		this.addConversion("(.+?(?=exitwhen))exitwhen(.*)", "$1if$2\n$1\tbreak", true, true);
		this.addConversion("loop", "while true", true, true);
		this.addConversion("local ", "", true, false);
		this.addConversion("set ", "", true, false);
		this.addConversion("call ", "", true, false);
		this.addConversion("\nendglobals", "", false, false);
		this.addConversion("globals\n", "", false, false);
		this.protectKeyword("this");
		this.protectKeyword("var");
		this.protectKeyword("let");
		this.protectKeyword("for");
		this.protectKeyword("while");
		this.protectKeyword("end");
		this.protectKeyword("switch");
		this.protectKeyword("case");
		this.protectKeyword("in");
		this.protectKeyword("it");
		this.protectKeyword("to");
		this.protectKeyword("from");
		this.protectKeyword("mod");
	}
	
	@Override
	public boolean hasConverter() {
		return true;
	}
	
	public static void main(String[] args) {
		IOUtils.writeClipboard((new JassToWurst()).run(IOUtils.readClipboard()));
	}
}
