package resource.patient;

import exception.AuthorizationException;
import jpaUtil.JpaUtil;
import model.Glucose;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;
import repository.GlucoseRepository;
import repository.PatientRepository;
import representation.GlucoseRepresentation;
import resource.ResourceUtils;
import security.JWT;

import javax.persistence.EntityManager;
import java.util.List;

public class PatientGlucoseResource extends ServerResource {
    private long patientId;
    private long glucoseId;

    protected void doInit() {
        patientId = Long.parseLong(getAttribute("patientId"));
        glucoseId = Long.parseLong(getAttribute("glucoseId"));
    }


    @Get("json")
    public GlucoseRepresentation getGlucose() throws AuthorizationException {
        ResourceUtils.checkRole(this, JWT.ROLE_PATIENT);
        ResourceUtils.checkIfTokenExpired(this);
        ResourceUtils.checkPerson(this, patientId);

        EntityManager em = JpaUtil.getEntityManager();
        PatientRepository patientRepository = new PatientRepository(em);
        List<Glucose> glucoseList = patientRepository.getGlucoseList(this.patientId);
        Glucose glucose = new Glucose();
        for (Glucose g : glucoseList) {
            if (g.getId() == glucoseId) {
                glucose = g;
            }
        }
        GlucoseRepresentation glucoseRepresentation = new GlucoseRepresentation(glucose);
        em.close();
        return glucoseRepresentation;
    }

    @Put("json")
    public GlucoseRepresentation updateGlucose(GlucoseRepresentation glucoseRepresentationIn) throws AuthorizationException {
        ResourceUtils.checkRole(this, JWT.ROLE_PATIENT);
        ResourceUtils.checkIfTokenExpired(this);
        ResourceUtils.checkPerson(this, patientId);

        if (glucoseRepresentationIn == null) return null;

        EntityManager em = JpaUtil.getEntityManager();
        GlucoseRepository glucoseRepository = new GlucoseRepository(em);
        Glucose glucose = glucoseRepository.read(glucoseId);
        glucose.setGlucose(glucoseRepresentationIn.getGlucose());

        em.detach(glucose);
        glucose.setId(glucoseId);
        glucoseRepository.update(glucose);
        return glucoseRepresentationIn;
    }

    @Delete("json")
    public void deleteGlucose() throws AuthorizationException {
        ResourceUtils.checkRole(this, JWT.ROLE_PATIENT);
        ResourceUtils.checkIfTokenExpired(this);

        EntityManager em = JpaUtil.getEntityManager();
        GlucoseRepository glucoseRepository = new GlucoseRepository(em);
        ResourceUtils.checkPerson(this, glucoseRepository.read(glucoseId).getPatient().getId());
        glucoseRepository.delete(glucoseRepository.read(glucoseId).getId());
    }
}
