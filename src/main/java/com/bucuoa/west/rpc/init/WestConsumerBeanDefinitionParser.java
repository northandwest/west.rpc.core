package com.bucuoa.west.rpc.init;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.bucuoa.west.rpc.remoting.client.ConsumerRegister;
import com.bucuoa.west.rpc.remoting.client.DirectServiceAddressRegister;
import com.bucuoa.west.rpc.remoting.server.RemoteServiceCenter;
import com.bucuoa.west.rpc.spring.ApplicationBean;
import com.bucuoa.west.rpc.spring.ConsumerBean;
import com.bucuoa.west.rpc.spring.ProviderBean;
import com.bucuoa.west.rpc.spring.RegistryBean;
import com.bucuoa.west.rpc.spring.ServerBean;
import com.bucuoa.west.rpc.utils.ReflectUtils;
import com.bucuoa.west.rpc.utils.WStringUtils;

public class WestConsumerBeanDefinitionParser implements BeanDefinitionParser {
	
	static Logger logger = LoggerFactory.getLogger(WestConsumerBeanDefinitionParser.class);

	private final Class<?> beanClass;

	private final boolean required;

	public WestConsumerBeanDefinitionParser(Class<?> beanClass, boolean required) {
		this.beanClass = beanClass;
		this.required = required;
	}

	@Override
	public BeanDefinition parse(Element element, ParserContext parserContext) {
		 BeanDefinition bean = doParse(element, parserContext, beanClass, required);
		 
		 return bean;
	}

