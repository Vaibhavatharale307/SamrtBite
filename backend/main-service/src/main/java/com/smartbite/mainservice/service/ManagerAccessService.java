package com.smartbite.mainservice.service;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.smartbite.mainservice.entity.Menu;
import com.smartbite.mainservice.entity.Order;
import com.smartbite.mainservice.entity.RoleType;
import com.smartbite.mainservice.entity.User;
import com.smartbite.mainservice.exception.ResourceNotFoundException;
import com.smartbite.mainservice.repository.MenuRepository;
import com.smartbite.mainservice.repository.OrderRepository;
import com.smartbite.mainservice.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ManagerAccessService {

	private final UserRepository userRepository;
	private final MenuRepository menuRepository;
	private final OrderRepository orderRepository;

	public void verifyCanteenAccess(Long canteenId) {
		User user = getLoggedInUser();

		if (user.getRole().getRoleName() == RoleType.ADMIN) {
			return;
		}

		if (user.getRole().getRoleName() != RoleType.CANTEEN_MANAGER || user.getCanteen() == null
				|| !user.getCanteen().getCanteenId().equals(canteenId)) {
			throw new AccessDeniedException("You can access only your assigned canteen");
		}
	}

	public void verifyFoodAccess(Long foodId) {
		Menu menu = menuRepository.findById(foodId).orElseThrow(() -> new ResourceNotFoundException("Food item not found"));
		verifyCanteenAccess(menu.getCanteen().getCanteenId());
	}

	public void verifyOrderAccess(Long orderId) {
		Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
		verifyCanteenAccess(order.getCanteenId());
	}

	private User getLoggedInUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return userRepository.findByEmail(authentication.getName())
				.orElseThrow(() -> new AccessDeniedException("Logged-in user not found"));
	}
}
