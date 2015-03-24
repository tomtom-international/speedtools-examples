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
import com.tomtom.examples.exampleCreatingScalableServices.binders.IdBinder;

public final class IdConverter {

    // Prevent instantiation.
    private IdConverter() {
        assert false;
    }

    /**
     * This method converts a "domain" entity to a "binder" value.
     *
     * @param domain Domain value.
     * @return Binder value.
     */
    @Nonnull
    public static IdBinder fromDomain(@Nonnull final Uid<?> domain) {
        assert domain != null;

        // Trivial conversion.
        final IdBinder idBinder = new IdBinder(domain.toString());
        return idBinder;
    }

    /**
     * This method converts a "binder" value to a "domain" entity.
     *
     * @param binder Binder value.
     * @return Domain entity.
     */
    @Nonnull
    public static Uid<?> toDomain(@Nonnull final IdBinder binder) {
        assert binder != null;

        // Trivial conversion.
        final String id = binder.getId();
        assert id != null;
        return Uid.fromString(id);
    }
}