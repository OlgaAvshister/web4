package com.graphic.lab4.model.data;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@Entity
@Table(name = "s367785_points")
public class Point {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;
    private Double x;
    private Double y;
    private Double r;
    private Boolean result;

    private String date;

    @ManyToOne
    private User user;

    public Point() {}

    public Point(double x, double y, double r, boolean result) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.result = result;
    }

    public String toString() {
        return String.format("%s %s %s %s", x, y, r, result);
    }
}
