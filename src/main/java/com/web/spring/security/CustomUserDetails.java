package com.web.spring.security;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.web.spring.entity.User;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomUserDetails implements UserDetails{
	
	@Getter
	public final User user;
	
	public CustomUserDetails(User user) {
		this.user = user;
		log.info("CustomMemberDetails() ===> user= "+user);

	}
	

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collection = new ArrayList<>();
		collection.add(()->user.getRole());
		return collection;
	}

	@Override
	public String getPassword() {
		log.info("getPassword() ===>");
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		log.info("getUsername() ===>");
		return user.getUserId();
	}
///////////////////
	@Override
    public boolean isAccountNonExpired() { //계정이 Expired 되지 않았다
        log.info("isAccountNonExpired...");
        return true;
    }

    @Override
    public boolean isAccountNonLocked() { //계정이 Lock되지 않았다.
        log.info("isAccountNonLocked...");
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        log.info("isCredentialsNonExpired...");
        return true;
    }

    @Override
    public boolean isEnabled() {
        log.info("isEnabled...");
        return true;
    }

	@Override
	public String toString() {
		return "CustomMemberDetails [user=" + user.toString() + "]";
	}
}