	private  BeanDefinition doParse(Element element, ParserContext parserContext, Class<?> beanClass,
			boolean required) {

		RootBeanDefinition beanDefinition = new RootBeanDefinition();
		beanDefinition.setBeanClass(beanClass);
		beanDefinition.setLazyInit(false);

		String id = element.getAttribute("id");
		if ((id == null || id.length() == 0) && required) {
			String generatedBeanName = element.getAttribute("name");
			if (generatedBeanName == null || generatedBeanName.length() == 0) {
				// if (ProtocolConfig.class.equals(beanClass)) {
				// generatedBeanName = "dubbo";
				// } else {
				// generatedBeanName = element.getAttribute("interface");
				// }
			}
			if (generatedBeanName == null || generatedBeanName.length() == 0) {
				generatedBeanName = beanClass.getName();
			}
//			id = generatedBeanName;
//			int counter = 2;
//			while (parserContext.getRegistry().containsBeanDefinition(id)) {
//				id = generatedBeanName + (counter++);
//			}
		}

		if (id != null && id.length() > 0) {
			if (parserContext.getRegistry().containsBeanDefinition(id)) {
				throw new IllegalStateException("Duplicate spring bean id " + id);
			}
			parserContext.getRegistry().registerBeanDefinition(id, beanDefinition);
			beanDefinition.getPropertyValues().addPropertyValue("id", id);
//			System.out.println("id==>"+id);
		}

		if (ConsumerBean.class.equals(beanClass)) {
			String interfac3 = element.getAttribute("interface");
			
			String url = element.getAttribute("url");
			if(url != null && !url.equals(""))
			{
				DirectServiceAddressRegister.setConsumerUrl(id,url);
			}
			
			if (interfac3 != null && interfac3.length() > 0) {
				logger.debug("className==>{},{}",interfac3,element);
				ConsumerRegister.setConsumer(id, interfac3);
			}
//			String[] beanDefinitionNames = parserContext.getRegistry().getBeanDefinitionNames();
//			System.out.println(beanDefinitionNames);
//			String property = parserContext.getReaderContext().getEnvironment().getProperty(url);
//			PropertySourcesPlaceholderConfigurer pc = new PropertySourcesPlaceholderConfigurer();
//			PropertySource<?> propertySource = pc.getAppliedPropertySources().get(url);
//			String namek = propertySource.getName();
//			System.out.println(property);

//			for(String name : beanDefinitionNames)
//			{
//				BeanDefinition beanDefinition2 = parserContext.getRegistry().getBeanDefinition(name);
//				MutablePropertyValues propertyValues = beanDefinition2.getPropertyValues();
//				PropertyValue urlx = propertyValues.getPropertyValue("soa_service_echo_url");
//				if(urlx != null)
//				{
//					System.out.println(urlx.getValue());
//					
//				}
//			}
		}else 	if (ProviderBean.class.equals(beanClass)) {
			String interfac3 = element.getAttribute("interface");
			String ref = element.getAttribute("ref");
			logger.debug("init ProviderBean class===>{}",interfac3);

			if(ref != null && ref.length() > 0 )
			{
				
//				ProviderStubInvoker service = RemoteServiceCenter.getService(id);
				RemoteServiceCenter.setInterface(interfac3, id);
//				RemoteServiceCenter.setService(interfac3, service);
				
//				if(parserContext.getRegistry().containsBeanDefinition(ref))
//				{
//					BeanDefinition refBeanDefinition = parserContext.getRegistry().getBeanDefinition(ref);
//					if(!refBeanDefinition.isSingleton())
//					{
//						throw new IllegalStateException("not singlton!");
//					}
////					Object refbean = new RuntimeBeanReference(ref);
////					beanDefinition.getPropertyValues().addPropertyValue("ref",refbean);
//				
//				}
			}
			
//			else if (className != null && className.length() > 0) {
//				Class<?> clazz = ReflectUtils.forName(className);
//
//				try {
//					RemoteServiceCenter.setService(interfac3, clazz.newInstance());
//				} catch (InstantiationException | IllegalAccessException e) {
//					logger.error("WestConsumerBeanDefinitionParser error",e);
//				}
//				
//				logger.debug("init provider class===>{}?={}",interfac3,RemoteServiceCenter.getService(interfac3));
//				
//				RootBeanDefinition classDefinition = new RootBeanDefinition();
//				classDefinition.setBeanClass(clazz);
//				classDefinition.setLazyInit(false);
//				
//				parseProperties(element.getChildNodes(), classDefinition);
////				return classDefinition;
//				
//			}
		}else 	if (ApplicationBean.class.equals(beanClass)) {
			String className = beanClass.getName();
			
			if (className != null && className.length() > 0) {
				Class<?> clazz = ReflectUtils.forName(className);
				
				logger.debug("init ApplicationBean class===>{}",className);
				
//				RootBeanDefinition classDefinition = new RootBeanDefinition();
//				classDefinition.setBeanClass(clazz);
//				classDefinition.setLazyInit(false);
//				
//				parseProperties(element.getChildNodes(), classDefinition);

//				return classDefinition;
			}
		}else 	if (RegistryBean.class.equals(beanClass)) {
			String className = beanClass.getName();
			
			if (className != null && className.length() > 0) {
				Class<?> clazz = ReflectUtils.forName(className);
				
				logger.debug("init RegistryBean class===>{}",className);
				
//				RootBeanDefinition classDefinition = new RootBeanDefinition();
//				classDefinition.setBeanClass(clazz);
//				classDefinition.setLazyInit(false);
//				
//				parseProperties(element.getChildNodes(), classDefinition);

//				return classDefinition;
			}
		}else 	if (ServerBean.class.equals(beanClass)) {
			String className = beanClass.getName();
			
			if (className != null && className.length() > 0) {
				Class<?> clazz = ReflectUtils.forName(className);
				
				logger.debug("init ServerBean class===>{}",className);
				
//				RootBeanDefinition classDefinition = new RootBeanDefinition();
//				classDefinition.setBeanClass(clazz);
//				classDefinition.setLazyInit(false);
//				
//				parseProperties(element.getChildNodes(), classDefinition);

//				return classDefinition;
			}
		}
		
	    for (Method setter : beanClass.getMethods()) {
            if (!isProperty(setter, beanClass)) continue; //略过不是property的方法
            String name = setter.getName();
            String property = name.substring(3, 4).toLowerCase() + name.substring(4);
            //根据property名称来进行区别处理
            int proType = getPropertyType(property);
            String value = element.getAttribute(property);
            Object reference = value;
            switch (proType) {
                case 1: // registry
                    if (WStringUtils.isNotBlank(value)) {
//                        parseMultiRef("registry", value, beanDefinition, parserContext);
                    }
                    break;
                case 2: // protocol
                    if (WStringUtils.isNotBlank(value)) {
                        beanDefinition.getPropertyValues().addPropertyValue(property, reference);
                        //parseMultiRef("protocol", value, beanDefinition, parserContext);
                    }
                    break;
//                case 3:
//                    break;
//                case 4:
//                    break;
                case 5: // ref
                	value = element.getAttribute("ref");
                    if (WStringUtils.isNotBlank(value) ) {
                        BeanDefinition refBean = parserContext.getRegistry().getBeanDefinition(value);
                        if (!refBean.isSingleton() && beanClass == ProviderBean.class) {
                            throw new IllegalStateException("[West RPC-211]The exported service ref " + value + " must be singleton! Please set the " + value + " bean scope to singleton, eg: <bean id=\"" + value + "\" scope=\"singleton\" ...>");
                        }
                        reference = new RuntimeBeanReference(value);
                    } else {
                        reference = null;//保持住ref的null值
                    }
				try {
					beanDefinition.getPropertyValues().addPropertyValue("refBean", reference);
				} catch (Exception e) {
					e.printStackTrace();
				}
                    break;
                case 6: // parameters 解析子元素
//                    parseParameters(element.getChildNodes(), beanDefinition);
                    break;
                case 7:// methods 解析子元素
//                    parseMethods(id, element.getChildNodes(), beanDefinition, parserContext);
                    break;
//                case 8:
//                    break;
//                case 9:
//                    break;
//                case 10:
//                    break;
//                case 11:
//                    break;
//                case 12:
//                    break;
//                case 13:
//                    break;
//                case 14: // server
////                    parseMultiRef(property, value, beanDefinition, parserContext);
//                    break;
                case 15: // filter
//                    parseFilters(property, value, beanDefinition, parserContext);
                    break;
                case 16: // onreturn/ onconnect / onavailable / router
//                    parseMultiRef(property, value, beanDefinition, parserContext);
                    break;
                case 17: // mockref / cacheref / groupRouter
//                    if (StringUtils.isNotBlank(value)) {
//                        reference = new RuntimeBeanReference(value);
//                    } else {
//                        reference = null;//保持住ref的null值
//                    }
//                    beanDefinition.getPropertyValues().addPropertyValue(property, reference);
                    break;
                case 18: // clazz --> class
                    value = element.getAttribute("class");
                    if (value != null) {
                        beanDefinition.getPropertyValues().addPropertyValue(property, value);
                    }
                    break;
                case 19: // interfaceId --> interface
                    value = element.getAttribute("interface");
                    if (value != null) {
                        beanDefinition.getPropertyValues().addPropertyValue(property, value);
                    }
                    break;
                case 20: // providers / consumers
                    if (value != null) { // 非null字符串 绑定值到属性
                        beanDefinition.getPropertyValues().addPropertyValue(property, reference);
                    }
                    break;
                case 21:
//                    parseConsumerConfigs(id, element.getChildNodes(), beanDefinition, parserContext);
                    break;
                default:
                    // 默认非空字符串只是绑定值到属性
                    if (WStringUtils.isNotBlank(value)) {
                        beanDefinition.getPropertyValues().addPropertyValue(property, reference);
                    }
                    break;
            }
        }


		return beanDefinition;
	}
	
