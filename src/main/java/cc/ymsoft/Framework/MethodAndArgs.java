package cc.ymsoft.Framework;

import java.io.Serializable;

@SuppressWarnings("serial")
public class MethodAndArgs implements Serializable{
	private String methodName;
	private Class<?>[] types;
	private Object[] objects;
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public Class<?>[] getTypes() {
		return types;
	}
	public void setTypes(Class<?>[] types) {
		this.types = types;
	}
	public Object[] getObjects() {
		return objects;
	}
	public void setObjects(Object[] objects) {
		this.objects = objects;
	}
	public MethodAndArgs() {
		super();
	}
	public MethodAndArgs(String methodName, Class<?>[] types, Object[] objects) {
		
		this.methodName = methodName;
		this.types = types;
		this.objects = objects;
	}
	
	
}
