package com.graphic.lab4.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
@Accessors(chain = true)
@Data
public class PointForClient {
    @Min(-5)
    @Max(5)
    private Double x;
    @Min(-5)
    @Max(5)
    private Double y;
    @Min(1)
    @Max(5)
    private Double r;
    private Boolean result;

    private String date;

    public PointForClient(Double x, Double y, Double r, boolean b, String date) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.result = b;
        this.date = date;
    }
}
