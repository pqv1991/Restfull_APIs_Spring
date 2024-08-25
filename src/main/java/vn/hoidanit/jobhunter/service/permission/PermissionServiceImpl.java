package vn.hoidanit.jobhunter.service.permission;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.Permission;
import vn.hoidanit.jobhunter.domain.dto.pagination.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.PermissionRepository;

import java.util.Optional;

@Service
public class PermissionServiceImpl implements PermissionService {
    private final PermissionRepository permissionRepository;

    public PermissionServiceImpl(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }


    @Override
    public Permission handleCreatePermission(Permission permission) {
        return permissionRepository.save(permission);
    }

    @Override
    public Permission handleUpdatePermission(Permission permission) {
        Optional<Permission> permissionOptional = fetchPermissionById(permission.getId());
        if (permissionOptional.isPresent()) {
            Permission permissionUpdate = permissionOptional.get();
            permissionUpdate.setName(permission.getName());
            permissionUpdate.setMethod(permission.getMethod());
            permissionUpdate.setApiPath(permission.getApiPath());
            permissionUpdate.setModule(permission.getModule());
            permissionRepository.save(permissionUpdate);
            return permissionUpdate;
        }
        return null;
    }

    @Override
    public Optional<Permission> fetchPermissionById(long id) {
        Optional<Permission> permissionOptional = permissionRepository.findById(id);
        if (permissionOptional.isPresent()) {
            return permissionOptional;
        }
        return  null;
    }

    @Override
    public ResultPaginationDTO fetchAllPermissions(Specification<Permission> spec, Pageable pageable) {
        Page<Permission> permissionPage = permissionRepository.findAll(spec, pageable);
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
    public boolean isPermissionExist(Permission permission) {
        return permissionRepository.existsByMethodAndApiPathAndModule(permission.getMethod(), permission.getApiPath(), permission.getModule());

    }

    @Override
    public boolean isSameName(Permission permission) {
        Permission  permissionDB = fetchPermissionById(permission.getId()).get();
        if(permission != null){
            if(permissionDB.getName().equals(permission.getName())){
                return true;
            }
        }
        return false;
    }

    @Override
    public void deleteById(long id) {

        Optional<Permission> optionalPermission = permissionRepository.findById(id);
            Permission currentPermission = optionalPermission.get();
            currentPermission.getRoles().forEach(role->role.getPermissions().remove(currentPermission));
            permissionRepository.delete(currentPermission);
    }
}
