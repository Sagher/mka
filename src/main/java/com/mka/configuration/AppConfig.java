package com.mka.configuration;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.dbcp.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.http.CacheControl;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.resource.VersionResourceResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

/**
 *
 * @author Sagher Mehmood
 */
@EnableWebMvc
@Configuration
@EnableAsync
@EnableTransactionManagement
@PropertySource("classpath:app.properties")
@ComponentScan({"com.mka.*"})
public class AppConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private Environment env;

    private SessionFactory sessionFactory;

    // for resolving properties using ${}
    @Bean
    public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }


    /* *******************************************************
     * Override addResourceHandlers to configure static resources
     * and their caching values and paths, we can also map 
     * external resources to certain prefixes
     * 
     * @Param: ResourceHandlerRegistry
     *
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/")
                .setCacheControl(CacheControl.maxAge(60, TimeUnit.DAYS))
                .addResourceLocations("/").resourceChain(false)
                .addResolver(new VersionResourceResolver().addContentVersionStrategy("/**"));

        registry.addResourceHandler("/er/**").addResourceLocations(env.getProperty("external.resource.path"));

    }

    /* *******************************************************
     * Override addInterceptors to configure spring interceptor
     * This methods loads the HandlerInterceptor implemented class 
     * which will intercept every request before hitting the controller
     * we can also configure the InterceptorRegistry to exclude certain paths
     * 
     * @Param: InterceptorRegistry
     *
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getFilter())
                .addPathPatterns("/**")
                .excludePathPatterns("/403", "/login");
    }

    /* *******************************************************
     * Register InternalResourceViewResolver Bean
     *
     */
    @Bean
    public InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver
                = new InternalResourceViewResolver();
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/pages/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

    /* *******************************************************
     * Register MultipartResolver Bean
     *
     */
    @Bean
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }

    /* *******************************************************
     * Register Filters Bean for request interception
     *
     */
    @Bean
    Filters getFilter() {
        return new Filters();
    }


    /* *******************************************************
     * Register SessionFactory Bean for hibernate
     *
     */
    @Bean
    public SessionFactory sessionFactory() {
        if (sessionFactory != null) {
            return sessionFactory;
        } else {
            return buildSessionFactory();
        }
    }

    /* *******************************************************
     * Register BasicDataSource Bean for hibernate
     *
     */
    @Bean(name = "dataSource")
    public BasicDataSource dataSource() {

        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(env.getProperty("mysql.driver"));

        ds.setInitialSize(0);
        ds.setMaxActive(Integer.parseInt(env.getProperty("mysql.max.active.connection")));
        ds.setMaxIdle(0);
        ds.setDefaultAutoCommit(true);

        ds.setUrl(env.getProperty("mysql.url"));
        ds.setUsername(env.getProperty("mysql.user"));
        ds.setPassword(env.getProperty("mysql.password"));

        return ds;
    }

    /* *******************************************************
     * Register HibernateTransactionManager Bean for hibernate
     *
     */
    @Bean(name = "transactionManager")
    public HibernateTransactionManager txManager() {
        return new HibernateTransactionManager(sessionFactory());
    }

    private SessionFactory buildSessionFactory() {
        BasicDataSource dataSource = dataSource();
        try {
            Connection conn = dataSource.getConnection();
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(AppConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
        LocalSessionFactoryBuilder builder
                = new LocalSessionFactoryBuilder(dataSource);
        builder.scanPackages("com.mka.model")
                .addProperties(getHibernateProperties());

        return builder.buildSessionFactory();
    }

    private Properties getHibernateProperties() {
        Properties prop = new Properties();
        prop.put("hibernate.format_sql", env.getProperty("hibernate.format_sql"));
        prop.put("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
        prop.put("hibernate.use_sql_comments", env.getProperty("hibernate.use_sql_comments"));
        prop.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
        prop.put("hibernate.jdbc.batch_size", env.getProperty("hibernate.jdbc.batch_size"));

        prop.put("initialSize", env.getProperty("initialSize"));
        prop.put("maxActive", env.getProperty("maxActive"));
        prop.put("maxIdle", env.getProperty("maxIdle"));
        prop.put("maxConnLifetimeMillis", env.getProperty("maxConnLifetimeMillis"));
        prop.put("removeAbandonedTimeout", env.getProperty("removeAbandonedTimeout"));
        prop.put("removeAbandonedOnMaintenance ", env.getProperty("removeAbandonedOnMaintenance"));
        prop.put("removeAbandonedOnBorrow", env.getProperty("removeAbandonedOnBorrow"));

        return prop;
    }

}
