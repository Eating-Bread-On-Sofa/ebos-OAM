//package cn.edu.bjtu.ebosoam.service;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import org.apache.activemq.ActiveMQConnectionFactory;
//import org.apache.activemq.command.ActiveMQBytesMessage;
//import org.apache.activemq.command.ActiveMQMapMessage;
//import org.apache.activemq.command.ActiveMQTextMessage;
//import org.apache.activemq.util.ByteSequence;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//import org.springframework.web.client.RestTemplate;
//
//import javax.jms.*;
//
///**
// * Create by ZhiYuan
// * data:2021/5/7
// */
//
//@Component
//@Order(4)
//public class InitManhole implements ApplicationRunner {
//
//    @Value("${url}")
//    public String url;
//
//    @Autowired
//    RestTemplate restTemplate;
//
//    public MessageConsumer getConsumer(String topic){
//        MessageConsumer messageConsumer = null;
//        try {
//            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("admin","admin",url);
//            Connection connection = connectionFactory.createConnection();
//            connection.start();
//            Session session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
//            Destination destination = session.createTopic(topic);
//            messageConsumer = session.createConsumer(destination);
//            return messageConsumer;
//        }catch (Exception e){}
//        return messageConsumer;
//    }
//
//    public String subscribe(Message message) {
//        try {
//            if (message instanceof ActiveMQBytesMessage) {
//                ActiveMQBytesMessage activeMQMessage = (ActiveMQBytesMessage) message;
//                ByteSequence content = activeMQMessage.getContent();
//                String msg = new String(content.getData());
//                System.out.println("收到ActiveMQBytesMessage");
//                return msg;
//            } else if (message instanceof ActiveMQTextMessage) {
//                ActiveMQTextMessage activeMQTextMessage = (ActiveMQTextMessage) message;
//                System.out.println("收到ActiveMQTextMessage");
//                return activeMQTextMessage.getText();
//            } else if (message instanceof ActiveMQMapMessage) {
//                ActiveMQMapMessage activeMQMapMessage = (ActiveMQMapMessage) message;
//                String content = activeMQMapMessage.getContentMap().toString();
//                System.out.println("收到ActiveMQMapMessage");
//                return content;
//            } else {
//                System.out.println("收到" + message.getClass().toString());
//                return "";
//            }
//        } catch (Exception e) {
//            return "ActiveMQ接收出现异常";
//        }
//    }
//
//    @Override
//    public void run(ApplicationArguments args) throws Exception {
//        new Thread(()->{
//            MessageConsumer messageConsumer = getConsumer("Manhole");
//            while (true){
//                try {
//                    Message message =  messageConsumer.receive();
//                    JSONObject msg = JSON.parseObject(subscribe(message));
//                    System.out.println(msg);
//                } catch (JMSException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }
//}
