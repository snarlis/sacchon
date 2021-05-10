package resource.patient;

import exception.AuthorizationException;
import jpaUtil.JpaUtil;
import model.Carb;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;
import repository.CarbRepository;
import repository.PatientRepository;
import representation.CarbRepresentation;
import resource.ResourceUtils;
import security.JWT;

import javax.persistence.EntityManager;
import java.util.List;

public class PatientCarbResource extends ServerResource {
    private long patientId;
    private long carbId;

    protected void doInit() {
        patientId = Long.parseLong(getAttribute("patientId"));
        carbId = Long.parseLong(getAttribute("carbId"));
    }


    @Get("json")
    public CarbRepresentation getCarb() throws AuthorizationException {
        ResourceUtils.checkRole(this, JWT.ROLE_PATIENT);
        ResourceUtils.checkIfTokenExpired(this);

        EntityManager em = JpaUtil.getEntityManager();

        PatientRepository patientRepository = new PatientRepository(em);
        List<Carb> carbList = patientRepository.getCarbList(this.patientId);
        Carb carb = new Carb();

        for (Carb c : carbList) {
            if (c.getId() == carbId) {
                carb = c;
            }
        }
        CarbRepresentation carbRepresentation = new CarbRepresentation(carb);
        em.close();
        return carbRepresentation;
    }

    @Put("json")
    public CarbRepresentation updateCarb(CarbRepresentation carbRepresentation) throws AuthorizationException {
        ResourceUtils.checkRole(this, JWT.ROLE_PATIENT);
        ResourceUtils.checkIfTokenExpired(this);
        ResourceUtils.checkPerson(this, patientId);

        if (carbRepresentation == null) return null;

        carbRepresentation.setId(carbId);
        carbRepresentation.setPatientId(patientId);
        EntityManager em = JpaUtil.getEntityManager();
        CarbRepository carbRepository = new CarbRepository(em);
        Carb carb = carbRepresentation.createCarb();
        em.detach(carb);
        carb.setId(carbId);
        carbRepository.update(carb);
        return carbRepresentation;
    }

    @Delete("json")
    public void deleteCarb() throws AuthorizationException {
        ResourceUtils.checkRole(this, JWT.ROLE_PATIENT);
        ResourceUtils.checkIfTokenExpired(this);
        EntityManager em = JpaUtil.getEntityManager();
        CarbRepository carbRepository = new CarbRepository(em);
        carbRepository.delete(carbRepository.read(carbId).getId());
    }
}