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

package me.hippo.api.lumos.testing;

import me.hippo.api.lumos.CommandDispatcher;

import me.hippo.api.lumos.exception.CommandException;
import org.junit.Test;

import java.util.Random;

/**
 * @author Hippo
 * @version 1.1.0, 07/25/2019
 * @since 1.0.0
 */
public final class Main {

    @Test
    public void test() {
        Random random = new Random();
        try {

            CommandDispatcher.register(
                    label("hack tool")
                    .then(
                            argument("user", string())
                            .executes(commandContext -> System.out.println("Hacked " + commandContext.getString("user")))
                    )
                    .then(
                            label("ip")
                            .then(
                                    label("ping")
                                    .then(
                                            argument("ip", string())
                                            .executes(commandContext -> System.out.println("pinging " + commandContext.getString("ip")))
                                    )

                            )
                            .then(
                                    label("proxy")
                                    .then(
                                            argument("ip", string())
                                            .executes(commandContext -> System.out.println(commandContext.getString("ip") + " is a proxy: " + random.nextBoolean()))
                                    )
                            )
                    )
                    .then(
                            label("gc")
                            .executes(commandContext -> System.out.println("Collected garbage in hacker tool 1337."))
                    )
                    .executes(commandContext -> {
                        System.out.println("Usage: ");
                        System.out.println("> Hack tool <user>");
                        System.out.println("> Hack tool ip <ping | proxy> <ip>");
                        System.out.println("> Hack tool gc");
                    })
            );

            CommandDispatcher.execute("hack tool");
            CommandDispatcher.execute("hack tool Asyc");
            CommandDispatcher.execute("hack tool ip ping hippo.com");
            CommandDispatcher.execute("hack tool ip proxy hippo.com");
            CommandDispatcher.execute("hack tool gc");



            for (String suggestion : CommandDispatcher.getSuggestions("hack man i")) {
                System.out.println(suggestion); //prints "ip"
            }
        }catch (CommandException e) {
            e.printStackTrace();
        }


    }
}
