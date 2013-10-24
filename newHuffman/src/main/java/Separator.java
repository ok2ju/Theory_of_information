import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;

public class Separator {

	Parser parser;
	Parser staticTable;
	FileWorker fileWorker = new FileWorker();
	Map<Character, String> codeList = new HashMap<Character, String>();
	String message;

	Gson gson = new Gson();

	final String left = "1";
	final String right = "0";

	public Separator(String message) {
		parser = new Parser(message);
		staticTable = new Parser(message);
		this.message = message;
	}

	public Separator(Parser newParser) {
		parser = newParser;
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
				codeList.put(character, code + currentCode); // reverse code
			}
		}
	}

	public void separateTable() throws IOException {
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

		System.out.println(codeList); // codes for each symbol

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < message.length(); i++) { // form code-string
			char ch = message.charAt(i);
			sb.append(codeList.get(ch));
		}

		System.out.println("Code: " + sb); // code

		System.out.println("Encode string: " + eightBit(sb.toString())); // encode
																			// string
		fileWorker.writeFile("code.txt", sb.toString());

		fileWorker.writeFile("encode_String.txt", codeList.toString()); // write
																		// code-table
		fileWorker.updateFile("encode_String.txt", eightBit(sb.toString()));

		System.out.println("Average length" + " [" + getAverageLength() + "]");

		lalka(eightBit(sb.toString()));
	}

	public void lalka(String lol) throws IOException {
		String source = fileWorker.read("encode_String.txt");

		int endCodeTable = source.indexOf('}');
		String codeTable = source.substring(1, endCodeTable);
		String delimiter = ", ";
		String[] temp = codeTable.split(delimiter);
		// String encodeString = source.substring(endCodeTable+2,
		// source.length()-1);// encodeString
		String encodeString = lol.substring(0, lol.length() - 1);
		String lastSymbol = source.substring(source.length() - 1,
				source.length());

		Map<String, String> newCodeTable = new HashMap<String, String>();

		for (int i = 0; i < temp.length; i++) {
			char[] str = temp[i].toCharArray();
			/*
			 * newCodeTable.put(str[0], temp[i].substring(temp[i].indexOf('=') +
			 * 1));
			 */
			newCodeTable.put(temp[i].substring(temp[i].indexOf('=') + 1),
					Character.toString(str[0]));

		}

		StringBuilder sb = new StringBuilder();

		char[] enc = encodeString.toCharArray();
		for (char ch : enc) {
			int number = (int) ch;
			String binCode = Integer.toBinaryString(number);
			while (binCode.length() < 8) {
				binCode = '0' + binCode;
			}

			sb.append(binCode);
		}
		System.out.println(sb);

		String binCodeResult = sb.toString();
		String buff = "";
		StringBuilder sbs = new StringBuilder();
		int lastSymbolsCount = Integer.parseInt(lastSymbol);
		String bin = binCodeResult.substring(0, binCodeResult.length()
				- lastSymbolsCount); // binCode - zero's
		System.out.println(bin);

		for (Character ch : bin.toCharArray()) {
			buff += ch.toString();
			if (newCodeTable.containsKey(buff)) {
				sbs.append(newCodeTable.get(buff));
				buff = "";
			}
		}
		System.out.println(sbs.toString());

	}

	public char bin2dec(String binNumber) {
		int result = 0;

		for (int i = binNumber.length() - 1, j = 0; i > -1; i--, j++) {
			result += Math.pow(2, i) * (binNumber.charAt(j) == '1' ? 1 : 0);
		}
		// System.out.println("Number of symbol: "+result);
		return (char) result;
	}

	public String convertToBinString(StringBuilder sb, int lastSymbol) {
		String str = eightBit(sb.toString());
		StringBuilder ss = new StringBuilder();

		char c[] = str.toCharArray();
		for (char ch : c) {
			int numberOfSymbol = (int) ch;
			String ttr = Integer.toBinaryString(numberOfSymbol);

			while (ttr.length() < 8) {
				ttr = '0' + ttr;
			}
			ss.append(ttr);
		}

		String code = ss.toString();
		code = code.substring(0, code.length() - lastSymbol);

		return code;
	}

	public String eightBit(String code) {

		StringBuilder sb = new StringBuilder();
		int count = 0;

		while (code.length() % 8 != 0) {
			code = code + '0';
			count++; // number of '0' that i added to end of code-string
		}

		String gfx[] = code.split("(?<=\\G.{8})");

		for (String element : gfx) {
			sb.append(bin2dec(element));
		}

		sb.append(count);

		return sb.toString();
	}

	public double getAverageLength() {
		List<Record> sourceList = staticTable.getTable();

		double averageLength = 0;

		for (int i = 0; i < sourceList.size(); i++) {
			String code = codeList.get(sourceList.get(i).getSymbol().charAt(0));
			averageLength += sourceList.get(i).getChance() * code.length();
		}

		return (double) averageLength;
	}

}