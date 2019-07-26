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
import me.hippo.systems.lumos.exception.CommandSyntaxException;
import me.hippo.systems.lumos.node.CommandNode;

import java.util.*;

/**
 * @author Hippo
 * @since 07/25/2019
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
    public static void execute(String input) throws CommandSyntaxException {
        String[] args = input.split(" ");
        CommandContext commandContext = new CommandContext(input);
        CommandNode commandNode = COMMAND_NODES.get(args[0]);
        if(commandNode == null) {
            throw new CommandSyntaxException("Unknown command.");
        }
        for (int i = 1; i < args.length; i++) {
            String arg = args[i];
            String argName = arg;
            for (String key : commandContext.getArguments().keySet()) {
                ArgumentValueWrapper value = commandContext.getArguments().get(key);

                if(value.getValueString().equalsIgnoreCase(arg)) {
                    argName = key;
                }
            }
            CommandNode child = commandNode.getChild(argName);
            if(child != null) {
                commandNode = child;
            }else {
                throw new CommandSyntaxException("Invalid command usage.");
            }
        }
        commandNode.getCommand().execute(commandContext);
    }

    public static List<String> getSuggestions(String input) {
        String[] args = input.split(" ");
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
