package rest_controller.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import rest_controller.model.Role;
import rest_controller.model.User;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        Role adminRole = roleService.findByName("ROLE_ADMIN");
        if (adminRole == null) {
            adminRole = new Role("ROLE_ADMIN");
            roleService.saveRole(adminRole);
            System.out.println("Роль ADMIN создана");
        }
        // Создание роли USER
        Role userRole = roleService.findByName("ROLE_USER");
        if (userRole == null) {
            userRole = new Role("ROLE_USER");
            roleService.saveRole(userRole);
            System.out.println("Роль USER создана");
        }
        // ПОЛУЧАЕМ РОЛИ ЗАНОВО ИЗ БАЗЫ
        adminRole = roleService.findByName("ROLE_ADMIN");
        userRole = roleService.findByName("ROLE_USER");
        // Создание пользователя andrey
        if (userService.findByUsername("andrey") == null) {
            User andrey = new User();
            andrey.setUsername("andrey");
            andrey.setPassword("100");
            andrey.setName("Андрей");
            andrey.setSurName("Рясной");
            andrey.setDepartment("СНГ");
            andrey.setSalary(150000);

            Set<Role> roles = new HashSet<>();
            roles.add(adminRole);
            roles.add(userRole);
            andrey.setRoles(roles);

            userService.saveUser(andrey);
            System.out.println("Пользователь andrey создан");
        }
        // Создание пользователя vika
        if (userService.findByUsername("vika") == null) {
            User vika = new User();
            vika.setUsername("vika");
            vika.setPassword("100");
            vika.setName("Вика");
            vika.setSurName("Рясная");
            vika.setDepartment("Dree");
            vika.setSalary(0);

            Set<Role> roles = new HashSet<>();
            roles.add(userRole);
            vika.setRoles(roles);

            userService.saveUser(vika);
            System.out.println("Пользователь vika создан");
        }
    }
}