package consulting.segieth.security;

import jakarta.persistence.*;
import org.jspecify.annotations.Nullable;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "userprofile")
public class UserProfile {

    public static final int DESCRIPTION_MAX_LENGTH = 300;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name = "";

    @Column(name = "email", nullable = false)
    private String email = "";

    @Column(name = "provider", nullable = false)
    private String provider = "";

    @Column(name = "description", nullable = false, length = DESCRIPTION_MAX_LENGTH)
    private String description = "";

    protected UserProfile() { // To keep Hibernate happy
    }

    public UserProfile(String provider, String email) {
    	this(provider,email,email);
    }

    public UserProfile(String provider, String email, String name) {
        setProvider(provider);
        setEmail(email);
        setName(name);
    }

    public @Nullable Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !getClass().isAssignableFrom(obj.getClass())) {
            return false;
        }
        if (obj == this) {
            return true;
        }

        UserProfile other = (UserProfile) obj;
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


	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
