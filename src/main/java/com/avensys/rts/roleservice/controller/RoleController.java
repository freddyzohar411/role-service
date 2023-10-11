package com.avensys.rts.roleservice.controller;

import com.avensys.rts.roleservice.constants.MessageConstants;
import com.avensys.rts.roleservice.payloadrequest.RoleRequestDTO;
import com.avensys.rts.roleservice.service.RoleService;
import com.avensys.rts.roleservice.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;
    @Autowired
    private MessageSource messageSource;
    private static final Logger LOG = LoggerFactory.getLogger(RoleController.class);
    @PostMapping("/create")

    public ResponseEntity<?> createRole(@RequestBody RoleRequestDTO roleRequestDTO){
    LOG.info("createRole request received");
    roleService.createRole(roleRequestDTO);
        return ResponseUtil.generateSuccessResponse(roleRequestDTO, HttpStatus.CREATED,
                messageSource.getMessage(MessageConstants.MESSAGE_CREATED, null, LocaleContextHolder.getLocale()));

    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?>updateRole(@RequestBody RoleRequestDTO roleRequestDTO,Integer id){
        LOG.info("updateRole request received");
        roleService.updateRole(roleRequestDTO,id);
        return ResponseUtil.generateSuccessResponse(roleRequestDTO,HttpStatus.CREATED,messageSource.getMessage(MessageConstants.MESSAGE_UPDATED,null,LocaleContextHolder.getLocale()));
    }
    @GetMapping("/{id}")
    public ResponseEntity<?>getRole(@PathVariable Integer id){
        LOG.info("getRole request received");
        RoleRequestDTO roleRequestDTO = roleService.getRole(id);
        return ResponseUtil.generateSuccessResponse(roleRequestDTO,HttpStatus.CREATED,messageSource.getMessage(MessageConstants.MESSAGE_UPDATED,null,LocaleContextHolder.getLocale()));
    }
    @GetMapping("/roleList")
    public ResponseEntity<Object>getRoleList(){
        LOG.info("getRoleList request received");
        return ResponseUtil.generateSuccessResponse(roleService.getRoleList(),HttpStatus.CREATED,messageSource.getMessage(MessageConstants.MESSAGE_UPDATED,null,LocaleContextHolder.getLocale()));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteRole(@PathVariable Integer id){
        LOG.info("deleteRole request received");
        roleService.deleteRole(id);
        return ResponseUtil.generateSuccessResponse(null, HttpStatus.OK, messageSource.getMessage(MessageConstants.MESSAGE_SUCCESS, null, LocaleContextHolder.getLocale()));


    }






}
