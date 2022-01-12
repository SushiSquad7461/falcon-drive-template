// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {

    public static final class kDrive {
        
        // motor constants
        public static final int FRONT_LEFT_ID = 1;
        public static final int FRONT_RIGHT_ID = 15;
        public static final int BACK_LEFT_ID = 42;
        public static final int BACK_RIGHT_ID = 14;
        public static final CANSparkMaxLowLevel.MotorType MOTOR_TYPE = CANSparkMaxLowLevel.MotorType.kBrushless;

        // pidf constants - find via characterization
        public static final double ksVolts = 0;
        public static final double kvVoltSecondsPerMeter = 0;
        public static final double kaVoltSecondsSquaredPerMeter = 0;
        public static final double kPDriveVel = 0;
        public static final double kMaxVoltage = 10;

        // odometry constants - drivetrain measurements
        public static final double kTrackwidthMeters = 0.69; // width between sides of dt
        public static final DifferentialDriveKinematics kDriveKinematics = 
            new DifferentialDriveKinematics(kTrackwidthMeters);

        // path-following constants
        public static final double kMaxSpeedMetersPerSecond = 3; // set to somewhat below free speed
        //                                                          could increase this to go faster 
        //                                                          theoretically
        public static final double kMaxAccelerationMetersPerSecondSquared = 3; // doesn't really matter


        // ramsete parameters
        public static final double kRamseteB = 2;
        public static final double kRamseteZeta = 0.7;
    }

}