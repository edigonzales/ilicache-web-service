package ch.so.agi.ilicache;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;

// Umweg via spring.config.location ist nötig, weil PropertySource direkt nur mit
// Properties-Files umgehen kann.
//@TestPropertySource(properties = { "spring.config.location = classpath:application.yml" })
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class IlicacheWebServiceApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

	@Test
	void contextLoads() {
	    System.out.println("Hallo Test.");
	}
	
    @Test
    public void index_Ok() throws Exception {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/mirror/ilisite.xml", String.class))
                .contains("DATASECTION");
    }


}
