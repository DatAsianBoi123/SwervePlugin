package com.datasiqn.swerveplugin;

public record ModuleLocation(double x, double y) {
    public ModuleLocation multiply(double scalar) {
        return new ModuleLocation(x * scalar, y * scalar);
    }

    public ModuleLocation divide(double scalar) {
        return new ModuleLocation(x / scalar, y / scalar);
    }
}
