// ChatType.java
package com.example.yunxu_ai.enums;
//创建一个聊天类型的枚举,通过创建枚举类型来替换硬编码的"chat"字符串,ChatController.java中的
public enum ChatType {
    CHAT("chat"),
    QUESTION("question"),
    ANSWER("answer"),
    SYSTEM("system"),
    GROUP_CHAT("group_chat"),
    PRIVATE_CHAT("private_chat"),
    AI_CHAT("ai_chat"),
    HUMAN_CHAT("human_chat"),
    CUSTOMER_SERVICE("customer_service");

    private final String value;

    ChatType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}