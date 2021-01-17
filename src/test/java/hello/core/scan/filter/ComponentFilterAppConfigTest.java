package hello.core.scan.filter;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Component;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.context.annotation.ComponentScan.*;

public class ComponentFilterAppConfigTest {

    @Test
    void filterScan() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(ComponentFilterAppConfig.class);
        assertThrows(NoSuchBeanDefinitionException.class, () ->
                ac.getBean("beanB", BeanB.class)
        );

        assertThrows(NoSuchBeanDefinitionException.class, () ->
                ac.getBean("beanA", BeanA.class)
        );
    }

    //포함할 항목 : MyIncludeComponent.class 어노테이션
    //제외할 항목 : MyExcludeComponent.class 어노테이션

    @Configuration
    @ComponentScan(
            basePackages = "hello.core.scan",
            includeFilters = @Filter(classes = MyIncludeComponent.class),
            excludeFilters = {
                    @Filter(classes = MyExcludeComponent.class),
                    @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = BeanA.class)}
                    )
    static class ComponentFilterAppConfig {
    }


}
