package com.smartbite.mainservice.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.smartbite.mainservice.dto.*;
import com.smartbite.mainservice.entity.Canteen;
import com.smartbite.mainservice.entity.Menu;
import com.smartbite.mainservice.exception.ResourceNotFoundException;
import com.smartbite.mainservice.repository.CanteenRepository;
import com.smartbite.mainservice.repository.MenuRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CanteenServiceImpl implements CanteenService {
	private final CanteenRepository canteenRepo;

	private final MenuRepository menuRepo;

	public CanteenResponse addCanteen(CanteenRequest request) {

		return mapCanteen(canteenRepo.save(Canteen.builder()
				.canteenName(request.getCanteenName())
				.openingTime(request.getOpeningTime())
				.closingTime(request.getClosingTime())
				.active(request.getActive())
				.build()));
	}

	public CanteenResponse updateCanteen(Long id, CanteenRequest request) {
		
		Canteen canteen = canteenRepo.findById(id).
				orElseThrow(() -> new ResourceNotFoundException("Canteen Not Found"));

		canteen.setOpeningTime(request.getOpeningTime());
		canteen.setClosingTime(request.getClosingTime());
		canteen.setActive(request.getActive());
		canteen.setCanteenName(request.getCanteenName());

		return mapCanteen(canteenRepo.save(canteen));

	}

	public void deleteCanteen(Long id) {
		
		if (!canteenRepo.existsById(id))
			throw new ResourceNotFoundException("Canteen Not Found");

		canteenRepo.deleteById(id);

	}

	public List<CanteenResponse> getAllCanteens() {
		return canteenRepo.findAll().stream()
				.map(this::mapCanteen)
				.toList();
	}

	public MenuResponse addMenu(MenuRequest request) {
		
		Canteen canteen = canteenRepo.findById(request.getCanteenId())
				.orElseThrow(() -> new ResourceNotFoundException("Canteen Not Found"));
		
		return mapMenu(menuRepo.save(Menu.builder()
				.foodName(request.getFoodName())
				.description(request.getDescription())
				.price(request.getPrice())
				.category(request.getCategory())
				.available(request.getAvailable())
				.canteen(canteen)
				.build()));
	}

	public MenuResponse updateMenu(Long id, MenuRequest request) {
		
		Menu menu = menuRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Food Not Found"));
		
		Canteen canteen = canteenRepo.findById(request.getCanteenId())
				.orElseThrow(() -> new ResourceNotFoundException("Canteen Not Found"));
		
		menu.setFoodName(request.getFoodName());
		menu.setDescription(request.getDescription());
		menu.setPrice(request.getPrice());
		menu.setCategory(request.getCategory());
		menu.setAvailable(request.getAvailable());
		menu.setCanteen(canteen);
		
		return mapMenu(menuRepo.save(menu));
	}

	public void deleteMenu(Long id) {
		
		if (!menuRepo.existsById(id))
		{
			throw new ResourceNotFoundException("Food Not Found");
		}
		
		menuRepo.deleteById(id);
	}

	public List<MenuResponse> getMenuByCanteen(Long id) {
		
		return menuRepo.findByCanteen_CanteenId(id).stream()
				.map(this::mapMenu)
				.toList();
	}

	public MenuResponse getMenuByFoodId(Long id) {
		
		return mapMenu(menuRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Food Not Found")));
	}

	private CanteenResponse mapCanteen(Canteen c) {
		
		return CanteenResponse.builder()
				.canteenId(c.getCanteenId())
				.canteenName(c.getCanteenName())
				.openingTime(c.getOpeningTime())
				.closingTime(c.getClosingTime())
				.active(c.getActive())
				.build();
	}
	
	

	private MenuResponse mapMenu(Menu menu) {
		
		
		return MenuResponse.builder()
				.foodId(menu.getFoodId())
				.canteenId(menu.getCanteen().getCanteenId())
				.foodName(menu.getFoodName())
				.description(menu.getDescription())
				.price(menu.getPrice())
				.category(menu.getCategory())
				.available(menu.getAvailable())
				.build();
	}
	
	
}
