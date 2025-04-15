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
import vn.hoidanit.jobhunter.domain.Resume;
import vn.hoidanit.jobhunter.domain.respone.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.respone.resume.ResCreateResumeDTO;
import vn.hoidanit.jobhunter.domain.respone.resume.ResFetchResumeDTO;
import vn.hoidanit.jobhunter.domain.respone.resume.ResUpdateResumeDTO;
import vn.hoidanit.jobhunter.service.ResumeService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class ResumeController {
    private ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping("/resumes")
    @ApiMessage("create resumes success")
    public ResponseEntity<ResCreateResumeDTO> createNewResume(@Valid @RequestBody Resume resume)
            throws IdInvalidException {
        // check id exist
        boolean isIdExist = this.resumeService.checkResumeExistByUserAndJob(resume);
        if (!isIdExist) {
            throw new IdInvalidException("User id/job id không tồn tại");
        }

        ResCreateResumeDTO currentResume = this.resumeService.create(resume);
        return ResponseEntity.status(HttpStatus.CREATED).body(currentResume);
    }

    @PutMapping("/resumes")
    @ApiMessage("update resumes success")
    public ResponseEntity<ResUpdateResumeDTO> updateResume(@RequestBody Resume resume) throws IdInvalidException {
        Optional<Resume> reqResumeOptional = this.resumeService.fetchById(resume.getId());
        boolean existResumeById = this.resumeService.existById(resume.getId());
        if (existResumeById != true) {
            throw new IdInvalidException("Resumne với id = " + resume.getId() + " không tồn tại");
        }
        Resume reqResume = reqResumeOptional.get();
        reqResume.setStatus(resume.getStatus());
        ResUpdateResumeDTO currentResume = this.resumeService.update(reqResume);
        return ResponseEntity.status(HttpStatus.OK).body(currentResume);
    }

    @DeleteMapping("/resumes/{id}")
    @ApiMessage("delete resumes success")
    public ResponseEntity<Void> deleteResume(@PathVariable("id") long id) throws IdInvalidException {
        boolean existById = this.resumeService.existById(id);
        if (existById != true) {
            throw new IdInvalidException("Id " + id + " không tồn tại");
        }
        this.resumeService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @GetMapping("/resumes")
    @ApiMessage("fetch all resume")
    public ResponseEntity<ResultPaginationDTO> findAllResume(
            @Filter Specification<Resume> spec,
            Pageable pageable) {

        ResultPaginationDTO Resumes = this.resumeService.fetchAllResume(spec, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(Resumes);
    }

    @GetMapping("/resumes/{id}")
    @ApiMessage("fetch resume by id")
    public ResponseEntity<ResFetchResumeDTO> findJobById(@PathVariable("id") long id) throws IdInvalidException {
        Optional<Resume> resumeOptional = this.resumeService.fetchById(id);
        if (!resumeOptional.isPresent()) {
            throw new IdInvalidException("Resume co id = " + id + " không tồn tại");
        }

        return ResponseEntity.status(HttpStatus.OK).body(this.resumeService.getResume(resumeOptional.get()));
    }
}
