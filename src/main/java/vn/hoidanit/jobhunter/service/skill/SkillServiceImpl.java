package vn.hoidanit.jobhunter.service.skill;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.dto.pagination.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.SkillRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SkillServiceImpl implements SkillService{
    private final SkillRepository skillRepository;

    public SkillServiceImpl(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    @Override
    public Skill handleCreateSkill(Skill skill) {
        return skillRepository.save(skill);
    }

    @Override
    public ResultPaginationDTO fetchAllSkills(Specification<Skill> specification, Pageable pageable) {
        Page<Skill> skillPage = skillRepository.findAll(specification,pageable);
        ResultPaginationDTO res = new ResultPaginationDTO();
        ResultPaginationDTO.Meta  mt = new ResultPaginationDTO.Meta();
        mt.setPage(pageable.getPageNumber()+1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(skillPage.getTotalPages());
        mt.setTotal(skillPage.getTotalElements());
        res.setMeta(mt);
        res.setResult(skillPage.getContent());
        return res;
    }

    @Override
    public Optional<Skill> fetchSkillById(long id) {
        return skillRepository.findById(id);
    }

    @Override
    public Skill handleUpdateSkill(Skill skill) {
      Optional<Skill>  currentSkill = skillRepository.findById(skill.getId());
      if(currentSkill.isPresent()){
          Skill skillUpdate = currentSkill.get();
          skillUpdate.setName(skill.getName());
         return skillRepository.save(skillUpdate);
      }
      return null;
    }

    @Override
    public boolean isNameExists(String name) {
        return skillRepository.existsByName(name);
    }

    @Override
    public void handleDeleteSkill(long id) {
        Optional<Skill> skillOptional = skillRepository.findById(id);
        Skill currentSkill = skillOptional.get();
        currentSkill.getJobs().forEach(job -> job.getSkills().remove(currentSkill));
        currentSkill.getSubscribers().forEach(subs->subs.getSkills().remove(currentSkill));

        skillRepository.delete(currentSkill);}
}