	  private int getPropertyType(String propertyName) {
	        int type = -1;
	        if ("registry".equals(propertyName)) {
	            type = 1;
	        } else if ("protocol".equals(propertyName)) {
	            type = 2;
//	        } else if ("onreturn".equals(propertyName)) {
//	            type = 3;
//	        } else if ("onthrow".equals(propertyName)) {
//	            type = 4;
	        } else if ("ref".equals(propertyName)) {
	            type = 5;
	        } else if ("refBean".equals(propertyName)) {
	            type = 5;
	        }else if ("parameters".equals(propertyName)) {
	            type = 6;
	        } else if ("methods".equals(propertyName)) {
	            type = 7;
//	        } else if ("arguments".equals(propertyName)) {
//	            type = 8;
//	        } else if ("registries".equals(propertyName)) {
//	            type = 9;
//	        } else if ("protocols".equals(propertyName)) {
//	            type = 10;
//	        } else if ("application".equals(propertyName)) {
//	            type = 11;
//	        } else if ("zkClient".equals(propertyName)) {
//	            type = 12;
//	        } else if ("zkClients".equals(propertyName)) {
//	            type = 13;
	        } else if ("server".equals(propertyName)) {
	            type = 14;
	        } else if ("filter".equals(propertyName)) {
	            type = 15;
	        } else if ("onreturn".equals(propertyName)
	                || "onconnect".equals(propertyName)
	                || "onavailable".equals(propertyName)
	                || "router".equals(propertyName)) {  // 逗号分隔的多个ref
	            type = 16;
	        } else if ("mockref".equals(propertyName)
	                || "cacheref".equals(propertyName)
	                || "groupRouter".equals(propertyName)) { // 单个ref
	            type = 17;
	        } else if ("clazz".equals(propertyName)) {
	            type = 18;
	        } else if ("interfaceId".equals(propertyName)) {
	            type = 19;
	        } else if ("providers".equals(propertyName)
	                || "consumers".equals(propertyName)) { // 可以将属性置为 空字符串
	            type = 20;
	        } else if ("consumerConfigs".equals(propertyName)) {
	            type = 21;
	        }
	        return type;
	    }
	  
