package com.cohort5.RestBil_System_Backend.config;

import com.cohort5.RestBil_System_Backend.Model.MenuItem;
import com.cohort5.RestBil_System_Backend.Model.Role;
import com.cohort5.RestBil_System_Backend.Repository.MenuItemRepository;
import com.cohort5.RestBil_System_Backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Seeds initial menu items and owner user into the database on application startup
 */
@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private UserService userService;

    @Override
    public void run(String... args) throws Exception {
        if (menuItemRepository.count() == 0) {
            seedMenuItems();
        }
        
        if (!userService.ownerExists()) {
            seedOwner();
        }
    }

    private void seedMenuItems() {
        MenuItem[] items = {
            new MenuItem(null, "Margherita Pizza", "Classic pizza with tomato sauce, mozzarella, and basil", 12.99, "Pizza", true),
            new MenuItem(null, "Pepperoni Pizza", "Pizza topped with pepperoni and mozzarella cheese", 14.99, "Pizza", true),
            new MenuItem(null, "Caesar Salad", "Fresh romaine lettuce with Caesar dressing and croutons", 8.99, "Salad", true),
            new MenuItem(null, "Greek Salad", "Mixed greens with feta cheese, olives, and tomatoes", 9.99, "Salad", true),
            new MenuItem(null, "Cheeseburger", "Beef patty with cheese, lettuce, tomato, and pickles", 11.99, "Burger", true),
            new MenuItem(null, "Chicken Burger", "Grilled chicken breast with lettuce and mayo", 10.99, "Burger", true),
            new MenuItem(null, "Spaghetti Carbonara", "Pasta with bacon, eggs, and parmesan cheese", 13.99, "Pasta", true),
            new MenuItem(null, "Fettuccine Alfredo", "Creamy pasta with parmesan cheese", 12.99, "Pasta", true),
            new MenuItem(null, "Grilled Salmon", "Fresh salmon fillet with lemon butter sauce", 18.99, "Seafood", true),
            new MenuItem(null, "Fish and Chips", "Battered fish with french fries", 15.99, "Seafood", true),
            new MenuItem(null, "Chicken Wings", "Spicy buffalo wings with ranch dressing", 9.99, "Appetizer", true),
            new MenuItem(null, "Mozzarella Sticks", "Fried mozzarella with marinara sauce", 7.99, "Appetizer", true),
            new MenuItem(null, "Chocolate Cake", "Rich chocolate layer cake with frosting", 6.99, "Dessert", true),
            new MenuItem(null, "Tiramisu", "Italian coffee-flavored dessert", 7.99, "Dessert", true),
            new MenuItem(null, "Iced Coffee", "Cold brew coffee with ice", 4.99, "Beverage", true)
        };

        for (MenuItem item : items) {
            menuItemRepository.save(item);
        }

        System.out.println("✓ Seeded 15 menu items successfully!");
    }

    private void seedOwner() {
        userService.createUser("owner", "owner123", Role.OWNER);
        System.out.println("✓ Seeded owner user successfully! (username: owner, password: owner123)");
    }
}
