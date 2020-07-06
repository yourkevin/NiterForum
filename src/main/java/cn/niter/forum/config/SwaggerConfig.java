package cn.niter.forum.config;

/**
 * @author wadao
 * @version 1.0
 * @date 2020/6/5 23:21
 * @site niter.cn
 */

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo())
                .select()
             //   .apis(RequestHandlerSelectors.any())
                .apis(RequestHandlerSelectors.basePackage("cn.niter.forum.api"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("NiterForum API Doc")
                .description("This is a restful api document of NiterForum.")
                .contact(new Contact("瓦刀","niter.cn","kuaileky@qq.com"))
                .version("1.0")
                .build();
    }

}
