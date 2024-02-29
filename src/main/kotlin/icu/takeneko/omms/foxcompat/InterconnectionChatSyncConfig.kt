package icu.takeneko.omms.foxcompat

import com.foxapplication.mc.core.config.interfaces.FieldAnnotation
import java.util.concurrent.CopyOnWriteArrayList

class InterconnectionChatSyncConfig {
    @FieldAnnotation(value = "是否启用聊天消息同步", name = "聊天消息同步功能开关")
    var enable: Boolean = true

    @FieldAnnotation(value = "是否启用节点白名单", name = "节点白名单功能开关")
    var nodeWhitelist: Boolean = false

    @FieldAnnotation(value = "节点白名单使用黑名单模式", name = "节点黑名单功能开关")
    var nodeWhitelistUseReverse: Boolean = true

    @FieldAnnotation(value = "节点白名单列表", name = "节点白名单列表")
    var nodeWhitelistList: List<String> = CopyOnWriteArrayList()

    @FieldAnnotation(value = "是否启用玩家白名单", name = "玩家白名单功能开关")
    var playerWhitelist: Boolean = false

    @FieldAnnotation(value = "玩家白名单使用黑名单模式", name = "玩家黑名单功能开关")
    var playerWhitelistUseReverse: Boolean = true

    @FieldAnnotation(value = "玩家白名单列表，使用的是***UUID***匹配", name = "玩家白名单列表")
    var playerWhitelistList: List<String> = CopyOnWriteArrayList()
}