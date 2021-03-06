package com.maker.store.security.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class AppAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private RedirectStrategy redirectStrategy= new DefaultRedirectStrategy();

    @Override
    protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String targetUrl=determineTargetUrl(authentication);
        redirectStrategy.sendRedirect(request,response,targetUrl);
    }

    protected String determineTargetUrl(Authentication authentication) {
        String url="";
        Collection<? extends GrantedAuthority> authorities=authentication.getAuthorities();
        List<String> roles= new ArrayList<String>();
        for (GrantedAuthority a :
                authorities) {
            roles.add(a.getAuthority());
        }
        if(isUser(roles)){
            url="/home";
        }else if(isMaker(roles)){
            url="/maker";
        }else {
            url="/accessDenied";
        }
        return url;
    }

    private  boolean isUser(List<String> roles){
        if(roles.contains("ROLE_USER")){
            return true;
        }else {
            return false;
        }
    }

    private boolean isMaker(List<String> roles){
        if(roles.contains("ROLE_MAKER")){
            return true;
        }else {
            return false;
        }
    }
    @Override
    public RedirectStrategy getRedirectStrategy() {
        return redirectStrategy;
    }

    @Override
    public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
        this.redirectStrategy = redirectStrategy;
    }


}
