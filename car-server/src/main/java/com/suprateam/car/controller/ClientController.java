package com.suprateam.car.controller;

import com.suprateam.car.dto.UserDto;
import com.suprateam.car.model.Client;
import com.suprateam.car.service.UserService;
import com.suprateam.car.service.impl.ClientServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/client")
public class ClientController {


    @Autowired
    private ClientServiceImpl clientService;


    @ApiOperation(value = "Add User")
    @PostMapping("/add")
    ResponseEntity<?> addUser(@RequestBody Client userDto) {
        return ResponseEntity.ok(clientService.saveClient(userDto));
    }


    @ApiOperation(value = "Delete User")
    @DeleteMapping("/delete/{id}")
    ResponseEntity<?> deleteUser(@PathVariable Long id) {
        return new ResponseEntity<String>(clientService.deleteClient(id), HttpStatus.OK);

    }


    @ApiOperation(value = "Get All User")
    @GetMapping("")
    ResponseEntity<?> getAllUser() {
        return ResponseEntity.ok(clientService.getAllClient());
    }


}
