package antifraud.business.user;

import antifraud.business.user.userdto.UserRequest;
import antifraud.business.user.userdto.UserResponse;
import antifraud.persistence.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> save(UserRequest userRequest) {
        Optional<User> userFromDb = userRepository.findUserByUsername(userRequest.getUsername());
        if (userFromDb.isPresent()) {
            return Optional.empty();
        }
        User user = new User();
        user.setName(userRequest.getName());
        user.setUsername(userRequest.getUsername());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        if (!userRepository.existsByRole("ADMINISTRATOR")) {
            user.setRole("ADMINISTRATOR");
            user.setStatus("UNLOCKED");
        }
        else {
            user.setRole("MERCHANT");
            user.setStatus("LOCKED");
        }
        return Optional.of(userRepository.save(user));
    }

    public List<UserResponse> findAllByOrderByIdAsc() {
        List<UserResponse> users = new ArrayList<>();
        userRepository.findAll().forEach(user -> users.add(new UserResponse(user.getId(), user.getName(), user.getUsername(), user.getRole())));
        users.sort(Comparator.naturalOrder());
        return users;
    }

    public void setRole(User user) {
        userRepository.save(user);
    }

    public void setLock(User user) {
        userRepository.save(user);
    }

    @Transactional
    public boolean deleteUser(String username) {
        Optional<User> byUsername = userRepository.findUserByUsername(username);
        if (byUsername.isEmpty()) {
            return false;
        }
        userRepository.delete(byUsername.get());
        return true;
    }

    public Optional<User> findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with provided username not found."));
    }
}
