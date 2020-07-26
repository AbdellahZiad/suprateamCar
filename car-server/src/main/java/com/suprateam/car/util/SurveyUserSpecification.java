package com.suprateam.car.util;

import com.suprateam.car.dto.SurveyUserDto;
import com.suprateam.car.model.SurveyUser;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;

import static java.util.Optional.ofNullable;

public class SurveyUserSpecification {


    public static Specification<SurveyUser> filterKeyword(SurveyUserDto filter) {
        return null;

//                Specification.where(ofNullable(keyword.getValue()).map(value -> assertIsLikeKeyword(Keyword_.value, value)).orElse(null))
//                .or(ofNullable(keyword.getLanguage()).filter(Objects::nonNull).map(name -> assertIsLikeKeywordAndLanguage(Keyword_.language, name.getName())).orElse(null))
//                .or(ofNullable(keyword.getSynonyms()).map(synonyms -> assertIsLikeKeywordAndSynonym(Keyword_.synonyms, synonyms)).orElse(null))
//                .and(ofNullable(keyword.isActive()).filter(Objects::nonNull).map(active -> assetKeywordIsActive(Keyword_.active, true)).orElse(null));
    }
}
