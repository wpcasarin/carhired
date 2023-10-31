package org.ifsul.carhired.api.base;

import org.springframework.hateoas.CollectionModel;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public abstract class BaseController<T> {
    protected CollectionModel<T> addGetAllSelfLink(List<T> entityList, Class<?> controllerClass) {
        var entities = CollectionModel.of(entityList);
        entities.add(linkTo(controllerClass).withSelfRel());
        return entities;
    }

}