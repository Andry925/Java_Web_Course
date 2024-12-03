package org.example.galacticmarket;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;


@SpringBootApplication(scanBasePackages = {
		"org.example.galacticmarket",
		"featuretoggle.service",
		"featuretoggle"
})
@EnableAspectJAutoProxy
public class DemoGalacticmarketApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoGalacticmarketApplication.class, args);
	}

}
