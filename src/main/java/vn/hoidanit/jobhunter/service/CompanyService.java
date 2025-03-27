package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.respone.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.CompanyRepository;
import vn.hoidanit.jobhunter.repository.UserRepository;

@Service
public class CompanyService {

    private final UserRepository userRepository;

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository, UserRepository userRepository) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
    }

    public Company handleCreateCompany(Company company) {
        return this.companyRepository.save(company);
    }

    public ResultPaginationDTO fetchAllCompanies(Specification<Company> spec, Pageable pageable) {
        Page<Company> pageCompany = this.companyRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageCompany.getNumber() + 1);
        mt.setPageSize(pageCompany.getSize());

        mt.setPages(pageCompany.getTotalPages());
        mt.setTotal(pageCompany.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(pageCompany.getContent());
        return rs;
    }

    public Company handleUpdateCompany(Company reqCompany) {
        Company company = this.companyRepository.findCompanyById(reqCompany.getId());
        if (company != null) {
            company.setName(reqCompany.getName());
            company.setDescription(reqCompany.getDescription());
            company.setAddress(reqCompany.getAddress());
            company.setLogo(reqCompany.getLogo());
            company = this.companyRepository.save(company);
        }
        return company;
    }

    public void handleDeleteCompany(long id){
        Optional<Company> comOptional = this.companyRepository.findById(id);
        if(comOptional.isPresent()){
            Company com = comOptional.get();

            List<User> users = this.userRepository.findByCompany(com);
            this.userRepository.deleteAll(users);
        }

         this.companyRepository.deleteById(id);

    }

    public Optional<Company> findById(long id){
        return this.companyRepository.findById(id);
    }

 
}
