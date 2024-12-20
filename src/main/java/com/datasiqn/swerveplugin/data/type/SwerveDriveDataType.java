package com.datasiqn.swerveplugin.data.type;

import com.datasiqn.swerveplugin.ModuleState;
import com.datasiqn.swerveplugin.SwerveSpeeds;
import com.datasiqn.swerveplugin.ModuleLocation;
import com.datasiqn.swerveplugin.data.SwerveDriveData;
import edu.wpi.first.shuffleboard.api.data.ComplexDataType;

import java.util.Map;
import java.util.function.Function;

public class SwerveDriveDataType extends ComplexDataType<SwerveDriveData> {
    public static final String NAME = "SWERVE_DRIVE";
    public static final SwerveDriveDataType INSTANCE = new SwerveDriveDataType();

    private SwerveDriveDataType() {
        super(SwerveDriveDataType.NAME, SwerveDriveData.class);
    }

    @Override
    public Function<Map<String, Object>, SwerveDriveData> fromMap() {
        return map -> {
            double[] locations = (double[]) map.get("moduleLocations");
            double[] speeds = (double[]) map.get("moduleSpeeds");
            double heading = (double) map.get("heading");
            double xVelocity = (double) map.get("xVelocity");
            double yVelocity = (double) map.get("yVelocity");
            double maxVelocity = (double) map.get("maxVelocity");
            double angularVelocity = (double) map.get("angularVelocity");

            ModuleState[] moduleStates = new ModuleState[locations.length / 2];
            for (int i = 0; i < locations.length - 1; i += 2) {
                ModuleLocation location = new ModuleLocation(locations[i], locations[i + 1]);
                moduleStates[i / 2] = new ModuleState(location, speeds[i], speeds[i + 1]);
            }

            return new SwerveDriveData(moduleStates, heading, new SwerveSpeeds(xVelocity, yVelocity, angularVelocity), maxVelocity);
        };
    }

    @Override
    public SwerveDriveData getDefaultValue() {
        return new SwerveDriveData(new ModuleState[0], 0, new SwerveSpeeds(0, 0, 0), 1);
    }
}
