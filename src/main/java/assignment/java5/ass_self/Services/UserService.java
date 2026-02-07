package assignment.java5.ass_self.Services;

import assignment.java5.ass_self.Entities.User;
import assignment.java5.ass_self.Repository.UsersRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserService {
    private final UsersRepository usersRepository;

    public UserService(UsersRepository usersRepository){
        this.usersRepository = usersRepository;
    }

    public Optional<User> findUserByEmail(String email){
        return usersRepository.findByEmail(email);
    }
}
