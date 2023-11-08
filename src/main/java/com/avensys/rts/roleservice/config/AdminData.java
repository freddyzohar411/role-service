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
		save.setPermissionName("Write");
		save.setPermissionDescription("Write");
		save.setCreatedBy(1l);
		save.setUpdatedBy(1l);

		PermissionEntity edit = new PermissionEntity();
		edit.setPermissionName("Edit");
		edit.setPermissionDescription("Edit");
		edit.setCreatedBy(1l);
		edit.setUpdatedBy(1l);

		PermissionEntity get = new PermissionEntity();
		get.setPermissionName("Read");
		get.setPermissionDescription("Read");
		get.setCreatedBy(1l);
		get.setUpdatedBy(1l);

		PermissionEntity delete = new PermissionEntity();
		delete.setPermissionName("Delete");
		delete.setPermissionDescription("Delete");
		delete.setCreatedBy(1l);
		delete.setUpdatedBy(1l);

		try {
			permissionService.save(save);
			permissionService.save(edit);
			permissionService.save(get);
			permissionService.save(delete);
		} catch (ServiceException e) {

		}
	}
}