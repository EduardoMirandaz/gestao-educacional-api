package br.com.dbccompany.time7.gestaodeensino;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
//@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
@SpringBootApplication
public class GestaoDeEnsinoVemSerApplication {
	public static void main(String[] args) {
		SpringApplication.run(GestaoDeEnsinoVemSerApplication.class, args);
	}

}
