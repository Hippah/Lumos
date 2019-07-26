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

package me.hippo.systems.lumos.context;


import me.hippo.systems.lumos.CommandDispatcher;
import me.hippo.systems.lumos.argument.ArgumentType;
import me.hippo.systems.lumos.argument.impl.BooleanArgumentType;
import me.hippo.systems.lumos.argument.impl.NumberArgumentType;
import me.hippo.systems.lumos.argument.impl.StringArgumentType;
import me.hippo.systems.lumos.node.CommandNode;
import me.hippo.systems.lumos.node.argument.ArgumentCommandNode;
import me.hippo.systems.lumos.node.label.LabelCommandNode;
import me.hippo.systems.lumos.util.TypeUtil;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author Hippo
 * @since 07/25/2019
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
    public CommandContext(String input) {
        this.arguments = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        String[] args = input.split(" ");
        String command = args[0];
        CommandNode node = CommandDispatcher.get(command);

        for(int i = 1; i < args.length; i++) {
            String arg = args[i];

            /*check for label nodes*/
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
        }
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