    private boolean isProperty(Method method, Class<?> beanClass) {
        String methodName = method.getName();
        boolean flag = methodName.length() > 3 && methodName.startsWith("set") && Modifier.isPublic(method.getModifiers()) && method.getParameterTypes().length == 1;
        Method getter = null;
        if (flag) {
            Class<?> type = method.getParameterTypes()[0];
            try {
                getter = beanClass.getMethod("get" + methodName.substring(3), new Class<?>[0]);
            } catch (NoSuchMethodException e) {
                try {
                    getter = beanClass.getMethod("is" + methodName.substring(3), new Class<?>[0]);
                } catch (NoSuchMethodException e2) {
                }
            }
            flag = getter != null && Modifier.isPublic(getter.getModifiers()) && type.equals(getter.getReturnType());
        }
        return flag;
    }

	private static void parseProperties(NodeList nodeList, RootBeanDefinition beanDefinition) {
		if (nodeList != null && nodeList.getLength() > 0) {
			
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				
				if (node instanceof Element) {
					if ("property".equals(node.getNodeName()) || "property".equals(node.getLocalName())) {
						
						String name = ((Element) node).getAttribute("name");
						
						if (name != null && name.length() > 0) {
							
							String value = ((Element) node).getAttribute("value");
							String ref = ((Element) node).getAttribute("ref");
							
							if (value != null && value.length() > 0) {
								beanDefinition.getPropertyValues().addPropertyValue(name, value);
							} else if (ref != null && ref.length() > 0) {
								beanDefinition.getPropertyValues().addPropertyValue(name,	new RuntimeBeanReference(ref));
							} else {
								throw new UnsupportedOperationException("Unsupported <property name=\"" + name
										+ "\"> sub tag, Only supported <property name=\"" + name
										+ "\" ref=\"...\" /> or <property name=\"" + name + "\" value=\"...\" />");
							}
						}
					}
				}
				
			}
		}
		
	}
	
}