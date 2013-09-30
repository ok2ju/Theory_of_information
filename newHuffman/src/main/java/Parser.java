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

	public Parser() {

	}

	public List<Record> getTable() {
		return table;
	}

	public void setTable(List<Record> table) {
		this.table = table;
	}

	public List<Record> getTable(String message) {

		for (char character : message.toCharArray()) {
			if (!frequenceList.containsKey(character)) {
				frequenceList.put(character, 1);
			} else {
				Integer frequence = frequenceList.get(character);
				frequenceList.remove(character);
				frequenceList.put(character, frequence + 1);
			}
		}

		Iterator<Entry<Character, Integer>> it = frequenceList.entrySet()
				.iterator();
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

		for (int i = 0; i < table.size(); i++) {
			System.out.println(table.get(i).getSymbol() + " - "
					+ table.get(i).getFrequence() + " - "
					+ table.get(i).getChance());
		}

		System.out.println("ENTROPY" + " [" + getEntropy() + "]");

		return table;
	}

	public double getEntropy() {

		List<Record> list = getTable();

		double entropy = 0;

		for (int i = 0; i < list.size(); i++) {
			entropy += list.get(i).getChance()
					* (Math.log(list.get(i).getChance()) / Math.log(2));
		}

		return (double) entropy;
	}

	public void sortTable() {

		Collections.sort(table, new Comparator<Record>() {
			public int compare(Record r1, Record r2) {
				return r2.getFrequence() - r1.getFrequence();
			}
		});
	}

	public void showTable() {
		for (int i = 0; i < table.size(); i++) {
			System.out.println(table.get(i).getSymbol() + " - "
					+ table.get(i).getFrequence() + " - "
					+ table.get(i).getChance());
		}
	}

}
