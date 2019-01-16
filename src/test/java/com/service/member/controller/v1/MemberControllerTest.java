package com.service.member.controller.v1;

import com.service.member.model.File;
import com.service.member.model.Member;
import com.service.member.service.MemberService;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class MemberControllerTest {

    @Mock
    private MemberService memberService;

    @InjectMocks
    private MemberController memberController;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(memberController).build();

    }


    @Test
    public void testListAllMembers() throws Exception {
        List<Member> beforeSavedMembers = memberService.findAll();

        Resource resource = new ClassPathResource("image/test-image.png");
        InputStream stream = resource.getInputStream();
        Member member1 = new Member("Member1FirstName", "Member1LastName", LocalDate.now(), "911", new File("test", "test", IOUtils.toByteArray(stream)));
        memberService.save(member1);


        Member member2 = new Member("Member2FirstName", "Member2LastName", LocalDate.now(), "777", null);
        memberService.save(member2);

        List<Member> members = Arrays.asList(member1, member2);

        when(memberService.findAll()).thenReturn(members);

        mockMvc.perform(get("/api/v1/member/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void testGetMember() {
    }

    @Test
    public void testCreateMember() {
    }

    @Test
    public void testUpdateMember() {
    }

    @Test
    public void testDeleteMember() {
    }
}