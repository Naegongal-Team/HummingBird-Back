package com.negongal.hummingbird.global.auth.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {

    public String getCookie(String cookieName, HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if(cookies!=null){
            for (Cookie c : cookies) {
                String name = c.getName();
                if (name.equals(cookieName)) {
                    return c.getValue();
                }
            }
        }
        return null;
    }

    public void deleteCookie(String cookieName, HttpServletResponse response){
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

}
