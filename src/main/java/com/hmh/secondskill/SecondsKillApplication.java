package com.hmh.secondskill;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.system.ApplicationPidFileWriter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.function.Consumer;

@Slf4j
@SpringBootApplication
@EnableTransactionManagement
@EnableKafka
//@ComponentScan("com.hmh.secondskill")
public class SecondsKillApplication {

	/**
	 * @author hmh
	 * @date: 3/6 20:49
	 */
	public static void main(String[] args) {
		SpringApplication.run(SecondsKillApplication.class, args);
//		new SpringApplicationBuilder(SecondsKillApplication.class).
//				listeners(new ApplicationPidFileWriter()).run(args);
//		Consumer consumer = SpringBeanFactory.getBean(Consumer.class);
//		new Thread(consumer, "消费者").start();
//		log.info("消费者线程启动成功!");
	}

}
