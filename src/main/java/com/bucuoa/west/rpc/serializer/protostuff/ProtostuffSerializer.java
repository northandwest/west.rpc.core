package com.bucuoa.west.rpc.serializer.protostuff;

import com.bucuoa.west.rpc.serializer.Serializer;

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
