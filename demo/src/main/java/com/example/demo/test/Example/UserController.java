package com.example.demo.test.Example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @GetMapping("/userAll")
    public ResponseEntity<List<Map<String, Object>>> getAllContents() {
        List<Map<String, Object>> users = new ArrayList<>();
        for (User user : this.userRepository.findAll()) {
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("surname", user.getSurname());
            userMap.put("name", user.getName());
            userMap.put("age", user.getAge());
            users.add(userMap);
        }
        return ResponseEntity.ok(users);
    }
    @GetMapping("/populate")
    public String populate() {
        String[] surnames = {"Smith", "Doe", "Williams", "Taylor", "Brown"};
        String[] names = {"John", "Jane", "David", "Sarah", "Michael"};
        Random random = new Random();

        for (int i = 0; i < 5; i++) {
            User user = new User();
            user.setSurname(surnames[random.nextInt(surnames.length)]);
            user.setName(names[random.nextInt(names.length)]);
            user.setAge(random.nextInt(80) + 18);
            userRepository.save(user);
        }

        return "Database populated with 5 users!";
    }
    @GetMapping("/hello")
    public String hello() {

        return "Hello !";
    }

}
