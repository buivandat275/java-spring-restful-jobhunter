package vn.hoidanit.jobhunter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.web.bind.annotation.RestController;

import vn.hoidanit.jobhunter.domain.Job;

@RestController
public interface JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job>{
    
}
