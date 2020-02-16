package com.funtl.oauth2.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter{

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		//配置客户端
		clients
			//放在内存中
			.inMemory()
			.withClient("client")
			.secret(passwordEncoder.encode("secret"))
			.authorizedGrantTypes("authorization_code")
			.scopes("app")
			.redirectUris("http://www.funtl.com");
	}
	
//	@Override
//	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//		//配置客户端
//		clients
//			//放在内存中
//			.inMemory()
//			.withClient("client")
//			.secret("secret")
//			.authorizedGrantTypes("authorization_code")
//			.scopes("app")
//			.redirectUris("http://www.funtl.com");
//	}
}
