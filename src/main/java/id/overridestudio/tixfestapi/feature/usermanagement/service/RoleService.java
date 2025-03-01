package id.overridestudio.tixfestapi.feature.usermanagement.service;

import id.overridestudio.tixfestapi.feature.usermanagement.dto.request.RoleRequest;
import id.overridestudio.tixfestapi.feature.usermanagement.dto.response.RoleResponse;
import id.overridestudio.tixfestapi.feature.usermanagement.entity.Role;

import java.util.List;

public interface RoleService {
    RoleResponse create (RoleRequest roleRequest);
    RoleResponse update (String id, RoleRequest roleRequest);
    RoleResponse getById (String id);
    Role getOne(String id);
    Role getOneByName(String name);
    List<RoleResponse> getAll ();
    void delete (String id);
}
