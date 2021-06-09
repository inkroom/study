package cn.inkroom.study.spring;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

/**
 * @author inkbox
 * @date 2021/6/9
 */
public class Entry {

    public static void main(String[] args) {
        DefaultListableBeanFactory factory = new XmlBeanFactory(new ClassPathResource("bean.xml"));
        Object bean = factory.getBean("bean");
        bean.toString();

    }
}
