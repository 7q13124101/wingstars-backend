package com.wingstars.auth.component;

import com.wingstars.auth.entity.Role;
import com.wingstars.auth.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {
    private final RoleRepository roleRepository;

    @Override
    public void run(ApplicationArguments args) {
        saveRoleIfNotFound("USER", "Regular User");
        saveRoleIfNotFound("ADMIN", "System Administrator");
        saveRoleIfNotFound("SUPER_ADMIN", "Full Access");
    }

    private void saveRoleIfNotFound(String code, String desc) {
        if (!roleRepository.existsByCode(code)) {
            roleRepository.save(
                    Role.builder()
                            .code(code)
                            .name(code)
                            .description(desc)
                            .build()
            );
        }
    }
}
