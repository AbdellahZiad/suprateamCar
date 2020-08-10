package com.suprateam.car.service.impl;


import com.suprateam.car.model.Client;
import com.suprateam.car.repository.ClientRepository;
import com.suprateam.car.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.suprateam.car.util.SurveyUserSpecification.filterSurveyM;

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

    public List<Client> filter(Pageable pageable, String filter) {
        return doFilterM(pageable, filter);
    }

    private List<Client> doFilterM(Pageable pageable, String filter) {
        if (filter != null)

            return clientRepository.findAll(filterSurveyM(filter), pageable).getContent();

        return clientRepository.findAll();

    }

    private List<Client> filterWithDouble(String filter) {
        return clientRepository.findAll();
//        .stream().filter(
//                client ->
//                        Integer.valueOf(client.getNumberDay().intValue()).equals(Double.valueOf(filter).intValue())
//                                ||
//                                Integer.valueOf(Double.valueOf(surveyUser.getFinalPrice()).intValue()).equals(Integer.valueOf(Double.valueOf(filter).intValue()))
//                                ||
//                                Integer.valueOf(surveyUser.getDiscountLoading().intValue()).equals(Double.valueOf(filter).intValue())
//
//
//        ).collect(Collectors.toList()));


    }

    private static boolean isNumberStr(String valueFromCell) {
        try {
            Double d = Double.parseDouble(valueFromCell);
            return true;

        } catch (NumberFormatException e) {
            return false;
        }
    }
}
