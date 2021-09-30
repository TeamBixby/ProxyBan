/*
 *
 *   ____                      ____
 *  |  _ \ _ __ _____  ___   _| __ )  __ _ _ __
 *  | |_) | '__/ _ \ \/ / | | |  _ \ / _` | '_ \
 *  |  __/| | | (_) >  <| |_| | |_) | (_| | | | |
 *  |_|   |_|  \___/_/\_\\__, |____/ \__,_|_| |_|
 *                       |___/
 * MIT License
 *
 * Copyright (c) 2021 alvin0319
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package me.alvin0319.proxyban

import dev.waterdog.waterdogpe.command.CommandSettings
import dev.waterdog.waterdogpe.event.defaults.PlayerPreLoginEvent
import dev.waterdog.waterdogpe.plugin.Plugin
import dev.waterdog.waterdogpe.utils.config.YamlConfig
import me.alvin0319.proxyban.command.BanCommand
import me.alvin0319.proxyban.command.PardonCommand
import java.text.SimpleDateFormat
import java.util.Date

class ProxyBan : Plugin() {

    lateinit var banList: YamlConfig

    override fun onEnable() {
        instance = this
        loadConfig()
        banList = YamlConfig(dataFolder.resolve("banned_players.yml"))
        proxy.eventManager.subscribe(PlayerPreLoginEvent::class.java, ::onPlayerPreLogin)

        proxy.commandMap.registerCommand(
            BanCommand(
                "pban",
                CommandSettings.builder()
                    .apply {
                        usageMessage = ""
                        permission = "pban.command.ban"
                        permissionMessage = "You don't have permission to use this command."
                        description = "Ban specific player from proxy."
                    }.build()
            )
        )
        proxy.commandMap.registerCommand(
            PardonCommand(
                "ppardon",
                CommandSettings.builder()
                    .apply {
                        usageMessage = ""
                        permission = "pban.command.pardon"
                        permissionMessage = "You don't have permission to use this command."
                        description = "Pardon specific player from proxy."
                    }.build()
            )
        )
    }

    override fun onDisable() {
        banList.save()
    }

    fun onPlayerPreLogin(event: PlayerPreLoginEvent) {
        val player = event.loginData.displayName
        if (isBanned(player)) {
            val message = StringBuilder(banList.getString("banned-message", "You are banned."))
            if (getLeftTime(player) != -1L) {
                message.append(
                    "\n${
                    config.getString(
                        "banned-message-expire",
                        "Your ban will expire at %0"
                    )
                    }".replace("%0", SimpleDateFormat("dd/MM/yyyy").format(Date(getLeftTime(player))))
                )
            }
            event.cancelReason = message.toString()
            event.setCancelled()
        }
    }

    fun isBanned(player: String): Boolean {
        if (banList.exists(player.lowercase()) && getLeftTime(player) != -1L && getLeftTime(player) >= Date().time) {
            config.remove(player.lowercase())
        }
        return banList.exists(player.lowercase()) || (
            banList.exists(player.lowercase()) && (banList.getLong(player.lowercase()) != -1L) && (
                banList.getLong(
                    player.lowercase()
                ) >= Date().time
                )
            )
    }

    fun getLeftTime(player: String): Long {
        return banList.getLong(player.lowercase(), -1)
    }

    companion object {
        @JvmStatic
        lateinit var instance: ProxyBan
            private set
    }
}
