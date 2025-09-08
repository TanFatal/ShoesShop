package com.example.learningAPISpring.auth.entities;

import com.example.learningAPISpring.entity.Address;
import com.example.learningAPISpring.entity.Cart;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Table(name = "AUTH_USER_DETAILS")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {
    @Id
    @GeneratedValue
    @Column
    private UUID id;

    private String firstName;

    private String lastName;

    @JsonIgnore
    private String password;

    private Date createdOn;

    private Date updatedOn;

    @Column(nullable = false,unique = true)
    private String email;

    private String phoneNumber;

    private String provider;

    private String verificationCode;

    private boolean enabled=false;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "AUTH_USER_AUTHORITY",joinColumns = @JoinColumn(referencedColumnName = "id"),inverseJoinColumns = @JoinColumn(referencedColumnName = "id"))
    private Set<Authority> roles= new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Address> addressList;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Cart cart;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

}
