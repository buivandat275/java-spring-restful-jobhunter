package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Permission;
import vn.hoidanit.jobhunter.domain.respone.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.PermissionRepository;

@Service
public class PermissionService {
    private PermissionRepository permissionRepository;
    public PermissionService(PermissionRepository permissionRepository){
        this.permissionRepository = permissionRepository;
    }

    public boolean isPermissionExist(Permission p){
        return permissionRepository.existsByModuleAndApiPathAndMethod(
            p.getModule(),
            p.getApiPath(),
            p.getMethod()
        );

    }

    public boolean isSameName(Permission p){
        Permission permissionDB = this.fetchById(p.getId());
        if(permissionDB != null){
            if(permissionDB.getName().equals(p.getName()))
            return true;
        }
        return false;
    }

    public Permission create(Permission permission) {

        permission = this.permissionRepository.save(permission);
        // convert responsy
        return permission;
    }

    public Permission fetchById(long id){
        return this.permissionRepository.findPermissionById(id);
    }

    public boolean existById(long id){
        return this.permissionRepository.existsById(id);
    } 
    public Permission Update(Permission p) {
        Permission permission = this.permissionRepository.findPermissionById(p.getId());
        if (permission != null) {
            permission.setName(p.getName());
            permission.setMethod(p.getMethod());
            permission.setApiPath(p.getApiPath());
            permission.setModule(p.getModule());
            permission = this.permissionRepository.save(permission);
            return permission;
        }
        return null;
    }

    public void delete(long id){
        // delete permission_role
        Optional<Permission> permissioOptional = this.permissionRepository.findById(id);
        Permission currentPermission = permissioOptional.get();
        currentPermission.getRoles().forEach(role -> role.getPermissions().remove(currentPermission));

        //delete permission

        this.permissionRepository.delete(currentPermission);

    }

     public ResultPaginationDTO fetchAllPermission(Specification<Permission> spec, Pageable pageable) {
        Page<Permission> pageP = this.permissionRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageP.getTotalPages());
        mt.setTotal(pageP.getTotalElements());

        rs.setMeta(mt);
        List<Permission> listPermissions = pageP.getContent();
        rs.setResult(listPermissions);
        return rs;
    }




}
