package cc.ymsoft.test;

public class HelloImp implements HelloService {

	@Override
	public String SayHello(String name) {
		// TODO Auto-generated method stub
		
		return "你好:"+name;
	}


}
