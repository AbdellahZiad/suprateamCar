package com.suprateam.car.service.impl;


import com.suprateam.car.model.Voiture;
import com.suprateam.car.repository.RoleRepository;
import com.suprateam.car.repository.VoitureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.suprateam.car.util.SurveyUserSpecification.filterVM;

@Service
public class VoitureServiceImpl {


    VoitureRepository VoitureRepository;


    RoleRepository roleRepository;

    @Autowired
    public VoitureServiceImpl(RoleRepository roleRepository, VoitureRepository VoitureRepository
    ) {
        this.VoitureRepository = VoitureRepository;
        this.roleRepository = roleRepository;
    }


    public List<Voiture> getAllVoiture() {
        return VoitureRepository.findAll();
    }


    public String deleteVoiture(Long id) {
//        VoitureRepository.desactivateUserById(id);

        return "Ok";
    }

    public Voiture saveVoiture(Voiture Voiture) {
        return VoitureRepository.save(Voiture);

    }

    public List<Voiture> filter(Pageable pageable, String filter) {
        return doFilterM(pageable, filter);
    }

    private List<Voiture> doFilterM(Pageable pageable, String filter) {
        if (filter != null)
            return VoitureRepository.findAll(filterVM(filter), pageable).getContent();
        return VoitureRepository.findAll();

    }

    private List<Voiture> filterWithDouble(String filter) {
        return VoitureRepository.findAll();
//        .stream().filter(
//                Voiture ->
//                        Integer.valueOf(Voiture.getNumberDay().intValue()).equals(Double.valueOf(filter).intValue())
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
