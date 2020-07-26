package com.suprateam.car.controller;

import com.suprateam.car.dto.UserDto;
import com.suprateam.car.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {


    @Autowired
    private UserService userService;


    @ApiOperation(value = "Add User")
    @PostMapping("/")
    ResponseEntity<?> addUser(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.saveUser(userDto));
    }


    @ApiOperation(value = "Delete User")
    @DeleteMapping("/delete/{id}")
    ResponseEntity<?> deleteUser(@PathVariable Long id) {
        return new ResponseEntity<String>(userService.deleteUser(id), HttpStatus.OK);

    }


    @ApiOperation(value = "Get All User")
    @GetMapping("")
    ResponseEntity<?> getAllUser() {
        return ResponseEntity.ok(userService.getAllUser());
    }


}
