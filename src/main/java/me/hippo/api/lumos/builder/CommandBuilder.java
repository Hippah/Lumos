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

package me.hippo.api.lumos.builder;

import me.hippo.api.lumos.Command;
import me.hippo.api.lumos.argument.ArgumentType;
import me.hippo.api.lumos.argument.impl.BooleanArgumentType;
import me.hippo.api.lumos.argument.impl.NumberArgumentType;
import me.hippo.api.lumos.argument.impl.StringArgumentType;
import me.hippo.api.lumos.node.CommandNode;
import me.hippo.api.lumos.node.argument.ArgumentCommandNode;
import me.hippo.api.lumos.node.label.LabelCommandNode;

/**
 * @author Hippo
 * @version 1.0.0, 07/25/2019
 * @since 1.0.0
 */
public final class CommandBuilder {

    /**
     * The command node.
     */
    private final CommandNode commandNode;

    /**
     * Creates a new {@link CommandBuilder} for labels.
     *
     * @param label  The label.
     */
    public CommandBuilder(String label) {
        this.commandNode = new LabelCommandNode(label);
    }

    /**
     * Creates a new {@link CommandBuilder} for valued arguments.
     * @param name  The name.
     * @param argumentType  The type.
     */
    public CommandBuilder(String name, ArgumentType argumentType) {
        this.commandNode = new ArgumentCommandNode(name, argumentType);
    }

    /**
     * Adds an argument/child node.
     *
     * @param argument  The argument.
     * @return  {@code this}.
     */
    public CommandBuilder then(CommandBuilder argument) {
        commandNode.addChild(argument.build());
        return this;
    }

    /**
     * Sets the {@link #commandNode}'s command.
     *
     * @param command  The command.
     * @return  {@code this}.
     */
    public CommandBuilder executes(Command command) {
        commandNode.setCommand(command);
        return this;
    }

    /**
     * Builds the {@link CommandBuilder}.
     *
     * @return  The {@link #commandNode}.
     */
    public CommandNode build() {
        return commandNode;
    }

    /*Just to make things look pretty when making commands (if you static import)*/

    public static CommandBuilder label(String label) {
        return new CommandBuilder(label);
    }
    public static CommandBuilder argument(String name, ArgumentType argumentType) {
        return new CommandBuilder(name, argumentType);
    }
    public static NumberArgumentType number() {
        return new NumberArgumentType();
    }
    public static BooleanArgumentType bool() {
        return new BooleanArgumentType();
    }
    public static StringArgumentType string() {
        return new StringArgumentType();
    }
}
