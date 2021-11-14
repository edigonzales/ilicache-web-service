package ch.so.agi.ilicache;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
// Umweg via spring.config.location ist nötig, weil PropertySource direkt nur mit
// Properties-Files umgehen kann.
@TestPropertySource(properties = { "spring.config.location = classpath:application-test.yml" })
class IlicacheWebServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
