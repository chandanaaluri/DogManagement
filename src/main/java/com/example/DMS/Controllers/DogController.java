package com.example.DMS.Controllers;

import com.example.DMS.Models.Dog;
import com.example.DMS.Models.Trainer;
import com.example.DMS.repository.DogRepository;
import com.example.DMS.repository.TrainerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DogController {

    @Autowired
    private DogRepository dogRepo;

    @Autowired
    private TrainerRepository trainerRepo;

    // Redirect root to dogHome
    @GetMapping("/")
    public ModelAndView defaultRoute() {
        return new ModelAndView("redirect:/dogHome");
    }

    // Show homepage
    @GetMapping("/dogHome")
    public ModelAndView home() {
        return new ModelAndView("home");
    }

    // Show form to add new dog
    @GetMapping("/add")
    public ModelAndView addDogForm() {
        ModelAndView mv = new ModelAndView("addNewDog");
        mv.addObject("trainers", trainerRepo.findAll());
        return mv;
    }

    // Process new dog addition
    @PostMapping("/addNewDog")
    public ModelAndView addNewDog(Dog dog, @RequestParam("trainerId") int id) {
        Trainer trainer = trainerRepo.findById(id).orElse(null);
        if (trainer != null) {
            dog.setTrainer(trainer);
            dogRepo.save(dog);
        }
        return new ModelAndView("redirect:/dogHome");
    }

    // View all dogs
    @GetMapping("/viewModifyDelete")
    public ModelAndView viewAllDogs() {
        ModelAndView mv = new ModelAndView("viewDogs");
        mv.addObject("dogs", dogRepo.findAll());
        return mv;
    }

    // Edit an existing dog
    @PostMapping("/editDog")
    public ModelAndView editDog(Dog dog) {
        dogRepo.save(dog);
        return new ModelAndView("redirect:/viewModifyDelete");
    }

    // Delete dog by id
    @GetMapping("/deleteDog")
    public ModelAndView deleteDog(@RequestParam("id") int id) {
        dogRepo.findById(id).ifPresent(dogRepo::delete);
        return new ModelAndView("redirect:/viewModifyDelete");
    }

    // Search for a dog by ID
    @GetMapping("/search")
    public ModelAndView searchDogById(@RequestParam("id") int id) {
        ModelAndView mv = new ModelAndView();
        Dog dog = dogRepo.findById(id).orElse(null);
        if (dog != null) {
            mv.setViewName("searchresults");
            mv.addObject("dog", dog);
        } else {
            mv.setViewName("error");
            mv.addObject("message", "Dog not found!");
        }
        return mv;
    }

    // Show form to add new trainer
    @GetMapping("/addTrainer")
    public ModelAndView addTrainerForm() {
        return new ModelAndView("addNewTrainer");
    }

    // Process trainer addition
    @PostMapping("/trainerAdded")
    public ModelAndView trainerAdded(Trainer trainer) {
        trainerRepo.save(trainer);
        return new ModelAndView("redirect:/add");
    }
}
