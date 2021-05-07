import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class RuleSet {

	static class Conversion {
		String search;
		String replace;
		boolean prevSep;
		boolean nextSep;

		public Conversion(String search, String replace, boolean prevSep, boolean nextSep) {
			this.search = search;
			this.replace = replace;
			this.prevSep = prevSep;
			this.nextSep = nextSep;
		}
	}

	private HashMap<String, Integer> intendationMap;
	private ArrayList<Conversion> conversionList;
	private ArrayList<Conversion> preConversionList;
	
	public RuleSet() {
		this.intendationMap = new HashMap<>();
		this.conversionList = new ArrayList<>();
		this.preConversionList = new ArrayList<>();
	}

	protected void addBlock(String open, String close) {
		this.intendationMap.put(open.toLowerCase(), 1);
		this.intendationMap.put(close.toLowerCase(), -1);
	}

	protected void addBlockPrefix(String[] prefix, String open, String close) {
		this.intendationMap.put(open.toLowerCase(), 1);
		this.intendationMap.put(close.toLowerCase(), -1);
	}

	protected void addBlock(String open, String mid, String close) {
		this.intendationMap.put(open.toLowerCase(), 1);
		this.intendationMap.put(mid.toLowerCase(), 2);
		this.intendationMap.put(close.toLowerCase(), -1);
	}

	protected void addBlock(String open, String mid1, String mid2, String close) {
		this.intendationMap.put(open.toLowerCase(), 1);
		this.intendationMap.put(mid1.toLowerCase(), 2);
		this.intendationMap.put(mid2.toLowerCase(), 2);
		this.intendationMap.put(close.toLowerCase(), -1);
	}

	protected void addConversion(String search, String replace, boolean prevSep, boolean nextSep) {
		this.conversionList.add(new Conversion(search, replace, prevSep, nextSep));
	}
	protected void addPreConversion(String search, String replace, boolean prevSep, boolean nextSep) {
		this.preConversionList.add(new Conversion(search, replace, prevSep, nextSep));
	}
	protected void protectKeyword(String word) {
		this.preConversionList.add(new Conversion(word, word + "_", true, true));
	}

	public int matchIdentifier(String identifier) {
		if (this.intendationMap.containsKey(identifier.toLowerCase())) {
			return this.intendationMap.get(identifier.toLowerCase());
		}
		return 0;
	}

	public String convert(String in) {
		String workingString = in;
		for (Conversion conversion : this.conversionList) {
			Pattern pattern = Pattern.compile((conversion.prevSep ? "(?<![^\\s\\p{Punct}])" : "") + conversion.search
					+ (conversion.nextSep ? "(?![^\\s\\p{Punct}])" : ""));
			Matcher matcher = pattern.matcher(workingString);
			workingString = matcher.replaceAll(conversion.replace);
		}
		return workingString;
	}
	
	public String preConvert(String in) {
		String workingString = in;
		for (Conversion conversion : this.preConversionList) {
			Pattern pattern = Pattern.compile((conversion.prevSep ? "(?<![^\\s\\p{Punct}])" : "") + conversion.search
					+ (conversion.nextSep ? "(?![^\\s\\p{Punct}])" : ""));
			Matcher matcher = pattern.matcher(workingString);
			workingString = matcher.replaceAll(conversion.replace);
		}
		return workingString;
	}

	public boolean hasConverter() {
		return false;
	}
	
	public String run(String in) {
		return Formatter.run(this, in);
	}
}
