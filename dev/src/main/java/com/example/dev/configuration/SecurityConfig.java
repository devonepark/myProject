package com.example.dev.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


//public class SecurityConfig extends WebSecurityConfigurerAdapter {
/*
 * 기존: WebSecurityConfigurerAdapter를 상속하고 configure매소드를 오버라이딩하여 설정하는 방법 => 현재:
 * SecurityFilterChain을 리턴하는 메소드를 빈에 등록하는 방식(컴포넌트 방식으로 컨테이너가 관리)
 * //https://spring.io/blog/2022/02/21/spring-security-without-the-
 * websecurityconfigureradapter
 */
@Configuration
@EnableWebSecurity // 웹보안 활성화
public class SecurityConfig {//extends WebSecurityConfigurerAdapter{

	/*
	 * @Override protected void configure(HttpSecurity http) throws Exception {
	 * http.csrf().disable() // csrf 공격을 막기 위해 state 값을 전달 받지 않는다 .formLogin() // 기본
	 * 제공하는 로그인 화면 사용 .and() .httpBasic(); // http 통신으로 basic auth를 사용 할 수 있다. (ex:
	 * Authorization: Basic bzFbdGfmZrptWY30YQ==) }
	 */

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	http.authorizeRequests()
        // URL matching for accessibility
        .antMatchers("/", "/login", "/register").permitAll()
        .antMatchers("/admin/**").hasAnyAuthority("ADMIN")
        .antMatchers("/account/**").hasAnyAuthority("USER")
        .anyRequest().authenticated()
        .and()
        // form login
        .csrf().disable().formLogin()
        .loginPage("/login")
        .failureUrl("/login?error=true")
        .usernameParameter("email")
        .passwordParameter("password")
        .and()
        // logout
        .logout()
        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
        .logoutSuccessUrl("/")
        .and()
        .exceptionHandling()
        .accessDeniedPage("/access-denied");

        http.headers().frameOptions().sameOrigin();

        return http.build();

		/*
		 * http .authorizeHttpRequests(authorize -> authorize
		 * .anyRequest().authenticated() )
		 * .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt); return
		 * http.build();
		 */
    }

    /**
     * authenticationManager bean 생성 하여 셋팅 안할시 grant_type : password 지원 안함
     * @return
     * @throws Exception
     */
    /*@Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
      return super.authenticationManagerBean();
    }*/

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
