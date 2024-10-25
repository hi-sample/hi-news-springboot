package net.hifor.demo.config;

import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.support.config.FastJsonConfig;
import com.alibaba.fastjson2.support.spring6.http.converter.FastJsonHttpMessageConverter;
import com.alibaba.fastjson2.support.spring6.webservlet.view.FastJsonJsonView;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p> fastjson2集成配置 </p>
 * https://github.com/alibaba/fastjson2/blob/main/docs/spring_support_cn.md
 *
 * @author KDev <br/>
 * @date 2024/9/25 <br/>
 */
@Configuration
@EnableWebMvc
public class FastJson2Configuration implements WebMvcConfigurer {

    @Bean
    public FastJsonConfig fastJsonConfig(){
        // 自定义配置
        FastJsonConfig config = new FastJsonConfig();
        // 默认格式为: yyyy-MM-dd HH:mm:ss
        //config.setDateFormat("yyyy-MM-dd HH:mm:ss");

        // 配置序列化的行为
        config.setWriterFeatures(
                //JSONWriter.Feature.PrettyFormat
        );

        // 配置反序列化的行为
        config.setReaderFeatures(
                JSONReader.Feature.FieldBased,
                JSONReader.Feature.SupportArrayToBean
        );

        return config;
    }

    /**
     * 使用 FastJsonHttpMessageConverter 来替换 Spring MVC 默认的 HttpMessageConverter,
     * 以提高 @RestController @ResponseBody @RequestBody 注解的 JSON序列化和反序列化速度。
     * @param converters
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();

        converter.setFastJsonConfig(fastJsonConfig());
        converter.setDefaultCharset(StandardCharsets.UTF_8);

        List<MediaType> supportedMediaTypes = new ArrayList<>();
        // 添加支持的媒体类型 HTTP Accept
        supportedMediaTypes.add(MediaType.APPLICATION_JSON);
        converter.setSupportedMediaTypes(supportedMediaTypes);

        converters.add(0, converter);
    }

    /**
     * 使用 FastJsonJsonView 来设置 Spring MVC 默认的视图模型解析器，
     * 以提高 @Controller @ResponseBody ModelAndView JSON序列化速度。
     * @param registry
     */
    /*@Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        FastJsonJsonView fastJsonJsonView = new FastJsonJsonView();
        //自定义配置...
        //FastJsonConfig config = new FastJsonConfig();
        //config.set...
        //fastJsonJsonView.setFastJsonConfig(config);
        registry.enableContentNegotiation(fastJsonJsonView);
    }*/

}
