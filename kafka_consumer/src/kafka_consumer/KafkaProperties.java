package kafka_consumer;
public interface KafkaProperties{
  final static String zkConnect = "10.180.92.233:2181";  
  final static String groupId = "bbb";
  final static String topic = "gcp.ceph";
  final static String kafkaServerURL = "10.180.92.233";
  final static int kafkaServerPort = 2181;
  final static int kafkaProducerBufferSize = 64*1024;
  final static int connectionTimeOut = 100000;
  final static int reconnectInterval = 10000;
  final static String clientId = "SimpleConsumerDemoClient";
}