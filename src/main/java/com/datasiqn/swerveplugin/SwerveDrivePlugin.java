package com.datasiqn.swerveplugin;

import com.datasiqn.swerveplugin.data.type.SwerveDriveDataType;
import com.datasiqn.swerveplugin.widget.SwerveDriveWidget;
import edu.wpi.first.shuffleboard.api.data.DataType;
import edu.wpi.first.shuffleboard.api.plugin.Description;
import edu.wpi.first.shuffleboard.api.plugin.Plugin;
import edu.wpi.first.shuffleboard.api.widget.ComponentType;
import edu.wpi.first.shuffleboard.api.widget.WidgetType;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.IntFunction;

@Description(group = "com.datasiqn", name = "Swerve Drive Plugin", version = "1.0.0", summary = "Plugin to use with a swerve drivetrain")
public class SwerveDrivePlugin extends Plugin {
    @Override
    public List<DataType> getDataTypes() {
        return List.of(SwerveDriveDataType.INSTANCE);
    }

    @Override
    public List<ComponentType> getComponents() {
        return List.of(WidgetType.forAnnotatedWidget(SwerveDriveWidget.class));
    }

    @Override
    public Map<DataType, ComponentType> getDefaultComponents() {
        return Map.of(SwerveDriveDataType.INSTANCE, WidgetType.forAnnotatedWidget(SwerveDriveWidget.class));
    }

    public static <T, M> M[] intersperse(T[] arr, Function<T, M> leftMapper, Function<T, M> rightMapper, IntFunction<M[]> newArray) {
        M[] interspersed = newArray.apply(arr.length * 2);
        for (int i = 0; i < interspersed.length - 1; i += 2) {
            interspersed[i] = leftMapper.apply(arr[i / 2]);
            interspersed[i + 1] = rightMapper.apply(arr[i / 2]);
        }
        return interspersed;
    }
}
