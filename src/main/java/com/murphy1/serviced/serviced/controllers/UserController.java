package com.murphy1.serviced.serviced.controllers;

import com.murphy1.serviced.serviced.model.Admin;
import com.murphy1.serviced.serviced.model.Agent;
import com.murphy1.serviced.serviced.model.EndUser;
import com.murphy1.serviced.serviced.model.User;
import com.murphy1.serviced.serviced.services.AdminService;
import com.murphy1.serviced.serviced.services.AgentService;
import com.murphy1.serviced.serviced.services.EndUserService;
import com.murphy1.serviced.serviced.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.Optional;

@Slf4j
@Controller
public class UserController {

    private AgentService agentService;
    private AdminService adminService;
    private EndUserService endUserService;
    private UserService userService;

    private String globalUserType;

    public UserController(AgentService agentService, AdminService adminService, EndUserService endUserService, UserService userService) {
        this.agentService = agentService;
        this.adminService = adminService;
        this.endUserService = endUserService;
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login(){
        return "login.html";
    }

    @GetMapping("/users")
    public String getAllUsers(Model model){
        //Check role. If user is admin they will see the Analytics option in Navbar
        String role = userService.getRole(userService.getCurrentUserName());
        model.addAttribute("role", role);

        model.addAttribute("users", userService.getAllUsers());

        return "users.html";
    }

    @GetMapping("/users/new/{user}")
    public String newUser(Model model, @PathVariable String user){
        //Check role. If user is admin they will see the Analytics option in Navbar
        String role = userService.getRole(userService.getCurrentUserName());
        model.addAttribute("role", role);

        switch (user)
        {
            case "agent":
                model.addAttribute("users", new Agent());
                globalUserType = "agent";
                break;

            case "admin":
                model.addAttribute("users", new Admin());
                globalUserType = "admin";
                break;

            case "enduser":
                model.addAttribute("users", new EndUser());
                globalUserType = "enduser";
                break;

            default:
                throw new RuntimeException("That object does not exist!");
        }

        return "forms/new_user.html";
    }

    @GetMapping("/users/update/{userType}/{userId}")
    public String updateUser(Model model, @PathVariable String userType, @PathVariable String userId){
        //Check role. If user is admin they will see the Analytics option in Navbar
        String analyticsCheck = userService.getRole(userService.getCurrentUserName());
        model.addAttribute("role", analyticsCheck);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        Optional<Boolean> roles = authentication.getAuthorities().stream()
                .map(role -> ((GrantedAuthority) role).getAuthority().equals("ADMIN"))
                .findFirst();

        if (userType.equals("AGENT")){
            model.addAttribute("users", agentService.findAgentById(Long.valueOf(userId)));
            globalUserType = "agent";
            if (roles.get()){
                return "forms/user_admin.html";
            }
            return "forms/new_user.html";
        }
        else if (userType.equals("ADMIN")){
            model.addAttribute("users", adminService.findAdminById(Long.valueOf(userId)));
            globalUserType = "admin";
            if (roles.get()){
                return "forms/user_admin.html";
            }
            return "forms/new_user.html";
        }
        else if (userType.equals("END_USER")){
            model.addAttribute("users", endUserService.findEndUserById(Long.valueOf(userId)));
            globalUserType = "enduser";
            if (roles.get()){
                return "forms/user_admin.html";
            }
            return "forms/new_user.html";
        }
        else{
            throw new RuntimeException("user does not exist!");
        }

    }

    @GetMapping("users/delete/{userType}/{userId}")
    public String deleteUser(Model model, @PathVariable String userType, @PathVariable String userId){

        if (userType.equals("agent")){
            agentService.deleteAgent(Long.valueOf(userId));
            return "redirect:/users/agents";
        }
        else if (userType.equals("admin")){
            adminService.deleteAdmin(Long.valueOf(userId));
            return "redirect:/users/admins";
        }
        else if (userType.equals("enduser")){
            endUserService.deleteEndUser(Long.valueOf(userId));
            return "redirect:/users/endusers";
        }
        else{
            throw new RuntimeException("user does not exist!");
        }

    }

    @PostMapping("/user/save")
    public String saveUser(@Valid @ModelAttribute("users") User user, BindingResult bindingResult) throws MessagingException {

        if (bindingResult.hasErrors()){
            bindingResult.getAllErrors().forEach(objectError ->
                    log.debug(objectError.toString())
                    );
            return "forms/new_user.html";
        }

        if (globalUserType.equals("agent")){
            agentService.saveAgent(userService.convertUserToAgent(user));
            return "redirect:/users";
        }
        else if (globalUserType.equals("admin")){
            adminService.saveAdmin(userService.convertUserToAdmin(user));
            return "redirect:/users";
        }
        else if (globalUserType.equals("enduser")){
            endUserService.saveEndUser(userService.convertUserToEndUser(user));
            return "redirect:/users";
        }
        else{
            throw new RuntimeException("user type does not exist!");
        }
    }

    @GetMapping("/users/change/end_user/{username}")
    public String updateRoleEndUser(@PathVariable String username){

        userService.changeToEndUser(userService.findUserByUsername(username));

        return "redirect:/users";
    }

    @GetMapping("/users/change/agent/{username}")
    public String updateRoleToAgent(@PathVariable String username){

        userService.changeToAgent(userService.findUserByUsername(username));

        return "redirect:/users";
    }

    @GetMapping("/users/change/admin/{username}")
    public String updateRoleToAdmin(@PathVariable String username){

        userService.changeToAdmin(userService.findUserByUsername(username));

        return "redirect:/users";
    }

    @PostMapping("/user/save/password")
    public String saveNewPassword(@ModelAttribute("users") User user) throws MessagingException {

        switch (user.getRoles()) {
            case "AGENT":
                agentService.saveAgent(userService.convertUserToAgent(user));
                return "redirect:/users";
            case "ADMIN":
                adminService.saveAdmin(userService.convertUserToAdmin(user));
                return "redirect:/users";
            case "END_USER":
                endUserService.saveEndUser(userService.convertUserToEndUser(user));
                return "redirect:/users";
            default:
                throw new RuntimeException("user type does not exist!");
        }

    }

}
