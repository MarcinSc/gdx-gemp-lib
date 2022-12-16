package com.gempukku.libgdx.network.client;

import com.gempukku.libgdx.lib.artemis.event.EntityEvent;

import java.util.List;

public class IncomingInformationPacket<T> {
    private IncomingInformationPacket() {
    }

    public enum Type {
        EVENT, CREATE_ENTITY, MODIFY_ENTITY, DESTROY_ENTITY;
    }

    private String entityId;
    private Type type;
    private List<T> entityData;
    private EntityEvent event;

    private IncomingInformationPacket(String entityId, Type type, List<T> entityData, EntityEvent event) {
        this.entityId = entityId;
        this.type = type;
        this.entityData = entityData;
        this.event = event;
    }

    public static <T> IncomingInformationPacket<T> event(String entityId, EntityEvent event) {
        return new IncomingInformationPacket<T>(entityId, Type.EVENT, null, event);
    }

    public static <T> IncomingInformationPacket<T> create(String entityId, List<T> entityData) {
        return new IncomingInformationPacket<T>(entityId, Type.CREATE_ENTITY, entityData, null);
    }

    public static <T> IncomingInformationPacket<T> update(String entityId, List<T> entityData) {
        return new IncomingInformationPacket<T>(entityId, Type.MODIFY_ENTITY, entityData, null);
    }

    public static <T> IncomingInformationPacket<T> destroy(String entityId) {
        return new IncomingInformationPacket<T>(entityId, Type.DESTROY_ENTITY, null, null);
    }

    public String getEntityId() {
        return entityId;
    }

    public Type getType() {
        return type;
    }

    public List<T> getEntityData() {
        return entityData;
    }

    public EntityEvent getEvent() {
        return event;
    }
}
