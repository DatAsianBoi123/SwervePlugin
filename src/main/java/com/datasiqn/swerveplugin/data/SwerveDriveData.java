package com.datasiqn.swerveplugin.data;

import com.datasiqn.swerveplugin.ModuleState;
import com.datasiqn.swerveplugin.SwerveDrivePlugin;
import com.datasiqn.swerveplugin.SwerveSpeeds;
import edu.wpi.first.shuffleboard.api.data.ComplexData;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SwerveDriveData extends ComplexData<SwerveDriveData> {
    private final ModuleState[] states;
    private final SwerveSpeeds speeds;
    private final double headingRadians;
    private final double maxVelocity;

    public SwerveDriveData(ModuleState[] states, double headingRadians, SwerveSpeeds speeds, double maxVelocity) {
        this.states = states;
        this.headingRadians = headingRadians;
        this.speeds = speeds;
        this.maxVelocity = maxVelocity;
    }

    @Override
    public Map<String, Object> asMap() {
        Map<String, Object> map = new HashMap<>();

        Double[] locations = SwerveDrivePlugin.intersperse(states, state -> state.location().x(), state -> state.location().y(), Double[]::new);
        Double[] moduleSpeeds = SwerveDrivePlugin.intersperse(states, ModuleState::velocity, ModuleState::angleRadians, Double[]::new);

        map.put("moduleLocations", Arrays.stream(locations).mapToDouble(Double::doubleValue).toArray());
        map.put("moduleSpeeds", Arrays.stream(moduleSpeeds).mapToDouble(Double::doubleValue).toArray());
        map.put("heading", headingRadians);
        map.put("xVelocity", speeds.xVelocity());
        map.put("yVelocity", speeds.yVelocity());
        map.put("maxVelocity", maxVelocity);
        map.put("angularVelocity", speeds.angularVelocityRadians());

        return map;
    }

    public ModuleState[] getStates() {
        return states;
    }

    public double getHeadingRadians() {
        return headingRadians;
    }

    public SwerveSpeeds getSpeeds() {
        return speeds;
    }

    public double getMaxVelocity() {
        return maxVelocity;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        SwerveDriveData that = (SwerveDriveData) o;
        return Double.compare(headingRadians, that.headingRadians) == 0 && Double.compare(maxVelocity, that.maxVelocity) == 0 && Arrays.equals(states, that.states) && speeds.equals(that.speeds);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(states);
        result = 31 * result + Double.hashCode(headingRadians);
        result = 31 * result + speeds.hashCode();
        result = 31 * result + Double.hashCode(maxVelocity);
        return result;
    }
}
