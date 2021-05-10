package resource.patient;

import exception.AuthorizationException;
import jpaUtil.JpaUtil;
import model.Patient;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;
import repository.PatientRepository;
import representation.PatientRepresentation;
import resource.ResourceUtils;
import security.JWT;

import javax.persistence.EntityManager;

public class PatientSettingsResource extends ServerResource {
    private long id;

    protected void doInit() {
        id = Long.parseLong(getAttribute("id"));
    }


    @Get("json")
    public PatientRepresentation getPatient() throws AuthorizationException {
        ResourceUtils.checkRole(this, JWT.ROLE_PATIENT);
        ResourceUtils.checkIfTokenExpired(this);

        EntityManager em = JpaUtil.getEntityManager();
        PatientRepository patientRepository = new PatientRepository(em);
        Patient patient = patientRepository.read(id);
        PatientRepresentation patientRepresentation = new PatientRepresentation(patient);
        em.close();
        return patientRepresentation;
    }

    @Put("json")
    public PatientRepresentation updatePatient(PatientRepresentation patientRepresentation) throws AuthorizationException {
        ResourceUtils.checkRole(this, JWT.ROLE_PATIENT);
        ResourceUtils.checkIfTokenExpired(this);
        ResourceUtils.checkPerson(this, id);

        EntityManager em = JpaUtil.getEntityManager();
        PatientRepository patientRepository = new PatientRepository(em);
        Patient patient = patientRepresentation.createPatient();
        Patient oldPatient = patientRepository.read(id);
        em.detach(patient);
        patient.setId(id);
        patient.setDateRegistered(oldPatient.getDateRegistered());
        patientRepository.update(patient);

        return patientRepresentation;
    }

    @Delete("json")
    public void deletePatient() throws AuthorizationException {
        ResourceUtils.checkRole(this, JWT.ROLE_PATIENT);
        ResourceUtils.checkIfTokenExpired(this);
        ResourceUtils.checkPerson(this, id);

        EntityManager em = JpaUtil.getEntityManager();
        PatientRepository patientRepository = new PatientRepository(em);
        patientRepository.delete(patientRepository.read(id).getId());
    }

}
