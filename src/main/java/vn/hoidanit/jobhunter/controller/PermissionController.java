package vn.hoidanit.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.Permission;
import vn.hoidanit.jobhunter.domain.dto.pagination.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.permission.PermissionService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class PermissionController {
    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }
    @PostMapping("/permissions")
    @ApiMessage("FETCH CREATE PERMISSION")
    public ResponseEntity<Permission> createPermission(@Valid @RequestBody Permission permission) throws IdInvalidException {
        if(permissionService.isPermissionExist(permission)){
            throw  new IdInvalidException("Permission da ton tai!");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(permissionService.handleCreatePermission(permission));
    }

    @PutMapping("/permissions")
    @ApiMessage("FETCH UPDATE PERMISSION")
    public ResponseEntity<Permission> updatePermission(@RequestBody Permission permission) throws IdInvalidException {
        if(permissionService.fetchPermissionById(permission.getId()).isEmpty()){
            throw  new IdInvalidException("Permission voi id "+ permission.getId()+" khong ton tai!");
        }
        if(permissionService.isPermissionExist(permission)){
           if(permissionService.isSameName(permission)){
               throw  new IdInvalidException("Permission da ton tai!");
           }
        }
        return ResponseEntity.ok().body(permissionService.handleUpdatePermission(permission));
    }

    @GetMapping("/permissions/{id}")
    @ApiMessage("FETCH GET PERMISSION BY ID")
    public ResponseEntity<Permission> getPermissionById(@PathVariable("id") long id) throws IdInvalidException {
        Optional<Permission> permission = permissionService.fetchPermissionById(id);
        if(permission == null){
            throw  new IdInvalidException("Permission voi id "+ id+" khong ton tai!");
        }

        return ResponseEntity.ok().body(permissionService.fetchPermissionById(permission.get().getId()).get());
    }

    @GetMapping("/permissions")
    @ApiMessage("FETCH ALL PERMISSION")
    public ResponseEntity<ResultPaginationDTO> getAllPermissions(@Filter Specification<Permission> spec, Pageable pageable) {

        return ResponseEntity.ok().body(permissionService.fetchAllPermissions(spec,pageable));
    }

    @DeleteMapping("/permissions/{id}")
    @ApiMessage("DELETE PERMISSION")
    public ResponseEntity<Void> deletePermission(@PathVariable("id") long id) throws IdInvalidException {
        if(permissionService.fetchPermissionById(id) == null ){
            throw  new IdInvalidException("Permission voi id "+ id+" khong ton tai!");
        }
        permissionService.deleteById(id);

        return ResponseEntity.ok().body(null);
    }

}
