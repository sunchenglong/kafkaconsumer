package kafka_consumer;

public class KafkaConsumerDemo2 implements KafkaProperties
{
  public static void main(String[] args){
	String filename = args[0];
	String datetime = args[1];  
	String output = args[2];
	//Consumer consumerThread1 = new Consumer("Consumer1",KafkaProperties.topic);
	simpleConsumer consumerThread1 = new simpleConsumer("Consumer1",KafkaProperties.topic,filename,datetime);
	consumerThread1.start();
	System.out.println("Start Download!");
	try {
		consumerThread1.join();
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	proccesstable1 p = new proccesstable1(filename,output);
	System.exit(1);
  }
}