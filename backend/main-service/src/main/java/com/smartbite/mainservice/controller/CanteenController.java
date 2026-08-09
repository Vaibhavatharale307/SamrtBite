package com.smartbite.mainservice.controller;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import com.smartbite.mainservice.dto.*;
import com.smartbite.mainservice.service.CanteenService;
import com.smartbite.mainservice.service.ManagerAccessService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/canteen")
@RequiredArgsConstructor
public class CanteenController {

	private final CanteenService canteenService;
	private final ManagerAccessService managerAccessService;

	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public CanteenResponse addCanteen(@RequestBody CanteenRequest r) {

		return canteenService.addCanteen(r);

	}

	@PutMapping("/{canteenId}")
	@PreAuthorize("hasRole('ADMIN')")
	public CanteenResponse updateCanteen(@PathVariable Long canteenId, @RequestBody CanteenRequest r) {

		return canteenService.updateCanteen(canteenId, r);

	}

	@DeleteMapping("/{canteenId}")
	@PreAuthorize("hasRole('ADMIN')")
	public void deleteCanteen(@PathVariable Long canteenId) {

		canteenService.deleteCanteen(canteenId);

	}

	@GetMapping
	public List<CanteenResponse> getAllCanteens() {

		return canteenService.getAllCanteens();

	}

	@PostMapping("/menu")
	@PreAuthorize("hasAnyRole('ADMIN', 'CANTEEN_MANAGER')")
	public MenuResponse addMenu(@RequestBody MenuRequest r) {
		managerAccessService.verifyCanteenAccess(r.getCanteenId());
		return canteenService.addMenu(r);
	}

	@PutMapping("/menu/{foodId}")
	@PreAuthorize("hasAnyRole('ADMIN', 'CANTEEN_MANAGER')")
	public MenuResponse updateMenu(@PathVariable Long foodId, @RequestBody MenuRequest r) {
		managerAccessService.verifyFoodAccess(foodId);
		managerAccessService.verifyCanteenAccess(r.getCanteenId());
		return canteenService.updateMenu(foodId, r);
	}

	@DeleteMapping("/menu/{foodId}")
	@PreAuthorize("hasAnyRole('ADMIN', 'CANTEEN_MANAGER')")
	public void deleteMenu(@PathVariable Long foodId) {
		managerAccessService.verifyFoodAccess(foodId);
		canteenService.deleteMenu(foodId);
	}

	
	@GetMapping("/menu/{canteenId}")
	public List<MenuResponse> getMenu(@PathVariable Long canteenId) {
		
		return canteenService.getMenuByCanteen(canteenId);
	}

	
	@GetMapping("/menu/item/{foodId}")
	public MenuResponse getMenuItem(@PathVariable Long foodId) {
		
		return canteenService.getMenuByFoodId(foodId);
	}
	
	
	
}
