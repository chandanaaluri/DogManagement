package com.example.DMS.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.DMS.Models.Dog;
import com.example.DMS.Models.Trainer;
import com.example.DMS.repository.DogRepository;
import com.example.DMS.repository.TrainerRepository;

@Controller
public class DogController {

    // Using a single shared ModelAndView is not thread-safe; creating a new instance for each method is better.
    @Autowired
    DogRepository dogRepo;
    @Autowired
    TrainerRepository trainerRepo;

    @RequestMapping("dogHome")
    public ModelAndView home() {
        ModelAndView mv = new ModelAndView("home");
        return mv;
    }

    @RequestMapping("add")
    public ModelAndView add() {
        ModelAndView mv = new ModelAndView("addNewDog.html");
        Iterable<Trainer> trainerList = trainerRepo.findAll();
        mv.addObject("trainers", trainerList);
        return mv;
    }

    @RequestMapping("addNewDog")
    public ModelAndView addNewDog(Dog dog, @RequestParam("trainerId") int id) {
        Trainer trainer = trainerRepo.findById(id).orElse(null);
        if (trainer != null) {
            dog.setTrainer(trainer);
            dogRepo.save(dog);
        }
        ModelAndView mv = new ModelAndView("home");
        return mv;
    }

    @RequestMapping("viewModifyDelete")
    public ModelAndView viewDogs() {
        ModelAndView mv = new ModelAndView("viewDogs");
        Iterable<Dog> dogList = dogRepo.findAll();
        mv.addObject("dogs", dogList);
        return mv;
    }

    @RequestMapping("editDog")
    public ModelAndView editDog(Dog dog) {
        dogRepo.save(dog);
        ModelAndView mv = new ModelAndView("editDog");
        return mv;
    }

    @RequestMapping("deleteDog")
    public ModelAndView deleteDog(@RequestParam("id") int id) {
        Dog dog = dogRepo.findById(id).orElse(null);
        if (dog != null) {
            dogRepo.delete(dog);
        }
        return home();
    }

    @RequestMapping("search")
    public ModelAndView searchById(@RequestParam("id") int id) {
        ModelAndView mv = new ModelAndView();
        Dog dogFound = dogRepo.findById(id).orElse(null);
        if (dogFound != null) {
            mv.addObject("dog", dogFound);
            mv.setViewName("searchresults");
        } else {
            mv.addObject("message", "Dog not found!");
            mv.setViewName("error"); // Use an error view or display a friendly message on the home page.
        }
        return mv;
    }

    @RequestMapping("addTrainer")
    public ModelAndView addTrainer() {
        ModelAndView mv = new ModelAndView("addNewTrainer.html");
        return mv;
    }

    @RequestMapping("trainerAdded")
    public ModelAndView addNewTrainer(Trainer trainer) {
        trainerRepo.save(trainer);
        ModelAndView mv = new ModelAndView("home");
        return mv;
    }
}
