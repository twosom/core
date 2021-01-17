package hello.core.scope;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import static org.assertj.core.api.Assertions.assertThat;

public class PrototypeTest {
    @Test
    void prototypeBeanFind() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(TempAppConfig.class);
        System.out.println("find prototypeBean1");
        PrototypeBean prototypeBean1 = (PrototypeBean)ac.getBean("prototypeBean");
        System.out.println("find prototypeBean2");
        PrototypeBean prototypeBean2 = (PrototypeBean)ac.getBean("prototypeBean");

        System.out.println("prototypeBean1 = " + prototypeBean1);
        System.out.println("prototypeBean2 = " + prototypeBean2);

        assertThat(prototypeBean1).isNotSameAs(prototypeBean2);

        ac.close();
    }


    @Configuration
    static class TempAppConfig {

        @Scope("prototype")
        @Bean(initMethod = "init", destroyMethod = "destroy")
        PrototypeBean prototypeBean() {
            return new PrototypeBean();
        }
    }


    static class PrototypeBean {

        public void init() {
            System.out.println("PrototypeBean.init");
        }


        public void destroy() {
            System.out.println("PrototypeBean.destroy");
        }
    }
}

