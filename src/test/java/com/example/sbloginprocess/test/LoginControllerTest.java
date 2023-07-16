package com.example.sbloginprocess.test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;


import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.sbloginprocess.controller.LoginController;
import com.example.sbloginprocess.model.User;
import com.example.sbloginprocess.repository.UserRepository;

import java.util.Optional;
import java.util.UUID;

@WebMvcTest(LoginController.class)
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void testLoginPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/login"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("login"));
    }

    @Test
    public void testSuccessfulLogin() throws Exception {
        String username = "john";
        String password = "password123";
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        when(userRepository.findById(username)).thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                .param("username", username)
                .param("password", password))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("welcome"))
                .andExpect(MockMvcResultMatchers.model().attribute("username", username));
    }

    @Test
    public void testFailedLogin() throws Exception {
        String username = "nonexistinguser";
        String password = "wrongpassword";

        when(userRepository.findById(username)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                .param("username", username)
                .param("password", password))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("login"))
                .andExpect(MockMvcResultMatchers.model().attribute("error", "Username/Password incorrect or user does not exist"));
    }

    @Test
    public void testRegistrationPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/register"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("register"));
    }

    @Test
    public void testSuccessfulRegistration() throws Exception {
        String randomUsername = UUID.randomUUID().toString();
        String randomPassword = UUID.randomUUID().toString();

        when(userRepository.findById(any())).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                .param("username", randomUsername)
                .param("password", randomPassword))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/login"));

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User newUser = userCaptor.getValue();
        assertEquals(randomUsername, newUser.getUsername());
        assertEquals(randomPassword, newUser.getPassword());
    }

    @Test
    public void testExistingUsernameRegistration() throws Exception {
        String existingUsername = "existinguser";
        String password = "existingpassword";
        User existingUser = new User();
        existingUser.setUsername(existingUsername);
        existingUser.setPassword(password);

        when(userRepository.findById(existingUsername)).thenReturn(Optional.of(existingUser));

        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                .param("username", existingUsername)
                .param("password", password))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("register"))
                .andExpect(MockMvcResultMatchers.model().attribute("error", "Username exists, you cannot create it"));
    }

    @Test
    public void testRegistrationWithRandomCredentials() throws Exception {
        String randomUsername = UUID.randomUUID().toString();
        String randomPassword = UUID.randomUUID().toString();

        when(userRepository.findById(any())).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                .param("username", randomUsername)
                .param("password", randomPassword))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/login"));
    }

    @Test
    public void testWelcomePage() throws Exception {
        String username = "john";
    
        mockMvc.perform(MockMvcRequestBuilders.get("/welcome")
                .param("username", username))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("welcome"))
                .andExpect(MockMvcResultMatchers.model().attribute("username", username));
    }

    @Test
    public void testLoginPageWithErrorMessage() throws Exception {
        String errorMessage = "Invalid credentials";
        mockMvc.perform(MockMvcRequestBuilders.get("/login")
                .param("error", errorMessage))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("login"))
                .andExpect(MockMvcResultMatchers.model().attribute("error", errorMessage));
    }

}
