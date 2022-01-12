// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.TalonFXInvertType;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Drive extends SubsystemBase {
  
  //private final CANSparkMax frontLeft, frontRight, backLeft, backRight;
  private final WPI_TalonFX frontLeft, frontRight, backLeft, backRight;
  //private final CANEncoder leftEncoder, rightEncoder;
  private final AHRS nav;

  private final DifferentialDrive diffDrive;
  private final DifferentialDriveOdometry odometry;


  public Drive() {

    // configuration
    /*frontLeft = new CANSparkMax(Constants.kDrive.FRONT_LEFT_ID, Constants.kDrive.MOTOR_TYPE);
    frontRight = new CANSparkMax(Constants.kDrive.FRONT_RIGHT_ID, Constants.kDrive.MOTOR_TYPE);
    backLeft = new CANSparkMax(Constants.kDrive.BACK_LEFT_ID, Constants.kDrive.MOTOR_TYPE);
    backRight = new CANSparkMax(Constants.kDrive.BACK_RIGHT_ID, Constants.kDrive.MOTOR_TYPE);

    frontLeft.restoreFactoryDefaults();
    frontRight.restoreFactoryDefaults();
    backLeft.restoreFactoryDefaults();
    backRight.restoreFactoryDefaults();

    backLeft.follow(frontLeft);
    backRight.follow(frontRight);

    leftEncoder = frontLeft.getEncoder();
    rightEncoder = frontRight.getEncoder();*/

    frontLeft = new WPI_TalonFX(Constants.kDrive.FRONT_LEFT_ID);
    frontRight = new WPI_TalonFX(Constants.kDrive.FRONT_RIGHT_ID);
    backLeft = new WPI_TalonFX(Constants.kDrive.BACK_LEFT_ID);
    backRight = new WPI_TalonFX(Constants.kDrive.BACK_RIGHT_ID);

    frontLeft.configFactoryDefault();
    frontRight.configFactoryDefault();
    backLeft.configFactoryDefault();
    backRight.configFactoryDefault();

    frontLeft.setInverted(TalonFXInvertType.Clockwise);
    frontRight.setInverted(TalonFXInvertType.CounterClockwise);
    backLeft.setInverted(TalonFXInvertType.Clockwise);
    backRight.setInverted(TalonFXInvertType.CounterClockwise);

    backLeft.follow(frontLeft);
    backRight.follow(frontRight); 
    
    diffDrive = new DifferentialDrive(frontLeft, frontRight);
    resetEncoders();

    nav = new AHRS(SPI.Port.kMXP);
    nav.reset();

    // odometry stuff
    odometry = new DifferentialDriveOdometry(nav.getRotation2d());
  }

  @Override
  public void periodic() {
    odometry.update(nav.getRotation2d(), 
                    /*leftEncoder.getPosition(), 
                    rightEncoder.getPosition());*/
                    frontLeft.getSelectedSensorPosition(),
                    frontRight.getSelectedSensorPosition());
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }

  // get current robot position
  public Pose2d getPose() {
    return odometry.getPoseMeters();
  }

  // return current wheel speeds
  public DifferentialDriveWheelSpeeds getWheelSpeeds() {
    //return new DifferentialDriveWheelSpeeds(leftEncoder.getVelocity(), rightEncoder.getVelocity());
    return new DifferentialDriveWheelSpeeds(frontLeft.getSelectedSensorVelocity(), frontRight.getSelectedSensorVelocity());
  }

  // reset odometry to given pose
  public void resetOdometry(Pose2d pose) {
    resetEncoders();
    odometry.resetPosition(pose, nav.getRotation2d());
  }

  // zero encoders
  public void resetEncoders() {
    /*leftEncoder.setPosition(0);
    rightEncoder.setPosition(0);*/
    frontLeft.setSelectedSensorPosition(0);
    frontRight.setSelectedSensorPosition(0);
  }

  // we might have to make sure setVoltage works the way we expect it to
  public void tankDriveVolts(double leftVolts, double rightVolts) {
    frontLeft.setVoltage(leftVolts);
    frontRight.setVoltage(-rightVolts);
    diffDrive.feed();
  }

  public double getAverageEncoderDistance() {
    //return (leftEncoder.getPosition() + rightEncoder.getPosition()) / 2.0;
    return (frontLeft.getSelectedSensorPosition() + frontRight.getSelectedSensorPosition()) / 2.0;
  }

  /*public CANEncoder getLeftEncoder() {
    return leftEncoder;
  }

  public CANEncoder getRightEncoder() {
    return rightEncoder;
  }*/

  // scales maximum drive speed (0 to 1.0)
  public void setMaxOutput(double maxOutput) {
    diffDrive.setMaxOutput(maxOutput);
  }

  // zero navx
  public void zeroHeading() {
    nav.reset();
  }

  // return heading in degrees (-180 to 180)
  public double getHeading() {
    return nav.getYaw();
    // note: getAngle returns accumulated yaw (can be <0 or >360)
    //   getYaw has a 360 degree period
  }

  // return turn rate deg/sec
  public double getTurnRate() {
    // note: not sure why this is neg but docs said so
    return -nav.getRate();
  }
}