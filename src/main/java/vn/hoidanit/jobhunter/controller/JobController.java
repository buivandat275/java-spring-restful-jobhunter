package vn.hoidanit.jobhunter.controller;


import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Job;

import vn.hoidanit.jobhunter.domain.respone.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.respone.job.ResCreateJobDTO;
import vn.hoidanit.jobhunter.domain.respone.job.ResUpdateJobDTO;
import vn.hoidanit.jobhunter.service.JobService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;
@RestController
@RequestMapping("/api/v1")
public class JobController {
    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping("/jobs")
    @ApiMessage("create jobs success")
    public ResponseEntity<ResCreateJobDTO> createNewUser(@Valid @RequestBody Job job)
            throws IdInvalidException {

        ResCreateJobDTO currentJob = this.jobService.create(job);
        return ResponseEntity.status(HttpStatus.CREATED).body(currentJob);
        // cach2: return ResponseEntity.ok(newUser);
    }

    @PutMapping("/jobs")
    @ApiMessage("update job success")
    public ResponseEntity<ResUpdateJobDTO> updateJob(@RequestBody Job job) throws IdInvalidException
    {   
        Optional<Job> currentJob = this.jobService.fetchJobById(job.getId()); 
        boolean existJobById = this.jobService.existById(job.getId());
        if(existJobById != true){
            throw new IdInvalidException("Job với id = "+job.getId()+" không tồn tại");
        }
        return ResponseEntity.ok().body(this.jobService.updateJob(job, currentJob.get()));
    }

    @DeleteMapping("/jobs/{id}")
    @ApiMessage("delete job success")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") long id) throws IdInvalidException {
        boolean existById = this.jobService.existById(id);
        if (existById != true) {
            throw new IdInvalidException("Id " + id + " không tồn tại");
        }
        this.jobService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @GetMapping("/jobs")
    @ApiMessage("fetch all job")
    public ResponseEntity<ResultPaginationDTO> findAllUser(
            @Filter Specification<Job> spec,
            Pageable pageable) {

        ResultPaginationDTO Jobs = this.jobService.fetchAllJob(spec, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(Jobs);
    }

    @GetMapping("/jobs/{id}")
    @ApiMessage("fetch job by id")
    public ResponseEntity<Job> findJobById(@PathVariable("id") long id) throws IdInvalidException {
        Optional<Job> job = this.jobService.fetchJobById(id);
        if (!job.isPresent()) {
            throw new IdInvalidException("Job co id = " + id + " không tồn tại");
        }

        return ResponseEntity.status(HttpStatus.OK).body(job.get());
    }
}
