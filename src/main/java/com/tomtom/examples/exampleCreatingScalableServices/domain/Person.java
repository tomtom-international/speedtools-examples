/*
 * Copyright (C) 2012-2019, TomTom (http://tomtom.com).
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

package com.tomtom.examples.exampleCreatingScalableServices.domain;

import com.tomtom.speedtools.domain.Uid;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * This is an example of a domain class. Domain classes are not used directly in APIs. They are converted to "binders"
 * first, by "converters" (see, for example, PersonBinder and PersonConverter).
 */
public class Person {
    @Nonnull
    final private Uid<Person> id;
    @Nonnull
    final private String name;
    @Nullable
    final private Integer age;

    public Person(
            @Nonnull final Uid<Person> id,
            @Nonnull final String name,
            @Nullable final Integer age) {
        assert id != null;
        assert name != null;
        this.id = id;
        this.name = name;
        this.age = age;
    }

    @Nonnull
    public Uid<Person> getId() {
        return id;
    }

    @Nonnull
    public String getName() {
        return name;
    }

    @Nullable
    public Integer getAge() {
        return age;
    }
}
