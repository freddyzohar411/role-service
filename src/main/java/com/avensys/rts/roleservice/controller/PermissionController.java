package com.avensys.rts.roleservice.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.avensys.rts.roleservice.entity.PermissionEntity;
import com.avensys.rts.roleservice.exception.ServiceException;
import com.avensys.rts.roleservice.service.PermissionService;
import com.avensys.rts.roleservice.util.JwtUtil;
import com.avensys.rts.roleservice.util.ResponseUtil;

@CrossOrigin
@RestController
@RequestMapping("/api/permission")
public class PermissionController {

	private static final Logger LOG = LoggerFactory.getLogger(PermissionController.class);

	@Autowired
	private PermissionService permissionService;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private JwtUtil jwtUtil;

	/**
	 * This method is used to create a permission.
	 * 
	 * @param permissionEntity
	 * @return
	 */
	@PostMapping("/add")
	public ResponseEntity<?> create(@RequestBody PermissionEntity permissionEntity,
			@RequestHeader(name = "Authorization") String token) {
		LOG.info("create permission request received");
		try {
			Long userId = jwtUtil.getUserId(token);
			permissionEntity.setCreatedBy(userId);
			permissionEntity.setUpdatedBy(userId);
			permissionService.save(permissionEntity);
			return ResponseUtil.generateSuccessResponse(null, HttpStatus.CREATED,
					messageSource.getMessage("permission.created", null, LocaleContextHolder.getLocale()));
		} catch (ServiceException e) {
			return ResponseUtil.generateSuccessResponse(null, HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	/**
	 * This method is used to update permission information
	 * 
	 * @param permissionEntity
	 * @return
	 */
	@PutMapping("/edit")
	public ResponseEntity<?> update(@RequestBody PermissionEntity permissionEntity,
			@RequestHeader(name = "Authorization") String token) {
		LOG.info("update permission request received");
		try {
			Long userId = jwtUtil.getUserId(token);
			permissionEntity.setUpdatedBy(userId);
			permissionService.update(permissionEntity);
			return ResponseUtil.generateSuccessResponse(null, HttpStatus.OK,
					messageSource.getMessage("permission.updated", null, LocaleContextHolder.getLocale()));
		} catch (ServiceException e) {
			return ResponseUtil.generateSuccessResponse(null, HttpStatus.NOT_FOUND, e.getMessage());
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) {
		try {
			permissionService.delete(id);
			return ResponseUtil.generateSuccessResponse(null, HttpStatus.OK,
					messageSource.getMessage("permission.deleted", null, LocaleContextHolder.getLocale()));
		} catch (ServiceException e) {
			return ResponseUtil.generateSuccessResponse(null, HttpStatus.NOT_FOUND, e.getMessage());
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> find(@PathVariable("id") Long id) {
		try {
			PermissionEntity permission = permissionService.getById(id);
			return ResponseUtil.generateSuccessResponse(permission, HttpStatus.OK, null);
		} catch (ServiceException e) {
			return ResponseUtil.generateSuccessResponse(null, HttpStatus.NOT_FOUND, e.getMessage());
		}
	}

	@GetMapping
	public ResponseEntity<?> findAll() {
		List<PermissionEntity> permissions = permissionService.fetchList();
		return ResponseUtil.generateSuccessResponse(permissions, HttpStatus.OK, null);
	}

}
