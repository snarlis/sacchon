package resource.chiefDoctor;

import exception.AuthorizationException;
import jpaUtil.JpaUtil;
import model.Doctor;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import repository.DoctorRepository;
import representation.DoctorRepresentation;
import resource.ResourceUtils;
import security.JWT;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DoctorInactiveListResource extends ServerResource {

    @Get("json")
    public List<DoctorRepresentation> getInactiveDoctorList() throws AuthorizationException {
        ResourceUtils.checkRole(this, JWT.ROLE_CHIEF_DOCTOR);
        ResourceUtils.checkIfTokenExpired(this);

        String period = getQueryValue("period");
        Date date1 = ResourceUtils.stringToDate(period, 0);
        Date date = new Date();
        long diff = date.getTime() - date1.getTime();
        Long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

        EntityManager em = JpaUtil.getEntityManager();
        DoctorRepository doctorRepository = new DoctorRepository(em);
        List<Doctor> doctorList = doctorRepository.getInactiveDoctor(days);

        List<DoctorRepresentation> doctorRepresentationList = new ArrayList<>();

        for (Doctor d : doctorList) {
            if (d != null) doctorRepresentationList.add(new DoctorRepresentation(d));
        }
        em.close();

        return doctorRepresentationList;
    }
}
