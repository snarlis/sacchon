package util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.io.FileWriter;   // Import the FileWriter class
import java.io.IOException;  // Import the IOException class to handle errors
import java.util.Date;

public class Logger {
    @Data
    private static class LoginAuditObject{
        private String username;
        private String role;
        private boolean isSuccessful;
        private Date loginTimeStamp;
    }

    public static void loginAudit(String username,String role,boolean isSuccessful) {
        try {
            LoginAuditObject loginAuditObject = new LoginAuditObject();
            loginAuditObject.setUsername(username);
            loginAuditObject.setRole(role);
            loginAuditObject.setSuccessful(isSuccessful);
            loginAuditObject.setLoginTimeStamp(new Date());

            ObjectMapper mapper = new ObjectMapper();
            //Converting the Object to JSONString
            String jsonString = mapper.writeValueAsString(loginAuditObject);

            FileWriter myWriter = new FileWriter("LoginAuditLog.txt",true);
            myWriter.write(jsonString);
            myWriter.write("\r\n");
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}