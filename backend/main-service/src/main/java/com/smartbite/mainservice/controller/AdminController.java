package com.smartbite.mainservice.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.smartbite.mainservice.dto.CanteenRequest;
import com.smartbite.mainservice.dto.CanteenResponse;
import com.smartbite.mainservice.entity.Canteen;
import com.smartbite.mainservice.entity.Role;
import com.smartbite.mainservice.entity.RoleType;
import com.smartbite.mainservice.entity.User;
import com.smartbite.mainservice.exception.ResourceNotFoundException;
import com.smartbite.mainservice.repository.CanteenRepository;
import com.smartbite.mainservice.repository.RoleRepository;
import com.smartbite.mainservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final UserRepository userRepository;
    private final CanteenRepository canteenRepository;
    private final RoleRepository roleRepository;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @GetMapping("/canteens")
    public ResponseEntity<List<CanteenResponse>> getAllCanteens() {
        return ResponseEntity.ok(canteenRepository.findAll().stream()
                .map(c -> CanteenResponse.builder()
                        .canteenId(c.getCanteenId())
                        .canteenName(c.getCanteenName())
                        .openingTime(c.getOpeningTime())
                        .closingTime(c.getClosingTime())
                        .active(c.getActive())
                        .build())
                .toList());
    }

    @PostMapping("/canteens")
    public ResponseEntity<CanteenResponse> createCanteen(@RequestBody CanteenRequest request) {
        Canteen saved = canteenRepository.save(Canteen.builder()
                .canteenName(request.getCanteenName())
                .openingTime(request.getOpeningTime())   // LocalTime → LocalTime ✅
                .closingTime(request.getClosingTime())   // LocalTime → LocalTime ✅
                .active(request.getActive() != null ? request.getActive() : true)
                .build());
        return ResponseEntity.ok(CanteenResponse.builder()
                .canteenId(saved.getCanteenId())
                .canteenName(saved.getCanteenName())
                .openingTime(saved.getOpeningTime())
                .closingTime(saved.getClosingTime())
                .active(saved.getActive())
                .build());
    }

    @DeleteMapping("/canteens/{id}")
    public ResponseEntity<String> deleteCanteen(@PathVariable Long id) {
        if (!canteenRepository.existsById(id))
            throw new ResourceNotFoundException("Canteen not found with id: " + id);
        canteenRepository.deleteById(id);
        return ResponseEntity.ok("Canteen deleted successfully");
    }

    @PutMapping("/users/{userId}/assign-canteen/{canteenId}")
    public ResponseEntity<String> assignManagerToCanteen(@PathVariable Long userId, @PathVariable Long canteenId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Canteen canteen = canteenRepository.findById(canteenId)
                .orElseThrow(() -> new ResourceNotFoundException("Canteen not found"));
        Role managerRole = roleRepository.findByRoleName(RoleType.CANTEEN_MANAGER)
                .orElseThrow(() -> new ResourceNotFoundException("Manager role not found"));
        user.setRole(managerRole);
        user.setCanteen(canteen);
        userRepository.save(user);
        return ResponseEntity.ok("Manager assigned to canteen successfully");
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id))
            throw new ResourceNotFoundException("User not found with id: " + id);
        userRepository.deleteById(id);
        return ResponseEntity.ok("User deleted successfully");
    }
}
