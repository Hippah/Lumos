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

package me.hippo.systems.api.context;


import me.hippo.systems.api.CommandDispatcher;
import me.hippo.systems.api.argument.ArgumentType;
import me.hippo.systems.api.argument.impl.NumberArgumentType;
import me.hippo.systems.api.argument.impl.BooleanArgumentType;
import me.hippo.systems.api.argument.impl.StringArgumentType;
import me.hippo.systems.api.exception.CommandNotFoundException;
import me.hippo.systems.api.node.CommandNode;
import me.hippo.systems.api.node.argument.ArgumentCommandNode;
import me.hippo.systems.api.node.label.LabelCommandNode;
import me.hippo.systems.api.util.TypeUtil;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.TreeMap;

/**
 * @author Hippo
 * @version 1.0.0, 07/25/2019
 * @since 1.0.0
 */
public final class CommandContext {

    /**
     * A map that maps an argument with an input value.
     */
    private final Map<String, ArgumentValueWrapper> arguments;

    /**
     * Creates a new {@link CommandContext} and populates {@link #arguments}.
     *
     * @param input  The command input.
     */
    public CommandContext(String input) throws CommandNotFoundException {
        this.arguments = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

        Scanner scanner = new Scanner(input);
        String commandName = scanner.next();
        while (CommandDispatcher.get(commandName) == null) {
            try {
                commandName = commandName.concat(" ").concat(scanner.next());
            }catch (NoSuchElementException e) {
                throw new CommandNotFoundException("Command for input " + input + " not found.");
            }
        }

        CommandNode commandNode = CommandDispatcher.get(commandName);

        if(scanner.hasNext()) {
            String argument = scanner.next().trim();

            do {
                if(argument.isEmpty()) {
                    argument = scanner.next().trim();
                }
                boolean shouldParse = true;
                boolean isDefinite = false;
                for (CommandNode child : commandNode.getChildren()) {
                    if (child.getName().toLowerCase().startsWith(argument.toLowerCase()) && child instanceof LabelCommandNode) {
                        if (child.getName().equalsIgnoreCase(argument)) {
                            isDefinite = true;

                        }
                        shouldParse = false;
                    }
                }
                if (shouldParse) {
                    Double parsedDouble = TypeUtil.parseDouble(argument);
                    Boolean parsedBoolean = TypeUtil.parseBoolean(argument);
                    for (CommandNode child : commandNode.getChildren()) {

                        if (child instanceof ArgumentCommandNode) {
                            ArgumentCommandNode argumentNode = (ArgumentCommandNode) child;
                            ArgumentType argumentType = argumentNode.getArgumentType();
                            if (parsedDouble != null && argumentType instanceof NumberArgumentType) {
                                arguments.put(child.getName(), new ArgumentValueWrapper(parsedDouble, argument));
                                argument = "";
                            } else if (parsedBoolean != null && argumentType instanceof BooleanArgumentType) {
                                arguments.put(child.getName(), new ArgumentValueWrapper(parsedBoolean, argument));
                                argument = "";
                            } else if (argumentType instanceof StringArgumentType) {
                                arguments.put(child.getName(), new ArgumentValueWrapper(argument, argument));
                                argument = "";
                            }
                        }
                    }
                } else if (isDefinite) {
                    commandNode = commandNode.getChild(argument);
                    argument = "";
                } else {
                    argument = argument.concat(" ").concat(scanner.next()).trim();
                }
            }while (scanner.hasNext());
        }

        /*String[] args = input.split(" ");
        String command = args[0];
        CommandNode node = CommandDispatcher.get(command);

        for(int i = 1; i < args.length; i++) {
            String arg = args[i];

            boolean label = false;
            for (CommandNode child : node.getChildren()) {
                if(child.getName().equalsIgnoreCase(arg) && child instanceof LabelCommandNode) {
                    label = true;
                }
            }

            if(!label) {
                Double parsedDouble = TypeUtil.parseDouble(arg);
                Boolean parsedBoolean = TypeUtil.parseBoolean(arg);
                for (CommandNode child : node.getChildren()) {

                    if(child instanceof ArgumentCommandNode) {
                        ArgumentCommandNode argumentNode = (ArgumentCommandNode)child;
                        ArgumentType argumentType = argumentNode.getArgumentType();
                        if (parsedDouble != null && argumentType instanceof NumberArgumentType) {
                            arguments.put(child.getName(), new ArgumentValueWrapper(parsedDouble, arg));
                        } else if (parsedBoolean != null && argumentType instanceof BooleanArgumentType) {
                            arguments.put(child.getName(), new ArgumentValueWrapper(parsedBoolean, arg));
                        } else if(argumentType instanceof StringArgumentType){
                            arguments.put(child.getName(), new ArgumentValueWrapper(arg, arg));
                        }
                    }
                }
            }else {
                node = node.getChild(arg);
            }
        }*/
    }

    public Number getNumber(String arg) {
        return (Number) arguments.get(arg).getValue();
    }

    public Boolean getBoolean(String arg) {
        return (Boolean)arguments.get(arg).getValue();
    }

    public String getString(String arg) {
        return (String)arguments.get(arg).getValue();
    }

    public Map<String, ArgumentValueWrapper> getArguments() {
        return arguments;
    }
}
