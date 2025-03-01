package id.overridestudio.tixfestapi.feature.usermanagement.service.impl;

import id.overridestudio.tixfestapi.feature.usermanagement.dto.request.RoleRequest;
import id.overridestudio.tixfestapi.feature.usermanagement.dto.response.RoleResponse;
import id.overridestudio.tixfestapi.feature.usermanagement.entity.Bank;
import id.overridestudio.tixfestapi.feature.usermanagement.entity.Role;
import id.overridestudio.tixfestapi.feature.usermanagement.repository.RoleRepository;
import id.overridestudio.tixfestapi.feature.usermanagement.service.RoleService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Transactional(rollbackFor = Exception.class)
    @PostConstruct
    public void init() {
        saveRole(roles);
    }

    private void saveRole(List<Role> roles) {
        for (Role role : roles) {
            if (!roleRepository.existsByName(role.getName())) {
                roleRepository.saveAndFlush(role);
            }
        }
    }

    private final List<Role> roles = Arrays.asList(
            new Role(null, "ROLE_SUPER_ADMIN"),
            new Role(null, "ROLE_ADMIN"),
            new Role(null, "ROLE_CUSTOMER"),
            new Role(null, "ROLE_EVENT_ORGANIZER"),
            new Role(null, "ROLE_REDEEM_TICKET"),
            new Role(null, "ROLE_GATE_KEEPER")
    );

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RoleResponse create(RoleRequest roleRequest) {
        Role role = Role.builder()
                .name(roleRequest.getName())
                .build();
        return toResponse(roleRepository.saveAndFlush(role));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RoleResponse update(String id, RoleRequest roleRequest) {
        Role role = getOne(id);
        role.setName(roleRequest.getName());
        role.setUpdatedAt(LocalDateTime.now());
        return toResponse(roleRepository.save(role));
    }

    @Override
    public RoleResponse getById(String id) {
        Role role = getOne(id);
        return toResponse(role);
    }

    @Override
    public Role getOne(String id) {
        return roleRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found"));
    }

    @Override
    public Role getOneByName(String name) {
        return roleRepository.findByName(name).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found"));
    }

    @Override
    public List<RoleResponse> getAll() {
        return roleRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public void delete(String id) {
        Role role = getOne(id);
        role.setDeletedAt(LocalDateTime.now());
        roleRepository.save(role);
    }

    private RoleResponse toResponse(Role role) {
        return RoleResponse.builder()
                .id(role.getId())
                .name(role.getName())
                .build();
    }
}
