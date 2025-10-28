package rs.fantasy.fantasyfootball.service;


import rs.fantasy.fantasyfootball.model.Injury;
import rs.fantasy.fantasyfootball.repository.InjuryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InjuryService {

    private final InjuryRepository injuryRepository;

    public InjuryService(InjuryRepository injuryRepository) {
        this.injuryRepository = injuryRepository;
    }

    public List<Injury> getAll() {
        return injuryRepository.findAll();
    }

    public Injury addInjury(Injury injury) {
        return injuryRepository.save(injury);
    }

    public void deleteInjury(Long id) {
        injuryRepository.deleteById(id);
    }
}
