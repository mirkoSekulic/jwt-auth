package com.auth.jwtauth.modules.users.specification;

import com.auth.jwtauth.modules.users.domain.User;
import com.auth.jwtauth.modules.users.domain.User_;
import com.auth.jwtauth.modules.users.filter.UserFilter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class UserSpecification implements Specification<User> {
    private static final long serialVersionUID = 1L;

    private UserFilter userFilter;

    private UserSpecification(UserFilter userFilter) {
        this.userFilter = userFilter;
    }

    public static UserSpecification of(UserFilter userFilter) {
        return new UserSpecification(userFilter);
    }

    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

        String likeFormat = "%%%s%%";

        List<Predicate> predicates = new ArrayList<>();

        if (StringUtils.isNotBlank(userFilter.getFirstName())) {
            predicates.add(criteriaBuilder.like(root.get(User_.firstName), String.format(likeFormat, userFilter.getFirstName())));
        }

        if (StringUtils.isNotBlank(userFilter.getLastName())) {
            predicates.add(criteriaBuilder.like(root.get(User_.lastName), String.format(likeFormat, userFilter.getLastName())));
        }

        if (StringUtils.isNotBlank(userFilter.getUsername())) {
            predicates.add(root.get(User_.username).in(userFilter.getUsername()));
        }



        Predicate[] predicateArray = predicates.toArray(new Predicate[0]);

        return criteriaBuilder.and(predicateArray);
    }
}
