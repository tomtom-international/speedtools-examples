/*
 *  Copyright (C) 2015. TomTom International BV (http://tomtom.com).
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

package com.tomtom.examples.exampleCreatingScalableServices.converters;

import javax.annotation.Nonnull;

import com.tomtom.speedtools.domain.Uid;
import com.tomtom.examples.exampleCreatingScalableServices.binders.PersonBinder;
import com.tomtom.examples.exampleCreatingScalableServices.domain.Person;

/**
 * Converters are used to convert "binder" values (used in APIs) to "domain" classes (used in the application). The
 * provide straightforward mappings from and to the domain classes.
 */
public final class PersonConverter {

    // Prevent instantiation.
    private PersonConverter() {
        assert false;
    }

    /**
     * This method converts a "domain" entity to a "binder" value.
     *
     * @param domain Domain value.
     * @return Binder value.
     */
    @Nonnull
    public static PersonBinder fromDomain(@Nonnull final Person domain) {
        assert domain != null;

        // Trivial conversion.
        final PersonBinder personBinder = new PersonBinder(
                domain.getId().toString(),
                domain.getName(),
                domain.getAge());
        return personBinder;
    }

    /**
     * This method converts a "binder" value to a "domain" entity.
     *
     * @param binder Binder value.
     * @return Domain entity.
     */
    @Nonnull
    public static Person toDomain(@Nonnull final PersonBinder binder) {
        assert binder != null;

        // Trivial conversion.
        final String id = binder.getId();

        return new Person(
                (id == null) ? new Uid<>() : Uid.fromString(id).as(Person.class),
                binder.getName(),
                binder.getAge());
    }
}
