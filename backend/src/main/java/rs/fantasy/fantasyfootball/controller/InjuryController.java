package rs.fantasy.fantasyfootball.controller;

import rs.fantasy.fantasyfootball.model.Injury;
import rs.fantasy.fantasyfootball.service.InjuryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/injuries")
@CrossOrigin(origins = "*")
public class InjuryController {

    private final InjuryService injuryService;

    public InjuryController(InjuryService injuryService) {
        this.injuryService = injuryService;
    }

    @GetMapping
    public List<Injury> getAll() {
        return injuryService.getAll();
    }

    @PostMapping
    public Injury add(@RequestBody Injury injury) {
        return injuryService.addInjury(injury);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        injuryService.deleteInjury(id);
    }
}
