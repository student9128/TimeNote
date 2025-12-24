package com.kevin.timenote.domain.model

enum class RepeatMode(val description: String) {
//    NONE("不提醒"),
    ONCE("提醒一次"),
    YEARLY("每年重复"),
    MONTHLY("每月重复"),
    DAILY("每日重复")
}
