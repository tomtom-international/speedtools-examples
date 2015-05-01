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

import com.tomtom.examples.ApiConstants;
import com.tomtom.speedtools.apivalidation.ApiDTO;
import com.tomtom.speedtools.utils.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This example class contains a binder record for Person.
 *
 * "Binders" are used in APIs for data transfer. It is generally good practice not to use "domain" classes in APIs, as
 * they are easily refactored, altering an API unwantedly. Furthermore, not al data types are supported (or efficient)
 * in binders, as they need to map to JSON or XML.
 *
 * Converting between binders and domain classes is usually done in "converter" classes (package "converters"). The
 * converter provide straightforward toDomain and fromDomain methods.
 *
 * The SpeedTools {@link com.tomtom.speedtools.apivalidation.ApiDTO} class provides a basic framework to define
 * these binders and to help with the validation of the properties.
 *
 * The validation framework offers a way to collect all validation errors in one go and return a single error document
 * to the web services caller containing a clear description of all violations.
 *
 * It also provides a simple mechanism to ensure binders are always initialized, filled and validated properly before
 * the applications gets data from them and to help ensure the binders are immutable, at least from the point where all
 * setters have been used once and the first getter has been called (so the binders value won't change after you start
 * reading its values).
 */
@XmlRootElement(name = "person")
@XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
public final class PersonDTO extends ApiDTO {

    @Nullable
    private String id;
    @Nullable
    private String name;
    @Nullable
    private Integer age;

    /**
     * The method SpeedTools "validate" has a fixed format, starting with start() and finishing with done(). All fields
     * must be checked in validate() function (this is checked by the framework).
     */
    @Override
    public void validate() {
        validator().start();                            // Start the validator, collect errors.

        validator().checkUid(false, "id", id);          // Optional, provide name for error messages.

        validator().checkString(true, "name", name,     // Required, check length.
                ApiConstants.API_VERSION_MIN_LENGTH,
                ApiConstants.API_VERSION_MAX_LENGTH);

        validator().checkInteger(false, "age", age,     // Optional, check range.
                ApiConstants.API_AGE_MIN,
                ApiConstants.API_AGE_MAX);

        validator().done();                             // End validation, throw errors (if needed).
    }

    public PersonDTO(
            @Nullable final String id,
            @Nonnull final String name,
            @Nullable final Integer age) {
        super();
        setId(id);                                      // Need to use the setters, because the getters
        setName(name);                                  // cannot be used before all setters have been
        setAge(age);                                    // used (once).
    }

    @SuppressWarnings("UnusedDeclaration")
    @Deprecated
    private PersonDTO() {
        // Default constructor required by JAX-B.
        super();
    }

    /**
     * The getters and setters for individual properties are listed below. The getters must be annotated with
     * XmlElement, in order to allow for correct usage of XML names. Always make sure the "required" value and the     *
     * NotNull or Nullable annotation match the "required" boolean in the validator!
     *
     * @return String.
     */
    @XmlElement(name = "id", required = false)
    @Nullable
    public String getId() {
        beforeGet();                                // SpeedTools framework check to verify the property
        return id;                                  // was validated properly.
    }

    public void setId(@Nullable final String id) {
        beforeSet();                                // Required to check if setters are used after getters
        this.id = id;                               // were used (once), to enforce immutability after getting.
    }

    @XmlElement(name = "name", required = true)
    @Nonnull
    public String getName() {
        beforeGet();
        assert name != null;
        return name;
    }

    public void setName(@Nullable final String name) {
        beforeSet();
        this.name = StringUtils.trim(name);
    }

    @XmlElement(name = "age", required = false)
    @Nullable
    public Integer getAge() {
        beforeGet();
        return age;
    }

    public void setAge(@Nullable final Integer age) {
        beforeSet();
        this.age = age;
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
