package com.avensys.rts.roleservice.bootstrap;

import com.avensys.rts.roleservice.entity.ModuleEntity;
import com.avensys.rts.roleservice.entity.PermissionEntity;
import com.avensys.rts.roleservice.repository.ModuleRepository;
import com.avensys.rts.roleservice.repository.PermissionRepository;
import com.avensys.rts.roleservice.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class ModulePermissionCommandlineRunner implements CommandLineRunner {

	@Autowired
	private PermissionRepository permissionRepository;
	@Autowired
	private ModuleRepository moduleRepository;

	private List<String> permissionList = List.of("Read", "Write", "Edit", "Delete");

	private List<String> moduleList = List.of("Accounts", "Jobs", "Candidates", "Settings");

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub

		moduleList.forEach(module -> {
			if (!moduleRepository.existsByModuleName(module)) {
				ModuleEntity moduleEntity = new ModuleEntity();
				moduleEntity.setModuleName(module);
				moduleEntity.setModuleDescription(("Provide a platform for creating, searching and managing %s listings.").formatted(module.toLowerCase()));
				moduleEntity.setCreatedBy(1l);
				moduleEntity.setUpdatedBy(1l);
				moduleEntity.setIsActive(true);
				moduleRepository.save(moduleEntity);
			}
		});
	}
}
