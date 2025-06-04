package br.com.cdb.BandoDigitalFinal2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class BandoDigitalFinal2Application {

	public static void main(String[] args) {
		SpringApplication.run(BandoDigitalFinal2Application.class, args);
	}

}
