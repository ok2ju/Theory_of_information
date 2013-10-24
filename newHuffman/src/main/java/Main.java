import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.io.IOUtils;

public class Main {

	public static void main(String[] args) throws IOException {
		
		Scanner sc = new Scanner(System.in);
		
		System.out.println("1. ���� �������� �������, 2. ��������� ������ �� �����");
		int choice = sc.nextInt();
		
		switch(choice){
		case 1:
			String userInput = "";
			InputStreamReader converter = new InputStreamReader(System.in);
			BufferedReader in = new BufferedReader(converter);
			
			Parser parser = new Parser();
			List<Record> list = new ArrayList<Record>();
			
			System.out.println("���������� ��������:");
			int count = sc.nextInt();
			
			for(int i=0;i<count;i++){
				Record record = new Record();
				
				System.out.println("������� ������");
				userInput = in.readLine();
				record.setSymbol(userInput);
				
				System.out.println("������� ������� �������");
				userInput = in.readLine();
				record.setFrequence(Integer.parseInt(userInput));
				
				System.out.println("������� �����������");
				userInput = in.readLine();
				record.setChance(Double.parseDouble(userInput));
				
				list.add(record);
			}
			
			parser.setTable(list);
			parser.sortTable();
			System.out.println("--------------<TABLE>-------------");
			parser.showTable();
			System.out.println("ENTROPY - ["+parser.getEntropy()+"]");
			System.out.println("--------------<CODE>--------------");
			
			Separator separator = new Separator(parser);
			separator.separateTable();
			
			break;
			
		case 2:
			try {
				FileInputStream inputStream = new FileInputStream("file.txt");
				String fileString = IOUtils.toString(inputStream);
				
				Separator sp = new Separator(fileString);			
				sp.separateTable();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}

	}

}
