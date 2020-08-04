package com.suprateam.car.service.impl;


import com.suprateam.car.dto.UserDto;
import com.suprateam.car.exception.APIException;
import com.suprateam.car.model.Client;
import com.suprateam.car.model.SmeUser;
import com.suprateam.car.repository.ClientRepository;
import com.suprateam.car.repository.RoleRepository;
import com.suprateam.car.repository.UserRepository;
import com.suprateam.car.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ClientServiceImpl {


    ClientRepository clientRepository;


    RoleRepository roleRepository;

    @Autowired
    public ClientServiceImpl(RoleRepository roleRepository, ClientRepository clientRepository
            ) {
        this.clientRepository = clientRepository;
        this.roleRepository = roleRepository;
    }


    public List<Client> getAllClient() {
        return clientRepository.findAll();
    }



    public String deleteClient(Long id) {
//        clientRepository.desactivateUserById(id);

        return "Ok";
    }

    public Client saveClient(Client client) {
        return clientRepository.save(client);

    }

}
