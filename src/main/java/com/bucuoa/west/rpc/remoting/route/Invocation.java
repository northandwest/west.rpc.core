package com.bucuoa.west.rpc.remoting.route;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Invocation implements Serializable{

    private String clazzName;

    private String methodName;
    Class<?>[] parameterTypes;
    
    public Class<?>[] getParameterTypes() {
		return parameterTypes;
	}

	public void setParameterTypes(Class<?>[] parameterTypes) {
		this.parameterTypes = parameterTypes;
	}

	private String alias;

    private String[] argsType; //考虑优化class？

    private transient Class[] argClasses;

    private Object[] args;
    
    private Object result;

    private Map<String,Object> attachments = new HashMap<String,Object>();

    public void addAttachment(String key,Object value){
        if(value==null){
            attachments.remove(key);
        } else {
            attachments.put(key, value);
        }
    }

    private transient String ifaceId; // 接口id， 有的时候不传递className和argsType

    public Object getAttachment(String key){
        return attachments.get(key);
    }

    public Map<String, Object> getAttachments() {
        return attachments;
    }

    public String getClazzName() {
        return clazzName;
    }

    public void setClazzName(String clazzName) {
        this.clazzName = clazzName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String[] getArgsType() {
        return argsType;
    }

    public void setArgsType(String[] argsType) {
        this.argsType = argsType;
        // 清空缓存
        this.argClasses = null;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
    
    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public void addAttachments(Map<String,Object> sourceMap){
        this.attachments.putAll(sourceMap);
    }

    public void setArgsType(Class[] argsType) {
        this.argsType = ClassTypeUtils.getTypeStrs(argsType);
    }

    public Class[] getArgClasses() {
        if (argClasses == null) {
            try {
            	if (argsType == null || argsType.length == 0) {
                    return new Class[0];
                } else {
                    argClasses = new Class[argsType.length];
                    for (int i = 0; i < argsType.length; i++) {
                    	argClasses[i] = ClassTypeUtils.getClass(argsType[i]);
                    }
                    return argClasses;
                }
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return argClasses;
    }

    public String getIfaceId() {
        return ifaceId;
    }

    public void setIfaceId(String ifaceId) {
        this.ifaceId = ifaceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Invocation)) return false;

        Invocation that = (Invocation) o;

        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(args, that.args)) return false;
        if (!Arrays.equals(argsType, that.argsType)) return false;
        if (!attachments.equals(that.attachments)) return false;
        if (clazzName != null ? !clazzName.equals(that.clazzName) : that.clazzName != null) return false;
        if (methodName != null ? !methodName.equals(that.methodName) : that.methodName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = clazzName != null ? clazzName.hashCode() : 0;
        result = 31 * result + (methodName != null ? methodName.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(argsType);
        result = 31 * result + Arrays.hashCode(args);
        result = 31 * result + attachments.hashCode();
        return result;
    }

    public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	@Override
    public String toString() {
        return "Invocation{" +
                "clazzName='" + clazzName + '\'' +
                ", methodName='" + methodName + '\'' +
                ", argsType=" + Arrays.toString(argsType) +
                ", args=" + Arrays.toString(args) +
                ", attachments=" + attachments +
                '}';
    }
}
