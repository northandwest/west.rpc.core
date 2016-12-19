package cc.ymsoft.test;

import cc.ymsoft.Framework.RpcFramework;

public class HelloInvoke {

	public static void main(String[] args) throws Exception {
		final HelloService helloService = RpcFramework.getObj(HelloService.class, "127.0.0.1", 1717);

		System.out.println(helloService.SayHello("徐浪"));
		System.out.println("Comsumer start");

	}

}
