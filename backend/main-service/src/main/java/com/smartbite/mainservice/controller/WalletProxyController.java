package com.smartbite.mainservice.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import com.smartbite.mainservice.dto.WalletRequest;
import com.smartbite.mainservice.entity.RoleType;
import com.smartbite.mainservice.entity.User;
import com.smartbite.mainservice.security.CurrentUserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/wallet")
@RequiredArgsConstructor
public class WalletProxyController {

	private final RestClient walletRestClient;
	private final CurrentUserService currentUserService;

	@GetMapping("/{userId}")
	public ResponseEntity<String> getWallet(@PathVariable Long userId) {
		verifySameUserOrAdmin(userId);
		return walletRestClient.get()
				.uri("/wallet/{userId}", userId)
				.retrieve()
				.toEntity(String.class);
	}

	@GetMapping("/{userId}/history")
	public ResponseEntity<String> getWalletHistory(@PathVariable Long userId) {
		verifySameUserOrAdmin(userId);
		return walletRestClient.get()
				.uri("/wallet/{userId}/history", userId)
				.retrieve()
				.toEntity(String.class);
	}

	@PutMapping("/addMoney")
	public ResponseEntity<String> addMoney(@RequestBody WalletRequest request) {
		verifySameUserOrAdmin(request.getUserId());
		return walletRestClient.put()
				.uri("/wallet/addMoney")
				.contentType(MediaType.APPLICATION_JSON)
				.body(request)
				.retrieve()
				.toEntity(String.class);
	}

	private void verifySameUserOrAdmin(Long userId) {
		User user = currentUserService.getCurrentUser();
		if (user.getRole().getRoleName() != RoleType.ADMIN && !user.getUserId().equals(userId)) {
			throw new AccessDeniedException("You can access only your own wallet");
		}
	}
}
