package resource.chiefDoctor;

import exception.AuthorizationException;
import jpaUtil.JpaUtil;
import model.Consultation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import repository.DoctorRepository;
import representation.ConsultationRepresentation;
import resource.ResourceUtils;
import security.JWT;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ReportDoctorConsultationListResource extends ServerResource {
    private long doctorId;

    protected void doInit() {
        doctorId = Long.parseLong(getAttribute("doctorId"));
    }

    @Get
    public List<ConsultationRepresentation> getConsultationList() throws AuthorizationException {
        ResourceUtils.checkRole(this, JWT.ROLE_CHIEF_DOCTOR);
        ResourceUtils.checkIfTokenExpired(this);

        String period = getQueryValue("period");
        Date date1 = ResourceUtils.stringToDate(period, 0);
        Date date = new Date();
        long diff = date.getTime() - date1.getTime();
        Long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

        EntityManager em = JpaUtil.getEntityManager();
        DoctorRepository doctorRepository = new DoctorRepository(em);

        List<Consultation> consultationList = doctorRepository.getConsultationList(doctorId, days);
        List<ConsultationRepresentation> consultationRepresentationList = new ArrayList<>();

        for (Consultation p : consultationList)
            consultationRepresentationList.add(new ConsultationRepresentation(p));

        em.close();
        return consultationRepresentationList;

    }
}