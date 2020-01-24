package com.example.study.model.specs;

import com.example.study.model.entity.Item;
import com.example.study.model.entity.Partner;
import com.example.study.model.entity.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class ItemSpecification {

    public static Specification<Item> searchWith(Map<String, Object> searchKeyword) {
        return (Specification<Item>) ((root, query, builder) -> {
            List<Predicate> predicate = getPredicateWithKeyword(searchKeyword, root, builder);
            return builder.and(predicate.toArray(new Predicate[0]));
        });
    }

    /*public static Specification<Client> clientsOfGroup(long groupNo) {
        return (root, query, cb) -> {
            Join<Client, ClientGroup> join = root.join(Client_.groups);
            return cb.equal(join.get(ClientGroup_.group).get(Group_.no), groupNo);
        };
    }*/

    private static List<Predicate> getPredicateWithKeyword(Map<String, Object> searchKeyword, Root<Item> root, CriteriaBuilder builder) {
        List<Predicate> predicate = new ArrayList<>();
        for (String key : searchKeyword.keySet()) {
            if("brandName".equals(key)){
                predicate.add(builder.like(root.get(key), "%"+searchKeyword.get(key)+"%"));
            }else if("partner".equals(key)){
                /*predicate.add(builder.like(root.get(key), "%"+searchKeyword.get(key)+"%"));*/
                Join<Item,Partner> join = root.join("partner");
                predicate.add(builder.like(join.get("name"), "%"+searchKeyword.get(key)+"%"));
            }else{
                predicate.add(builder.equal(root.get(key), searchKeyword.get(key)));
            }
        }
        return predicate;
    }

}