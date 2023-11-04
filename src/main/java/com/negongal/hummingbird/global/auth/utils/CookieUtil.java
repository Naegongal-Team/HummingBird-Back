package com.negongal.hummingbird.global.auth.utils;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Component
public class CookieUtil {

    public static Optional<Cookie> getCookie(HttpServletRequest request, String cookieName){
        Cookie[] cookies = request.getCookies();
        if(cookies!=null){
            for (Cookie c : cookies) {
                if (c.getName().equals(cookieName)) {
                    return Optional.of(c);
                }
            }
        }
        return null;
    }

    public static void deleteCookie(String cookieName, HttpServletResponse response){
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

}
