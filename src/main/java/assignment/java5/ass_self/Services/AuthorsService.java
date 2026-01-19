package assignment.java5.ass_self.Services;

import assignment.java5.ass_self.Entities.Author;
import assignment.java5.ass_self.Repository.AuthorsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorsService {
    @Autowired
    private AuthorsRepository authorsRepository;

    public List<Author> getAllAuthors(){
        return authorsRepository.findAll();
    }
}
