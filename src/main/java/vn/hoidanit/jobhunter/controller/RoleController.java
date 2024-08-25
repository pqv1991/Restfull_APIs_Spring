package vn.hoidanit.jobhunter.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.jobhunter.domain.Permission;
import vn.hoidanit.jobhunter.domain.Role;
import vn.hoidanit.jobhunter.domain.dto.pagination.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.role.RoleService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/roles")
    @ApiMessage("FETCH CREATE ROLE")
    public ResponseEntity<Role> createRole (@Valid  @RequestBody Role role) throws IdInvalidException {
        if(roleService.isNameExist(role.getName())){
            throw  new IdInvalidException("Role da ton tai!");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(roleService.handleCreateRole(role));
    }

    @PutMapping("/roles")
    @ApiMessage("FETCH UPDATE ROLE")
    public ResponseEntity<Role> updateRole (@RequestBody Role role) throws IdInvalidException {
        Optional<Role>  optionalRole = roleService.fetchRoleById(role.getId());
        if (optionalRole.isEmpty()){
            new IdInvalidException("Role voi "+ role.getId()+" khong ton tai!");
        }
//        if(roleService.isNameExist(role.getName())){
//            throw  new IdInvalidException("Role da ton tai!");
//        }
        return ResponseEntity.ok().body(roleService.handleUpdateRole(role));
    }

    @GetMapping("/roles/{id}")
    @ApiMessage("FETCH ROLE BY ID")
    public ResponseEntity<Role> getRoleById (@PathVariable("id") long id) throws IdInvalidException {
        Optional<Role>  optionalRole = roleService.fetchRoleById(id);
        if (optionalRole.isEmpty()){
            new IdInvalidException("Role voi "+ id+" khong ton tai!");
        }
        return ResponseEntity.ok().body(roleService.fetchRoleById(optionalRole.get().getId()).get());
    }

    @GetMapping("/roles")
    @ApiMessage("FETCH ALL ROLES")
    public ResponseEntity<ResultPaginationDTO> getAllRoles(@Filter Specification<Role> spec, Pageable pageable) {

        return ResponseEntity.ok().body(roleService.fetchAllRoles(spec,pageable));
    }

    @DeleteMapping("/roles/{id}")
    @ApiMessage("DELETE ROLE")
    public ResponseEntity<Void> deleteRole(@PathVariable("id") long id) throws IdInvalidException {
        if(roleService.fetchRoleById(id).isEmpty()){
            throw  new IdInvalidException("Role voi id "+ id+" khong ton tai!");
        }
        roleService.deleteRoleById(id);

        return ResponseEntity.ok().body(null);
    }


}
