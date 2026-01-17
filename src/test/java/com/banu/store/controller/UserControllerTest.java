package com.banu.store.controller;

import com.banu.store.entities.CreateNewUser;
import com.banu.store.entities.User;
import com.banu.store.mappers.UserMapper;
import com.banu.store.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;


import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private UserMapper userMapper;

    @Autowired
    private ObjectMapper objectMapper;

    //-----------------GET /users--------------------------------
    @Test
    void returnAllUsers() throws Exception {
        User user = new User();
        user.setId(1l);
        user.setName("abcd");
        user.setEmail("abcd@gmail.com");
        when(userRepository.findAll()).thenReturn(List.of(user));

        mockMvc.perform(get("/users").header("authToken", "1234"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].user_name").value("abcd"));

    }

    //-----------------GET /users/{id}----------------
    @Test
    void returnUserById_whenUserExists() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setName("def");
        user.setEmail("def@gmail.com");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("def@gmail.com"));
    }

    @Test
    void return404_whenUserDoesnotExists() throws Exception {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isNotFound());
    }


    //-----------------POST /users--------------------------
    @Test
    void createuser() throws Exception{
        User user = new User();
        user.setId(1L);
        user.setName("abcd");
        user.setEmail("abcd@gmail.com");

        when(userMapper.toEntity(any())).thenReturn(user);
        when(userRepository.save(any())).thenReturn(user);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.user_name").value("abcd"));
    }

    //---------------------PUT /users/id--------------------------

    @Test
    void updateUser_userExists_returnUpdatedUser() throws Exception{
        User user = new User();
        user.setId(1L);
        user.setName("abce");
        user.setEmail("abce@gmail.com");

        CreateNewUser request = new CreateNewUser();
        request.setEmail("new@gmail.com");
        request.setName("new");


        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user_name").value("new"));

    }

    @Test
    void updateUser_whenUserDoesNotExist_return404() throws Exception {

        Long userId = 99L;

        CreateNewUser request = new CreateNewUser();
        request.setName("test");
        request.setEmail("test@gmail.com");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        mockMvc.perform(put("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    // ---------- DELETE /users/{id} ----------
    @Test
    void shouldDeleteUser() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setName("abc");
        user.setEmail("abc@test.com");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());
    }



}