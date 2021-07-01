/*
 * Copyright (C) 2019-2021 German Vekhorev (DarksideCode)
 *
 * This file is part of Keiko Plugin Inspector.
 *
 * Keiko Plugin Inspector is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Keiko Plugin Inspector is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Keiko Plugin Inspector.  If not, see <https://www.gnu.org/licenses/>.
 */

package me.darksidecode.keiko.runtimeprotect.megane.heur.impl;

import lombok.NonNull;
import me.darksidecode.keiko.registry.Identity;
import me.darksidecode.keiko.runtimeprotect.megane.event.bukkit.BukkitPlayerChatEvent;
import me.darksidecode.keiko.runtimeprotect.megane.event.bukkit.BukkitPlayerJoinEvent;
import me.darksidecode.keiko.runtimeprotect.megane.event.craftbukkit.CraftBukkitCommandEvent;
import me.darksidecode.keiko.runtimeprotect.megane.event.minecraft.MinecraftOpAddEvent;
import me.darksidecode.keiko.runtimeprotect.megane.heur.Heuristic;
import me.darksidecode.keiko.runtimeprotect.megane.heur.RegisterHeuristic;
import me.darksidecode.keiko.runtimeprotect.megane.heur.Report;
import me.darksidecode.keiko.util.time.AtomicClock;
import me.darksidecode.keiko.util.time.Clock;

@RegisterHeuristic ({
        BukkitPlayerJoinEvent.class,
        MinecraftOpAddEvent.class
})
public class ForceOpHeuristic extends Heuristic {

    private static final long THRESHOLD_MILLIS = 500;

    private final Clock playerJoinClock = new AtomicClock();
    private final Clock playerChatClock = new AtomicClock();

    @Override
    public void onBukkitPlayerJoin(@NonNull BukkitPlayerJoinEvent e) {
        playerJoinClock.reset();
    }

    @Override
    public void onBukkitPlayerChat(@NonNull BukkitPlayerChatEvent e) {
        playerChatClock.reset();
    }

    @Override
    public void onMinecraftOpAdd(@NonNull MinecraftOpAddEvent e) {
        if (e.isIssuedByPlugin()) {
            Identity plugin = e.getPluginStates().getPlugin();

            if (!playerJoinClock.hasElapsed(THRESHOLD_MILLIS))
                report(plugin, "setOpOnJoin");
            else if (!playerChatClock.hasElapsed(THRESHOLD_MILLIS))
                report(plugin, "setOpOnChat");
        }
    }

    @Override
    public void onCraftBukkitCommand(@NonNull CraftBukkitCommandEvent e) {
        if (e.isIssuedByPlugin()) {
            Identity plugin = e.getPluginStates().getPlugin();

            if (!playerJoinClock.hasElapsed(THRESHOLD_MILLIS))
                report(plugin, "cmdOnJoin");
            else if (!playerChatClock.hasElapsed(THRESHOLD_MILLIS))
                report(plugin, "cmdOnChat");
        }
    }

    private void report(Identity plugin, String detail) {
        makeReport(Report.newBuilder(Report.Severity.HIGH)
                .localizedLine(i18nPrefix + "title",
                        plugin.getPluginName(), plugin.getClassName(), plugin.getMethodName())
                .line(" ")
                .localizedLine(i18nPrefix + detail)
                .build()
        );
    }

}