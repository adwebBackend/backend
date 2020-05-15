package fudan.se.project.security.jwt;

import fudan.se.project.service.JwtUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @Resource
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        if (!StringUtils.isEmpty(token)){
            try {
                String userId = jwtTokenUtil.getUserIdFromToken(token);
                if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    //通过用户名获取用户信息
                    UserDetails userDetails = userDetailsService.loadUserByUsername(userId);
                    //验证jwt是否过期
                    if (jwtTokenUtil.validateToken(token, userDetails)) {
                    /*
                    加载用户、角色、权限信息，Spring Security根据这些信息判断接口的访问权限
                    用已知的用户名和密码创建一个UsernamePasswordAuthenticationToken()对象：
                    1、把用户名和密码都设置到自己本地变量上
                    2、super(userDetails.getAuthorities());父类构造函数需要传一组权限进来
                    3、setAuthenticated(true);表示前面存储进去的信息是否经过身份认证
                    */
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        //把请求的信息设置到UsernamePasswordAuthenticationToken里面，包括发请求的ip、session等
                        authentication.setDetails(new WebAuthenticationDetailsSource()
                                .buildDetails(request));
                        //存放authentication到SecurityContextHolder
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            }
            catch (ExpiredJwtException e){
                returnResponse(response, "Token Expired");
            }
        }
        filterChain.doFilter(request, response);
    }

    private void returnResponse(HttpServletResponse response, String data) {
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json, text/plain, */*");
        response.setStatus(403);
        try {
            writer = response.getWriter();
            writer.print(data);
        }catch (IOException e){
            e.printStackTrace();
        }
        finally {
            if (writer != null)
                writer.close();
        }
    }
}
