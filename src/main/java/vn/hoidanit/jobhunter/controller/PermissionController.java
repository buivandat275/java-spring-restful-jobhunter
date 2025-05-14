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
import vn.hoidanit.jobhunter.domain.Permission;
import vn.hoidanit.jobhunter.domain.respone.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.PermissionService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class PermissionController {
    private PermissionService permissionService;

    public PermissionController(PermissionService permissionService){
        this.permissionService = permissionService;
    }

    @PostMapping("/permissions")
    @ApiMessage("create permission success")
    public ResponseEntity<Permission> createNewPermission(@Valid @RequestBody Permission permission)
            throws IdInvalidException {
        if(this.permissionService.isPermissionExist(permission)){
                    throw new IdInvalidException("Permission đã tồn tại");
                }
        Permission currentPermission = this.permissionService.create(permission);
        return ResponseEntity.status(HttpStatus.CREATED).body(currentPermission);
        // cach2: return ResponseEntity.ok(newUser);
    }

    @PutMapping("/permissions")
    @ApiMessage("update permissions success")
    public ResponseEntity<Permission> updatePermission(@RequestBody Permission p) throws IdInvalidException
    {   
        boolean existPermissionById = this.permissionService.existById(p.getId());
        if(existPermissionById != true){
            throw new IdInvalidException("Permission với id = "+p.getId()+" không tồn tại");
        }
        //check exist by module, api path. method
        if(this.permissionService.isPermissionExist(p)){
            //check name
            if(this.permissionService.isSameName(p)){
            throw new IdInvalidException("Permission đã tồn tại");
            }
        }
        return ResponseEntity.ok().body(this.permissionService.Update(p));
    }

     @GetMapping("/permissions")
    @ApiMessage("fetch all psermission")
    public ResponseEntity<ResultPaginationDTO> findAllPermission(
            @Filter Specification<Permission> spec,
            Pageable pageable) {

        ResultPaginationDTO skill = this.permissionService.fetchAllPermission(spec, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(skill);
    }

      @DeleteMapping("/permissions/{id}")
    @ApiMessage("delete permissions success")
    public ResponseEntity<Void> deletePermissions(@PathVariable("id") long id) throws IdInvalidException {
        boolean existById = this.permissionService.existById(id);
        if (existById != true) {
            throw new IdInvalidException("Id " + id + " không tồn tại");
        }
        this.permissionService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    
}
