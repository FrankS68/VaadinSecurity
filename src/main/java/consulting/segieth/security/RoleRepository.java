package consulting.segieth.security;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import consulting.segieth.security.Role.RoleType;

interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {

    // If you don't need a total row count, Slice is better than Page as it only performs a select query.
    // Page performs both a select and a count query.
    Slice<Role> findAllBy(Pageable pageable);
	List<Role> findByName(String name);
	List<Role> findByGroups(RoleType type);
}
