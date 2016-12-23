package com.bucuoa.west.rpc.serializer;

public interface Serializer {

	<T> byte[] serialize(T obj);

	<T> T deserialize(byte[] data, Class<T> cls);

}
