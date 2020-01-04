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

package me.hippo.systems.lumos;

import me.hippo.systems.lumos.builder.CommandBuilder;
import me.hippo.systems.lumos.context.ArgumentValueWrapper;
import me.hippo.systems.lumos.context.CommandContext;
import me.hippo.systems.lumos.exception.CommandException;
import me.hippo.systems.lumos.exception.CommandSyntaxException;
import me.hippo.systems.lumos.node.CommandNode;

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


            do {
                if(argument.isEmpty()) {
                    argument = scanner.next();
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
                    argument = argument.concat(" ").concat(scanner.next()).trim();
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
            do {
                if (argument.isEmpty()) {
                    argument = scanner.next();
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
                        argument = argument.concat(" ").concat(scanner.next()).trim();
                    }
                } else {
                    commandNode = child;
                    argument = "";
                }
            } while (scanner.hasNext());
        }

        return suggestions;
       /* String[] args = input.split(" ");
        CommandNode commandNode = COMMAND_NODES.get(args[0]);
        List<String> suggestions = new ArrayList<>();
        if(commandNode == null) {
            for (String key : COMMAND_NODES.keySet()) {
                if(key.startsWith(args[0])) {
                    suggestions.add(key);
                }
            }
            return suggestions;
        }
        for(int i = 1; i < args.length; i++) {
            String arg = args[i];
            CommandNode child = commandNode.getChild(arg);
            if(child == null) {
                for (CommandNode commandNodeChild : commandNode.getChildren()) {
                    if(commandNodeChild.getName().startsWith(arg)) {
                        suggestions.add(commandNodeChild.getName());
                    }
                }
            }else {
                commandNode = child;
            }
        }
        return suggestions;*/
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
