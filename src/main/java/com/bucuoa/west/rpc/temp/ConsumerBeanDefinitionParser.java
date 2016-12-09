package com.bucuoa.west.rpc.temp;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

import com.bucuoa.west.rpc.tags.Consumer;

public class ConsumerBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {
	protected Class getBeanClass(Element element) {
		return Consumer.class;
	}

	protected void doParse(Element element, BeanDefinitionBuilder bean) {
		String id = element.getAttribute("id");
		String interfac3 = element.getAttribute("interfac3");
		String alias = element.getAttribute("alias");
		String retries = element.getAttribute("retries");
		String check = element.getAttribute("check");
		String timeout = element.getAttribute("timeout");

		if (StringUtils.hasText(id)) {
			bean.addPropertyValue("id", id);
		}
		if (StringUtils.hasText(alias)) {
			bean.addPropertyValue("alias", alias);
		}
		if (StringUtils.hasText(retries)) {
			bean.addPropertyValue("retries", retries);
		}
		if (StringUtils.hasText(check)) {
			bean.addPropertyValue("check", check);
		}
		if (StringUtils.hasText(interfac3)) {
			bean.addPropertyValue("interfac3", interfac3);
		}

		if (StringUtils.hasText(timeout)) {
			bean.addPropertyValue("timeout", timeout);
		}
		
	
	}
}