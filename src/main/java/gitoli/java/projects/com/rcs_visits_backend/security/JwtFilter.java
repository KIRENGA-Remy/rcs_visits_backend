package gitoli.java.projects.com.rcs_visits_backend.security;

import gitoli.java.projects.com.rcs_visits_backend.service.UserService;
import gitoli.java.projects.com.rcs_visits_backend.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
    throws ServletException, IOException {
        logger.debug("Checking Authorization header...");
        final String authorizationHeader = request.getHeader("Authorization");
        if(authorizationHeader == null) {
            logger.warn("Authorization header is missing or invalid.");
        }
        String username = null;
        String jwtToken = null;
        if (authorizationHeader != null) {
            if (!authorizationHeader.startsWith("Bearer ")) {
                logger.warn("Authorization header is invalid.");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Authorization header format");
                return;
            }
            jwtToken = authorizationHeader.substring(7);
            try {
                username = jwtUtil.extractUsername(jwtToken);
            } catch (Exception e){
                logger.error("JWT Token is extraction failed", e);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT Token");
                return;
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                UserDetails userDetails = userService.loadUserByUsername(username);
                if (!jwtUtil.validateToken(jwtToken, userDetails)) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid/Expired JWT");
                    return;
                }
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (UsernameNotFoundException e){
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Username not found");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
