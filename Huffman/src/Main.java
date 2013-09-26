import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class Main {

	public static void main(String[] args) throws IOException {
		
		BufferedReader bf = new BufferedReader(new FileReader("file.txt"));
		
		try{
			StringBuilder sb = new StringBuilder();
			String line = bf.readLine();
			
			while(line != null){
				sb.append(line);
				line = bf.readLine();
			}
			String everything = sb.toString();
			
			Separator sp = new Separator(everything);
			sp.separateTable();
			
		} finally{
			bf.close();
		}
	}

}
