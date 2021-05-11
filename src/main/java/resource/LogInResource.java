package resource;

import com.google.common.hash.Hashing;
import jpaUtil.JpaUtil;
import model.ChiefDoctor;
import model.Doctor;
import model.Patient;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import repository.ChiefDoctorRepository;
import repository.DoctorRepository;
import repository.PatientRepository;
import representation.LoginRepresentation;
import security.JWT;

import javax.persistence.EntityManager;
import java.nio.charset.StandardCharsets;


public class LogInResource extends ServerResource {


    @Post
    public String logIn(LoginRepresentation loginRepresentation)  {
        String username = loginRepresentation.getUsername();
        String password = loginRepresentation.getPassword();
        String hashedPassword = Hashing.sha256()
                .hashString(password, StandardCharsets.UTF_8)
                .toString();
        String returnValue = "Invalid credentials";

        Patient patient = isPatient(username, hashedPassword);
        if (patient != null) {
            String jwt = JWT.createJWT(String.valueOf(patient.getId()),"sacchon","patient" );
            returnValue = "{\n\"jwt\":\""+jwt+"\"}"; //could be done via a cleaner way
            resetFlag(patient);
        }

        Doctor doctor = isDoctor(username, hashedPassword);
        if (doctor != null) {
            String jwt = JWT.createJWT(String.valueOf(doctor.getId()),"sacchon","doctor" );
            returnValue = "{\n\"jwt\":\""+jwt+"\"}"; //could be done via a cleaner way
        }

        ChiefDoctor chiefDoctor = isChiefDoctor(username, hashedPassword);
        if (chiefDoctor != null) {
            String jwt = JWT.createJWT(String.valueOf(chiefDoctor.getId()),"sacchon","chiefDoctor" );
            returnValue = "{\n\"jwt\":\""+jwt+"\"}"; //could be done via a cleaner way
        }

        return returnValue;
    }

    public Patient isPatient(String username, String hashedPassword) {
        EntityManager em = JpaUtil.getEntityManager();
        PatientRepository patientRepository = new PatientRepository(em);
        Patient patient = patientRepository.getByUsername(username);
        if (patient != null) {
            if (patient.getUsername().equals(username) && patient.getPassword().equals(hashedPassword)) {
                return patient;
            }
        }
        return null;
    }

    public Doctor isDoctor(String username, String hashedPassword) {
        EntityManager em = JpaUtil.getEntityManager();
        DoctorRepository doctorRepository = new DoctorRepository(em);
        Doctor doctor = doctorRepository.getByUsername(username);
        if (doctor != null) {
            if (doctor.getUsername().equals(username) && doctor.getPassword().equals(hashedPassword)) {
                return doctor;
            }
        }
        return null;
    }

    public ChiefDoctor isChiefDoctor(String username, String hashedPassword) {
        EntityManager em = JpaUtil.getEntityManager();
        ChiefDoctorRepository chiefDoctorRepository = new ChiefDoctorRepository(em);
        ChiefDoctor chiefDoctor = chiefDoctorRepository.getByUsername(username);

        if (chiefDoctor != null) {
            if (chiefDoctor.getUsername().equals(username) && chiefDoctor.getPassword().equals(hashedPassword)) {
                return chiefDoctor;
            }
        }
        return null;
    }

    public void resetFlag(Patient patient) {
        EntityManager em = JpaUtil.getEntityManager();
        PatientRepository patientRepository = new PatientRepository(em);
        patient.setConsultationChanged(false);
        patientRepository.update(patient);
    }


}
