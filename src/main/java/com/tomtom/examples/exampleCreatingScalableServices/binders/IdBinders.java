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

package com.tomtom.examples.exampleCreatingScalableServices.binders;

import com.tomtom.speedtools.apivalidation.ApiDataBinder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Set;

/**
 * This class contains a binder for lists IDs.
 */
@XmlRootElement(name = "ids")
@XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
public final class IdBinders extends ApiDataBinder {

    @Nullable
    private Set<IdBinder> idBinders;

    /**
     * The method "validate" has a fixed format, starting with start() and finishing with done(). All fields must be
     * checked in validate() function (this is checked by the framework).
     */
    @Override
    public void validate() {
        validator().start();
        validator().checkNotNullAndValidateAll(false, "id", idBinders);
        validator().done();
    }

    public IdBinders(
            @Nullable final Set<IdBinder> idBinders) {
        super();
        setIdBinders(idBinders);
    }

    @SuppressWarnings("UnusedDeclaration")
    @Deprecated
    private IdBinders() {
        // Default constructor required by JAX-B.
        super();
    }

    /**
     * The getters and setters for individual properties are listed below. The getters must be annotated with
     * XmlElement, in order to allow for correct usage of XML names. Always make sure the "required" value and the
     * NotNull or Nullable annotation match the "required" boolean in the validator!
     *
     * @return Set.
     */
    @XmlElement(name = "ids", required = true)
    @Nonnull
    public Set<IdBinder> getIdBinders() {
        beforeGet();
        assert idBinders != null;
        return idBinders;
    }

    public void setIdBinders(@Nullable final Set<IdBinder> idBinders) {
        beforeSet();
        this.idBinders = idBinders;
    }
}
