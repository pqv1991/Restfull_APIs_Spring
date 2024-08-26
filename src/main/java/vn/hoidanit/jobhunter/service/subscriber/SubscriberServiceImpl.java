package vn.hoidanit.jobhunter.service.subscriber;

import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.Subscriber;
import vn.hoidanit.jobhunter.domain.dto.convertDTO.ConvertToResEmailDTO;
import vn.hoidanit.jobhunter.domain.dto.email.ResEmailJobDTO;
import vn.hoidanit.jobhunter.repository.JobRepository;
import vn.hoidanit.jobhunter.repository.SkillRepository;
import vn.hoidanit.jobhunter.repository.SubscriberRepository;
import vn.hoidanit.jobhunter.service.email.EmailService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubscriberServiceImpl implements SubscriberService {
    private final SubscriberRepository subscriberRepository;
    private final SkillRepository skillRepository;
    private final EmailService emailService;
    private final JobRepository jobRepository;
    public SubscriberServiceImpl(SubscriberRepository subscriberRepository, SkillRepository skillRepository, EmailService emailService, JobRepository jobRepository) {
        this.subscriberRepository = subscriberRepository;
        this.skillRepository = skillRepository;
        this.emailService = emailService;

        this.jobRepository = jobRepository;
    }

    @Override
    public boolean isEmail(String email) {
        return subscriberRepository.existsByEmail(email);
    }

    @Override
    public Subscriber handleCreateSubscriber(Subscriber subscriber) {
       //check skill
        if(subscriber.getSkills() !=null){
            List<Long> listIdSkill = subscriber.getSkills().stream().map(x->x.getId()).collect(Collectors.toList());
            List<Skill> skillList = skillRepository.findByIdIn(listIdSkill);
            subscriber.setSkills(skillList);
        }
        return subscriberRepository.save(subscriber);
    }

    @Override
    public Subscriber handleUpdateSubscriber(Subscriber subscriberDb,Subscriber subscriber) {
        //check skill
        if(subscriber.getSkills() !=null){
            List<Long> listIdSkill = subscriber.getSkills().stream().map(x->x.getId()).collect(Collectors.toList());
            List<Skill> skillList = skillRepository.findByIdIn(listIdSkill);
            subscriberDb.setSkills(skillList);
        }
        return subscriberRepository.save(subscriberDb);
    }

    @Override
    public Subscriber fetchSubscriberById(long id) {
        return subscriberRepository.findById(id).get();
    }

    @Override
    public void sendSubscriberEmailJobs() {
        List<Subscriber> listSubs = this.subscriberRepository.findAll();
        if (listSubs != null && listSubs.size() > 0) {
            for (Subscriber sub : listSubs) {
                List<Skill> listSkills = sub.getSkills();
                if (listSkills != null && listSkills.size() > 0) {
                    List<Job> listJobs = this.jobRepository.findBySkillsIn(listSkills);
                    if (listJobs != null && listJobs.size() > 0) {
                    List<ResEmailJobDTO> arr = listJobs.stream().map(job -> ConvertToResEmailDTO.convertResEmailJobDTO(job)).collect(Collectors.toList());
                        this.emailService.sendEmailFromTemplateSync(
                                sub.getEmail(),
                                "Cơ hội việc làm hot đang chờ đón bạn, khám phá ngay",
                                "job",
                                sub.getName(),
                                arr);
                    }
                }
            }
        }
    }

    @Override
    public Subscriber getSubscriberByEmail(String email) {
        return subscriberRepository.findByEmail(email);
    }
}
