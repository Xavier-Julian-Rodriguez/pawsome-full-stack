package pawsome.springframework.pawsomeWebApp.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "pets")
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pet_id")
    private Long id;

    @NotBlank(message = "Pet name is mandatory")
    @Size(max = 255, message = "Pet name must not exceed 255 characters")
    @Column(name = "pet_name")
    private String name;

    @NotNull(message = "Pet age is mandatory")
    @DecimalMin(value = "0.0", inclusive = true, message = "Pet age must be greater than or equal to 0.1")
    @DecimalMax(value = "100.0", inclusive = true, message = "Pet age must be less than or equal to 100")
    @Column(name = "pet_age")
    private BigDecimal age;

    @NotBlank(message = "Pet species is mandatory")
    @Size(max = 255, message = "Pet species must not exceed 255 characters")
    @Column(name = "pet_species")
    private String species;

    @Column(name = "pet_image")
    private byte[] image;

    @Column(name = "create_date", updatable = false)
    @CreationTimestamp
    private Date createDate;

    @Column(name = "last_update")
    @UpdateTimestamp
    private Date lastUpdate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Recipe> recipes;

    @Transient
    private String imageBase64;

    public Pet() {}

    public Pet(Long id, String name, BigDecimal age, String species, byte[] image) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.species = species;
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getAge() {
        return age;
    }

    public void setAge(BigDecimal age) {
        this.age = age;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Date getCreateDate() {
        if(this.createDate == null) {
            this.createDate = new Date();
        }
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastUpdate() {
        if(lastUpdate == null) {
            this.lastUpdate = new Date();
        }
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Set<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(Set<Recipe> recipes) {
        this.recipes = recipes;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }
}
