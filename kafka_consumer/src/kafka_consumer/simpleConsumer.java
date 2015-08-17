package kafka_consumer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedWriter;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka_consumer.gentable;
public class simpleConsumer extends Thread {
  private final ConsumerConnector consumer;
  private final String topic;
  private final String name;
  private String filename;
  private String datetime; //取出总行数
  public simpleConsumer(String name,String topic,String filename,String datetime){
    consumer = kafka.consumer.Consumer.createJavaConsumerConnector(
            createConsumerConfig());
    this.topic = topic;
    this.name = name;
    this.filename = filename;
    this.datetime = datetime;
  }
  private static ConsumerConfig createConsumerConfig(){
    Properties props = new Properties();
    props.put("zookeeper.connect", KafkaProperties.zkConnect);
    props.put("group.id", KafkaProperties.groupId);
    props.put("zookeeper.session.timeout.ms", "60000");
    props.put("zookeeper.sync.time.ms", "200");
    props.put("auto.commit.interval.ms", "1000");
    //每次最少接收的字节数，默认是1
    //props.put("fetch.min.bytes", "1024");
    //每次最少等待时间，默认是100
    //props.put("fetch.wait.max.ms", "600000");
    return new ConsumerConfig(props);
  }
  public void run() {
    Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
    topicCountMap.put(topic, new Integer(1));
    Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
    KafkaStream<byte[], byte[]> stream =  consumerMap.get(topic).get(0);
    ConsumerIterator<byte[], byte[]> it = stream.iterator();
    String messageStr;
    File file = new File(this.filename);
    FileWriter fw = null;
	try {
		fw = new FileWriter(file.getAbsoluteFile());
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    BufferedWriter bw = new BufferedWriter(fw);
    String count = datetime;
    String date = new String();
    Pattern pattern = Pattern.compile("([0-9]+)-([0-9]+)-([0-9]+) ([0-9]+):([0-9]+):([0-9]+),([0-9]+)");
    while(it.hasNext()&& (date.equals("") || !date.equals(count))){
    	messageStr = new String(it.next().message());
    	System.out.println(messageStr);
    	//debug
    	Matcher matcher = pattern.matcher(messageStr);
    	if(matcher.find()){
    				date = matcher.group().split(" ")[0].split("-")[0]+
    				matcher.group().split(" ")[0].split("-")[1]+
    				matcher.group().split(" ")[0].split("-")[2];
    				//System.out.println("Matched!");
    				System.out.println(date);
    	}
    	//debug--
    	try {
    		System.out.println(gentable.getFields(messageStr));
    		if(!gentable.getFields(messageStr).equals("#")){
    			bw.write(gentable.getFields(messageStr));
    			bw.newLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    try {
		bw.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }
}
