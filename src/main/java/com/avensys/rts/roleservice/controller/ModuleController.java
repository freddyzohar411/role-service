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

import com.avensys.rts.roleservice.entity.ModuleEntity;
import com.avensys.rts.roleservice.exception.ServiceException;
import com.avensys.rts.roleservice.service.ModuleService;
import com.avensys.rts.roleservice.util.JwtUtil;
import com.avensys.rts.roleservice.util.ResponseUtil;

@CrossOrigin
@RestController
@RequestMapping("/api/module")
public class ModuleController {

	private static final Logger LOG = LoggerFactory.getLogger(ModuleController.class);

	@Autowired
	private ModuleService moduleService;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private JwtUtil jwtUtil;

	/**
	 * This method is used to create a module.
	 * 
	 * @param moduleEntity
	 * @return
	 */
	@PostMapping("/add")
	public ResponseEntity<?> create(@RequestBody ModuleEntity moduleEntity,
			@RequestHeader(name = "Authorization") String token) {
		LOG.info("create module request received");
		try {
			Long userId = jwtUtil.getUserId(token);
			moduleEntity.setCreatedBy(userId);
			moduleEntity.setUpdatedBy(userId);
			moduleService.save(moduleEntity);
			return ResponseUtil.generateSuccessResponse(null, HttpStatus.CREATED,
					messageSource.getMessage("module.created", null, LocaleContextHolder.getLocale()));
		} catch (ServiceException e) {
			return ResponseUtil.generateSuccessResponse(null, HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	/**
	 * This method is used to update module information
	 * 
	 * @param moduleEntity
	 * @return
	 */
	@PutMapping("/edit")
	public ResponseEntity<?> update(@RequestBody ModuleEntity moduleEntity,
			@RequestHeader(name = "Authorization") String token) {
		LOG.info("update module request received");
		try {
			Long userId = jwtUtil.getUserId(token);
			moduleEntity.setUpdatedBy(userId);
			moduleService.update(moduleEntity);
			return ResponseUtil.generateSuccessResponse(null, HttpStatus.OK,
					messageSource.getMessage("module.updated", null, LocaleContextHolder.getLocale()));
		} catch (ServiceException e) {
			return ResponseUtil.generateSuccessResponse(null, HttpStatus.NOT_FOUND, e.getMessage());
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) {
		try {
			moduleService.delete(id);
			return ResponseUtil.generateSuccessResponse(null, HttpStatus.OK,
					messageSource.getMessage("module.deleted", null, LocaleContextHolder.getLocale()));
		} catch (ServiceException e) {
			return ResponseUtil.generateSuccessResponse(null, HttpStatus.NOT_FOUND, e.getMessage());
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> find(@PathVariable("id") Long id) {
		try {
			ModuleEntity module = moduleService.getById(id);
			return ResponseUtil.generateSuccessResponse(module, HttpStatus.OK, null);
		} catch (ServiceException e) {
			return ResponseUtil.generateSuccessResponse(null, HttpStatus.NOT_FOUND, e.getMessage());
		}
	}

	@GetMapping
	public ResponseEntity<?> findAll() {
		List<ModuleEntity> permissions = moduleService.fetchList();
		return ResponseUtil.generateSuccessResponse(permissions, HttpStatus.OK, null);
	}

}
