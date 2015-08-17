package text.process;
import java.util.Date;
import java.util.Map;
import java.util.Stack;
public class uploader {
	private Map datemap;
	private int uploadid;
	private int size;
	private String clientip;
	private String serverip;
	public void setUploadid(int uploadid){
		this.uploadid = uploadid;
	}
	public void setSize(int size){
		this.size = size;
	}
	public void setClientip(String clientip){
		this.clientip = clientip;
	}
	public void setServerip(String serverip){
		this.serverip = serverip;
	}
	public void printUploader(){
		System.out.println("Date:");
	}
}
