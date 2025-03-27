package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.respone.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.SkillRepository;

@Service
public class SkillService {

    private final SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository){
        this.skillRepository = skillRepository;
    }

    public Skill handleCreateCompany(Skill skill){
        return this.skillRepository.save(skill);
    }

   public boolean existByName(String skill){
        return this.skillRepository.existsByName(skill);
    }

    public boolean existById(Long id){
        return this.skillRepository.existsById(id);
    }

    public Skill fetchSkillById(Long id){
       Optional<Skill> skillOptional = this.skillRepository.findById(id);
        if(skillOptional.isPresent()){
          return  skillOptional.get();
        }
       return null;
    }
    public Skill handleUpdateSkill(Skill skill){
        Skill currentSkill = this.fetchSkillById(skill.getId());
        if(currentSkill != null){
            currentSkill.setName(skill.getName());
            currentSkill = this.skillRepository.save(currentSkill);
        }
        return currentSkill;

    }

     public ResultPaginationDTO fetchAllSkill(Specification<Skill> spec, Pageable pageable) {
        Page<Skill> pageSkill = this.skillRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageSkill.getTotalPages());
        mt.setTotal(pageSkill.getTotalElements());

        rs.setMeta(mt);
        List<Skill> listSkill = pageSkill.getContent();
        rs.setResult(listSkill);
        return rs;
    }




}
