package com.bucuoa.west.rpc.temp;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

import com.bucuoa.west.rpc.tags.Provider;

public class ProviderBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {
	protected Class getBeanClass(Element element) {
		return Provider.class;
	}

	protected void doParse(Element element, BeanDefinitionBuilder bean) {
		String id = element.getAttribute("id");
		String interfac3 = element.getAttribute("interfac3");
		String alias = element.getAttribute("alias");
		String timeout = element.getAttribute("timeout");
		String ref = element.getAttribute("ref");
		
		if (StringUtils.hasText(id)) {
			bean.addPropertyValue("id", id);
		}
		if (StringUtils.hasText(alias)) {
			bean.addPropertyValue("alias", alias);
		}
		if (StringUtils.hasText(timeout)) {
			bean.addPropertyValue("timeout", timeout);
		}
		if (StringUtils.hasText(ref)) {
			bean.addPropertyValue("ref", ref);
		}
		if (StringUtils.hasText(interfac3)) {
			bean.addPropertyValue("interfac3", interfac3);
		}
	}
}