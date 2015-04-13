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

import com.tomtom.examples.ApiConstants;
import com.tomtom.speedtools.apivalidation.ApiDataBinder;
import com.tomtom.speedtools.utils.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * See comments in {@link com.tomtom.examples.exampleCreatingScalableServices.binders.PersonBinder}.
 */
@XmlRootElement(name = "version")
@XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
public final class VersionBinder extends ApiDataBinder {

    @Nullable
    private String version;

    @Override
    public void validate() {
        validator().start();
        validator().checkString(true, "version", version,
                ApiConstants.API_VERSION_MIN_LENGTH,
                ApiConstants.API_VERSION_MAX_LENGTH);
        validator().done();
    }

    public VersionBinder(
            @Nonnull final String version) {
        super();
        setVersion(version);
    }

    @SuppressWarnings("UnusedDeclaration")
    @Deprecated
    private VersionBinder() {
        // Default constructor required by JAX-B.
        super();
    }

    @XmlElement(name = "version", required = true)
    @Nonnull
    public String getVersion() {
        beforeGet();
        assert version != null;
        return version;
    }

    public void setVersion(@Nullable final String version) {
        beforeSet();
        this.version = StringUtils.trim(version);
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
}
