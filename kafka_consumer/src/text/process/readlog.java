package text.process;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class readlog {
	public static void main(String args[]){
        try {
        // read file content from file
        StringBuffer sb= new StringBuffer("");
        FileReader reader = new FileReader("log/log.txt");
        BufferedReader br = new BufferedReader(reader);
        String str = null;
        while((str = br.readLine()) != null) {
        	System.out.println(str);
        	sb.append(getFields(str)+"\n");
        	System.out.println(getFields(str));
        }
        br.close();
        reader.close();      
       //  write string to file
         FileWriter writer = new FileWriter("log/log_new.txt");
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
	/***
	 * @param str 原始字符串
	 * @return 返回时间结果
	 */
	public static Date getTime(String str){
		String regEx="([0-9]+)-([0-9]+)-([0-9]+) ([0-9]+):([0-9]+):([0-9]+),([0-9]+)"; 
        Pattern pattern = Pattern.compile(regEx);  
        Matcher matcher = pattern.matcher(str);  
        if(!matcher.find()){  
            //System.out.println("#");
            return null;
        }
        else{
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,S");
        	Date date = null;
			try {
				date = sdf.parse(matcher.group(0));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            return date;
        }
	}
	/***
	 * @param source 源字符串
	 * @param regEx	正则表达式
	 * @return 带匹配的字符串，如果没有输出#
	 */
	public static String getField(String source, String regEx){
        Pattern pattern = Pattern.compile(regEx);  
        Matcher matcher = pattern.matcher(source);  
        if(!matcher.find()){  
            return "#";
        }
        else{  
            return matcher.group(0);
        }
	}
	/***
	 * 目前日志抓取的正则表达式，所有需要字段
	 * @param source
	 * @return
	 */
	public static String getFields(String source){
		String result = "";
		//匹配第一个字段  时间
		String regExArray[]={
							 //日志时间 0
							 "([0-9]+)-([0-9]+)-([0-9]+) ([0-9]+):([0-9]+):([0-9]+),([0-9]+)",
							 //userid 1
							 "user=([0-9]+)",
							 //Statusid 2
							 "(current STATUS)( )?(:|=)?( )?([0-9]+)",
							//RECEIVE UploadStatus 3
							 "RECEIVE UploadStatus:([0-9]+)",
							 //uploadid 4
							 "(UploadId|uploadId)( )?(=|:)?( )?([0-9]+)",
							 //size 5
							 "(Size|size)( )?(=|:)?( )?([0-9]+)",
							 //clientip 6
							 "clientIp:'([0-9]+).([0-9]+).([0-9]+).([0-9]+)",
							 //serverip 7
							 "serverIp:'([0-9]+).([0-9]+).([0-9]+).([0-9]+)"
							};
		for (int i = 0; i < 8;i++){
			Pattern pattern = Pattern.compile(regExArray[i]);
			Matcher matcher = pattern.matcher(source);  
			if(!matcher.find()){  
				result += "#";
			}
			else{
				if(i==0){
					result += matcher.group();
				}
				else if(i < 6){
					result += matcher.group(0).split("=|:")[1];
				}
				else{
					result += matcher.group().split("'")[1];
				}
			}
			result += "\t";
		}
		return result;
	}
}
