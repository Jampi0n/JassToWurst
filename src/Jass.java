
public class Jass extends RuleSet{
	
	public Jass()
	{
		super();
		this.addBlock("if", "else", "elseif", "endif");
		this.addBlock("function", "endfunction");
		this.addBlock("globals", "endglobals");
		this.addBlock("loop", "endloop");
	}
	
	public static void main(String[] args) {
		IOUtils.writeClipboard((new Jass()).run(IOUtils.readClipboard()));
	}

}
