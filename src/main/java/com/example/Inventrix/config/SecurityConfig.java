package com.example.Inventrix.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Nonaktifkan CSRF karena API pakai JSON
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        // 🔓 Izinkan publik akses file gambar upload
                        .requestMatchers("/inventrix/barang/uploads/**").permitAll()

                        // 🔓 Login & guest login bebas tanpa token
                        .requestMatchers("/inventrix/auth/**").permitAll()

                        // 🔓 Endpoint publik untuk karyawan toko (guest)
                        .requestMatchers("/inventrix/barang/list", "/inventrix/barang/detail/**").permitAll()

                        // 🔒 Endpoint sensitif hanya untuk user login (OWNER, WAREHOUSE)
                        .requestMatchers("/inventrix/barang/tambah", "/inventrix/barang/edit/**").authenticated()

                        // 🔒 Semua endpoint lain butuh login
                        .anyRequest().authenticated()
                )

                // Nonaktifkan form login & basic auth
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())
                .logout(logout -> logout.disable())

                // Tambahkan JWT filter
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        System.out.println("✅ [SECURITY CONFIG] SecurityFilterChain aktif — folder uploads publik");
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
