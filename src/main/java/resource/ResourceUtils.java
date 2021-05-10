package resource;

import exception.AuthorizationException;
import io.jsonwebtoken.Claims;
import org.restlet.data.Header;
import org.restlet.resource.ServerResource;
import org.restlet.util.Series;
import security.JWT;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ResourceUtils {

    public static void checkRole(ServerResource serverResource, String role) throws AuthorizationException {
        Series<Header> headers = (Series<Header>) serverResource.getRequestAttributes().get("org.restlet.http.headers");
        String jwt = headers.getFirstValue("Authorization").substring(7);
        Claims claim = JWT.decodeJWT(jwt);
        if(!claim.getSubject().toLowerCase().equals(role)){
            throw new AuthorizationException("You're not authorized to send this call.");
        }
    }

    public static void checkPerson(ServerResource serverResource,long personId) throws AuthorizationException {
        Series<Header> headers = (Series<Header>) serverResource.getRequestAttributes().get("org.restlet.http.headers");
        String jwt = headers.getFirstValue("Authorization").substring(7);
        Claims claim = JWT.decodeJWT(jwt);
        if(!claim.getId().equals(String.valueOf(personId))){
            throw new AuthorizationException("You're not authorized to send this call.");
        }
    }

    public static void checkIfTokenExpired(ServerResource serverResource) throws AuthorizationException {
        Series<Header> headers = (Series<Header>) serverResource.getRequestAttributes().get("org.restlet.http.headers");
        String jwt = headers.getFirstValue("Authorization").substring(7);
        Claims claim = JWT.decodeJWT(jwt);
        if(claim.getExpiration().compareTo(new Date()) < 0 ){
            throw new AuthorizationException("Access token expired");
        }
    }

    public static Date stringToDate(String date, int offset) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(formatter.parse(date));
            c.add(Calendar.DAY_OF_MONTH, offset);
            return c.getTime();
        } catch (ParseException e) {
            return null;
        }
    }

    public static String dateToString(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
        return formatter.format(date);
    }
}
