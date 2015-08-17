package kafka_consumer;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Calendar;
public class proccesstable1 {
	private Map<String,storeTable> StoreMap;
	public static void main(String args[]){
		proccesstable1 p = new proccesstable1("raw.txt","result.txt");
	}
	public proccesstable1(String rawfile,String output){
        try {
        // read file content from file
        StoreMap = new HashMap<String, storeTable>();
        StringBuffer sb= new StringBuffer("");
        FileReader reader = new FileReader(rawfile);
        BufferedReader br = new BufferedReader(reader);
        String str = null;
        while((str = br.readLine()) != null) {
        	System.out.println(str);
        	stackStore(str);
        	/*
        	if(getFields(str)!="#"){
        		sb.append(getFields(str)+"\n");
        	}
        	System.out.println(getFields(str));
        	*/
        }
        String fileresult = PrintStoreMap(sb);
        
        br.close();
        reader.close();
       //  write string to file
         FileWriter writer = new FileWriter(output);
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
	public void stackStore(String str){
		//System.out.println(str.split("\t")[4]);
		String datestr = str.split("\t")[0].trim();
		String status = str.split("\t")[3].trim();
		String uploadid = str.split("\t")[4].trim();
		String size = str.split("\t")[5].trim();
		String clientip = str.split("\t")[6];
		String serverip = str.split("\t")[7];
		storeTable table = new storeTable();
		if(this.StoreMap.get(uploadid)!=null){
			table = this.StoreMap.get(uploadid);
		}
		if(table.size == null || table.size.equals("#")){
			table.size = size;
		}
		if(table.clientip == null || table.clientip.equals("#")){
			table.clientip = clientip;
		}
		if(table.serverip==null || table.serverip.equals("#")){
			table.serverip = serverip;
		}
		if(status.equals("100")){
			if(table.start100 == null || StrToDate(datestr).compareTo(table.start100) < 0)
				table.start100 = StrToDate(datestr);
			else{
				//No change
			}
		}else if(status.equals("110")){
			if(table.end110 == null || StrToDate(datestr).compareTo(table.end110) > 0)
				table.end110 = StrToDate(datestr);
			else{
				//No change
			}
		}else if(status.equals("200")){
			if(table.end200 == null || StrToDate(datestr).compareTo(table.end200) > 0)
				table.end200 = StrToDate(datestr);
			else{
				//No change
			}
		}
		this.StoreMap.put(uploadid, table);
	}
	public static Date StrToDate(String str){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,S");
        Date date = null;
		try {
			date = sdf.parse(str);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
           return date;
	}
	
	public String PrintStoreMap(StringBuffer sb){
		String result = new String();
		String speed;
		long size;
		long time;
		for(String key : this.StoreMap.keySet()){
			if(this.StoreMap.get(key).start100 == null || this.StoreMap.get(key).end110 == null || this.StoreMap.get(key).size == "#"){
				speed = "-1";
			}else{
				size =Long.parseLong(this.StoreMap.get(key).size);
				time=(this.StoreMap.get(key).end110.getTime()-this.StoreMap.get(key).start100.getTime())/1000;
				if(time!=0)
					speed = Long.toString((size/time));
				else
					speed = "div0";
			};
			result = key+"\t"+speed+"\t"+this.StoreMap.get(key).start100 +"\t" + this.StoreMap.get(key).end110 +"\t"
					+this.StoreMap.get(key).size+"\t"+ this.StoreMap.get(key).clientip +"\t" + this.StoreMap.get(key).serverip +"\n"; 
			System.out.println(result);
			sb.append(result);
		}
		return null;
	}
}
