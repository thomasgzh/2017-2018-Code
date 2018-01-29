package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cCompassSensor;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
<<<<<<< HEAD
=======
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
>>>>>>> master
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.CompassSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
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

    /* Public members
    * Devices
    * -------
    * Make sure to control both left and right arms in unison
    * LR - lower right arm DC motor
    * LL - lower left arm DC motor (must be reverse of LR)
    * UR - upper right arm DC motor
    * UL - upper left arm DC motor (must be same of UR)
    */
    public DcMotor  LR   = null;
    public DcMotor  LL  = null;
    public DcMotor  UR   = null;
    public DcMotor  UL = null;

    /* Arm position switch */
    public DigitalChannel ArmSwitch = null;
    public int URArmHome = 0;   // change when potentionmeter added
    public int ULArmHome = 0;

    /* Public members
    * Devices
    * -------
    * GGR - gripper grabber right servo motor
    * GGL - gripper grabber right servo motor
    */
    public Servo GGR = null;
    public Servo GGL = null;
    public double[] GRABBER_LEFT = {0.745, .255, .375};
    public double[] GRABBER_RIGHT = {0.54, .99, .895};

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

        // **** Mecanum drive ****
        // Define and Initialize Motors
        FL   = hwMap.dcMotor.get("FL");
        FR  = hwMap.dcMotor.get("FR");
        BL   = hwMap.dcMotor.get("BL");
        BR   = hwMap.dcMotor.get("BR");
        // reverse those motors
        FR.setDirection(DcMotor.Direction.REVERSE);
        BR.setDirection(DcMotor.Direction.REVERSE);
         // Set all motors to zero power
        FL.setPower(0);
        FR.setPower(0);
        BL.setPower(0);
        BR.setPower(0);
        // Set all motors to run without encoders.
        FL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        FR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        BR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        BL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // **** Arm motors ****
        // Define and Initialize Motors
        UL   = hwMap.dcMotor.get("UL");
        UR  = hwMap.dcMotor.get("UR");
        LL   = hwMap.dcMotor.get("LL");
        LR   = hwMap.dcMotor.get("LR");
        // reverse those motors
        UR.setDirection(DcMotor.Direction.REVERSE);
        // Set all motors to zero power
        LL.setPower(0);
        LR.setPower(0);
        UL.setPower(0);
        UR.setPower(0);
        // Set all motors to run with encoders.
        LR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        LL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        UR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        UL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // **** Gripper grabbers ****
        // Define and Initialize Motors
        GGR = hwMap.servo.get("GGR");
        GGL = hwMap.servo.get("GGL");
        // set initial positions
        GGL.setPosition(GRABBER_LEFT[0]);
        GGR.setPosition(GRABBER_RIGHT[0]);

        // **** Arm Switch ****
        // Define and initialize switch
        ArmSwitch = hwMap.get(DigitalChannel.class, "touch sensor");
        // set the digital channel to input.
        ArmSwitch.setMode(DigitalChannel.Mode.INPUT);
        // false = pressed
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
        FR.setPower(speed);
        FL.setPower(-speed);
        BL.setPower(speed);
        BR.setPower(-speed);
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


<<<<<<< HEAD
=======
    /********** Arm Control class **********/
    public class ArmControl {
        //declaring all my variables in one place for my sake
        private double UpperArmHomePosition = 0;        /* position value at home */
        private double UpperArmPosition = 0;            /* current position relative to home */
        private double UpperArmFinalTarget = 0;         /* final target position */
        private double UpperArmTarget = 0;              /* target position */
        private boolean Homed = false;
        private double LastError = 0;
        private ElapsedTime Time  = new ElapsedTime();

        /* Constructor */
        public ArmControl() {
        }

        /* Initialize standard Hardware interfaces */
        public void init() {
            UpperArmHomePosition = UpperArmPot.getVoltage();
        }

        public void MoveUp() {
            UpperArmFinalTarget += 0.01;
            if (UpperArmFinalTarget>0.6) UpperArmFinalTarget = 0.6;
        }

        public void MoveDown() {
            UpperArmFinalTarget -= 0.01;
            if (UpperArmFinalTarget<0.0) UpperArmFinalTarget = 0.0;
        }

        public void MoveHome() {
            UpperArmFinalTarget = 0.0;
        }

        public void HoldCurrentPosition() {
            UpperArmFinalTarget = UpperArmPosition;
        }

        public void MoveToPosition(double target) {
            UpperArmFinalTarget = target;
            if (UpperArmFinalTarget>1.0) UpperArmFinalTarget = 1.0;
            if (UpperArmFinalTarget<0.0) UpperArmFinalTarget = 0.0;
        }

        /* Call this method when you want to update the arm motors */
        public void Update(OpMode om) {
            boolean at_home;                 /* home switch active */
            double upper_arm;
            double error, error_rate;
            final double UPPER_ARM_HOLD_POWER = 0.01;
            final double UPPER_ARM_POWER = 0.2;

            /* Check to see if on home switch */
            at_home = false;
            if (ArmSwitch.getState()==false) {
                /* arm in home position */
                at_home = true;
                Homed = true;
                UpperArmHomePosition = UpperArmPot.getVoltage();

                //adds a lil' version thing to the telemetry so you know you're using the right version
                om.telemetry.addLine("At Home");
            }

            /* determine current position relative to home */
            UpperArmPosition = UpperArmPot.getVoltage() - UpperArmHomePosition;

            /* incrementally change target value */
            if (UpperArmTarget < UpperArmFinalTarget-0.01)
                UpperArmTarget += 0.02;
            if (UpperArmTarget > UpperArmFinalTarget+0.01)
                UpperArmTarget -= 0.02;

            /*********** control code **********/
            error = UpperArmTarget - UpperArmPosition;
            if (error>0.2) error = 0.2;
            if (error<-0.2) error = -0.2;

            upper_arm = UPPER_ARM_POWER * 5 * error;
            upper_arm += UPPER_ARM_HOLD_POWER*(2.5-UpperArmTarget)/2.5;
            if (upper_arm==0.0) upper_arm = UPPER_ARM_HOLD_POWER/4;

            error_rate = 1000*(error-LastError)/Time.milliseconds();
            om.telemetry.addData("rate","%.3f", error_rate);

            /* compensate for a dropping arm, large power boast to stop it from falling */
            if ((error>0.0)&&(error_rate<-0.05)) {
                upper_arm += UPPER_ARM_POWER/4;
                om.telemetry.addLine("Save me!!!!");
            }
            if ((error<0.0)&&(error_rate>-0.05)) {
                upper_arm -= UPPER_ARM_POWER/4;
                om.telemetry.addLine("Save me!!!!");
            }
            LastError = error;
            Time.reset();

            /* prevent negative power when...
                at home position or never homed
            */
            if (at_home || !Homed) {
                if (upper_arm < 0.0) upper_arm = 0.0;
            }

            /* when target is zero ...
            * prevent positive power
            * zero power if close to home
            */
            if (UpperArmTarget<0.01) {
                if (upper_arm > 0.0) upper_arm = 0.0;
                if (error > -0.1) upper_arm = 0.0;
            }

            om.telemetry.addData("Error/Power","%.2f %.2f", error, upper_arm);

            UR.setPower(upper_arm);
            UL.setPower(upper_arm);
        }
    }

>>>>>>> master
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
