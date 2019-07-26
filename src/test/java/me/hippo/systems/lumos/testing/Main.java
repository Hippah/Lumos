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

package me.hippo.systems.lumos.testing;

import me.hippo.systems.lumos.CommandDispatcher;
import static me.hippo.systems.lumos.builder.CommandBuilder.*;

import me.hippo.systems.lumos.exception.CommandSyntaxException;
import org.junit.Test;

import java.util.Random;
import java.util.concurrent.ExecutionException;

/**
 * @author Hippo
 * @since 07/25/2019
 */
public final class Main {

    @Test
    public void test() {
        Random random = new Random();
        try {

            CommandDispatcher.register(
                    label("hack")
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
                        System.out.println("> Hack <user>");
                        System.out.println("> Hack ip <ping | proxy> <ip>");
                        System.out.println("> Hack gc");
                    })
            );

            CommandDispatcher.execute("hack");
            CommandDispatcher.execute("hack Asyc");
            CommandDispatcher.execute("hack ip ping hippo.com");
            CommandDispatcher.execute("hack ip proxy hippo.com");
            CommandDispatcher.execute("hack gc");

            for (String suggestion : CommandDispatcher.getSuggestions("hack i")) {
                System.out.println(suggestion); //prints "ip"
            }
        }catch (CommandSyntaxException e) {
            e.printStackTrace();
        }


    }
}
