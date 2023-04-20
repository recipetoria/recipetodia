package com.jit.rec.recipetoria.security.applicationUser;

import com.jit.rec.recipetoria.entity.Ingredient;
import com.jit.rec.recipetoria.entity.Recipe;
import com.jit.rec.recipetoria.entity.Tag;
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
@Entity
public class ApplicationUser implements UserDetails {

    @Id
    @SequenceGenerator(name = "sequence", sequenceName = "sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    private Long id;

    @Enumerated(EnumType.STRING)
    private ApplicationUserRole applicationUserRole;
    private String email;
    private String password;
    private String name;
    private String photo;
    private boolean locked = true;

    @OneToMany(mappedBy = "applicationUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ingredient> shoppingList;
    //removed commenting from listOfRecipes
    @OneToMany(mappedBy = "applicationUser", cascade = CascadeType.ALL)
    private List<Recipe> listOfRecipes;
    //added field to activate db mapping with tags
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
