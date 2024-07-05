package cl.ucm.coffee.service;

import cl.ucm.coffee.persitence.repository.TestimonialRepository;
import cl.ucm.coffee.persitence.entity.TestimonialsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestimonialService {

    @Autowired
    private TestimonialRepository testimonialRepository;

    public ResponseEntity<?> createTestimonial(TestimonialsEntity testimonialsEntity) {
        try {
            testimonialRepository.save(testimonialsEntity);
            return ResponseEntity.ok("Testimonial created successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creando testimonio: " + e.getMessage());
        }
    }

    public ResponseEntity<?> findByCoffeeId(int idCoffee) {
        try {
            List<TestimonialsEntity> testimonials = testimonialRepository.findByIdCoffee(idCoffee);
            if (testimonials.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No hay testimonio(s) para este café");
            }
            return ResponseEntity.ok(testimonials);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al recuperar testimonios: " + e.getMessage());
        }
    }
}
