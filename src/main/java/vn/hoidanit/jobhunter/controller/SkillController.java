package vn.hoidanit.jobhunter.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.respone.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.SkillService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class SkillController {
    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @PostMapping("/skills")
    @ApiMessage("create skill success")
    public ResponseEntity<Skill> createCompany(@Valid @RequestBody Skill skill) throws IdInvalidException {
        if (skill.getName() == null) {
            throw new IdInvalidException("name không được để trống");
        }
        boolean existByName = this.skillService.existByName(skill.getName());
        if (existByName == true) {
            throw new IdInvalidException("SKill: " + skill.getName() + " đã tồn tại");
        }
        Skill newSkill = this.skillService.handleCreateCompany(skill);
        return ResponseEntity.status(HttpStatus.CREATED).body(newSkill);
    }

    @PutMapping("/skills")
    @ApiMessage("update skill success")
    public ResponseEntity<Skill> updateSKill(@Valid @RequestBody Skill skill) throws IdInvalidException {
        boolean existSkillById = this.skillService.existById(skill.getId());
        if (existSkillById != true) {
            throw new IdInvalidException("Skill với id = " + skill.getId() + " không tồn tại");
        }
        boolean existByName = this.skillService.existByName(skill.getName());
        if (existByName == true) {
            throw new IdInvalidException("SKill: " + skill.getName() + " đã tồn tại");
        }
        Skill currentSkill = this.skillService.handleUpdateSkill(skill);
        return ResponseEntity.status(HttpStatus.OK).body(currentSkill);
    }

    @GetMapping("/skills")
    @ApiMessage("fetch all skill")
    public ResponseEntity<ResultPaginationDTO> findAllSkill(
            @Filter Specification<Skill> spec,
            Pageable pageable) {

        ResultPaginationDTO skill = this.skillService.fetchAllSkill(spec, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(skill);
    }

}
