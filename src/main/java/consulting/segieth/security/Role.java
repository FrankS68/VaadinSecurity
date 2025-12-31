package consulting.segieth.security;

import jakarta.persistence.*;
import org.jspecify.annotations.Nullable;

import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "role")
public class Role {

    public static final int DESCRIPTION_MAX_LENGTH = 300;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "role_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name = "";

    public enum RoleType{User,Permission,Group};
    @Column(name = "type", nullable = false)
    private RoleType type = RoleType.Group;

    @Column(name = "description", nullable = false, length = DESCRIPTION_MAX_LENGTH)
    private String description = "";

    protected Role() { // To keep Hibernate happy
    }

    public Role(String name, RoleType type) {
    	this(name,type,"");
    }

    public Role(String name, RoleType type, String description) {
        setDescription(description);
        setType(type);
        setName(name);
    }

    public @Nullable Long getId() {
        return id;
    }
    
    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
        name = "Role2Role", 
        joinColumns = { @JoinColumn(name = "item_id") }, 
        inverseJoinColumns = { @JoinColumn(name = "group_id") }
    )
    Set<Role> groups = new HashSet<>();

    @ManyToMany(mappedBy = "groups")
    Set<Role> items = new HashSet<>();

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !getClass().isAssignableFrom(obj.getClass())) {
            return false;
        }
        if (obj == this) {
            return true;
        }

        Role other = (Role) obj;
        return getId() != null && getId().equals(other.getId());
    }

    @Override
    public int hashCode() {
        // Hashcode should never change during the lifetime of an object. Because of
        // this we can't use getId() to calculate the hashcode. Unless you have sets
        // with lots of entities in them, returning the same hashcode should not be a
        // problem.
        return getClass().hashCode();
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public RoleType getType() {
		return type;
	}

	public void setType(RoleType type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
