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

import me.hippo.api.lumos.context.CommandContext;

/**
 * @author Hippo
 * @version 1.0.0, 07/25/2019
 * @since 1.0.0
 */
@FunctionalInterface
public interface Command {
    /**
     * Executes the command.
     *
     * @param commandContext  The context of the command.
     */
    void execute(CommandContext commandContext);
}
