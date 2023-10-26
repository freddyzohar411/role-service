package com.avensys.rts.roleservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.avensys.rts.roleservice.constants.MessageConstants;
import com.avensys.rts.roleservice.exception.ServiceException;
import com.avensys.rts.roleservice.payloadrequest.RoleRequestDTO;
import com.avensys.rts.roleservice.service.RoleService;
import com.avensys.rts.roleservice.util.ResponseUtil;

@RestController
@RequestMapping("/role")
public class RoleController {

	private static final Logger LOG = LoggerFactory.getLogger(RoleController.class);

	@Autowired
	private RoleService roleService;

	@Autowired
	private MessageSource messageSource;

	/**
	 * This method is used to create a role.
	 * 
	 * @param roleRequestDTO
	 * @return
	 */
	@PostMapping
	public ResponseEntity<?> createRole(@RequestBody RoleRequestDTO roleRequestDTO) {
		LOG.info("createRole request received");
		try {
			roleService.createRole(roleRequestDTO);
			return ResponseUtil.generateSuccessResponse(null, HttpStatus.CREATED,
					messageSource.getMessage(MessageConstants.MESSAGE_CREATED, null, LocaleContextHolder.getLocale()));
		} catch (ServiceException e) {
			return ResponseUtil.generateSuccessResponse(null, HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	/**
	 * This method is used to update role information
	 * 
	 * @param roleRequestDTO
	 * @param roleId
	 * @return
	 */
	@PutMapping
	public ResponseEntity<?> updateRole(@RequestBody RoleRequestDTO roleRequestDTO) {
		LOG.info("updateRole request received");
		try {
			roleService.updateRole(roleRequestDTO);
			return ResponseUtil.generateSuccessResponse(null, HttpStatus.OK,
					messageSource.getMessage(MessageConstants.MESSAGE_UPDATED, null, LocaleContextHolder.getLocale()));
		} catch (ServiceException e) {
			return ResponseUtil.generateSuccessResponse(null, HttpStatus.NOT_FOUND, e.getMessage());
		}
	}

}
