package com.avensys.rts.roleservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.avensys.rts.roleservice.entity.PermissionEntity;
import com.avensys.rts.roleservice.exception.ServiceException;
import com.avensys.rts.roleservice.service.PermissionService;

@Component
public class AdminData implements ApplicationRunner {

	@Autowired
	private PermissionService permissionService;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		PermissionEntity save = new PermissionEntity();
		save.setPermissionName("Save");
		save.setPermissionDescription("Save");
		save.setCreatedBy(1);
		save.setUpdatedBy(1);

		PermissionEntity edit = new PermissionEntity();
		edit.setPermissionName("Edit");
		edit.setPermissionDescription("Edit");
		edit.setCreatedBy(1);
		edit.setUpdatedBy(1);

		PermissionEntity get = new PermissionEntity();
		get.setPermissionName("Get");
		get.setPermissionDescription("Get");
		get.setCreatedBy(1);
		get.setUpdatedBy(1);

		PermissionEntity delete = new PermissionEntity();
		delete.setPermissionName("Delete");
		delete.setPermissionDescription("Delete");
		delete.setCreatedBy(1);
		delete.setUpdatedBy(1);

		try {
			permissionService.save(save);
			permissionService.save(edit);
			permissionService.save(get);
			permissionService.save(delete);
		} catch (ServiceException e) {

		}
	}
}