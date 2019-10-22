package com.murphy1.serviced.serviced.services;

import com.murphy1.serviced.serviced.model.ResetToken;
import com.murphy1.serviced.serviced.model.User;

public interface ResetService {
    ResetToken generateToken(String email);
    User processToken(String token);
}
