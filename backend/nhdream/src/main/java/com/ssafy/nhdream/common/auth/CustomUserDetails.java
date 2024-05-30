package com.ssafy.nhdream.common.auth;

import com.ssafy.nhdream.entity.user.User;
import com.ssafy.nhdream.entity.user.UserType;
import com.ssafy.nhdream.entity.user.Wallet;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getLoginId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // 식별 ID
    public int getId() {return user.getId();}

    // 유저 직업
    public UserType getUserStatus() {return user.getType();}

    // 유저 이름
    public String getName() {return user.getName();}

    // 유저 지갑 주소
    public Wallet getWallet() {return user.getWallet();}

    // 유저 전화 번호
    public String getPhone() {return user.getPhone();}
}
