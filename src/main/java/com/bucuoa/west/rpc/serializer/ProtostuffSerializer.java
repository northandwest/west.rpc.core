package com.bucuoa.west.rpc.serializer;

public class ProtostuffSerializer implements  Serializer{

	@Override
	public <T> byte[] serialize(T obj) {
		return SerializationUtil.serialize(obj);
	}

	@Override
	public <T> T deserialize(byte[] data, Class<T> cls) {
		return SerializationUtil.deserialize(data, cls);
	}

}
