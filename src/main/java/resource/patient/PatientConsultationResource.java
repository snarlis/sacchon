package resource.patient;

import exception.AuthorizationException;
import jpaUtil.JpaUtil;
import model.Consultation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import repository.PatientRepository;
import representation.ConsultationRepresentation;
import resource.ResourceUtils;
import security.JWT;

import javax.persistence.EntityManager;
import java.util.List;

public class PatientConsultationResource extends ServerResource {
    private long patientId;
    private long consultationId;

    protected void doInit() {
        patientId = Long.parseLong(getAttribute("patientId"));
        consultationId = Long.parseLong(getAttribute("consultationId"));
    }


    @Get("json")
    public ConsultationRepresentation getConsultation() throws AuthorizationException {
        ResourceUtils.checkRole(this, JWT.ROLE_PATIENT);
        ResourceUtils.checkIfTokenExpired(this);
        ResourceUtils.checkPerson(this, patientId);

        EntityManager em = JpaUtil.getEntityManager();

        PatientRepository patientRepository = new PatientRepository(em);
        List<Consultation> consultationList = patientRepository.getConsultationList(this.patientId);
        Consultation consultation = new Consultation();
        for (Consultation c : consultationList) {
            if (c.getId() == consultationId) {
                consultation = c;
            }
        }
        ConsultationRepresentation consultationRepresentation = new ConsultationRepresentation(consultation);
        em.close();
        return consultationRepresentation;
    }

}
