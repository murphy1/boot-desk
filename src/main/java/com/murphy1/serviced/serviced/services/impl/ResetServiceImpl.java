package com.murphy1.serviced.serviced.services.impl;

import com.murphy1.serviced.serviced.exceptions.BadRequestException;
import com.murphy1.serviced.serviced.model.ResetToken;
import com.murphy1.serviced.serviced.model.User;
import com.murphy1.serviced.serviced.repositories.TokenRepository;
import com.murphy1.serviced.serviced.services.ResetService;
import com.murphy1.serviced.serviced.services.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class ResetServiceImpl implements ResetService {

    private UserService userService;
    private TokenRepository tokenRepository;

    public ResetServiceImpl(UserService userService, TokenRepository tokenRepository) {
        this.userService = userService;
        this.tokenRepository = tokenRepository;
    }

    @Override
    public ResetToken generateToken(String email) {
        User foundUser = userService.findUserByEmail(email);
        ResetToken token = null;

        if (foundUser != null){
            token = new ResetToken();
            token.setEmail(email);
        }

        tokenRepository.save(token);

        return token;
    }

    @Override
    public User processToken(String token) {

        Optional<ResetToken> tokenOptional = tokenRepository.findByToken(token);

        if (!tokenOptional.isPresent()){
            throw new BadRequestException("Token is not valid!");
        }

        LocalDate now = LocalDate.now();
        LocalDate tokenDate = tokenOptional.get().getDate();

        if (now.isAfter(tokenDate)){
            tokenRepository.delete(tokenOptional.get());
            throw new BadRequestException("Token is not valid!");
        }

        User user = userService.findUserByEmail(tokenOptional.get().getEmail());

        return user;

    }
}
