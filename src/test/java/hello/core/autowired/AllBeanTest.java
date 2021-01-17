package hello.core.autowired;

import hello.core.AppConfig;
import hello.core.AutoAppConfig;
import hello.core.discount.DiscountPolicy;
import hello.core.member.Grade;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
import hello.core.order.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.context.annotation.ComponentScan.*;

public class AllBeanTest {

    @Test
    void findAllBean() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(AutoAppConfig.class);

        DiscountService discountService = ac.getBean(DiscountService.class);

        Member member = new Member(1L, "MemberA", Grade.VIP);

        assertThat(discountService).isInstanceOf(DiscountService.class);
        int fixDiscountPrice = discountService.discount(member, 10000, "fix");

        assertThat(fixDiscountPrice).isEqualTo(1000);


        int rateDiscountPrice = discountService.discount(member, 50000, "rate");

        assertThat(rateDiscountPrice).isEqualTo(5000);

    }

    @Test
    void tempConfigTest() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(TempAppConfig.class, DiscountService.class);

        DiscountService discountService = ac.getBean(DiscountService.class);
        Member member = new Member(1L, "memberTemp", Grade.VIP);
        int fix = discountService.discount(member, 50000, "fix");
        assertThat(fix).isEqualTo(1000);

        int rate = discountService.discount(member, 50000, "rate");
        assertThat(rate).isEqualTo(5000);
    }


    @Test
    void orderServiceTest() {

        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(TempAppConfig.class, OrderServiceImpl.class);


        String[] beanDefinitionNames = ac.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            System.out.println("bean : " + ac.getBean(beanDefinitionName).getClass());
        }

        OrderServiceImpl orderServiceImpl = ac.getBean(OrderServiceImpl.class);
        MemberRepository memberRepository = orderServiceImpl.getMemberRepository();
        System.out.println("memberRepository = " + memberRepository);
    }


    @Component
    static class DiscountService {

        private final Map<String, DiscountPolicy> policyMap;
        private final List<DiscountPolicy> policyList;

        public DiscountService(Map<String, DiscountPolicy> policyMap, List<DiscountPolicy> policyList) {
            this.policyMap = policyMap;
            this.policyList = policyList;
            System.out.println("policyMap = " + policyMap);
            System.out.println("policyList = " + policyList);
        }


        public int discount(Member member, int price, String policy) {
            int resultPrice = 0;
            switch (policy) {
                case "fix":
                    DiscountPolicy fixDiscountPolicy = policyMap.get("fixDiscountPolicy");
                    resultPrice = fixDiscountPolicy.discount(member, price);
                    break;

                case "rate":
                    DiscountPolicy rateDiscountPolicy = policyMap.get("rateDiscountPolicy");
                    resultPrice = rateDiscountPolicy.discount(member, price);
                    break;
            }
            return resultPrice;
        }
    }


    @Configuration
    @ComponentScan(
            basePackages = {"hello.core.discount", "hello.core.member"},
            excludeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE,
                    classes = {OrderServiceImpl.class, AppConfig.class, AutoAppConfig.class})
    )
    static class TempAppConfig {
    }
}

