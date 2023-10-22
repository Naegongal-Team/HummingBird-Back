package com.negongal.hummingbird.login.oauth2;

import com.negongal.hummingbird.login.domain.Role;
import com.negongal.hummingbird.login.domain.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Getter
public class CustomUserDetail implements OAuth2User {

    private String oauthId;
    private String provider;
    private String nickname;
    private Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attributes;

    public CustomUserDetail(String oauthId, String provider, String nickname, Collection<? extends GrantedAuthority> authorities) {
        this.oauthId = oauthId;
        this.provider=provider;
        this.nickname=nickname;
        this.authorities = authorities;
    }

    public static CustomUserDetail create(User user) {
        List<GrantedAuthority> authorities;
        if(user.getRole()== Role.ADMIN){
            authorities = Collections.
                    singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        else {
            authorities = Collections.
                    singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        }

        return new CustomUserDetail(
                user.getOauth2Id(),
                user.getProvider(),
                user.getNickname(),
                authorities
        );
    }

    public static CustomUserDetail create(User user, Map<String, Object> attributes) {
        CustomUserDetail userDetails = CustomUserDetail.create(user);
        userDetails.setAttributes(attributes);
        return userDetails;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getName() {
        return String.valueOf(oauthId);
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
}
