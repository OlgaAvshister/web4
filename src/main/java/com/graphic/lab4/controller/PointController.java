package com.graphic.lab4.controller;

import com.graphic.lab4.repositories.PointRepository;
import com.graphic.lab4.repositories.UserRepository;
import com.graphic.lab4.model.Graphic;
import com.graphic.lab4.model.PointForClient;
import com.graphic.lab4.model.PointFromClient;
import com.graphic.lab4.model.data.Point;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@URL
public class PointController {
    private final PointRepository pointRepository;
    private final UserRepository userRepository;
    private final Graphic graphic;

    @Autowired
    PointController(PointRepository pointRepository, UserRepository userRepository, Graphic graphic) {
        this.pointRepository = pointRepository;
        this.graphic = graphic;
        this.userRepository = userRepository;
    }

    @CrossOrigin
    @GetMapping("/points")
    Collection<PointForClient> allPoints(Principal user) {
        System.out.println("all points request from "+user.getName());
        List<PointForClient> pointsForClient = new ArrayList<>();
        Collection<Point> points = pointRepository.findAllUserPoints(userRepository.findByUsername(user.getName()));
        for (Point p : points) {
            pointsForClient.add(new PointForClient(p.getX(), p.getY(), p.getR(), p.getResult(), p.getDate()));
        }
        return pointsForClient;
    }

    @CrossOrigin
    @PostMapping("/points")
    public ResponseEntity<PointForClient> newPoint(@Valid @RequestBody PointForClient pointFromClient, Principal user) {
        Point newPoint = new Point();
        newPoint.setX(pointFromClient.getX());
        newPoint.setY(pointFromClient.getY());
        newPoint.setR(pointFromClient.getR());
        newPoint.setResult(graphic.isInArea(pointFromClient));
        newPoint.setUser(userRepository.findByUsername(user.getName()));

        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss", Locale.US);

        newPoint.setDate(dateTime.format(formatter));
        Point p = pointRepository.save(newPoint);
        return ResponseEntity.ok(new PointForClient(p.getX(), p.getY(), p.getR(), p.getResult(), p.getDate()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
