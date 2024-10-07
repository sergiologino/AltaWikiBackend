package com.example.wikibackend.service;

import com.example.wikibackend.config.SwitchSchema;
import com.example.wikibackend.model.*;
import com.example.wikibackend.repository.UserAdminRepository;
import com.example.wikibackend.repository.UserRepository;
import com.example.wikibackend.repository.SpaceRepository;
import com.example.wikibackend.repository.UserSpaceAccessRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserSpaceAccessService {

    private final UserSpaceAccessRepository userSpaceAccessRepository;
    private final UserRepository userRepository;
//    private final UserAdminRepository userAdminRepository;
    private final SpaceRepository spaceRepository;

 //   private final OrganizationService organizationService;

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
    @SwitchSchema
    @Transactional
    public UserSpaceAccess assignSpaceAccess(UUID userId, UUID spaceId, AccessType accessType) {
//        Optional<UserAdmin> userAdmin=userAdminRepository.findById(userId);
//        UUID currentOrganizationId = userAdmin.get().getOrganizationId();
//        Long currentAlias=organizationService.getAlias(currentOrganizationId);
        Optional<Space> spaceOptional = spaceRepository.findById(spaceId);
        Optional<User> userOptional = userRepository.findById(userId);


        if (userOptional.isPresent() && spaceOptional.isPresent()) {
            User user = userOptional.get();
            Space space = spaceOptional.get();
            UserSpaceAccess userSpaceAccess = new UserSpaceAccess(user, space, accessType);
            return userSpaceAccessRepository.save(userSpaceAccess);
        } else {
            throw new IllegalArgumentException("Пользователь или раздел не найдены");
        }
    }

    // Получение по пользователю списка доступных ему разделов
    @SwitchSchema
    @Transactional
    public List<UserSpaceAccess> getUserSpaceAccesses(UUID userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            return userSpaceAccessRepository.findByUser(user.get());
        } else {
            throw new IllegalArgumentException("Пользователь не найден");
        }
    }

    // Получение по ID раздела списка пользователей, которым он доступен
    @SwitchSchema
    @Transactional
    public List<UserSpaceAccess> getSpaceUsers(UUID spaceId) {
        Optional<Space> space = spaceRepository.findById(spaceId);
        if (space.isPresent()) {
            return userSpaceAccessRepository.findBySpace(space.get());
        } else {
            throw new IllegalArgumentException("Раздел не найден");
        }
    }
}
