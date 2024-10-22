package pawsome.springframework.pawsomeWebApp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pawsome.springframework.pawsomeWebApp.dao.PetRepository;
import pawsome.springframework.pawsomeWebApp.dao.UserRepository;

import java.util.List;

@Service
public class ReportService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PetRepository petRepository;

    public List<Object[]> getUserActivityReport() {
        return userRepository.getUserActivityReport();
    }

    public List<Object[]> getPetsWithMostRecipesReport() {
        return petRepository.getPetWithMostRecipesReport();
    }
}