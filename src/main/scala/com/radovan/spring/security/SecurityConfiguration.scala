package com.radovan.spring.security

import com.radovan.spring.security.handler.LoginSuccessHandler
import com.radovan.spring.services.impl.UserDetailsImpl
import org.springframework.context.annotation.{Bean, Configuration}
import org.springframework.security.authentication.{AuthenticationManager, ProviderManager}
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.{HttpSecurity, WebSecurity}
import org.springframework.security.config.annotation.web.configuration.{EnableWebSecurity, WebSecurityCustomizer}
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.util.matcher.AntPathRequestMatcher

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
class SecurityConfiguration {

  @Bean def authenticationManager: AuthenticationManager = {
    val authProvider = new DaoAuthenticationProvider
    authProvider.setUserDetailsService(userDetailsService)
    authProvider.setPasswordEncoder(passwordEncoder)
    new ProviderManager(authProvider)
  }

  @Bean
  @throws[Exception]
  protected def configure(http: HttpSecurity): SecurityFilterChain = {

    http.formLogin(fl => fl.loginPage("/login").successHandler(new LoginSuccessHandler).loginProcessingUrl("/login")
      .usernameParameter("email").passwordParameter("password").permitAll)

    http.logout(logout => logout.permitAll.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
      .logoutSuccessUrl("/login?logout"))

    http.csrf(csrf => csrf.disable())

    http.authorizeHttpRequests(authorize => authorize
      .requestMatchers("/login","/loginErrorPage","/registerComplete","/registerFail").anonymous
      .requestMatchers("/books/**", "/aboutUs").permitAll
      .requestMatchers("/", "/home", "/loginPassConfirm").permitAll
      .requestMatchers("/admin/**").hasAuthority("ADMIN")
      .requestMatchers("/cart/**", "/order/**","/reviews/**").hasAuthority("ROLE_USER")
      .requestMatchers("/userRegistration","/register").anonymous
      .anyRequest.authenticated)
    http.build
  }

  @Bean
  def userDetailsService = new UserDetailsImpl

  @Bean
  def passwordEncoder = new BCryptPasswordEncoder

  @Bean
  def resourcesCustomizer: WebSecurityCustomizer = (web: WebSecurity) =>
    web.ignoring.requestMatchers("/resources/**", "/static/**", "/images/**", "/css/**", "/js/**")
}
