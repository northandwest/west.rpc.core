package cc.ymsoft.test;

import cc.ymsoft.Framework.RpcFramework;

public class GoodByePro {
public static void main(String[] args) throws InterruptedException {
	GoodByeService goodByeService=	new GoodByeImp();
	RpcFramework.regist(goodByeService, 1717, "127.0.0.1");
}
}
