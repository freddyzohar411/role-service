package com.avensys.rts.roleservice.controller;

import com.avensys.rts.roleservice.constants.MessageConstants;
import com.avensys.rts.roleservice.entity.RoleEntity;
import com.avensys.rts.roleservice.payloadrequest.RoleRequestDTO;
import com.avensys.rts.roleservice.service.RoleService;
import com.avensys.rts.roleservice.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;
    @Autowired
    private MessageSource messageSource;
    private static final Logger LOG = LoggerFactory.getLogger(RoleController.class);

    /**
     * This method is used to create a role.
     * @param roleRequestDTO
     * @return
     */
    @PostMapping("/create")

    public ResponseEntity<?> createRole(@RequestBody RoleRequestDTO roleRequestDTO){
    LOG.info("createRole request received");
    RoleEntity roleEntity=roleService.createRole(roleRequestDTO);
        return ResponseUtil.generateSuccessResponse(roleEntity, HttpStatus.CREATED,
                messageSource.getMessage(MessageConstants.MESSAGE_CREATED, null, LocaleContextHolder.getLocale()));

    }

    /**
     * This method is used to update role information
     * @param roleRequestDTO
     * @param id
     * @return
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<?>updateRole(@RequestBody RoleRequestDTO roleRequestDTO,Integer id){
        LOG.info("updateRole request received");
        RoleEntity roleEntity = roleService.updateRole(roleRequestDTO,id);
        return ResponseUtil.generateSuccessResponse(roleEntity,HttpStatus.CREATED,messageSource.getMessage(MessageConstants.MESSAGE_UPDATED,null,LocaleContextHolder.getLocale()));
    }

    /**
     * This method is used to retrieve role information
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<?>getRole(@PathVariable Integer id){
        LOG.info("getRole request received");
        RoleEntity roleEntity = roleService.getRole(id);
        return ResponseUtil.generateSuccessResponse(roleEntity,HttpStatus.CREATED,messageSource.getMessage(MessageConstants.MESSAGE_UPDATED,null,LocaleContextHolder.getLocale()));
    }

    /**
     *  This method is used to retrieve all role list
     * @param pageNo
     * @param pageSize
     * @param sortBy
     * @return
     */
    @GetMapping("/roleList")
    public ResponseEntity<Object>getRoleList(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "updatedAt") LocalDateTime sortBy
    ){
        LOG.info("getRoleList request received");
        List<RoleEntity> roleEntityList=roleService.getRoleList(pageNo, pageSize, sortBy);
        return ResponseUtil.generateSuccessResponse(roleEntityList,HttpStatus.CREATED,messageSource.getMessage(MessageConstants.MESSAGE_UPDATED,null,LocaleContextHolder.getLocale()));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteRole(@PathVariable Integer id){
        LOG.info("deleteRole request received");
        roleService.deleteRole(id);
        return ResponseUtil.generateSuccessResponse(null, HttpStatus.OK, messageSource.getMessage(MessageConstants.MESSAGE_SUCCESS, null, LocaleContextHolder.getLocale()));


    }
    @GetMapping("/search")
    public Page<RoleEntity> searchRole(@RequestParam("search") String search, Pageable pageable) {
        return roleService.search(search, pageable);
    }






}
