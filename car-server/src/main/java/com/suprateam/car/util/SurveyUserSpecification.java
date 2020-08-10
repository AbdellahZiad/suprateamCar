package com.suprateam.car.util;

import com.suprateam.car.model.Client;
import com.suprateam.car.model.Client_;
import com.suprateam.car.model.Voiture;
import com.suprateam.car.model.Voiture_;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;
import java.util.Date;

public class SurveyUserSpecification {

    public static Specification<Client> filterSurveyM(String filter) {

        if (filter == null || filter.isEmpty())
            return Specification.where(null);
        return Specification.where(assertIsLike(Client_.name, filter))
                .or(assertIsLike(Client_.cin, filter))
                .or(assertIsLike(Client_.tel, filter));

//                .or(assertIsEqual(SurveyUser_.qualityScore, filter))
//                .or(assertIsEqual(SurveyUser_.finalPrice, isNumberStr(filter)?Double.valueOf(filter)))
//                .or(assertIsEqual(SurveyUser_.discountLoading, filter));
    }


    private static Specification<Client> assertIsLike(SingularAttribute<Client, ?> attr, String keyword) {
        return (Root<Client> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            return cb.like(cb.lower(root.get(attr).as(String.class)), "%" + keyword.toLowerCase() + "%");
        };
    }


    private static Specification<Client> assertIsLikeV(SingularAttribute<Voiture, ?> attr, String keyword) {
        return (Root<Client> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            return cb.like(cb.lower(root.get(String.valueOf(attr)).as(String.class)), "%" + keyword.toLowerCase() + "%");
        };
    }

    private static Specification<Client> assertIsEqual(SingularAttribute<Client, Double> attr, String keyword) {
        if (!isNumberStr(keyword)) return null;

        return (Root<Client> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            return cb.lessThanOrEqualTo(root.get(attr), Double.valueOf(keyword));

        };
    }

    private static Specification<Client> assertDateEqual(SingularAttribute<Client, ?> attr, String date) {
        return (Root<Client> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            return cb.equal(root.get(attr).as(Date.class), date);
        };
    }

    private static boolean isNumberStr(String valueFromCell) {
        try {
            Double d = Double.parseDouble(valueFromCell);
            return true;

        } catch (NumberFormatException e) {
            return false;
        }
    }
//    private static Specification<OmegaClaimViewEntity> assertDateEqual(SingularAttribute<OmegaClaimViewEntity, ?> attr, java.sql.Date date) {


    //FILTER VOITURE
    public static Specification<Voiture> filterVM(String filter) {

        if (filter == null || filter.isEmpty())
            return Specification.where(null);

        return Specification.where(assertIsLikeV(Voiture_.marque, filter))
                .or(assertIsLikeV(Voiture_.matricule, filter));

//                .or(assertIsEqual(SurveyUser_.qualityScore, filter))
//                .or(assertIsEqual(SurveyUser_.finalPrice, isNumberStr(filter)?Double.valueOf(filter)))
//                .or(assertIsEqual(SurveyUser_.discountLoading, filter));
    }

}
