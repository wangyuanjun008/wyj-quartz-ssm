package com.wyj.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wyj.constant.CommonConstant;

public class LoginFilter implements Filter{

    private Logger logger = LoggerFactory.getLogger(LoginFilter.class);
    
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.debug("init  LoginFilter ...");         
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        HttpSession session = request.getSession();
        //判断Session中是否有登录用户信息
        String toke = (String) session.getAttribute(CommonConstant.LONGIN_TOKE);
        if(!StringUtils.isEmpty(toke)){
            chain.doFilter(req, resp);
        }else{
            //若没有则，跳转到登录页面
            response.sendRedirect(request.getContextPath() + "/user/toLogin");
        }        
    }

    public void destroy() {
        logger.debug("destroy LoginFilter ...");                 
    }

}
