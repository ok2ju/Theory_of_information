import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Separator {

	Parser parser;
	Map<Character, String> codeList = new HashMap<Character, String>();

	final String left = "1";
	final String right = "0";

	public Separator(String message) {
		parser = new Parser(message);
	}
	
	public Separator(){
		
	}

	public void createCode(Record r, String code) {
		String symbol = r.getSymbol();

		for (int i = 0; i < symbol.length(); i++) {
			char character = symbol.charAt(i);
			if (!codeList.containsKey(character)) {
				codeList.put(character, code);
			} else {
				String currentCode = codeList.get(character);
				codeList.remove(character);
				codeList.put(character, code + currentCode);    //reverse code
			}
		}
	}

	public void separateTable() {
		List<Record> list = parser.getTable();

		while (list.size() > 1) {
			Record lastRecord = list.get(list.size() - 1);
			Record penult = list.get(list.size() - 2);

			createCode(penult, left);
			createCode(lastRecord, right);

			Record newRecord = new Record();
			newRecord.setSymbol(penult.getSymbol() + lastRecord.getSymbol());
			newRecord.setChance(penult.getChance() + lastRecord.getChance());
			newRecord.setFrequence(penult.getFrequence()
					+ lastRecord.getFrequence());

			list.add(newRecord);

			list.remove(penult);
			list.remove(lastRecord);

			parser.sortTable();
		}
		
		System.out.println(codeList); //codes for each symbol

		StringBuilder sb = new StringBuilder();

		for (Map.Entry<Character, String> e : codeList.entrySet()) {
			sb.append(e.getValue());
		}
		System.out.println(sb); // code
		// System.out.println(sb.length()); // code lenght

		System.out.println(eightBit(sb.toString())); // encode string

		writeFile("code.txt", sb.toString());
		writeFile("encode_String.txt", eightBit(sb.toString()));
		
		System.out.println("Average Length"+" ["+getAverageLength()+"]");

	}

	public char bin2dec(String binNumber) {
		int result = 0;

		for (int i = binNumber.length() - 1, j = 0; i > -1; i--, j++) {
			result += Math.pow(2, i) * (binNumber.charAt(j) == '1' ? 1 : 0);
		}
		System.out.println(result);
		return (char) result;
	}

	public String eightBit(String code) {

		StringBuilder sb = new StringBuilder();
		int count = 0;

		if (code.length() % 8 == 0) {
			String gfx[] = code.split("(?<=\\G.{8})");

			for (String element : gfx) {
				sb.append(bin2dec(element));
			}
		} else {
			while (code.length() % 8 != 0) {
				code = code + '0';
				count++;
			}

			String gfx[] = code.split("(?<=\\G.{8})");

			for (String element : gfx) {
				sb.append(bin2dec(element));
			}
		}
		
		System.out.println(count+" ---------------------------< COUNT");

		return sb.toString();
	}

	public double getAverageLength() {

		List<Record> list = parser.getTable();
		double averageLength = 0;

		for (int i = 0; i < list.size(); i++) {
			String code = codeList.get(list.get(i).getSymbol().charAt(0));
			averageLength += list.get(i).getChance() * code.length();
		}

		return (double) averageLength;
	}

	public void writeFile(String fileName, String text) {
		try {
			PrintWriter out = new PrintWriter(
					new File(fileName).getAbsoluteFile());
			try {
				out.print(text);
			} finally {
				out.close();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}