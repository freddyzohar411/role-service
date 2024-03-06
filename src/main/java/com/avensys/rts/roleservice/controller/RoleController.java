package com.avensys.rts.roleservice.controller;

import java.util.ArrayList;
import java.util.List;

import com.avensys.rts.roleservice.entity.UserGroupEntity;
import com.avensys.rts.roleservice.payload.response.RoleResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.avensys.rts.roleservice.constants.MessageConstants;
import com.avensys.rts.roleservice.entity.RoleEntity;
import com.avensys.rts.roleservice.exception.ServiceException;
import com.avensys.rts.roleservice.payload.request.RoleListingRequestDTO;
import com.avensys.rts.roleservice.payload.request.RoleRequestDTO;
import com.avensys.rts.roleservice.service.RoleService;
import com.avensys.rts.roleservice.util.JwtUtil;
import com.avensys.rts.roleservice.util.ResponseUtil;

import javax.management.relation.Role;

@CrossOrigin
//@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/role")
public class RoleController {

	private static final Logger LOG = LoggerFactory.getLogger(RoleController.class);

	@Autowired
	private RoleService roleService;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private JwtUtil jwtUtil;

	/**
	 * This method is used to create a role.
	 * 
	 * @param roleRequestDTO
	 * @return
	 */
	@PostMapping("/add")
	public ResponseEntity<?> createRole(@RequestBody RoleRequestDTO roleRequestDTO,
			@RequestHeader(name = "Authorization") String token) {
		LOG.info("create role request received ");
		try {
			Long userId = jwtUtil.getUserId(token);
			roleRequestDTO.setCreatedBy(userId);
			roleRequestDTO.setUpdatedBy(userId);
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
	@PutMapping("/edit")
	public ResponseEntity<?> updateRole(@RequestBody RoleRequestDTO roleRequestDTO,
			@RequestHeader(name = "Authorization") String token) {
		LOG.info("update role request received");
		try {
			Long userId = jwtUtil.getUserId(token);
			roleRequestDTO.setUpdatedBy(userId);
			roleService.updateRole(roleRequestDTO);
			return ResponseUtil.generateSuccessResponse(null, HttpStatus.OK,
					messageSource.getMessage(MessageConstants.MESSAGE_UPDATED, null, LocaleContextHolder.getLocale()));
		} catch (ServiceException e) {
			return ResponseUtil.generateSuccessResponse(null, HttpStatus.NOT_FOUND, e.getMessage());
		}
	}

	/**
	 * This method is used to delete role information
	 * 
	 * @param id
	 * @return
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> deleteRole(@PathVariable Long id) {
		LOG.info("deleteRole request received");
		try {
			roleService.deleteRole(id);
			return ResponseUtil.generateSuccessResponse(null, HttpStatus.OK,
					messageSource.getMessage(MessageConstants.MESSAGE_DELETED, null, LocaleContextHolder.getLocale()));
		} catch (ServiceException e) {
			return ResponseUtil.generateSuccessResponse(null, HttpStatus.NOT_FOUND, e.getMessage());
		}
	}

	/**
	 * This method is used to retrieve role information
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("/{id}")
	public ResponseEntity<?> getRoleById(@PathVariable("id") Long id) {
		try {
			RoleEntity role = roleService.getRoleById(id);
			return ResponseUtil.generateSuccessResponse(ResponseUtil.mapRoleEntitytoResponse(role), HttpStatus.OK, null);
		} catch (ServiceException e) {
			return ResponseUtil.generateSuccessResponse(null, HttpStatus.NOT_FOUND, e.getMessage());
		}
	}

	/**
	 * This method is used to retrieve all role list
	 * 
	 * @param pageNo
	 * @param pageSize
	 * @param sortBy
	 * @return
	 */
	@GetMapping("/")
	public ResponseEntity<Object> getRoleList(@RequestParam(defaultValue = "0") Integer pageNo,
			@RequestParam(defaultValue = "10") Integer pageSize,
			@RequestParam(defaultValue = "roleName") String sortBy) {
		LOG.info("get role list request received");
		List<RoleEntity> roleEntityList = roleService.getRoleList(pageNo, pageSize, sortBy);
		return ResponseUtil.generateSuccessResponse(ResponseUtil.mapRoleEntityListtoResponse(roleEntityList),
				HttpStatus.OK,
				messageSource.getMessage(MessageConstants.MESSAGE_FETCHED, null, LocaleContextHolder.getLocale()));
	}

	@GetMapping("/search")
	public ResponseEntity<?> searchRole(@RequestParam("search") String search, Pageable pageable) {
		Page<RoleEntity> page = roleService.search(search, pageable);
		return ResponseUtil.generateSuccessResponse(page, HttpStatus.OK,
				messageSource.getMessage(MessageConstants.MESSAGE_FETCHED, null, LocaleContextHolder.getLocale()));
	}

	@PostMapping("/listing")
	public ResponseEntity<Object> getRolesListing(@RequestBody RoleListingRequestDTO roleListingRequestDTO) {
		Integer page = roleListingRequestDTO.getPage();
		Integer pageSize = roleListingRequestDTO.getPageSize();
		String sortBy = roleListingRequestDTO.getSortBy();
		String sortDirection = roleListingRequestDTO.getSortDirection();
		String searchTerm = roleListingRequestDTO.getSearchTerm();
		if (searchTerm == null || searchTerm.isEmpty()) {
			return ResponseUtil.generateSuccessResponse(
					ResponseUtil.mapRoleEntityPageToRoleListingResponse(
							roleService.getUserGroupListingPage(page, pageSize, sortBy, sortDirection)),
					HttpStatus.OK,
					messageSource.getMessage(MessageConstants.MESSAGE_SUCCESS, null, LocaleContextHolder.getLocale()));
		}
		return ResponseUtil.generateSuccessResponse(
				ResponseUtil.mapRoleEntityPageToRoleListingResponse(roleService.getUserGroupListingPageWithSearch(page,
						pageSize, sortBy, sortDirection, searchTerm)),
				HttpStatus.OK,
				messageSource.getMessage(MessageConstants.MESSAGE_SUCCESS, null, LocaleContextHolder.getLocale()));
	}

}
