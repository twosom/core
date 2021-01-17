package hello.core.member;

import hello.core.AppConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MemberServiceTest {

    MemberService memberService;


    @BeforeEach
    public void beforeEach() {
        memberService = new AppConfig().memberService();
    }


    @Test
    void join() {
        //given -이러한 것이 주어졌을 때
        Member member = new Member(1L, "memberA", Grade.VIP);

        //when  -이렇게 했을 때
        memberService.join(member);
        Member findMember = memberService.findMember(1L);

        //then  -이렇게 된다.
        Assertions.assertThat(member).isEqualTo(findMember);

    }

}
