package com.interview.repository;

import com.interview.model.User;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class UserRepository {

    private final Map<Long, User> users = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(5);

    public UserRepository() {
        // Initialize with sample data from JSONPlaceholder
        initializeSampleData();
    }

    private void initializeSampleData() {
        createSampleUser(1L, "Leanne Graham", "Bret", "Sincere@april.biz", "1-770-736-8031 x56442", "hildegard.org");
        createSampleUser(2L, "Ervin Howell", "Antonette", "Shanna@melissa.tv", "010-692-6593 x09125", "anastasia.net");
        createSampleUser(3L, "Clementine Bauch", "Samantha", "Nathan@yesenia.net", "1-463-123-4447", "ramiro.info");
        createSampleUser(4L, "Patricia Lebsack", "Karianne", "Julianne.OConner@kory.org", "493-170-9623 x156", "kale.biz");
        createSampleUser(5L, "Chelsey Dietrich", "Kamren", "Lucio_Hettinger@annie.ca", "(254)954-1289", "demarco.info");
    }

    private void createSampleUser(Long id, String name, String username, String email, String phone, String website) {
        User user = new User(name, username, email, phone, website);
        user.setId(id);
        // Set timestamps manually for sample data
        try {
            java.lang.reflect.Field createdAtField = user.getClass().getSuperclass().getDeclaredField("createdAt");
            createdAtField.setAccessible(true);
            createdAtField.set(user, LocalDateTime.now());

            java.lang.reflect.Field updatedAtField = user.getClass().getSuperclass().getDeclaredField("updatedAt");
            updatedAtField.setAccessible(true);
            updatedAtField.set(user, LocalDateTime.now());
        } catch (Exception e) {
            // Ignore reflection errors
        }
        users.put(id, user);
    }

    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    public Optional<User> findById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    public Optional<User> findByUsername(String username) {
        return users.values().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }

    public Optional<User> findByEmail(String email) {
        return users.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }

    public User save(User user) {
        if (user.getId() == null) {
            user.setId(idGenerator.incrementAndGet());
            // Set timestamps manually for new users
            try {
                java.lang.reflect.Field createdAtField = user.getClass().getSuperclass().getDeclaredField("createdAt");
                createdAtField.setAccessible(true);
                createdAtField.set(user, LocalDateTime.now());

                java.lang.reflect.Field updatedAtField = user.getClass().getSuperclass().getDeclaredField("updatedAt");
                updatedAtField.setAccessible(true);
                updatedAtField.set(user, LocalDateTime.now());
            } catch (Exception e) {
                // Ignore reflection errors
            }
        } else {
            // Update timestamp for existing users
            try {
                java.lang.reflect.Field updatedAtField = user.getClass().getSuperclass().getDeclaredField("updatedAt");
                updatedAtField.setAccessible(true);
                updatedAtField.set(user, LocalDateTime.now());
            } catch (Exception e) {
                // Ignore reflection errors
            }
        }
        users.put(user.getId(), user);
        return user;
    }

    public void deleteById(Long id) {
        users.remove(id);
    }

    public boolean existsById(Long id) {
        return users.containsKey(id);
    }
}
