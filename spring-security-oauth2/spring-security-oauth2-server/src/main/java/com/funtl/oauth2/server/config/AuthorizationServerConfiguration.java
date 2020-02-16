package com.funtl.oauth2.server.config;


import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter{

	//配置数据源
	@Bean  //配置
	@Primary //为了防止重复配置 
	@ConfigurationProperties(prefix = "spring.datasource.hikari") //指定配置
	public DataSource datasource() {
		return  DataSourceBuilder.create().build();
	}
	
	//将token存储起来
	@Bean
	public  TokenStore tokenStore() {
		return new JdbcTokenStore(datasource());
	}
	
	//获取客户端相关信息
	@Bean
	public ClientDetailsService jdbcClientDetails(){
		return new JdbcClientDetailsService(datasource());
	}
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		//配置客户端
		/*clients
			//放在内存中
			.inMemory()
			.withClient("client")
			.secret(passwordEncoder.encode("secret"))
			.authorizedGrantTypes("authorization_code")
			.scopes("app")
			.redirectUris("http://www.funtl.com");*/
		
		//客户端信息走jdbc
		clients.withClientDetails(jdbcClientDetails());
	}
	
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		//token存放在数据库中
		endpoints.tokenStore(tokenStore());
	}
}
