package com.spearhead.agidoda;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AgidodaApplication implements CommandLineRunner {
	public static void main(String[] args) {
		SpringApplication.run(AgidodaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		String objective="Completing the first working example.\n" +
				"1.	Read Paragraph\n"+
				"2. Create Verb Question Table\n" +
				"3. By reading Dictionary Model.\n";

		System.out.println("Father Month Feb");
		System.out.println(objective);
		//System.exit(0);
	}
}
