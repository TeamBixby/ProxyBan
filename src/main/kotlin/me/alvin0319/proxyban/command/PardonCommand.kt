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
package me.alvin0319.proxyban.command

import dev.waterdog.waterdogpe.command.Command
import dev.waterdog.waterdogpe.command.CommandSender
import dev.waterdog.waterdogpe.command.CommandSettings
import dev.waterdog.waterdogpe.logger.Color
import me.alvin0319.proxyban.ProxyBan

class PardonCommand(name: String, settings: CommandSettings) : Command(name, settings) {

    override fun onExecute(sender: CommandSender, alias: String?, fakeArgs: Array<out String>): Boolean {
        if (!sender.hasPermission(settings.permission)) {
            sender.sendMessage("${Color.RED}${settings.permissionMessage}")
            return true
        }
        val args = fakeArgs.toMutableList()
        if (args.isEmpty()) {
            sender.sendMessage("${Color.RED}Usage: /$name <player: String>")
            return true
        }
        val player = args.removeFirst()
        if (!ProxyBan.instance.banList.exists(player.lowercase())) {
            sender.sendMessage("${Color.RED}${player.lowercase()} is not banned.")
            return true
        }
        ProxyBan.instance.banList.remove(player.lowercase())
        sender.sendMessage("${Color.GREEN}You have pardoned ${player.lowercase()}.")
        return true
    }
}
