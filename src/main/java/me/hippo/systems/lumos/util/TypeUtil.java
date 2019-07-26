/*
 * Copyright 2019 Hippo
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

package me.hippo.systems.lumos.util;

/**
 * @author Hippo
 * @since 07/25/2019
 */
public enum TypeUtil {
    ;

    /**
     * Parses {@code input} to a {@link Double}.
     *
     * @param input  The input.
     * @return  The double value, if {@code input} is not a double it will return {@code null}.
     */
    public static Double parseDouble(String input) {
        try {
            return Double.parseDouble(input);
        }catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Parses {@code input} to a {@link Boolean}.
     *
     * @param input  The input.
     * @return  The boolean value, if {@code input} is not a boolean it will return {@code null}.
     */
    public static Boolean parseBoolean(String input) {
        boolean value = Boolean.parseBoolean(input);
        return !value && !input.equalsIgnoreCase("false") ? null : value;
    }

}
