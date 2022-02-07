package com.jitendra.javaspearhead;

import com.jitendra.javaspearhead.learning.CommonUtilExample;
import com.jitendra.javaspearhead.learning.services.KaveriWinter;
import com.jitendra.javaspearhead.learning.services.PoSEntity3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JavaSpearheadApplication implements CommandLineRunner {

	@Autowired
	CommonUtilExample commonUtilExample;

	@Autowired
    KaveriWinter kaveriWinter;

	@Autowired
	PoSEntity3  poSEntity3;
	public static void main(String[] args) {
		SpringApplication.run(JavaSpearheadApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		kaveriWinter.execute();
	}
}
