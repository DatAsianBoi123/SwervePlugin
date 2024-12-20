package com.datasiqn.swerveplugin;

public record SwerveSpeeds(double xVelocity, double yVelocity, double angularVelocityRadians) {
    public double magnitudeSquared() {
        return xVelocity * xVelocity + yVelocity * yVelocity;
    }

    public double magnitude() {
        return Math.sqrt(magnitudeSquared());
    }

    public double angleRadians() {
        return Math.atan2(-xVelocity, -yVelocity);
    }
}
