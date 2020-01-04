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

package me.hippo.systems.api.node.argument;

import me.hippo.systems.api.argument.ArgumentType;
import me.hippo.systems.api.node.CommandNode;

/**
 * @author Hippo
 * @version 1.0.0, 07/25/2019
 * @since 1.0.0
 */
public final class ArgumentCommandNode extends CommandNode {

    /**
     * The name.
     */
    private final String name;

    /**
     * The {@link ArgumentType}.
     */
    private final ArgumentType argumentType;

    /**
     * Creates a new {@link ArgumentCommandNode} with the desired name and argument type.
     *
     * @param name  The name.
     * @param argumentType  The argument type.
     */
    public ArgumentCommandNode(String name, ArgumentType argumentType) {
        this.name = name;
        this.argumentType = argumentType;
    }

    /**
     * Gets the name.
     *
     * @return  The name.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Gets the argument type.
     *
     * @return  The argument type.
     */
    public ArgumentType getArgumentType() {
        return argumentType;
    }
}
