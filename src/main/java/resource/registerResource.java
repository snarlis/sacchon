package resource;

import com.google.common.hash.Hashing;
import exception.AuthorizationException;
import jpaUtil.JpaUtil;
import model.Patient;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import repository.PatientRepository;
import representation.PatientRepresentation;

import javax.persistence.EntityManager;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class registerResource extends ServerResource {


    @Post("json")
    public PatientRepresentation add(PatientRepresentation patientRepresentationIn) throws AuthorizationException {

        if (patientRepresentationIn == null) return null;
        if (patientRepresentationIn.getUsername() == null) return null;
        if (patientRepresentationIn.getPassword() == null) return null;

        Patient patient = patientRepresentationIn.createPatient();
        if (patientRepresentationIn.getDateRegistered() == null) patient.setDateRegistered(new Date());
        patient.setPassword(Hashing.sha256()
                .hashString(patient.getPassword(), StandardCharsets.UTF_8)
                .toString());
        EntityManager em = JpaUtil.getEntityManager();
        PatientRepository patientRepository = new PatientRepository(em);
        patientRepository.save(patient);
        PatientRepresentation p = new PatientRepresentation(patient);
        em.close();
        return p;
    }

}
