package resource;

import exception.AuthorizationException;
import jpaUtil.JpaUtil;
import model.Glucose;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import repository.GlucoseRepository;
import representation.GlucoseRepresentation;
import security.JWT;

import javax.persistence.EntityManager;

public class GlucoseResource extends ServerResource{
    private long id;

    protected void doInit(){
        id= Long.parseLong(getAttribute("id"));
    }


    @Get("json")
    public GlucoseRepresentation getGlucose() throws AuthorizationException {
        ResourceUtils.checkRole(this, JWT.ROLE_CHIEF_DOCTOR);
        ResourceUtils.checkIfTokenExpired(this);

        EntityManager em= JpaUtil.getEntityManager();
        GlucoseRepository glucoseRepository= new GlucoseRepository(em);
        Glucose glucose= glucoseRepository.read(id);
        GlucoseRepresentation glucoseRepresentation= new GlucoseRepresentation(glucose);
        em.close();
        return glucoseRepresentation;
    }
}
