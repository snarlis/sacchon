package resource;

import jpaUtil.JpaUtil;
import model.ChiefDoctor;
import model.Doctor;
import model.Patient;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import repository.ChiefDoctorRepository;
import repository.DoctorRepository;
import repository.PatientRepository;
import security.JWT;

import javax.persistence.EntityManager;


public class LogInResource extends ServerResource {


    @Get
    public String logIn()  {
        String username = getQueryValue("username");
        String password = getQueryValue("password");
        String returnValue = "Invalid credentials";

        Patient patient = isPatient(username, password);
        if (patient != null) {
            String jwt = JWT.createJWT(String.valueOf(patient.getId()),"sacchon","patient" );
            returnValue = "{\n\"jwt\":\""+jwt+"\"}"; //could be done via a cleaner way
            resetFlag(patient);
        }

        Doctor doctor = isDoctor(username, password);
        if (doctor != null) {
            String jwt = JWT.createJWT(String.valueOf(doctor.getId()),"sacchon","doctor" );
            returnValue = "{\n\"jwt\":\""+jwt+"\"}"; //could be done via a cleaner way
        }

        ChiefDoctor chiefDoctor = isChiefDoctor(username, password);
        if (chiefDoctor != null) {
            String jwt = JWT.createJWT(String.valueOf(chiefDoctor.getId()),"sacchon","chiefDoctor" );
            returnValue = "{\n\"jwt\":\""+jwt+"\"}"; //could be done via a cleaner way
        }

        return returnValue;
    }

    public Patient isPatient(String username, String password) {
        EntityManager em = JpaUtil.getEntityManager();
        PatientRepository patientRepository = new PatientRepository(em);
        Patient patient = patientRepository.getByUsername(username);
        if (patient != null) {
            if (patient.getUsername().equals(username) && patient.getPassword().equals(password)) {
                return patient;
            }
        }
        return null;
    }

    public Doctor isDoctor(String username, String password) {
        EntityManager em = JpaUtil.getEntityManager();
        DoctorRepository doctorRepository = new DoctorRepository(em);
        Doctor doctor = doctorRepository.getByUsername(username);
        if (doctor != null) {
            if (doctor.getUsername().equals(username) && doctor.getPassword().equals(password)) {
                return doctor;
            }
        }
        return null;
    }

    public ChiefDoctor isChiefDoctor(String username, String password) {
        EntityManager em = JpaUtil.getEntityManager();
        ChiefDoctorRepository chiefDoctorRepository = new ChiefDoctorRepository(em);
        ChiefDoctor chiefDoctor = chiefDoctorRepository.getByUsername(username);
        if (chiefDoctor != null) {
            if (chiefDoctor.getUsername().equals(username) && chiefDoctor.getPassword().equals(password)) {
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
