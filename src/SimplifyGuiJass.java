import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimplifyGuiJass {
	private String andConditions(String in) {
		Pattern andPattern = Pattern.compile(
				"(if\\s*\\(\\s*not\\s*(.*)\\s*\\)\\s*then\\s*\n\\s*return\\s*false\\s*\n\\s*endif\\s*\n\\s*)+return\\s*true");
		Matcher andMatcher = andPattern.matcher(in);
		Pattern conditionPattern = Pattern
				.compile("if\\s*\\(\\s*not\\s*(.*)\\s*\\)\\s*then\\s*\n\\s*return\\s*false\\s*\n\\s*endif");
		StringBuffer sb = new StringBuffer();
		while (andMatcher.find()) {
			String and = andMatcher.group();
			System.out.println("AND:");
			System.out.println(and);
			Matcher conditionMatcher = conditionPattern.matcher(and);
			LinkedList<String> conditions = new LinkedList<>();
			while (conditionMatcher.find()) {
				String cond = conditionMatcher.group(1).trim();
				conditions.add(cond);
			}
			String lastCondition = and.substring(and.lastIndexOf("\n")).trim();
			if (!lastCondition.equals("return true") && !lastCondition.equals("return false")) {
				conditions.add(lastCondition);
			}
			System.out.println("COND:");
			for (String c : conditions) {
				System.out.println(c);
			}
			String combinedCondition = "return " + String.join(" and ", conditions);
			System.out.println(combinedCondition);
			andMatcher.appendReplacement(sb, combinedCondition);
		}
		andMatcher.appendTail(sb);
		return sb.toString();
	}

	private String orConditions(String in) {
		Pattern orPattern = Pattern.compile(
				"(if\\s*\\(\\s*(.*)\\s*\\)\\s*then\\s*\n\\s*return\\s*true\\s*\n\\s*endif\\s*\n\\s*)+return\\s*false");
		Matcher orMatcher = orPattern.matcher(in);
		Pattern conditionPattern = Pattern
				.compile("if\\s*\\(\\s*(.*)\\s*\\)\\s*then\\s*\n\\s*return\\s*true\\s*\n\\s*endif");
		StringBuffer sb = new StringBuffer();
		while (orMatcher.find()) {
			String or = orMatcher.group();
			System.out.println("OR:");
			System.out.println(or);
			Matcher conditionMatcher = conditionPattern.matcher(or);
			LinkedList<String> conditions = new LinkedList<>();
			while (conditionMatcher.find()) {
				String cond = conditionMatcher.group(1).trim();
				conditions.add(cond);
			}
			String lastCondition = or.substring(or.lastIndexOf("\n")).trim();
			if (!lastCondition.equals("return true") && !lastCondition.equals("return false")) {
				conditions.add(lastCondition);
			}
			System.out.println("COND:");
			for (String c : conditions) {
				System.out.println(c);
			}
			String combinedCondition = "return " + String.join(" or ", conditions);
			System.out.println(combinedCondition);
			orMatcher.appendReplacement(sb, combinedCondition);
		}
		orMatcher.appendTail(sb);
		return sb.toString();
	}

	private String inlineConditions(String in) {
		Pattern oneLineCondPattern = Pattern.compile(
				"function\\s+(\\w*)\\s+takes\\s+nothing\\s+returns\\s+boolean\\s*\n\\s*return\\s+(.*)\\s*\n\\s*endfunction");
		boolean changed = true;
		HashMap<String, String> condMap = new HashMap<>();
		System.out.println("Inlining Conditions...");
		int i = 1;
		while(changed) {
			System.out.println("Pass " + i);
			changed = false;
			Matcher oneLineCondMatcher = oneLineCondPattern.matcher(in);
			while (oneLineCondMatcher.find()) {
				condMap.put(oneLineCondMatcher.group(1), oneLineCondMatcher.group(2));
			}
			for (Map.Entry<String, String> entry : condMap.entrySet()) {
				Pattern p = Pattern.compile(entry.getKey() + "\\s*\\(\\)");
				Matcher m = p.matcher(in);
				if(m.find()) {
					in = m.replaceAll(entry.getValue());
					changed = true;
				}
			}
			i++;
		}
		System.out.println("Removing Conditions...");
		for (Map.Entry<String, String> entry : condMap.entrySet()) {
			if (in.split(entry.getKey() + "\\b", -1).length - 1 == 1) {
				in = in.replaceAll(
						"function\\s+" + entry.getKey()
								+ "\\s+takes\\s+nothing\\s+returns\\s+boolean\\s*\n\\s*return\\s+(.*)\\s*\n\\s*endfunction",
						"");
				System.out.println("Removed: " + entry.getKey());
			}
		}
		return in;
	}

	private String simplify(String in) {
		return inlineConditions(orConditions(andConditions(in)));
	}

	public static void main(String[] args) {
		IOUtils.writeClipboard((new SimplifyGuiJass()).simplify(IOUtils.readClipboard()));
	}
}
