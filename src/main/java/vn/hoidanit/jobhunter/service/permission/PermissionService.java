package vn.hoidanit.jobhunter.service.permission;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import vn.hoidanit.jobhunter.domain.Permission;
import vn.hoidanit.jobhunter.domain.dto.pagination.ResultPaginationDTO;

import java.util.Optional;

public interface PermissionService {

    Permission handleCreatePermission(Permission permission);
    Permission handleUpdatePermission(Permission permission);

    Optional<Permission> fetchPermissionById(long id);
    ResultPaginationDTO fetchAllPermissions(Specification<Permission> spec, Pageable pageable);

    void deleteById(long id);

    boolean isPermissionExist(Permission permission);
}
