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
 * @version 1.0.0, 07/25/2019
 * @since 1.1.0
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
        input = translateBoolean(input);

        boolean value = Boolean.parseBoolean(input);
        return !value && !input.equalsIgnoreCase("false") ? null : value;
    }

    /**
     * Translates a possible boolean values to true or false.
     *
     * @param input The boolean input.
     * @return  The translated boolean.
     */
    private static String translateBoolean(String input) {
        return translateTrueBoolean(translateFalseBoolean(input));
    }

    /**
     * Translates a possible false boolean value.
     *
     * @param input  The false input.
     * @return  The translated boolean.
     */
    private static String translateFalseBoolean(String input) {
        input = input.toLowerCase();
        return input.equals("off") || input.equals("no") || input.equals("false") ? "false" : input;
    }

    /**
     * Translates a possible true boolean.
     *
     * @param input  The true input.
     * @return  The translated boolean.
     */
    private static String translateTrueBoolean(String input) {
        input = input.toLowerCase();
        return input.equals("on") || input.equals("yes") || input.equals("true") ? "true" : input;
    }

}
