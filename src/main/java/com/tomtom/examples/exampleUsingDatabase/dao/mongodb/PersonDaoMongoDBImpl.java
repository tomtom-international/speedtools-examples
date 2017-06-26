/*
 * Copyright (C) 2012-2017. TomTom International BV (http://tomtom.com).
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

package com.tomtom.examples.exampleUsingDatabase.dao.mongodb;

import com.mongodb.DBCollection;
import com.tomtom.examples.exampleCreatingScalableServices.domain.Person;
import com.tomtom.examples.exampleUsingDatabase.dao.PersonDao;
import com.tomtom.examples.exampleUsingDatabase.dao.mongodb.mappers.PersonMapper;
import com.tomtom.speedtools.domain.Uid;
import com.tomtom.speedtools.mongodb.*;
import com.tomtom.speedtools.mongodb.mappers.MapperRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;

import static com.tomtom.speedtools.loghelper.LogHelper.logId;

/**
 * MongoDB Implementation of {@link PersonDao}.
 */
public final class PersonDaoMongoDBImpl implements PersonDao {

    private static final Logger LOG = LoggerFactory.getLogger(PersonDaoMongoDBImpl.class);

    private static final String PERSON_COLLECTION_NAME = "person";

    @Nonnull
    private final DBCollection collection;
    @Nonnull
    private final PersonMapper personMapper;


    @Inject
    public PersonDaoMongoDBImpl(
            @Nonnull final MongoDB db,
            @Nonnull final MapperRegistry mapperRegistry) {
        super();
        assert db != null;
        assert mapperRegistry != null;

        // Get mappers from registry ans store them in appropriate scope.
        this.collection = db.getCollection(PERSON_COLLECTION_NAME);
        personMapper = mapperRegistry.findMapper(PersonMapper.class);

        LOG.debug("PersonDaoMongoDBImpl: Using database collection: {}", collection.getName());
    }

    @Override
    public void storePerson(@Nonnull final Person person) throws EntityStoreException {
        assert person != null;
        LOG.debug("storePerson: person={}", logId(person.getId()));
        DaoUtils.storeEntity(collection, personMapper, person);
    }

    @Override
    public void removePerson(@Nonnull final Uid<Person> personId) throws EntityRemoveException {
        assert personId != null;
        LOG.debug("removePerson: person={}", logId(personId));
        DaoUtils.removeEntityByField(collection, personMapper.id, personId);
    }

    @Nonnull
    @Override
    public Person getPerson(@Nonnull final Uid<Person> personId) throws InternalDaoException, EntityNotFoundException {
        assert personId != null;

        final MongoDBQuery query;
        query = new MongoDBQuery().
                eq(personMapper.id, personId);

        final Person result = DaoUtils.findOne(collection, personMapper, query);
        LOG.debug("getPersonInternal: person={}, result={}", logId(personId), result);
        return result;
    }

    @Nonnull
    @Override
    public List<Person> getPersons() throws InternalDaoException {

        final List<Person> result = DaoUtils.find(collection, personMapper, new MongoDBQuery());
        LOG.debug("getPersons: result={}", result);
        return result;
    }
}
