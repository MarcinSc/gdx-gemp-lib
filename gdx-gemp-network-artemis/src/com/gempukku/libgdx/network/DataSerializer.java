package com.gempukku.libgdx.network;

public interface DataSerializer<T> {
    T serializeData(Object data);

    Object deserializeData(T data);
}
