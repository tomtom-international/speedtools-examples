/*
 *  Copyright (C) 2012-2017. TomTom International BV (http://tomtom.com).
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

package com.tomtom.examples.exampleUsingDatabase.dao.mongodb.mappers;

import com.tomtom.examples.exampleCreatingScalableServices.domain.Person;
import com.tomtom.speedtools.domain.Uid;
import com.tomtom.speedtools.mongodb.mappers.EntityMapper;

@SuppressWarnings("ALL")
public class PersonMapper extends EntityMapper<Person> {
    public final EntityType entityType = entityType(Person.class);

    public final Field<Uid<Person>> id = idField("getId", CONSTRUCTOR);
    public final Field<String> name = stringField("name", "getName", CONSTRUCTOR);
    public final Field<Integer> age = integerField("age", "getAge", CONSTRUCTOR);
}
