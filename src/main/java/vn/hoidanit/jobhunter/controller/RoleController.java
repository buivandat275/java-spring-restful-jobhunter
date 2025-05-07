package vn.hoidanit.jobhunter.controller;


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
import vn.hoidanit.jobhunter.domain.Role;
import vn.hoidanit.jobhunter.domain.respone.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.RoleService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class RoleController {
    private RoleService roleService;
    public RoleController(RoleService roleService){
        this.roleService = roleService;
    }

    @PostMapping("/roles")
    @ApiMessage("create roles success")
    public ResponseEntity<Role> createNewRole(@Valid @RequestBody Role role)
            throws IdInvalidException {
        if(this.roleService.existByName(role.getName())){
                    throw new IdInvalidException("Role với name = "+role.getName()+" đã tồn tại");
                }
                
        return ResponseEntity.status(HttpStatus.CREATED).body(this.roleService.create(role));
        // cach2: return ResponseEntity.ok(newUser);
    }

     @PutMapping("/roles")
    @ApiMessage("update roles success")
    public ResponseEntity<Role> updateRole(@RequestBody Role r) throws IdInvalidException
    {   
        boolean existRoleId = this.roleService.existById(r.getId());
        if(existRoleId != true){
            throw new IdInvalidException("Role với id = "+r.getId()+" không tồn tại");
        }

        // if(this.roleService.existByName(r.getName())){
        //     throw new IdInvalidException("Role với name = "+r.getName()+" đã tồn tại");
        // }
       
        return ResponseEntity.ok().body(this.roleService.Update(r));
    }

     @GetMapping("/roles")
    @ApiMessage("fetch all role")
    public ResponseEntity<ResultPaginationDTO> findAllRole(
            @Filter Specification<Role> spec,
            Pageable pageable) {

        ResultPaginationDTO skill = this.roleService.fetchAllRole(spec, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(skill);
    }

      @DeleteMapping("/roles/{id}")
    @ApiMessage("delete roles success")
    public ResponseEntity<Void> deleteRoles(@PathVariable("id") long id) throws IdInvalidException {
        boolean existById = this.roleService.existById(id);
        if (existById != true) {
            throw new IdInvalidException("Id " + id + " không tồn tại");
        }
        this.roleService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }


    @GetMapping("/roles/{id}")
    @ApiMessage("fetch role by id")
    public ResponseEntity<Role> getRoleById(
           @PathVariable("id") long id) throws IdInvalidException {
            Role role = this.roleService.fetchById(id);
            if(role ==  null ){
                throw new IdInvalidException("Role với id = " +id + " không tồn tại");
            }
        return ResponseEntity.ok().body(role);
    }

}
