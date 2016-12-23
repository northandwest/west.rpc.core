package com.bucuoa.west.rpc.remoting.protocal.netty;

import java.util.List;

import com.bucuoa.west.rpc.serializer.Serializer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class RpcDecoder extends ByteToMessageDecoder {
	private Serializer serializer;
	
	private Class<?> genericClass;

	public RpcDecoder(Class<?> genericClass,Serializer serializer) {
		this.genericClass = genericClass;
		this.serializer = serializer;
	}

	@Override
	public void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if (in.readableBytes() < 4) {
			return;
		}
		in.markReaderIndex();
		int dataLength = in.readInt();
		if (in.readableBytes() < dataLength) {
			in.resetReaderIndex();
			return;
		}
		byte[] data = new byte[dataLength];
		in.readBytes(data);
		Object deserialize = serializer.deserialize(data, genericClass);
		out.add(deserialize);
	}

	public void setSerializer(Serializer serializer) {
		this.serializer = serializer;
	}
	
	
}
