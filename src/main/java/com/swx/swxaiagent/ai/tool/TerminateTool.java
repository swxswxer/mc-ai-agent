package com.swx.swxaiagent.ai.tool;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

public class TerminateTool {

    @Tool(description = """  
            当您认为请求已得到答复或无法继续执行任务时，请终止交互。
            “完成所有任务后，调用此工具进行答案汇总输出并结束工作。
            """)
    public String doTerminate(@ToolParam(description = "需要返回的最终答案") String finalAnswer) {
        return finalAnswer;
    }
}
