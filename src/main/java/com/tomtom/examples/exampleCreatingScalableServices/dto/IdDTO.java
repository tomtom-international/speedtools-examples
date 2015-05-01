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

package com.tomtom.examples.exampleCreatingScalableServices.dto;

import com.tomtom.speedtools.apivalidation.ApiDTO;
import com.tomtom.speedtools.utils.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.annotation.Nullable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * This class contains a binder for IDs. For an explanation of the use of the SpeedTools framework in
 * binders, see {@link PersonDTO}.
 */
@XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
public final class IdDTO extends ApiDTO {

    @Nullable
    private String id;

    @Override
    public void validate() {
        validator().start();
        validator().checkUid(true, "id", id);
        validator().done();
    }

    public IdDTO(
            @Nullable final String id) {
        super();
        setId(id);
    }

    @SuppressWarnings("UnusedDeclaration")
    @Deprecated
    private IdDTO() {
        // Default constructor required by JAX-B.
        super();
    }

    @XmlElement(name = "id", required = true)
    @Nullable
    public String getId() {
        beforeGet();
        assert id != null;
        return id;
    }

    public void setId(@Nullable final String id) {
        beforeSet();
        this.id = StringUtils.trim(id);
    }

    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override
    public boolean equals(@Nullable final Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj, false);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, false);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
