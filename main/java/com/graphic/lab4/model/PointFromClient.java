package com.graphic.lab4.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;


@Data
public class PointFromClient {

    private Double x;

    private Double y;
    private Double r;

    public PointFromClient(Double x, Double y, Double r) {
        this.x = x;
        this.y = y;
        this.r = r;
    }
}
