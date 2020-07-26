//package com.suprateam.car.controller;
//
//import com.suprateam.car.dto.UserDto;
//import com.suprateam.car.model.SmeUser;
//import com.suprateam.car.service.UserServiceAuth;
//import io.swagger.annotations.ApiOperation;
//import org.apache.catalina.User;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
////import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/user")
//public class UserAuthController {
//
//    @Autowired
//    private UserServiceAuth userService;
//
//    //@Secured({"ROLE_ADMIN", "ROLE_USER"})
////    @PreAuthorize("hasRole('ROLE_ADMIN')")
////    @RequestMapping(value="/users", method = RequestMethod.GET)
////    public List<SmeUser> listUser(){
////        return userService.findAll();
////    }
////
////    //@Secured("ROLE_USER")
////    @PreAuthorize("hasRole('ROLE_USER')")
////    ////@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
////    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
////    public SmeUser getOne(@PathVariable(value = "id") Long id){
////        return userService.findById(id);
////    }
//
//
//    @RequestMapping(value="/add", method = RequestMethod.POST)
//    public SmeUser saveUser(@RequestBody UserDto user){
//        return userService.save(user);
//    }
//
//    @ApiOperation(value = "Delete User")
//    @DeleteMapping("/delete/{id}")
//    ResponseEntity<?> deleteUser(@PathVariable Long id) {
//        return new ResponseEntity<String>(userService.deleteUser(id), HttpStatus.OK);
//
//    }
//
//
//    @ApiOperation(value = "Get All User")
//    @GetMapping("")
//    ResponseEntity<?> getAllUser() {
//        return ResponseEntity.ok(userService.getAllUser());
//    }
//
//
//}
