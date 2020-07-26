package com.suprateam.car.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class SurveyUser implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String statusApp;

    private Double qualityScore;

    private Date dateCreate;

    private String statusSystem;

    private String agent;

    private String companyName;

    private String by;

    private String riskLocation;

    private String occupancy;

    private String riskRegion; // zip code

    private Double sumInsuredTotal;

    private String decision;

    private Double pricingRateFLEXA;

//    private String  scoringDiscountLoadingFLEXA;

    private Date dateTreatment;

    private String decisionFLEXA;

    private String decisionNATCAT;

    private String pricingRateNATCAT;

    private String loadingOnYearsBusiness;

    private String finalPrice;

    private String referralReason;

    private String declinedReason;

    private String surveyID;

    private String companyID;

    private Double discountLoading;

//    private Double pricingTOTAL;
//    private Double discountLoading;

    @ManyToOne(targetEntity = SurveyParameters.class)
    private SurveyParameters surveyParameters;

    @ManyToOne(targetEntity = Client.class,fetch = FetchType.EAGER)
    private Client client;

    @ManyToOne(targetEntity = SmeUser.class)
    private SmeUser user;

    @OneToMany(mappedBy = "surveyUser")
    private List<Answer> answerList;

    @OneToMany(mappedBy = "surveyUser")
    @JsonIgnore
    List<Media> mediaList;

}
