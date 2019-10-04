package com.murphy1.serviced.serviced.controllers;

import com.murphy1.serviced.serviced.model.Admin;
import com.murphy1.serviced.serviced.model.Agent;
import com.murphy1.serviced.serviced.model.EndUser;
import com.murphy1.serviced.serviced.model.User;
import com.murphy1.serviced.serviced.services.AdminService;
import com.murphy1.serviced.serviced.services.AgentService;
import com.murphy1.serviced.serviced.services.EndUserService;
import com.murphy1.serviced.serviced.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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

    @GetMapping("/users/{user}")
    public String getAllAgents(Model model, @PathVariable String user){

        switch (user)
        {
            case "agents":
                model.addAttribute("users", agentService.getAllAgents());
                break;

            case "admins":
                model.addAttribute("users", adminService.getAllAdmins());
                break;

            case "endusers":
                model.addAttribute("users", endUserService.getAllEndUsers());
                break;

            default:
                throw new RuntimeException("That page does not exist!");
        }
        return "users.html";
    }

    @GetMapping("/users/new/{user}")
    public String newUser(Model model, @PathVariable String user){

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

        if (userType.equals("agent")){
            model.addAttribute("users", agentService.findAgentById(Long.valueOf(userId)));
            globalUserType = "agent";
            return "forms/new_user.html";
        }
        else if (userType.equals("admin")){
            model.addAttribute("users", adminService.findAdminById(Long.valueOf(userId)));
            globalUserType = "admin";
            return "forms/new_user.html";
        }
        else if (userType.equals("enduser")){
            model.addAttribute("users", endUserService.findEndUserById(Long.valueOf(userId)));
            globalUserType = "enduser";
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
    public String saveUser(@ModelAttribute User user){

        if (globalUserType.equals("agent")){
            agentService.saveAgent(userService.convertUserToAgent(user));
            return "redirect:/users/agents";
        }
        else if (globalUserType.equals("admin")){
            adminService.saveAdmin(userService.convertUserToAdmin(user));
            return "redirect:/users/admins";
        }
        else if (globalUserType.equals("enduser")){
            endUserService.saveEndUser(userService.convertUserToEndUser(user));
            return "redirect:/users/endusers";
        }
        else{
            throw new RuntimeException("user type does not exist!");
        }
    }

}
