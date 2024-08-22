package vn.hoidanit.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.dto.pagination.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.skill.SkillService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class SkillController {
    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @PostMapping("/skills")
    public ResponseEntity<Skill> createSkills(@Valid @RequestBody Skill skill) throws IdInvalidException {
        //check name
        if(skill.getName() != null && skillService.isNameExists(skill.getName())){
            throw new IdInvalidException("Skill name = " + skill.getName() + " đã tồn tại");
        }
       return ResponseEntity.status(HttpStatus.CREATED).body(skillService.handleCreateSkill(skill));
    }
    @GetMapping("/skills")
    public ResponseEntity<ResultPaginationDTO> getAllSkills(@Filter Specification<Skill> specification, Pageable pageable){
        return ResponseEntity.ok().body(skillService.fetchAllSkills(specification,pageable));
    }
    @PutMapping("/skills")
    public ResponseEntity<Skill> updateSkill(@Valid @RequestBody Skill skill) throws IdInvalidException {
        Optional<Skill> skillDb =  skillService.fetchSkillById(skill.getId());
        if(skillDb.isEmpty()){
            throw  new IdInvalidException("Skill vơi id = "+skill.getId()+" không tồn tại");
        }
        if(skill.getName() != null&& skillService.isNameExists(skill.getName())){
            throw new IdInvalidException("Skill name = " + skill.getName() + " đã tồn tại");
        }
        Skill skillUpdate = skillService.handleUpdateSkill(skill);
        return ResponseEntity.ok().body(skillUpdate);
    }

    @DeleteMapping("/skills/{id}")
    @ApiMessage("Delete a skill")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
        // check id
        Optional<Skill> currentSkill = this.skillService.fetchSkillById(id);
        if (currentSkill.isEmpty()) {
            throw new IdInvalidException("Skill id = " + id + " không tồn tại");
        }
        this.skillService.handleDeleteSkill(id);
        return ResponseEntity.ok().body(null);
    }

}
