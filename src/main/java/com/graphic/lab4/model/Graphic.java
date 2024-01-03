package com.graphic.lab4.model;

import org.springframework.stereotype.Component;

@Component
public class Graphic {

    //entry point
    public boolean isInArea(PointForClient point) {
        double x = point.getX();
        double y = point.getY();
        double r = point.getR();

        return x >= 0 && y >= 0 && (Math.pow(x, 2) + Math.pow(y, 2) <= Math.pow(r, 2)) ||
                x <= 0 && y <= 0 && x >= -r && y >= -r ||
                x >= 0 && y <= 0 && y >= x - r/2;
    }


}
