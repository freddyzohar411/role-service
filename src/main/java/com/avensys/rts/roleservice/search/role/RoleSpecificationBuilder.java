package com.avensys.rts.roleservice.search.role;

import com.avensys.rts.roleservice.entity.RoleEntity;
import com.avensys.rts.roleservice.search.SearchCriteria;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/***
 * 
 * @author kotaiah nalleboina
 *
 */
public class RoleSpecificationBuilder {

	List<SearchCriteria> params;

	public RoleSpecificationBuilder() {
		params = new ArrayList<>();
	}

	public RoleSpecificationBuilder with(String key, String operation, String value) {
		params.add(new SearchCriteria(key, operation, value));
		return this;
	}

	public Specification<RoleEntity> build() {
		if (params.size() == 0) {
			return null;
		}

		List<Specification<RoleEntity>> specs = params.stream().map(SearchSpecifications::new)
				.collect(Collectors.toList());

		Specification<RoleEntity> result = specs.get(0);

		for (int i = 1; i < params.size(); i++) {
			result = Specification.where(result).or(specs.get(i));
		}
		return result;
	}

}
