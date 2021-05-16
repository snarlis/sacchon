package security;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class JWTTest {

    @Test
    void testJWT(){
        String jwt = JWT.createJWT("1","sacchon","patient");
        Claims claim = JWT.decodeJWT(jwt);
        assertEquals(claim.getId(),"1");
        assertEquals(claim.getIssuer(),"sacchon");
        assertEquals(claim.getSubject(),"role");
    }

}
