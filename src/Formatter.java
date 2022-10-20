public class Formatter {
	private RuleSet ruleSet;

	public Formatter(RuleSet ruleSet) {
		this.ruleSet = ruleSet;
	}

	private String findFirstIdentifier(String line) {
		String identifier = "";
		for (char c : line.toCharArray()) {
			if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')) {
				identifier += c;
			} else {
				break;
			}
		}

		return identifier;
	}

	private String format(String input) {
		String[] indentationString = new String[128];
		indentationString[0] = "";
		for (int i = 1; i < indentationString.length; ++i) {
			indentationString[i] = indentationString[i - 1] + "\t";
		}
		if(this.ruleSet.hasConverter()) {
			input = this.ruleSet.preConvert(input);
		}

		String[] lines = input.split("\n");
		StringBuilder outBuilder = new StringBuilder();
		int indentation = 0;
		int lineNum = 0;
		for (String line : lines) {
			try {
			line = line.trim();
			//int intDif = this.ruleSet.matchIdentifier(findFirstIdentifier(line));
			int intDif = this.ruleSet.matchLine(line);
			if (intDif == -1) {
				indentation += intDif;
			}
			if (intDif == 2) {
				indentation--;
			}
			int numTabs = indentation;
			if (intDif == 2) {
				indentation++;
			}
			if (intDif == 1) {
				indentation += intDif;
			}
			System.out.println(lineNum + ": " + indentationString[numTabs] + line);
			outBuilder.append(indentationString[numTabs] + line + "\n");
			} catch(Exception e) {
				System.err.println("Error in line " + lineNum + ": " + line);
				throw e;
			}
			lineNum++;
		}
		String formatted = outBuilder.toString();
		if(this.ruleSet.hasConverter()) {
			formatted = this.ruleSet.convert(formatted);
		}
		return formatted;
	}
	
	public static String run(RuleSet ruleSet, String in) {
		Formatter f = new Formatter(ruleSet);
		return f.format(in);
	}
}
