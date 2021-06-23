/*
 * Copyright 2021 German Vekhorev (DarksideCode)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.darksidecode.keiko.registry;

import lombok.NonNull;
import me.darksidecode.keiko.util.StringUtils;

public class Identity {

    private final String filePath, pluginName, className, methodName;

    public Identity(String filePath, String pluginName, String className, String methodName) {
        this(false, filePath, pluginName, className, methodName);
    }

    Identity(boolean allowNulls, String filePath, String pluginName, String className, String methodName) {
        this.filePath   = get(allowNulls, filePath,   "filePath"  );
        this.pluginName = get(allowNulls, pluginName, "pluginName");
        this.className  = get(allowNulls, className,  "className" );
        this.methodName = get(allowNulls, methodName, "methodName");
    }

    private String get(boolean allowNull, String val, @NonNull String what) {
        if (val == null) {
            if (allowNull)
                return null;
            else
                throw new NullPointerException(what + " cannot be null");
        } else
            return StringUtils.basicReplacements(val);
    }

    private boolean isFilterBase() {
        return filePath == null || pluginName == null || className == null || methodName == null;
    }

    boolean matches(@NonNull Identity other) {
        if (!isFilterBase())
            throw new IllegalStateException(
                    "match is only allowed to be called on filter base Identity classes");

        if (other.isFilterBase())
            throw new IllegalStateException(
                    "cannot match a filter base Identity class");

        return     (filePath   == null /* match any */ || StringUtils.matchWildcards(other.filePath,   filePath  ))
                && (pluginName == null /* match any */ || StringUtils.matchWildcards(other.pluginName, pluginName))
                && (className  == null /* match any */ || StringUtils.matchWildcards(other.className,  className ))
                && (methodName == null /* match any */ || StringUtils.matchWildcards(other.methodName, methodName));
    }

}