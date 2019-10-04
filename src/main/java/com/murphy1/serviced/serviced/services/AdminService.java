package com.murphy1.serviced.serviced.services;

import com.murphy1.serviced.serviced.model.Admin;

import java.util.List;

public interface AdminService {
    List<Admin> getAllAdmins();
    Admin saveAdmin(Admin admin);
    void deleteAdmin(Long id);
    Admin findAdminById(Long id);
}
