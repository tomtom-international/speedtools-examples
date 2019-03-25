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

package com.tomtom.examples.exampleUsingDatabase.dao;


import com.tomtom.examples.exampleCreatingScalableServices.domain.Person;
import com.tomtom.speedtools.domain.Uid;
import com.tomtom.speedtools.mongodb.EntityNotFoundException;
import com.tomtom.speedtools.mongodb.EntityRemoveException;
import com.tomtom.speedtools.mongodb.EntityStoreException;
import com.tomtom.speedtools.mongodb.InternalDaoException;

import javax.annotation.Nonnull;
import java.util.List;

public interface PersonDao {

    /**
     * Stores the {@link Person} given as parameter.
     *
     * @param person The {@link Person} to store.
     * @throws EntityStoreException When the entity could not be stored.
     */
    void storePerson(@Nonnull Person person) throws EntityStoreException;

    /**
     * Removes the {@link Person} represented by the given ID. Note that this does not mark the person as 'deleted'. It
     * actually removes the record from the database.
     *
     * @param personId ID of {@link Person} to remove.
     * @throws EntityRemoveException When the entity could not be removed.
     */
    void removePerson(@Nonnull Uid<Person> personId) throws EntityRemoveException;

    /**
     * Gets the {@link Person} with given ID.
     *
     * @param personId Id of the {@link Person}.
     * @return {@link Person} instance.
     * @throws EntityNotFoundException When the {@link Person} could not be read from the data store.
     * @throws InternalDaoException    When entity could not be read.
     */
    @Nonnull
    Person getPerson(@Nonnull Uid<Person> personId) throws InternalDaoException, EntityNotFoundException;

    /**
     * Gets all persons.
     *
     * @return List of {@link Person} instances.
     * @throws InternalDaoException When entity could not be read.
     */
    @Nonnull
    List<Person> getPersons() throws InternalDaoException;
}
