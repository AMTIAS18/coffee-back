package cl.ucm.coffee.web.controller;

import cl.ucm.coffee.persitence.entity.TestimonialsEntity;
import cl.ucm.coffee.service.TestimonialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/testimonials")
public class TestimonialController {

    @Autowired
    private TestimonialService testimonialService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody TestimonialsEntity testimonialsEntity) {
        return testimonialService.createTestimonial(testimonialsEntity);
    }

    @GetMapping("/findByCoffeeId")
    public ResponseEntity<?> findByCoffeeId(@RequestParam String coffeeId) {
        return testimonialService.findByCoffeeId(coffeeId);
    }
}

