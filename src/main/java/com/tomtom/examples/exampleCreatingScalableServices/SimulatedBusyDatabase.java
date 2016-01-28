/*
 *  Copyright (C) 2012-2016. TomTom International BV (http://tomtom.com).
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.tomtom.examples.exampleCreatingScalableServices;

import com.tomtom.examples.exampleCreatingScalableServices.domain.Person;
import com.tomtom.speedtools.domain.Uid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This simple class acts as if it was a busy, slow database (see busy loop in {@link #doHardTask()}).
 */
public final class SimulatedBusyDatabase {
    private static final Logger LOG = LoggerFactory.getLogger(SimulatedBusyDatabase.class);

    // Sample data store to store domain entities. This would normally be a database or so.
    @Nonnull
    private final ConcurrentHashMap<Uid<Person>, Person> map;

    public SimulatedBusyDatabase() {
        super();

        // Create a sample data store with entries. Note that this class is a singleton.
        this.map = new ConcurrentHashMap<>();
        final Uid<Person> id1 = Uid.fromString("1-1-1-1-1").as(Person.class);
        this.map.put(id1, new Person(id1, "User 1", 30));
        final Uid<Person> id2 = Uid.fromString("1-1-1-1-2").as(Person.class);
        this.map.put(id2, new Person(id2, "User 2", 42));
    }

    public void put(@Nonnull final Person person) {
        doHardTask();
        map.put(person.getId(), person);
    }

    @Nullable
    public Person putIfAbsent(@Nonnull final Person person) {
        doHardTask();
        return map.putIfAbsent(person.getId(), person);
    }

    @Nullable
    public Person get(@Nonnull final Uid<Person> id) {
        doHardTask();
        return map.get(id);
    }

    @Nullable
    public Person remove(@Nonnull final Uid<Person> id) {
        doHardTask();
        return map.remove(id);
    }

    @Nonnull
    public Enumeration<Uid<Person>> keys() {
        doHardTask();
        return map.keys();
    }

    /**
     * Do something really busy.
     */
    private void doHardTask() {
        for (int i = 0; i < 100000; ++i) {
            if (map.containsKey(Uid.fromString("1-1-1-1-" + i).as(Person.class))) {

                // Execute something to not optimize this statement away.
                LOG.trace("waitSomeTime: found, i={}", i);
            }
        }
    }
}
