package com.Project.DMS.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import com.Project.DMS.Models.Dog;
import com.Project.DMS.Models.Trainer;
import com.Project.DMS.repository.DogRepository;
import com.Project.DMS.repository.TrainerRepository;

@Controller
public class DogController {

    @Autowired
    private DogRepository dogRepo;
    
    @Autowired
    private TrainerRepository trainerRepo;

    /* Home/Index Mappings */
 // Root endpoint - FIXES WHITE LABEL ERROR
    @GetMapping("/")
    public String rootRedirect() {
        return "redirect:/dogHome";
    }

    @GetMapping("dogHome")
    public ModelAndView home() {
        return new ModelAndView("home");
    }

    /* Dog Operations */
    @GetMapping("add")
    public ModelAndView addDogForm() {
        ModelAndView mv = new ModelAndView("addNewDog");
        mv.addObject("trainers", trainerRepo.findAll());
        return mv;
    }

    @PostMapping("addNewDog")
    public ModelAndView addNewDog(Dog dog, @RequestParam("trainerId") int id) {
        Trainer trainer = trainerRepo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid trainer ID: " + id));
        dog.setTrainer(trainer);
        dogRepo.save(dog);
        return new ModelAndView("redirect:/dogHome");
    }

    @GetMapping("viewModifyDelete")
    public ModelAndView viewDogs() {
        ModelAndView mv = new ModelAndView("viewDogs");
        mv.addObject("dogs", dogRepo.findAll());
        return mv;
    }

    @PostMapping("editDog")
    public ModelAndView editDog(Dog dog) {
        dogRepo.save(dog);
        return new ModelAndView("redirect:/viewModifyDelete");
    }

    @PostMapping("deleteDog")
    public ModelAndView deleteDog(@RequestParam("id") int id) {
        dogRepo.deleteById(id);
        return new ModelAndView("redirect:/viewModifyDelete");
    }

    @GetMapping("search")
    public ModelAndView searchById(@RequestParam("id") int id) {
        ModelAndView mv = new ModelAndView("searchResults");
        mv.addObject("dog", dogRepo.findById(id).orElse(new Dog()));
        return mv;
    }

    /* Trainer Operations */
    @GetMapping("trainerAdded")
    public ModelAndView addTrainerForm() {
        return new ModelAndView("addNewTrainer");
    }

    @PostMapping("trainerAdded")
    public ModelAndView addNewTrainer(Trainer trainer) {
        trainerRepo.save(trainer);
        return new ModelAndView("redirect:/dogHome");
    }
}
