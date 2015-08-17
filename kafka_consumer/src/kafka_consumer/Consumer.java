package kafka_consumer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedWriter;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;


public class Consumer extends Thread {
  private final ConsumerConnector consumer;
  private final String topic;
  private final String name;
  
  public Consumer(String name,String topic){
    consumer = kafka.consumer.Consumer.createJavaConsumerConnector(
            createConsumerConfig());
    this.topic = topic;
    this.name = name;
  }

  private static ConsumerConfig createConsumerConfig(){
    Properties props = new Properties();
    props.put("zookeeper.connect", KafkaProperties.zkConnect);
    props.put("group.id", KafkaProperties.groupId);
    props.put("zookeeper.session.timeout.ms", "60000");
    props.put("zookeeper.sync.time.ms", "200");
    props.put("auto.commit.interval.ms", "1000");
    //ÿ�����ٽ��յ��ֽ�����Ĭ����1
    //props.put("fetch.min.bytes", "1024");
    //ÿ�����ٵȴ�ʱ�䣬Ĭ����100
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
    File file = new File("log/log.txt");
    FileWriter fw = null;
	try {
		fw = new FileWriter(file.getAbsoluteFile());
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    BufferedWriter bw = new BufferedWriter(fw);
    int count = 200000;
    while(it.hasNext() && (--count>0)){
    	messageStr = new String(it.next().message());
    	System.out.println(messageStr);
    	try {
			bw.write(messageStr);
			bw.newLine();
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
