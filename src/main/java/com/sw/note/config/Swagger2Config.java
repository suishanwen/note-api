package com.sw.note.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Description: Swagger2 配置文件，配置swagger2的一些基本内容
 * @Author: wangyongfei
 * @CreateDate: 2019/1/25 9:26
 * @UpdateUser: wangyongfei
 * @UpdateDate: 2019/1/25 9:26
 * @UpdateRemark: 修改内容
 * @Version: 0.0.1
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.sw.note.api"))
                .paths(PathSelectors.any())
                .build();
    }

    //构建api文档的详细信息函数
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("笔记API")  // 页面标题
                .version("1.0.0")
                .description("笔记API")
                .build();
    }
}
