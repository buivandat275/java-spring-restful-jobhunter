package vn.hoidanit.jobhunter.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.web.bind.annotation.RestController;

import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Skill;

@RestController
public interface JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job>{
    List<Job> findBySkillsIn(List<Skill> skills);
}
