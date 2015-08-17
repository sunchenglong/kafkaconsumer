package text.process;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class tableprocess {
	public static void main(String args[]){
		        try {
		        // read file content from file
		        StringBuffer sb= new StringBuffer("");
		        FileReader reader = new FileReader("log/testlog.txt");
		        BufferedReader br = new BufferedReader(reader);
		        Map bemap = new HashMap();
		        String str = null;
		        while((str = br.readLine()) != null) {
		        	//System.out.println(str);
		        	process(str,bemap);
		        }
		        br.close();
		        reader.close();
		       //  write string to file
		         FileWriter writer = new FileWriter("log/result.txt");
		         BufferedWriter bw = new BufferedWriter(writer);
		         bw.write(sb.toString());     
		         bw.close();
		         writer.close();
		        }
		        catch(FileNotFoundException e) {
		              e.printStackTrace();
		        	}
		        catch(IOException e) {
		              e.printStackTrace();
		        	}
		  }
	public static void  process(String str, Map bemap){
		String date = str.split("\t")[0].toString();
		String uploadid = str.split("\t")[4].toString();
		uploader  upload = new uploader();
		if(date != "#" && uploadid != "#"){
			upload.setUploadid(Integer.parseInt(uploadid));
			String size = str.split("\t")[5].toString();
			String clientip = str.split("\t")[6].toString();
			String serverip = str.split("\t")[7].toString();
			if(size != "#" ){
			}
			bemap.put(uploadid, upload);
		}
	}	
}