package cc.ymsoft.test;

import cc.ymsoft.Framework.RpcFramework;

public class HelloPro {
	public static void main(String[] args) throws Exception {
		HelloService hello = new HelloImp();
		RpcFramework.regist(hello, 1717, "127.0.0.1");

		System.out.println("Server start");
	}

}
