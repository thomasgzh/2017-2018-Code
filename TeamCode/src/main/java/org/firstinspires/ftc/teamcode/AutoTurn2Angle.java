package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

/**
 * Created by Nicholas on 1/24/2018.
 */

@Autonomous(name="AutoTurn2Angle", group ="Test")
public class AutoTurn2Angle extends LinearOpMode {

    RobotConfig robot = new RobotConfig();
    private ElapsedTime runtime = new ElapsedTime();


    //clock reseter
    public void resetClock() {lastReset = runtime.seconds();
    }

    //public void angleTurn(double) {



    //mode 'stuff'
    //modes lists which steps and in what order to accomplish them
    int mode = 0;
    int [] modes = {1, 100};

    //time based variables
    double lastReset = 0;
    double now = 0;

    float startAngle;
    float currentAngle;
    float turnAngle;

    //servo initialization
    //servo1
    //servo2

    BNO055IMU imu;
    Orientation angles;
    Acceleration gravity;

    @Override
    public void runOpMode() throws InterruptedException {
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        telemetry.update();
        imu = hardwareMap.get(BNO055IMU.class, "imu");

        imu.initialize(parameters);
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        gravity = imu.getGravity();

        //declaring all my variables in one place for my sake
        final double MOVE_SPEED = 0.5;
        final double ROTATE_SPEED = 0.3;
        final double TEST_TIME = 2.0;

        startAngle = angles.firstAngle;
        currentAngle = angles.firstAngle;

        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Wait for start");    //
        telemetry.update();

        //waits for that giant PLAY button to be pressed on RC
        waitForStart();

        resetClock();

        // telling the code to run until you press that giant STOP button on RC
        // include opModeIsActive in all while loops so that STOP button terminates all actions
        while (opModeIsActive() && modes[mode] < 100) {

            telemetry.addData("IMUangle", angles);
            telemetry.addData("IMUgravity", gravity);
            telemetry.addData("Start Angle", startAngle);
            telemetry.addData("Current Angle", currentAngle);
            telemetry.addData("Turn Angle", turnAngle);
            telemetry.update();

            //keeps now up to date
            now = runtime.seconds() - lastReset;

            currentAngle = angles.firstAngle;

            turnAngle = startAngle-currentAngle;

            if (turnAngle > 180)
                turnAngle -= 180;
            if (turnAngle < -180)
                turnAngle += 180;
            //MODE 1: Check Vumark
            /*if (modes[mode] == 1) {
                if (now > 1) {
                    mode++;
                    resetClock();
                }

                //Vuforia check
            }*/

            //MODE 2: Lock arm in resting pos
            switch (modes[mode]) {

                case 1:
                    startAngle = angles.firstAngle;
                    if (turnAngle < 90){
                        robot.RotateRight(ROTATE_SPEED);
                    }


                case 2:
                    robot.GGL.setPosition(robot.GRABBER_LEFT[1]);
                    robot.GGR.setPosition(robot.GRABBER_RIGHT[1]);
                    if (now > 0.5) {
                        mode++;
                        resetClock();
                    }
                    break;

                case 3:
                    if (now > 0.9) {
                        robot.Arm.MoveHome();
                        // wait until home until next step
                        if (robot.ArmSwitch.getState() == false) {
                            mode++;
                            resetClock();
                        }
                    } else {
                        robot.Arm.MoveToPosition(0.2);
                    }
                    break;

                //MODE 3-7 to be added if advance past state to score jewel

                //MODE 8: Grab preloaded glyph

                case 7:
                    if (now > 1) {
                        mode++;
                        resetClock();
                    }
                    robot.MoveForward(MOVE_SPEED);
                    break;

                case 8:
                    if (now > 1) {
                        mode++;
                        resetClock();
                    }
                    robot.RotateLeft(MOVE_SPEED);
                    break;

                case 9:
                    if (now > 0.3) {
                        mode++;
                        resetClock();
                    }
                    robot.MoveForward(MOVE_SPEED);
                    break;

                //MODE 11: Release glyph
                case 11:
                    robot.GGL.setPosition(robot.GRABBER_LEFT[0]);
                    robot.GGR.setPosition(robot.GRABBER_RIGHT[0]);
                    if (now > 0.5) {
                        mode++;
                        resetClock();
                    }
                    break;

                //MODE 12: Back up a bit
                case 12:
                    if (now > 0.3) {
                        mode++;
                        resetClock();
                    }
                    robot.MoveBackward(MOVE_SPEED);
                    break;

                //MODE 13: Do a 180
                case 13:
                    if (now > 2.4) {
                        mode++;
                        resetClock();
                    }
                    robot.RotateRight(ROTATE_SPEED);
                    break;

                //MODE 14: Bonus glyphs! (if time)


            }  // end of switch

            robot.Arm.Update();

        }
    }
}
