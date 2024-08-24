package vn.hoidanit.jobhunter.service.role;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.Permission;
import vn.hoidanit.jobhunter.domain.Role;
import vn.hoidanit.jobhunter.domain.dto.pagination.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.PermissionRepository;
import vn.hoidanit.jobhunter.repository.RoleRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public RoleServiceImpl(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    @Override
    public Role handleCreateRole(Role role) {
        // check permission
        if(role.getPermissions() !=null) {
            List<Long> listIdPermission = role.getPermissions().stream().map(x ->x.getId()).collect(Collectors.toList());
            List<Permission> permissionList = permissionRepository.findByIdIn(listIdPermission);
            role.setPermissions(permissionList);
        }
        return roleRepository.save(role);
    }

    @Override
    public Role handleUpdateRole(Role role) {
        if(role.getPermissions()!=null){
            List<Long> listIdPermission = role.getPermissions().stream().map(x->x.getId()).collect(Collectors.toList());
            List<Permission> permissionList = permissionRepository.findByIdIn(listIdPermission);
            role.setPermissions(permissionList);
        }
        Optional<Role>  optionalRole = fetchRoleById(role.getId());
            if(optionalRole.isPresent()){
                Role updateRole = optionalRole.get();
                updateRole.setName(role.getName());
                updateRole.setDescription(role.getDescription());
                updateRole.setActive(role.isActive());
                updateRole.setPermissions(role.getPermissions());
                roleRepository.save(updateRole);
                return updateRole;
            }

        return null;
    }

    @Override
    public Optional<Role> fetchRoleById(long id) {
        Optional<Role> roleOptional = roleRepository.findById(id);
        if(roleOptional.isPresent()){
            return roleOptional;
        }
        return null;
    }

    @Override
    public ResultPaginationDTO fetchAllRoles(Specification<Role> spec, Pageable pageable) {
        Page<Role> permissionPage = roleRepository.findAll(spec, pageable);
        ResultPaginationDTO res = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(permissionPage.getTotalPages());
        mt.setTotal(permissionPage.getTotalElements());
        res.setMeta(mt);
        res.setResult(permissionPage.getContent());
        return  res;
    }

    @Override
    public void deleteRoleById(long id) {
        roleRepository.deleteById(id);
    }

    @Override
    public boolean isNameExist(String name) {
        return roleRepository.existsByName(name);
    }
}
