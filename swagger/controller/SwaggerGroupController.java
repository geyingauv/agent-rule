package cn.wkldtop.wkldv2server.mini.controller;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 查看注册的所有group
 */
@RestController
@RequestMapping("/swagger")
public class SwaggerGroupController {

    private final List<GroupedOpenApi> groupedApis;

    public SwaggerGroupController(List<GroupedOpenApi> groupedApis) {
        this.groupedApis = groupedApis;
    }

    @GetMapping("/groups")
    public List<String> getGroups() {
        return groupedApis.stream()
                .map(GroupedOpenApi::getGroup)
                .collect(Collectors.toList());
    }
}

