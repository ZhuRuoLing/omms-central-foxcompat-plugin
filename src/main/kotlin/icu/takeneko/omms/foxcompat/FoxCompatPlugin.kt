package icu.takeneko.omms.foxcompat

import com.foxapplication.embed.hutool.core.io.FastByteArrayOutputStream
import com.foxapplication.mc.core.FoxCore
import com.foxapplication.mc.core.Platform
import com.foxapplication.mc.core.config.webconfig.WebConfig
import com.foxapplication.mc.interconnection.common.Interconnection
import com.foxapplication.mc.interconnection.common.util.MessageUtil
import icu.takeneko.omms.central.network.chatbridge.Broadcast
import icu.takeneko.omms.central.network.chatbridge.sendBroadcast
import icu.takeneko.omms.central.plugin.PluginMain
import icu.takeneko.omms.central.plugin.callback.ChatbridgeBroadcastReceivedCallback
import net.kyori.adventure.nbt.BinaryTagIO
import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.nbt.IntArrayBinaryTag
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import java.util.*
import kotlin.concurrent.thread


class FoxCompatPlugin : PluginMain() {

    override fun onInitialize() {
        Runtime.getRuntime().addShutdownHook(thread(start = false, name = "FoxCompatStopThread") {
            Interconnection.stop()
        })
        FoxCore.Init(Platform.Other)
        Interconnection.Init()
        InterconnectionChatSync.init()
        Interconnection.onServerStarted()
        ChatbridgeBroadcastReceivedCallback.INSTANCE.register {
            if (InterconnectionChatSync.config.enable) {
                val compoundTag = CompoundBinaryTag.empty()
                compoundTag.putString("message", it.content)
                compoundTag.putString(
                    "displayName",
                    JSONComponentSerializer.json().serialize(Component.text(it.player))
                )
                compoundTag.put("UUID", IntArrayBinaryTag.intArrayBinaryTag(*toIntArray(UUID.randomUUID())))
                val out = FastByteArrayOutputStream()
                BinaryTagIO.writer().write(compoundTag, out)
                MessageUtil.send("ALL", "InterconnectionChatData", out.toByteArray())
            }
        }
        InterconnectionChatSync.onReceiveMessage {
            if (InterconnectionChatSync.config.enable) {
                val compoundTag = it.message.inputStream().use { i -> BinaryTagIO.reader().read(i) }
                val sender = LegacyComponentSerializer.legacySection().serialize(JSONComponentSerializer.json().deserialize(compoundTag.getString("displayName")))
                sendBroadcast(Broadcast().apply {
                    setChannel("GLOBAL")
                    setContent(compoundTag.getString("message"))
                    setPlayer(sender)
                    setServer("Interconnection")
                })
            }
        }
    }

    override fun preOnReload() {

    }

    override fun postOnReload() {

    }

    private fun toIntArray(uuid: UUID): IntArray {
        val l = uuid.mostSignificantBits
        val m = uuid.leastSignificantBits
        return toIntArray(l, m)
    }

    private fun toIntArray(uuidMost: Long, uuidLeast: Long): IntArray {
        return intArrayOf((uuidMost shr 32).toInt(), uuidMost.toInt(), (uuidLeast shr 32).toInt(), uuidLeast.toInt())
    }
}