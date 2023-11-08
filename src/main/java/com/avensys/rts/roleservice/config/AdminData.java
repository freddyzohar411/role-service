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

		PermissionEntity read = new PermissionEntity();
		read.setPermissionName("Read");
		read.setPermissionDescription("Read");
		read.setCreatedBy(1l);
		read.setUpdatedBy(1l);

		PermissionEntity write = new PermissionEntity();
		write.setPermissionName("Write");
		write.setPermissionDescription("Write");
		write.setCreatedBy(1l);
		write.setUpdatedBy(1l);

		PermissionEntity edit = new PermissionEntity();
		edit.setPermissionName("Edit");
		edit.setPermissionDescription("Edit");
		edit.setCreatedBy(1l);
		edit.setUpdatedBy(1l);

		PermissionEntity delete = new PermissionEntity();
		delete.setPermissionName("Delete");
		delete.setPermissionDescription("Delete");
		delete.setCreatedBy(1l);
		delete.setUpdatedBy(1l);

		try {
			permissionService.save(read);
			permissionService.save(write);
			permissionService.save(edit);
			permissionService.save(delete);
		} catch (ServiceException e) {

		}
	}
}