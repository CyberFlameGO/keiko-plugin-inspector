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

package me.darksidecode.keiko.util;

import me.darksidecode.keiko.proxy.Keiko;
import me.darksidecode.keiko.registry.Identity;
import me.darksidecode.keiko.registry.IndexedPlugin;

public final class RuntimeUtils {

    private RuntimeUtils() {}

    public static Identity getCaller() {
        StackTraceElement[] stacktrace = new Exception().getStackTrace();

        for (StackTraceElement e : stacktrace) {
            String callerClassName = e.getClassName();
            String callerMethodName = e.getMethodName();

            IndexedPlugin plugin = Keiko.INSTANCE
                    .getPluginContext().getClassOwner(callerClassName);

            if (plugin != null)
                return new Identity(
                        plugin.getJar().getAbsolutePath(), plugin.getName(),
                        callerClassName, callerMethodName
                );
        }

        return null;
    }

}
