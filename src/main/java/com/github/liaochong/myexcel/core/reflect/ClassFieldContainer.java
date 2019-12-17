/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.liaochong.myexcel.core.reflect;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author liaochong
 * @version 1.0
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClassFieldContainer {

    Class<?> clazz;

    List<Field> declaredFields = new ArrayList<>();

    Map<String, Field> fieldMap = new HashMap<>();

    ClassFieldContainer parent;

    public Field getFieldByName(String fieldName) {
        return this.getFieldByName(fieldName, this);
    }

    public List<Field> getFieldsByAnnotation(Class<? extends Annotation> annotationClass) {
        Objects.requireNonNull(annotationClass);
        List<Field> annotationFields = new ArrayList<>();
        this.getFieldsByAnnotation(this, annotationClass, annotationFields);
        return annotationFields;
    }

    public List<Field> getFields() {
        List<Field> fields = new ArrayList<>();
        this.getFieldsByContainer(this, fields);
        return fields;
    }

    private void getFieldsByContainer(ClassFieldContainer classFieldContainer, List<Field> fields) {
        fields.addAll(classFieldContainer.getDeclaredFields());
        ClassFieldContainer parentContainer = classFieldContainer.getParent();
        if (Objects.isNull(parentContainer)) {
            return;
        }
        this.getFieldsByContainer(parentContainer, fields);
    }

    private void getFieldsByAnnotation(ClassFieldContainer classFieldContainer, Class<? extends Annotation> annotationClass, List<Field> annotationFieldContainer) {
        List<Field> annotationFields = classFieldContainer.declaredFields.stream().filter(field -> field.isAnnotationPresent(annotationClass)).collect(Collectors.toList());
        annotationFieldContainer.addAll(annotationFields);
        ClassFieldContainer parentContainer = classFieldContainer.getParent();
        if (Objects.isNull(parentContainer)) {
            return;
        }
        this.getFieldsByAnnotation(parentContainer, annotationClass, annotationFieldContainer);
    }

    private Field getFieldByName(String fieldName, ClassFieldContainer container) {
        Field field = container.getFieldMap().get(fieldName);
        if (Objects.nonNull(field)) {
            return field;
        }
        ClassFieldContainer parentContainer = container.getParent();
        if (Objects.isNull(parentContainer)) {
            return null;
        }
        return getFieldByName(fieldName, parentContainer);
    }

}
