import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.commons.io.IOUtils;


public class FileWorker {
	
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
	
	public static String read(String fileName) throws IOException {
		FileInputStream inputStream = new FileInputStream(fileName);
        String fileString = IOUtils.toString(inputStream);

        return fileString;
	}
	
	public void updateFile(String fileName, String newText) throws IOException{
		String newLine = System.getProperty("line.separator");
		StringBuilder sb = new StringBuilder();
	    String oldFile = read(fileName);
	    sb.append(oldFile+newLine);
	    sb.append(newText);
	    writeFile(fileName, sb.toString());
		
	}
}
