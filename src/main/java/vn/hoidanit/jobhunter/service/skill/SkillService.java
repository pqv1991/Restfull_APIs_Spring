package vn.hoidanit.jobhunter.service.skill;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.dto.pagination.ResultPaginationDTO;

import java.util.Optional;

public interface SkillService {
    Skill handleCreateSkill (Skill skill);
    ResultPaginationDTO fetchAllSkills(Specification<Skill> specification, Pageable pageable);
    Optional<Skill> fetchSkillById(long id);

    Skill handleUpdateSkill (Skill skill);
    boolean isNameExists(String name);
     void handleDeleteSkill(long id);
}
