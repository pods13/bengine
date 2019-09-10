package com.pods.bengine;

import com.tinify.Tinify;
import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvEntry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.StandardEnvironment;

import java.util.Map;
import java.util.stream.Collectors;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class Application implements ApplicationRunner {

    @Value("${TINIFY_API_KEY}")
    private String tinifyApiKey;

    public static void main(String[] args) {
        Map<String, Object> dotenv = Dotenv.load().entries().stream()
                .collect(Collectors.toMap(DotenvEntry::getKey, DotenvEntry::getValue));
        new SpringApplicationBuilder(Application.class)
                .bannerMode(Banner.Mode.OFF)
                .environment(new StandardEnvironment() {
                    @Override
                    protected void customizePropertySources(MutablePropertySources propertySources) {
                        super.customizePropertySources(propertySources);
                        propertySources.addLast(new MapPropertySource("dotenvProperties", dotenv));
                    }
                }).run(args);
    }

    @Override
    public void run(ApplicationArguments args) {
        Tinify.setKey(tinifyApiKey);
    }
}
