package com.ex.forblog.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    AccountService accountService;

    private MockMvc mockMvc;

    private AccountDto.AccountRegister registerAccountDto;

    private ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .alwaysDo(print())
                .build();

        registerAccountDto = AccountDto.AccountRegister.builder()
                .name("hsoh")
                .password("password")
                .email("hsoh@gmail.com")
                .build();
    }

    /**
     * 계정을 등록한다
     * id:1, name:hsoh, password:password, email:hsoh@gmail.com
     * 성공적으로 등록되면 201 상태코드를 반환한다.
     * 성공적으로 등록되면 등록한 계정정보가 반환된다.
     */
    @Test
    public void registAccount() throws Exception {
        // Given

        // When
        final ResultActions resultActions = mockMvc.perform(
                post("/accounts").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(this.registerAccountDto))
                        .with(csrf()));

        // Then
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("hsoh"))
                .andExpect(jsonPath("$.email").value("hsoh@gmail.com"));
    }

    /**
     * name, password, email 필드중 하나라도 없으면 등록하면 실패한다.
     * 404 상태코드를 반환한다.
     */
    @Test
    public void registAccountBadRequest() throws Exception {
        //Given
        this.registerAccountDto.setName(" ");
        this.registerAccountDto.setPassword("1234");

        //When
        final ResultActions resultActions = mockMvc.perform(
                post("/accounts").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(this.registerAccountDto))
                        .with(csrf()));

        //Then
        resultActions.andExpect(status().isBadRequest());
    }

    /**
     * 계정 리스트를 조회한다.
     * 성공적으로 조회하면 200 상태코드를 반환한다.
     */
    @Test
    public void getAllAccounts() throws Exception {
        // Given
        accountService.register(this.registerAccountDto);

        // When
        final ResultActions resultActions = mockMvc.perform(get("/accounts"));

        // Then
        resultActions.andExpect(status().isOk());
    }

    /**
     * 특정 id로 계정을 조회한다.
     * 성공적으로 조회하면 200 상태코드를 반환한다.
     * 성공적으로 조회되면 계정정보가 반환된다.
     */
    @Test
    public void getAccount() throws Exception {
        // Given
        final Account account = accountService.register(this.registerAccountDto);

        // When
        final ResultActions resultActions = mockMvc.perform(get("/accounts/" + account.getId()));

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("hsoh"))
                .andExpect(jsonPath("$.email").value("hsoh@gmail.com"));
    }


}