package com.example.Inventrix.config;

import com.example.Inventrix.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostConstruct
    public void init() {
        System.out.println("✅ [INIT] JwtAuthFilter berhasil diinisialisasi oleh Spring Boot");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String uri = request.getRequestURI();
        System.out.println("🚀 [FILTER] Dipanggil untuk endpoint: " + uri);

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("⚠️ [WARNING] Tidak ada Authorization header atau format Bearer salah");
            filterChain.doFilter(request, response);
            System.out.println("🔚 [FILTER] Selesai memproses request: " + uri);
            return;
        }

        String token = authHeader.substring(7).trim();
        System.out.println("🟩 [TOKEN] Ditemukan: " + token);

        try {
            Claims claims = jwtUtil.extractAllClaims(token);
            String username = claims.getSubject();
            String role = claims.get("role", String.class);

            System.out.println("👤 [JWT] USERNAME: " + username + " | ROLE: " + role);

            if (username != null && role != null) {
                var authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));
                var authToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);

                System.out.println("✅ [JWT] Authentication berhasil untuk user: " + username);
            }

        } catch (ExpiredJwtException e) {
            System.out.println("❌ [JWT] Token kadaluarsa: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        } catch (JwtException e) {
            System.out.println("❌ [JWT] Token tidak valid: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        } catch (Exception e) {
            System.out.println("❌ [JWT] Error tidak terduga: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        filterChain.doFilter(request, response);
        System.out.println("🔚 [FILTER] Selesai memproses request: " + uri);
    }
}
