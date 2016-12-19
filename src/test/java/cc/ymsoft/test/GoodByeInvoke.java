package cc.ymsoft.test;

import cc.ymsoft.Framework.RpcFramework;

public class GoodByeInvoke {
	public static void main(String[] args) {
		GoodByeService goodByeService = RpcFramework.getObj(GoodByeService.class, "127.0.0.1", 14527);
		System.out.println(goodByeService.sayGoodbye());
	}
}
