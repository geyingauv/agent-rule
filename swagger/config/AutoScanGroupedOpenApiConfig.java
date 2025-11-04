import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 自动扫描controller并注册为group
 */
@Configuration
public class AutoScanGroupedOpenApiConfig {

    @Bean
    public List<GroupedOpenApi> groupedApis() {
        ClassPathScanningCandidateComponentProvider scanner =
                new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(RestController.class));

        String basePackage = "xxxxx"; //修改为controller所在包路径
        Set<String> groups = new HashSet<>();

        scanner.findCandidateComponents(basePackage).forEach(candidate -> {
            try {
                Class<?> clazz = Class.forName(candidate.getBeanClassName());
                RequestMapping mapping = clazz.getAnnotation(RequestMapping.class);
                if (mapping == null) return;
                for (String path : mapping.value()) {
                    String g = extractGroupName(path);
                    if (g != null) groups.add(g);
                }
            } catch (Exception ignored) {
            }
        });

        // 注册group
        List<GroupedOpenApi> list = new ArrayList<>();
        for (String group : groups) {
            list.add(GroupedOpenApi.builder()
                    .group(group)
                    .pathsToMatch("/v2/" + group + "/**", "/api/v2/" + group + "/**")
                    .addOpenApiCustomizer(o -> o.info(new Info()
                            .title(group.toUpperCase() + " API")
                            .description("Auto-generated group for " + group)
                            .version("v1.0")))
                    .build());
            System.out.println("[Swagger Group] Registered: " + group);
        }
        return list;
    }

    /**
     * 从RequestMapping中提取groupName
     */
    private static String extractGroupName(String path) {
        if (path == null) return null;
        String[] parts = path.split("/");
        if (parts.length >= 4 && "api".equals(parts[1]) && "v2".equals(parts[2])) return parts[3];
        if (parts.length >= 3 && "v2".equals(parts[1])) return parts[2];
        return null;
    }
}
