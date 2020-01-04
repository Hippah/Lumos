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

package me.hippo.systems.api.node;

import me.hippo.systems.api.Command;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Hippo
 * @version 1.0.0, 07/25/2019
 * @since 1.0.0
 */
public abstract class CommandNode {

    /**
     * A map to store all the {@link CommandNode}'s children.
     */
    private final Map<String, CommandNode> children;

    /**
     * The {@link Command} that the node will execute.
     */
    private Command command;

    public CommandNode() {
        this.children = new HashMap<>();
    }

    /**
     * Gets the name of the node.
     *
     * @return  The name.
     */
    public abstract String getName();

    /**
     * Adds a child node.
     *
     * @param child  The child node.
     */
    public void addChild(CommandNode child) {
        children.put(child.getName(), child);
    }

    /**
     * Gets the command.
     *
     * @return  The command.
     */
    public Command getCommand() {
        return command;
    }

    /**
     * Sets the command.
     *
     * @param command  The command.
     */
    public void setCommand(Command command) {
        this.command = command;
    }

    /**
     * Gets all the children.
     *
     * @return  The children.
     */
    public Collection<CommandNode> getChildren() {
        return children.values();
    }

    /**
     * Gets a child node.
     *
     * @param child  The name of the child node.
     * @return  The child node.
     */
    public CommandNode getChild(String child) {
        return children.get(child);
    }

}
