package com.smartbite.mainservice.service;
import java.util.List;
import com.smartbite.mainservice.dto.*;
public interface CanteenService {
    CanteenResponse addCanteen(CanteenRequest request); CanteenResponse updateCanteen(Long id, CanteenRequest request); void deleteCanteen(Long id); List<CanteenResponse> getAllCanteens();
    MenuResponse addMenu(MenuRequest request); MenuResponse updateMenu(Long id, MenuRequest request); void deleteMenu(Long id); List<MenuResponse> getMenuByCanteen(Long id); MenuResponse getMenuByFoodId(Long id);
}
