import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Parser {

	List<Record> table = new ArrayList<Record>();

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
		
		System.out.println(table.size());

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
