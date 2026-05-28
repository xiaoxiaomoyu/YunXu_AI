package com.example.yunxu_ai.tools.agent;

import com.example.yunxu_ai.config.YunxuAgentProperties;
import com.example.yunxu_ai.tools.CourseTools;
import org.junit.jupiter.api.Test;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

class ToolRegistrationTests {

    @Test
    void registersCourseAndAgentTools() {
        YunxuAgentProperties properties = new YunxuAgentProperties();
        properties.setWorkspaceDir("tmp/test-agent-workspace");
        properties.getTerminal().setAllowedWorkingDir("tmp/test-agent-workspace");
        ToolRegistration registration = new ToolRegistration();
        ReflectionTestUtils.setField(registration, "searchApiKey", "");

        ToolCallback[] callbacks = registration.allTools(properties, new CourseTools(null, null, null));

        assertThat(callbacks)
                .extracting(ToolCallback::getToolDefinition)
                .extracting(definition -> definition.name())
                .contains("queryCourse", "readFile", "searchWeb", "scrapeWebPage", "downloadResource", "executeTerminalCommand", "generatePDF", "doTerminate");
    }
}
