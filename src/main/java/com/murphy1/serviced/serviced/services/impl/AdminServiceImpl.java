package com.murphy1.serviced.serviced.services.impl;

import com.murphy1.serviced.serviced.exceptions.BadRequestException;
import com.murphy1.serviced.serviced.exceptions.NotFoundException;
import com.murphy1.serviced.serviced.model.Admin;
import com.murphy1.serviced.serviced.model.User;
import com.murphy1.serviced.serviced.repositories.AdminRepository;
import com.murphy1.serviced.serviced.services.AdminService;
import com.murphy1.serviced.serviced.services.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {

    private AdminRepository adminRepository;
    private UserService userService;

    public AdminServiceImpl(AdminRepository adminRepository, UserService userService) {
        this.adminRepository = adminRepository;
        this.userService = userService;
    }

    @Override
    public List<Admin> getAllAdmins() {

        List<Admin> admins = new ArrayList<>();

        adminRepository.findAll().iterator().forEachRemaining(admins :: add);

        return admins;
    }

    @Override
    public Admin saveAdmin(Admin admin) {

        if (!admin.getPassword().equals(admin.getPasswordCheck())){
            throw new BadRequestException("Passwords must match!");
        }

        if (!admin.getEmail().equals(admin.getEmailCheck())){
            throw new BadRequestException("Emails must match!");
        }

        if (admin.getId() == null){

            List<User> users = userService.getAllUsers();
            for (User user : users){
                if (user.getUsername().equalsIgnoreCase(admin.getUsername())){
                    throw new BadRequestException("Username is already taken!");
                }
                else if (user.getEmail().equalsIgnoreCase(admin.getEmail())){
                    throw new BadRequestException("An account already exists with this email!");
                }
            }

            admin.setActive(true);

        }

        admin.setRoles("ADMIN");

        return adminRepository.save(admin);
    }

    @Override
    public void deleteAdmin(Long id) {
        adminRepository.deleteById(id);
    }

    @Override
    public Admin findAdminById(Long id) {

        Optional<Admin> optionalAdmin = adminRepository.findById(id);

        if (optionalAdmin.isEmpty()){
            throw new NotFoundException("Admin id does not exist!");
        }

        return optionalAdmin.get();
    }
}
