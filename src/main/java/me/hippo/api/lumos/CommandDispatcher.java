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

package me.hippo.api.lumos;

import me.hippo.api.lumos.builder.CommandBuilder;
import me.hippo.api.lumos.exception.CommandSyntaxException;
import me.hippo.api.lumos.context.ArgumentValueWrapper;
import me.hippo.api.lumos.context.CommandContext;
import me.hippo.api.lumos.exception.CommandException;
import me.hippo.api.lumos.node.CommandNode;

import java.util.*;

/**
 * @author Hippo
 * @version 1.1.0, 07/25/2019
 * @since 1.0.0
 */
public final class CommandDispatcher {

    /**
     * A map to store all our {@link CommandNode}s.
     */
    private static final Map<String, CommandNode> COMMAND_NODES = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    /**
     * Registers a command.
     * <p>
     *     You use the {@link CommandBuilder} to create the command.
     * </p>
     *
     * @param commandBuilder  The builder.
     */
    public static void register(CommandBuilder commandBuilder) {
        CommandNode commandNode = commandBuilder.build();
        COMMAND_NODES.put(commandNode.getName(), commandNode);
    }

    /**
     * Parses a command then executes it.
     *
     * @param input  The command input.
     * @throws CommandSyntaxException  If the command usage is invalid, or if the command doesn't even exist.
     */
    public static void execute(String input) throws CommandException {
        CommandContext commandContext = new CommandContext(input);
        Scanner scanner = new Scanner(input);
        String commandName = scanner.next();

        while (COMMAND_NODES.get(commandName) == null) {
            commandName = commandName.concat(" ").concat(scanner.next());
        }

        CommandNode commandNode = COMMAND_NODES.get(commandName);

        if (scanner.hasNext()) {
            String argument = scanner.next().trim();

            boolean next = false;
            do {
                if(argument.isEmpty()) {
                    argument = scanner.next();
                }

                if(next) {
                    argument = argument.concat(" ").concat(scanner.next()).trim();
                    next = false;
                }

                String argumentName = argument;
                for (String key : commandContext.getArguments().keySet()) {
                    ArgumentValueWrapper argumentValueWrapper = commandContext.getArguments().get(key);

                    if(argumentValueWrapper.getValueString().equalsIgnoreCase(argument)) {
                        argumentName = key;
                    }
                }

                CommandNode child = commandNode.getChild(argumentName);

                if(child != null) {
                    commandNode = child;
                    argument = "";
                }else {
                    next = true;
                }
            }while (scanner.hasNext());
        }

        if(commandNode.getCommand() == null) {
            throw new CommandSyntaxException("Invalid command usage of " + input);
        }

        commandNode.getCommand().execute(commandContext);
    }

    public static List<String> getSuggestions(String input) {
        List<String> suggestions = new ArrayList<>();
        Scanner scanner = new Scanner(input);
        String commandName = scanner.next();

        CommandNode commandNode = COMMAND_NODES.get(commandName);


        while (commandNode == null && scanner.hasNext()) {
            commandName = commandName.concat(" ").concat(scanner.next()).trim();
            commandNode = COMMAND_NODES.get(commandName);
        }

        if(commandNode == null) {
            for (String key : COMMAND_NODES.keySet()) {
                if(key.startsWith(commandName)) {
                    suggestions.add(key);
                }
            }
            return suggestions;
        }
        if(scanner.hasNext()) {
            String argument = scanner.next();
            boolean next = false;
            do {
                if (argument.isEmpty()) {
                    argument = scanner.next();
                }
                if(next) {
                    argument = argument.concat(" ").concat(scanner.next()).trim();
                    next = false;
                }
                CommandNode child = commandNode.getChild(argument);
                if (child == null) {
                    if (!scanner.hasNext()) {
                        for (CommandNode commandNodeChild : commandNode.getChildren()) {
                            if (commandNodeChild.getName().toLowerCase().startsWith(argument.toLowerCase())) {
                                suggestions.add(commandNodeChild.getName());
                            }
                        }
                        return suggestions;
                    } else {
                        next = true;
                    }
                } else {
                    commandNode = child;
                    argument = "";
                }
            } while (scanner.hasNext());
        }

        return suggestions;
    }

    /**
     * Gets a {@link CommandNode}.
     *
     * @param name  The name.
     * @return  The node.
     */
    public static CommandNode get(String name) {
        return COMMAND_NODES.get(name);
    }
}
