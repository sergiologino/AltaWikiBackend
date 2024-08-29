package com.example.wikibackend.service;

import com.example.wikibackend.dto.UserSpaceAccessDTO;
import com.example.wikibackend.model.UserSpaceAccess;
import com.example.wikibackend.model.User;
import com.example.wikibackend.model.Space;
import com.example.wikibackend.repository.UserRepository;
import com.example.wikibackend.repository.SpaceRepository;
import com.example.wikibackend.repository.UserSpaceAccessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserSpaceAccessService {

    private final UserSpaceAccessRepository userSpaceAccessRepository;
    private final UserRepository userRepository;
    private final SpaceRepository spaceRepository;

    @Autowired
    public UserSpaceAccessService(UserSpaceAccessRepository userSpaceAccessRepository, UserRepository userRepository, SpaceRepository spaceRepository) {
        this.userSpaceAccessRepository = userSpaceAccessRepository;
        this.userRepository = userRepository;
        this.spaceRepository = spaceRepository;
    }

    // Получение полного списка разделов
    public List<Space> getAllSpaces() {
        return spaceRepository.findAll();
    }

    // Назначение пользователю списка доступных разделов
    public UserSpaceAccess assignSpaceAccess(UserSpaceAccessDTO userSpaceAccessDTO) {
        Optional<User> user = userRepository.findById(userSpaceAccessDTO.getUserId());
        Optional<Space> space = spaceRepository.findById(userSpaceAccessDTO.getSpaceId());

        if (user.isPresent() && space.isPresent()) {
            UserSpaceAccess userSpaceAccess = new UserSpaceAccess(user.get(), space.get(), userSpaceAccessDTO.getAccessType());
            return userSpaceAccessRepository.save(userSpaceAccess);
        } else {
            throw new IllegalArgumentException("User or Space not found");
        }
    }

    // Получение по пользователю списка доступных ему разделов
    public List<UserSpaceAccess> getUserSpaceAccesses(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            return userSpaceAccessRepository.findByUser(user.get());
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }

    // Получение по ID раздела списка пользователей, которым он доступен
    public List<UserSpaceAccess> getSpaceUsers(Long spaceId) {
        Optional<Space> space = spaceRepository.findById(spaceId);
        if (space.isPresent()) {
            return userSpaceAccessRepository.findBySpace(space.get());
        } else {
            throw new IllegalArgumentException("Space not found");
        }
    }
}
