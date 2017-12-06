package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cCompassSensor;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.CompassSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;
import com.qualcomm.robotcore.hardware.configuration.I2cSensor;
import com.qualcomm.robotcore.util.ElapsedTime;


public class RobotConfig
{
    /* Public members
    * Devices
    * -------
    * FL - front left DC motor
    * FR - front right DC motor
    * BL - back left DC motor
    * BR - back right DC motor
    * ColorSensor - color sensor
    *
    * Methods
    * -------
    * MoveStop()
    * MoveForward(speed)
    * MoveBackward(speed)
    * MoveLeft(speed)
    * MoveRight(speed)
    * RotateLeft(speed)
    * RotateRight(speed)
    */

    public DcMotor  FL   = null;
    public DcMotor  FR  = null;
    public DcMotor  BL   = null;
    public DcMotor  BR = null;
    public DcMotor  LR   = null;
    public DcMotor  LL  = null;
    public DcMotor  UR   = null;
    public DcMotor  UL = null;


    public Servo GGR = null;
    public Servo GGL = null;


    /* Local OpMode members. */
    HardwareMap hwMap  = null;
    private ElapsedTime period  = new ElapsedTime();

    /* Constructor */
    public RobotConfig() {
    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // save reference to HW Map
        hwMap = ahwMap;

        // Define and Initialize Motors
        FL   = hwMap.dcMotor.get("FL");
        FR  = hwMap.dcMotor.get("FR");
        BL   = hwMap.dcMotor.get("BL");
        BR   = hwMap.dcMotor.get("BR");
        UL   = hwMap.dcMotor.get("UL");
        UR  = hwMap.dcMotor.get("UR");
        LL   = hwMap.dcMotor.get("LL");
        LR   = hwMap.dcMotor.get("LR");
        GGR = hwMap.servo.get("GGR");

        // reverse those motors

        FL.setDirection(DcMotor.Direction.REVERSE);
        BL.setDirection(DcMotor.Direction.REVERSE);
        LL.setDirection(DcMotor.Direction.REVERSE);
        UL.setDirection(DcMotor.Direction.REVERSE);
        UR.setDirection(DcMotor.Direction.REVERSE);
        GGR.setDirection(Servo.Direction.REVERSE);
        // Set all motors to zero power
        FL.setPower(0);
        FR.setPower(0);
        BL.setPower(0);
        BR.setPower(0);

        // Set all motors to run without encoders.
        // May want to use RUN_USING_ENCODERS if encoders are installed.
        FL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        FR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        BR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        BL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        LR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        LL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        UR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        UL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);



        GGR = hwMap.servo.get("GGR");

        GGL = hwMap.servo.get("GGL");


    }

    /* forward is positive speed, backward is negative speed */
    public void MoveForwardBackward(double speed) {
        FR.setPower(speed);
        FL.setPower(speed);
        BL.setPower(speed);
        BR.setPower(speed);
    }
    /* left is positive speed, right is negative speed */
    public void MoveLeftRight(double speed) {
        FR.setPower(-speed);
        FL.setPower(speed);
        BL.setPower(-speed);
        BR.setPower(speed);
    }
    /* rotate left is positive speed, rotate right is negative speed */
    public void RotateLeftRight(double speed) {
        FR.setPower(speed);
        FL.setPower(-speed);
        BL.setPower(-speed);
        BR.setPower(speed);
    }

    /* Short hand movement methods */
    public void MoveStop() {
        MoveForwardBackward(0.0);
    }
    public void MoveForward(double speed) {
        MoveForwardBackward(speed);
    }
    public void MoveBackward(double speed) {
        MoveForwardBackward(-speed);
    }
    public void MoveLeft(double speed) {
        MoveLeftRight(speed);
    }
    public void MoveRight(double speed) {
        MoveLeftRight(-speed);
    }
    public void RotateLeft(double speed) {
        RotateLeftRight(speed);
    }
    public void RotateRight(double speed) {
        RotateLeftRight(-speed);
    }


    /***
     *
     * waitForTick implements a periodic delay. However, this acts like a metronome with a regular
     * periodic tick.  This is used to compensate for varying processing times for each cycle.
     * The function looks at the elapsed cycle time, and sleeps for the remaining time interval.
     *
     * @param periodMs  Length of wait cycle in mSec.
     * @throws InterruptedException
     */
    public void waitForTick(long periodMs)  throws InterruptedException {

        long  remaining = periodMs - (long)period.milliseconds();

        // sleep for the remaining portion of the regular cycle period.
        if (remaining > 0)
            Thread.sleep(remaining);

        // Reset the cycle clock for the next pass.
        period.reset();
    }
}