package resource;

import exception.AuthorizationException;
import jpaUtil.JpaUtil;
import model.Carb;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import repository.CarbRepository;
import representation.CarbRepresentation;
import security.JWT;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CarbListResource extends ServerResource {
    @Get("json")
    public List<CarbRepresentation> getCarb() throws AuthorizationException {
        ResourceUtils.checkRole(this, JWT.ROLE_CHIEF_DOCTOR);
        ResourceUtils.checkIfTokenExpired(this);

        EntityManager em = JpaUtil.getEntityManager();
        CarbRepository carbRepository = new CarbRepository(em);
        List<Carb> carbs = carbRepository.findAll();
        em.close();

        List<CarbRepresentation> carbRepresentationList = new ArrayList<>();
        for (Carb p : carbs)
            carbRepresentationList.add(new CarbRepresentation(p));

        return carbRepresentationList;
    }

    @Post("json")
    public CarbRepresentation add(CarbRepresentation carbRepresentationIn) throws AuthorizationException {
        ResourceUtils.checkRole(this, JWT.ROLE_CHIEF_DOCTOR);
        ResourceUtils.checkIfTokenExpired(this);

        if (carbRepresentationIn == null) return null;

        Carb carb = carbRepresentationIn.createCarb();
        if (carb.getDate() == null) carb.setDate(new Date());

        EntityManager em = JpaUtil.getEntityManager();
        CarbRepository carbRepository = new CarbRepository(em);
        carbRepository.save(carb);
        CarbRepresentation p = new CarbRepresentation(carb);
        return p;
    }
}
