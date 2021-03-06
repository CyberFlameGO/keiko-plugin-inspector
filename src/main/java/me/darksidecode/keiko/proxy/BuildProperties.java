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

package me.darksidecode.keiko.proxy;

import lombok.Getter;
import lombok.NonNull;
import me.darksidecode.keiko.installation.MalformedVersionException;
import me.darksidecode.keiko.installation.Version;

import java.util.Objects;
import java.util.Properties;

public class BuildProperties {

    @Getter
    private final Version version;

    @Getter
    private final String timestamp;

    BuildProperties(@NonNull Properties properties) throws MalformedVersionException {
        version = Version.valueOf(Objects.requireNonNull(properties.getProperty("version"),
                "invalid build.properties: missing \"version\""));

        timestamp = Objects.requireNonNull(properties.getProperty("timestamp"),
                "invalid build.properties: missing \"timestamp\"");
    }

}
