package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.turkraft.springfilter.builder.FilterBuilder;
import com.turkraft.springfilter.converter.FilterSpecification;
import com.turkraft.springfilter.converter.FilterSpecificationConverter;
import com.turkraft.springfilter.parser.FilterParser;
import com.turkraft.springfilter.parser.node.FilterNode;

import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Resume;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.respone.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.respone.resume.ResCreateResumeDTO;
import vn.hoidanit.jobhunter.domain.respone.resume.ResFetchResumeDTO;
import vn.hoidanit.jobhunter.domain.respone.resume.ResUpdateResumeDTO;
import vn.hoidanit.jobhunter.repository.JobRepository;
import vn.hoidanit.jobhunter.repository.ResumeRepository;
import vn.hoidanit.jobhunter.repository.UserRepository;
import vn.hoidanit.jobhunter.util.SecurityUtil;

@Service
public class ResumeService {
     @Autowired
    FilterBuilder fb;

    @Autowired
    private FilterParser filterParser;

    @Autowired
    private FilterSpecificationConverter filterSpecificationConverter;

    private ResumeRepository resumeRepository;
    private JobRepository jobRepository;
    private UserRepository userRepository;

    public ResumeService(ResumeRepository resumeRepository, JobRepository jobRepository,
            UserRepository userRepository) {
        this.resumeRepository = resumeRepository;
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
    }

    public boolean checkResumeExistByUserAndJob(Resume resume) {
        if (resume.getUser() == null)
            return false;
        Optional<User> userOptional = this.userRepository.findById(resume.getUser().getId());
        if (userOptional.isEmpty())
            return false;

        if (resume.getJob() == null)
            return false;
        Optional<Job> jobOptional = this.jobRepository.findById(resume.getJob().getId());
        if (jobOptional.isEmpty())
            return false;

        return true;
    }

    public ResCreateResumeDTO create(Resume resume) {

        resume = this.resumeRepository.save(resume);
        // convert responsy
        ResCreateResumeDTO dto = new ResCreateResumeDTO();
        dto.setId(resume.getId());
        dto.setCreatedBy(resume.getCreatedBy());
        dto.setCreatedAt(resume.getCreatedAt());

        return dto;
    }

    public boolean existById(long id){
        return this.resumeRepository.existsById(id);
    }

    public Optional<Resume> fetchById(long id){
        return this.resumeRepository.findById(id);
    }
    public ResUpdateResumeDTO update(Resume resume){
        resume = this.resumeRepository.save(resume);
        // convert responsy
        ResUpdateResumeDTO dto = new ResUpdateResumeDTO();
        dto.setStatus(resume.getStatus());
        dto.setUpdatedAt(resume.getUpdatedAt());
        dto.setUpdatedBy(resume.getUpdatedBy());

        return dto;

    }

    public void delete(long id){
        this.resumeRepository.deleteById(id);
    }

    public ResFetchResumeDTO getResume(Resume resume){
        ResFetchResumeDTO res = new ResFetchResumeDTO();
        res.setId(resume.getId());
        res.setEmail(resume.getEmail());
        res.setUrl(resume.getUrl());
        res.setStatus(resume.getStatus());
        res.setCreatedBy(resume.getCreatedBy());
        res.setCreatedAt(resume.getCreatedAt());
        res.setUpdatedAt(resume.getUpdatedAt());
        res.setUpdatedBy(resume.getUpdatedBy());

        if(resume.getJob() != null){
            res.setCompanyName(resume.getJob().getCompany().getName());
        }

        res.setUser(new ResFetchResumeDTO.UserResume(resume.getUser().getId(), resume.getUser().getName()));
        res.setJob(new ResFetchResumeDTO.JobResume(resume.getJob().getId(), resume.getJob().getName()));
        return res;
    }

    public ResultPaginationDTO fetchAllResume(Specification<Resume> spec, Pageable pageable) {
        Page<Resume> pageResume = this.resumeRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageResume.getTotalPages());
        mt.setTotal(pageResume.getTotalElements());

        rs.setMeta(mt);

        List<ResFetchResumeDTO> listResume = pageResume.getContent()
                    .stream().map(item -> this.getResume(item))
                    .collect(Collectors.toList());
        rs.setResult(listResume);
        return rs;
    }
   public ResultPaginationDTO fetchResumeByUser(Pageable pageable){
    // query builder
    String email =  SecurityUtil.getCurrentUserLogin().isPresent() == true ? SecurityUtil.getCurrentUserLogin().get() : "" ;

    FilterNode node = filterParser.parse("email='" + email + "'");
    FilterSpecification<Resume> spec = filterSpecificationConverter.convert(node);
    Page<Resume> pageResume = this.resumeRepository.findAll(spec, pageable);
    
    ResultPaginationDTO rs = new ResultPaginationDTO();
    ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

    mt.setPage(pageable.getPageNumber() + 1);
    mt.setPageSize(pageable.getPageSize());

    mt.setPages(pageResume.getTotalPages());
    mt.setTotal(pageResume.getTotalElements());

    rs.setMeta(mt);

    // remove sensitive data
    List<ResFetchResumeDTO> listResume = pageResume.getContent()
            .stream().map(item -> this.getResume(item))
            .collect(Collectors.toList());

    rs.setResult(listResume);

    return rs;
   }

}
