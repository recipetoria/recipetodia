package com.jit.rec.recipetoria.applicationUser;

import com.jit.rec.recipetoria.ingredient.Ingredient;
import com.jit.rec.recipetoria.recipe.Recipe;
import com.jit.rec.recipetoria.tag.Tag;
import lombok.*;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"shoppingList", "listOfRecipes", "tags"})
@ToString(exclude = {"shoppingList", "listOfRecipes", "tags"})
@Entity
public class ApplicationUser implements UserDetails {

    @Id
    @SequenceGenerator(name = "sequence", sequenceName = "sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    private Long id;

    @Enumerated(EnumType.STRING)
    private ApplicationUserRole applicationUserRole;

    @Column(unique = true)
    private String email;

    private String password;

    private String name;

    private String profilePhoto;

    private boolean locked = true;

    @OneToMany(mappedBy = "applicationUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ingredient> shoppingList;

    @OneToMany(mappedBy = "applicationUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Recipe> listOfRecipes;

    @OneToMany(mappedBy = "applicationUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Tag> tags;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(applicationUserRole.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
