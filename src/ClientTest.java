import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void openRespons() throws ParseException {
        String servResp = "{\"data\":\"{\\\"p3\\\":{\\\"name\\\":\\\"Marcus\\\",\\\"favorite Color\\\":\\\"Green\\\",\\\"age\\\":\\\"34\\\"}}\",\"httpStatusCode\":200}\n";

        //Test
        assertEquals(Client.openRespons(servResp), "Marcus");
        }
}