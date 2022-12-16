package com.gempukku.libgdx.network;

public interface NetworkMessageMarshaller<T> {
    String marshall(NetworkMessage<T> networkMessage);

    NetworkMessage<T> unmarshall(T data);
}
