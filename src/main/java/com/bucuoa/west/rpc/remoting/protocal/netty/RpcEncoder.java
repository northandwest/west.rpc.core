package com.bucuoa.west.rpc.remoting.protocal.netty;

import com.bucuoa.west.rpc.serializer.Serializer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;


@SuppressWarnings("rawtypes")
public class RpcEncoder extends MessageToByteEncoder {
	private Serializer serializer;
	
    private Class<?> genericClass;

    public RpcEncoder(Class<?> genericClass,Serializer serializer) {
        this.genericClass = genericClass;
        this.serializer = serializer;
    }

    @Override
    public void encode(ChannelHandlerContext ctx, Object in, ByteBuf out) throws Exception {
        if (genericClass.isInstance(in)) {
            byte[] data = serializer.serialize(in);
            out.writeInt(data.length);
            out.writeBytes(data);
        }
    }
}