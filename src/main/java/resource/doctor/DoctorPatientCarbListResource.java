package resource.doctor;

import exception.AuthorizationException;
import jpaUtil.JpaUtil;
import model.Carb;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import repository.CarbRepository;
import repository.PatientRepository;
import representation.CarbRepresentation;
import resource.ResourceUtils;
import security.JWT;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

public class DoctorPatientCarbListResource extends ServerResource {
    private long patientId;

    protected void doInit() {

        patientId = Long.parseLong(getAttribute("patientId"));
    }


    @Get("json")
    public List<CarbRepresentation> getCarbList() throws AuthorizationException {
        ResourceUtils.checkRole(this, JWT.ROLE_DOCTOR);
        ResourceUtils.checkIfTokenExpired(this);

        EntityManager em = JpaUtil.getEntityManager();

        PatientRepository patientRepository = new PatientRepository(em);
        List<Carb> carbList = patientRepository.getCarbList(this.patientId);
        List<CarbRepresentation> carbRepresentationList = new ArrayList<>();

        for (Carb c : carbList) {
            carbRepresentationList.add(new CarbRepresentation(c));
        }

        em.close();
        return carbRepresentationList;
    }

    @Post("json")
    public CarbRepresentation addCarb(CarbRepresentation carbRepresentationIn) throws AuthorizationException {
        ResourceUtils.checkRole(this, JWT.ROLE_DOCTOR);
        ResourceUtils.checkIfTokenExpired(this);

        if (carbRepresentationIn == null) return null;

        carbRepresentationIn.setPatientId(this.patientId);
        Carb carb = carbRepresentationIn.createCarb();
        EntityManager em = JpaUtil.getEntityManager();
        CarbRepository carbRepository = new CarbRepository(em);
        carbRepository.save(carb);
        CarbRepresentation c = new CarbRepresentation(carb);
        return c;
    }

}