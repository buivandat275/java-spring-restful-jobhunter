package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.respone.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.CompanyService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import java.net.http.HttpRequest;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@RestController
@RequestMapping("/api/v1")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/companies")
    @ApiMessage("create company success")
    public ResponseEntity<Company> createCompany(@Valid @RequestBody Company postManCompany) {
        Company newCompany = this.companyService.handleCreateCompany(postManCompany);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCompany);
    }

    @GetMapping("/companies")
    @ApiMessage("fetch all company")
    public ResponseEntity<ResultPaginationDTO> getAllCompany(
        @Filter Specification<Company> spec,
        Pageable pageable
       ) {
        ResultPaginationDTO companies = this.companyService.fetchAllCompanies(spec, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(companies);
    }

    @PutMapping("/companies")
    @ApiMessage("update company success ")
    public ResponseEntity<Company> updateCompany(@Valid @RequestBody Company reqCompany) {
        Company company = this.companyService.handleUpdateCompany(reqCompany);
        return ResponseEntity.status(HttpStatus.OK).body(company);
    }

    @DeleteMapping("/companies/{id}")
    @ApiMessage("delete company success")
    public ResponseEntity<Void> deleteCompany(@PathVariable("id") long id) throws IdInvalidException {
        this.companyService.handleDeleteCompany(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

}
