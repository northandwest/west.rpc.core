package com.bucuoa.west.rpc.remoting.comuication;

import java.io.Serializable;

import com.bucuoa.west.rpc.conf.Constants;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;



public abstract class BaseMessage implements  Serializable{

    private  MessageHeader msgHeader;

    private ByteBuf msg;

    private ByteBuf msgBody;    //just invocation body..

    private transient Channel channel;

    public ByteBuf getMsg() {
        return msg;
    }

    public void setMsg(ByteBuf msg) {
        this.msg = msg;
    }

//    public String getTargetAddress() {
//        return targetAddress;
//    }
//
//    public void setTargetAddress(String targetAddress) {
//        this.targetAddress = targetAddress;
//    }


    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    protected BaseMessage(boolean initHeader) {
        if (initHeader) {
            msgHeader = new MessageHeader();
            // TODO 是否加版本号信息
            //msgHeader.addHeadKey(Constants.HeadKey.jsfVersion, Constants.JSF_VERSION);
        }
    }

    public int getProtocolType(){
        return msgHeader.getProtocolType();
    }

    public MessageHeader getMsgHeader() {
        return msgHeader;
    }

    public void setMsgHeader(MessageHeader msgHeader) {
        this.msgHeader = msgHeader;
    }

    public int getRequestId() {
		return msgHeader != null ? msgHeader.getMsgId() : -1;
	}

	/**
	 * @param msgId
	 */
	public void setRequestId(Integer msgId) {

		msgHeader.setMsgId(msgId);
	}

    public boolean isHeartBeat() {
        int msgType = msgHeader.getMsgType();
        return msgType == Constants.HEARTBEAT_REQUEST_MSG
                || msgType == Constants.HEARTBEAT_RESPONSE_MSG;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseMessage)) return false;

        BaseMessage that = (BaseMessage) o;

        if (!msgHeader.equals(that.msgHeader)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return msgHeader.hashCode();
    }

    @Override
    public String toString() {
        return "BaseMessage{" +
                "msgHeader=" + msgHeader +
                '}';
    }

    public ByteBuf getMsgBody() {
        return msgBody;
    }

    public void setMsgBody(ByteBuf msgBody) {
        this.msgBody = msgBody;
    }
}