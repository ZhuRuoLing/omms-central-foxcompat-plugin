package icu.takeneko.omms.foxcompat

import com.foxapplication.embed.hutool.log.Log
import com.foxapplication.embed.hutool.log.LogFactory
import com.foxapplication.mc.core.config.LocalFoxConfig
import com.foxapplication.mc.core.config.webconfig.WebConfig
import com.foxapplication.mc.interaction.base.data.BaseMessage
import com.foxapplication.mc.interconnection.common.util.MessageUtil
import java.util.concurrent.CopyOnWriteArrayList

object InterconnectionChatSync {
    private val log: Log = LogFactory.get()
    private val handlers: CopyOnWriteArrayList<(BaseMessage) -> Unit> = CopyOnWriteArrayList()
    lateinit var config: InterconnectionChatSyncConfig
    private lateinit var localFoxConfig: LocalFoxConfig
    fun init() {
        log.info("InterconnectionChatSyn Init")

        localFoxConfig = LocalFoxConfig(InterconnectionChatSyncConfig::class.java)
        config = localFoxConfig.beanFoxConfig.bean as InterconnectionChatSyncConfig
        WebConfig.addConfig(localFoxConfig.beanFoxConfig)

        MessageUtil.addListener("InterconnectionChatData") { message: BaseMessage ->
            if (config.nodeWhitelist) {
                if (config.nodeWhitelistUseReverse) {
                    if (config.nodeWhitelistList.contains(message.form)) {
                        return@addListener
                    }
                } else {
                    if (!config.nodeWhitelistList.contains(message.form)) {
                        return@addListener
                    }
                }
            }
            handlers.forEach { it(message) }
        }
    }

    fun onReceiveMessage(handler: (BaseMessage) -> Unit) {
        handlers.add(handler)
    }
}