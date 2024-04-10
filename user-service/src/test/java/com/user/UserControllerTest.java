package com.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.user.controller.UserController;
import com.user.entity.User;
import com.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    public void testCreateUser() throws Exception {
        User newUser = new User();
        newUser.setFirstName("testuser");
        newUser.setLastName("testuser");
        newUser.setEmail("testuser@example.com");

        Mockito.when(userService.createUser(Mockito.any(User.class))).thenReturn(newUser);

        ResultActions result = mockMvc.perform(post("/user")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(newUser)));

        result.andDo(print()).andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName",
                        is(newUser.getFirstName())))
                .andExpect(jsonPath("$.lastName",
                        is(newUser.getLastName())))
                .andExpect(jsonPath("$.email",
                        is(newUser.getEmail())));

    }

    @Test
    public void testUpdateUser() throws Exception {
        User newUser = new User();
        newUser.setFirstName("testuser");
        newUser.setFirstName("testuser");
        newUser.setEmail("testuser@example.com");

        Mockito.when(userService.updateUser(Mockito.any(User.class))).thenReturn(newUser);

        ResultActions result = mockMvc.perform(put("/user/{id}", 1)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(newUser)));

        result.andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName",
                        is(newUser.getFirstName())))
                .andExpect(jsonPath("$.lastName",
                        is(newUser.getLastName())))
                .andExpect(jsonPath("$.email",
                        is(newUser.getEmail())));
    }
}
