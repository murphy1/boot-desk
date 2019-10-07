package com.murphy1.serviced.serviced.services.impl;

import com.murphy1.serviced.serviced.model.Admin;
import com.murphy1.serviced.serviced.repositories.AdminRepository;
import com.murphy1.serviced.serviced.services.AdminService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {

    private AdminRepository adminRepository;

    public AdminServiceImpl(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public List<Admin> getAllAdmins() {

        List<Admin> admins = new ArrayList<>();

        adminRepository.findAll().iterator().forEachRemaining(admins :: add);

        return admins;
    }

    @Override
    public Admin saveAdmin(Admin admin) {
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
            throw new RuntimeException("Admin id does not exist!");
        }

        return optionalAdmin.get();
    }
}