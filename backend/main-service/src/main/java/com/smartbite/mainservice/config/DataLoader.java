package com.smartbite.mainservice.config;

import java.math.BigDecimal;
import java.time.LocalTime;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.smartbite.mainservice.entity.*;
import com.smartbite.mainservice.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final RoleRepository roleRepo;
    private final UserRepository userRepo;
    private final CanteenRepository canteenRepo;
    private final MenuRepository menuRepo;
    private final OrderRepository orderRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (roleRepo.count() > 0) {
            log.info("Data already loaded, skipping DataLoader.");
            return;
        }

        log.info("Loading dummy data...");

        // Roles
        Role adminRole = roleRepo.save(new Role(null, RoleType.ADMIN));
        Role studentRole = roleRepo.save(new Role(null, RoleType.STUDENT));
        Role managerRole = roleRepo.save(new Role(null, RoleType.CANTEEN_MANAGER));

        // Canteens
        Canteen canteen1 = canteenRepo.save(Canteen.builder()
                .canteenName("Main Block Canteen")
                .openingTime(LocalTime.parse("08:00"))
                .closingTime(LocalTime.parse("20:00"))
                .active(true)
                .build());

        Canteen canteen2 = canteenRepo.save(Canteen.builder()
                .canteenName("Tech Block Canteen")
                .openingTime(LocalTime.parse("09:00"))
                .closingTime(LocalTime.parse("18:00"))
                .active(true)
                .build());

        Canteen canteen3 = canteenRepo.save(Canteen.builder()
                .canteenName("Library Cafe")
                .openingTime(LocalTime.parse("10:00"))
                .closingTime(LocalTime.parse("17:00"))
                .active(false)
                .build());

        // Users
        User admin = new User();
        admin.setName("Admin User");
        admin.setEmail("admin@smartbite.com");
        admin.setPassword(passwordEncoder.encode("Admin@123"));
        admin.setPhone("9000000001");
        admin.setRole(adminRole);
        admin = userRepo.save(admin);

        User manager1 = new User();
        manager1.setName("Rajesh Kumar");
        manager1.setEmail("rajesh@smartbite.com");
        manager1.setPassword(passwordEncoder.encode("Manager@123"));
        manager1.setPhone("9000000002");
        manager1.setRole(managerRole);
        manager1.setCanteen(canteen1);
        manager1 = userRepo.save(manager1);

        User manager2 = new User();
        manager2.setName("Priya Sharma");
        manager2.setEmail("priya@smartbite.com");
        manager2.setPassword(passwordEncoder.encode("Manager@123"));
        manager2.setPhone("9000000003");
        manager2.setRole(managerRole);
        manager2.setCanteen(canteen2);
        manager2 = userRepo.save(manager2);

        User student1 = new User();
        student1.setName("Arjun Mehta");
        student1.setEmail("arjun@student.com");
        student1.setPassword(passwordEncoder.encode("Student@123"));
        student1.setPhone("9000000004");
        student1.setRole(studentRole);
        student1 = userRepo.save(student1);

        User student2 = new User();
        student2.setName("Sneha Patel");
        student2.setEmail("sneha@student.com");
        student2.setPassword(passwordEncoder.encode("Student@123"));
        student2.setPhone("9000000005");
        student2.setRole(studentRole);
        student2 = userRepo.save(student2);

        User student3 = new User();
        student3.setName("Vikram Singh");
        student3.setEmail("vikram@student.com");
        student3.setPassword(passwordEncoder.encode("Student@123"));
        student3.setPhone("9000000006");
        student3.setRole(studentRole);
        student3 = userRepo.save(student3);

        // Menu for Canteen 1
        menuRepo.save(Menu.builder().canteen(canteen1).foodName("Masala Dosa").description("Crispy dosa with potato filling").price(new BigDecimal("45.00")).category("South Indian").available(true).imageUrl("https://images.unsplash.com/photo-1630383249896-424e482df921?w=400").build());
        menuRepo.save(Menu.builder().canteen(canteen1).foodName("Vada Pav").description("Mumbai street style vada pav").price(new BigDecimal("20.00")).category("Snacks").available(true).imageUrl("https://images.unsplash.com/photo-1606491956689-2ea866880c84?w=400").build());
        menuRepo.save(Menu.builder().canteen(canteen1).foodName("Chicken Biryani").description("Aromatic basmati rice with chicken").price(new BigDecimal("120.00")).category("Main Course").available(true).imageUrl("https://images.unsplash.com/photo-1589302168068-964664d93dc0?w=400").build());
        menuRepo.save(Menu.builder().canteen(canteen1).foodName("Cold Coffee").description("Chilled coffee with ice cream").price(new BigDecimal("60.00")).category("Beverages").available(true).imageUrl("https://images.unsplash.com/photo-1461023058943-07fcbe16d735?w=400").build());
        menuRepo.save(Menu.builder().canteen(canteen1).foodName("Samosa").description("Crispy fried samosa with chutney").price(new BigDecimal("15.00")).category("Snacks").available(true).imageUrl("https://images.unsplash.com/photo-1601050690597-df0568f70950?w=400").build());
        menuRepo.save(Menu.builder().canteen(canteen1).foodName("Paneer Butter Masala").description("Rich creamy paneer curry").price(new BigDecimal("110.00")).category("Main Course").available(false).imageUrl("https://images.unsplash.com/photo-1565557623262-b51c2513a641?w=400").build());

        // Menu for Canteen 2
        menuRepo.save(Menu.builder().canteen(canteen2).foodName("Burger").description("Aloo tikki burger with cheese").price(new BigDecimal("80.00")).category("Snacks").available(true).imageUrl("https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=400").build());
        menuRepo.save(Menu.builder().canteen(canteen2).foodName("French Fries").description("Crispy golden fries with ketchup").price(new BigDecimal("55.00")).category("Snacks").available(true).imageUrl("https://images.unsplash.com/photo-1573080496219-bb080dd4f877?w=400").build());
        menuRepo.save(Menu.builder().canteen(canteen2).foodName("Pasta Arrabiata").description("Spicy tomato pasta").price(new BigDecimal("95.00")).category("Main Course").available(true).imageUrl("https://images.unsplash.com/photo-1563379926898-05f4575a45d8?w=400").build());
        menuRepo.save(Menu.builder().canteen(canteen2).foodName("Mango Shake").description("Fresh mango milkshake").price(new BigDecimal("70.00")).category("Beverages").available(true).imageUrl("https://images.unsplash.com/photo-1623065422902-30a2d299bbe4?w=400").build());
        menuRepo.save(Menu.builder().canteen(canteen2).foodName("Hakka Noodles").description("Wok tossed noodles with veggies").price(new BigDecimal("85.00")).category("Main Course").available(true).imageUrl("https://images.unsplash.com/photo-1612929633738-8fe44f7ec841?w=400").build());

        // Sample orders
        orderRepo.save(Order.builder().userId(student1.getUserId()).canteenId(canteen1.getCanteenId()).foodId(1L).quantity(2).totalAmount(new BigDecimal("90.00")).status(OrderStatus.COMPLETED).pickUpSlot("10:00-10:30").build());
        orderRepo.save(Order.builder().userId(student1.getUserId()).canteenId(canteen1.getCanteenId()).foodId(3L).quantity(1).totalAmount(new BigDecimal("120.00")).status(OrderStatus.READY).pickUpSlot("12:00-12:30").build());
        orderRepo.save(Order.builder().userId(student2.getUserId()).canteenId(canteen2.getCanteenId()).foodId(7L).quantity(1).totalAmount(new BigDecimal("80.00")).status(OrderStatus.PLACED).pickUpSlot("13:00-13:30").build());
        orderRepo.save(Order.builder().userId(student2.getUserId()).canteenId(canteen1.getCanteenId()).foodId(4L).quantity(2).totalAmount(new BigDecimal("120.00")).status(OrderStatus.PREPARING).pickUpSlot("14:00-14:30").build());
        orderRepo.save(Order.builder().userId(student3.getUserId()).canteenId(canteen2.getCanteenId()).foodId(9L).quantity(1).totalAmount(new BigDecimal("95.00")).status(OrderStatus.CANCELLED).pickUpSlot("11:00-11:30").build());

        log.info("Dummy data loaded successfully!");
        log.info("Admin: admin@smartbite.com / Admin@123");
        log.info("Manager 1: rajesh@smartbite.com / Manager@123");
        log.info("Manager 2: priya@smartbite.com / Manager@123");
        log.info("Student 1: arjun@student.com / Student@123");
        log.info("Student 2: sneha@student.com / Student@123");
        log.info("Student 3: vikram@student.com / Student@123");
    }
}
