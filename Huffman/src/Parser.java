import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Parser {

	List<Record> table = new ArrayList<Record>();
	Map<Character, Integer> frequenceList = new HashMap<Character, Integer>();

	public Parser(String message) {
		getTable(message);
	}

	public List<Record> getTable() {
		return table;
	}

	public void setTable(List<Record> table) {
		this.table = table;
	}

	public List<Record> getTable(String message) {
		/*
		final int[] occurences = new int[256];
		final char[] array = message.toCharArray();

		for (final char c : array) {
			occurences[c] = occurences[c] + 1;
		}

		for (int i = 0; i < array.length; i++) {
			Record record = new Record();

			double chanceSymbol = (double) occurences[array[i]] / array.length;

			// System.out.println(occurences[array[i]]+" - "+
			// array[i]+" - "+chanceSymbol);

			record.setSymbol(Character.toString(array[i]));
			record.setFrequence(occurences[array[i]]);
			record.setChance(chanceSymbol);
			if (!table.contains(record)) {
				table.add(record);
			}
		}

		for (int i = 0; i < table.size(); i++) {
			for (int j = i + 1; j < table.size(); j++) {
				if (table.get(i).getSymbol() == table.get(j).getSymbol()) {
					table.remove(table.get(j));
				}
			}
		}
		
		System.out.println(table.size());*/
		
		for (char character : message.toCharArray()) {
			if (!frequenceList.containsKey(character)) {
				frequenceList.put(character, 1);
			} else {
				Integer frequence = frequenceList.get(character);
				frequenceList.remove(character);
				frequenceList.put(character, frequence + 1);
			}
		}
		
		Iterator<Entry<Character, Integer>> it = frequenceList.entrySet().iterator();
		while (it.hasNext()) {
			Entry current = it.next();
			Character ch = (Character) current.getKey();
			Integer frequence = (Integer) current.getValue();

			Double prob = frequence.doubleValue() / message.length();
			
			Record rec = new Record();
			rec.setSymbol(ch.toString());
			rec.setFrequence(frequence);
			rec.setChance(prob);

			table.add(rec);
		}

		sortTable();

		return table;
	}

	public void sortTable() {

		Collections.sort(table, new Comparator<Record>() {
			public int compare(Record r1, Record r2) {
				return r2.getFrequence() - r1.getFrequence();
			}
		});
	}

}
