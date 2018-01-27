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
    int [] modes = {0, 1, 2, 3, 4, 5, 2, 6, 4, 7, 2, 100};

    //time based variables
    double lastReset = 0;
    double now = 0;

    float startAngle;
    float currentAngle;
    float turnAngle;

    //servo initialization
    //servo1
    //servo2

    //vvIMPORTANT
    BNO055IMU imu;
    Orientation angles;
    Acceleration gravity;
    //^^IMPORTANT

    @Override
    public void runOpMode() throws InterruptedException {
        //vvIMPORTANT
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
        //^^IMPORTANT

        //declaring all my variables in one place for my sake
        final double MOVE_SPEED = 0.5;
        final double ROTATE_SPEED = 0.4;
        final double TEST_TIME = 2.0;

        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Wait for start");    //
        telemetry.update();

        //waits for that giant PLAY button to be pressed on RC
        waitForStart();

        resetClock();//vvIMPORTANT
        startAngle = angles.firstAngle;
        //^^IMPORTANT

        // telling the code to run until you press that giant STOP button on RC
        // include opModeIsActive in all while loops so that STOP button terminates all actions
        while (opModeIsActive() && modes[mode] < 100) {

            //vvIMPORTANT
            angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            gravity = imu.getGravity();
            //^^IMPORTANT
            //vvless important
            telemetry.addData("IMUangle", angles);
            telemetry.addData("IMUgravity", gravity);
            telemetry.addData("Start Angle", startAngle);
            telemetry.addData("Current Angle", currentAngle);
            telemetry.addData("Turn Angle", turnAngle);
            telemetry.addData("mode", modes[mode]);
            telemetry.update();
            //^^less important

            //keeps now up to date
            now = runtime.seconds() - lastReset;

            //vvIMPORTANT
            currentAngle = angles.firstAngle;
            turnAngle = startAngle-currentAngle;

            if (turnAngle > 180)
                turnAngle -= 360;
            if (turnAngle < -180)
                turnAngle += 360;
            //^^IMPORTANT

            switch (modes[mode]) {

                case 0:
                    mode++;
                    resetClock();
                    startAngle = angles.firstAngle;
                    robot.MoveStop();
                    break;

                //right turn example. use 5 less then the degrees you want
                case 1:
                    robot.RotateRight(ROTATE_SPEED);
                    if (turnAngle > 85) {
                        //include with each mode. <-- means what is added to this requirement
                        mode++;
                        resetClock();
                        startAngle = angles.firstAngle;//<--
                        robot.MoveStop();//<--
                    }
                    break;

                case 2:
                    robot.GGL.setPosition(robot.GRABBER_LEFT[1]);
                    robot.GGR.setPosition(robot.GRABBER_RIGHT[1]);
                    if (now > 1.0) {
                        mode++;
                        resetClock();
                        startAngle = angles.firstAngle;
                        robot.MoveStop();
                    }
                    break;

                //left turn example
                case 3:
                    robot.RotateLeft(ROTATE_SPEED);
                    if (turnAngle < -85) {
                        mode++;
                        resetClock();
                        startAngle = angles.firstAngle;
                        robot.MoveStop();
                    }
                    break;

                case 4:
                    robot.GGL.setPosition(robot.GRABBER_LEFT[0]);
                    robot.GGR.setPosition(robot.GRABBER_RIGHT[0]);
                    if (now > 1.0) {
                        mode++;
                        resetClock();
                        startAngle = angles.firstAngle;
                        robot.MoveStop();
                    }
                    break;

                case 5:
                    robot.RotateLeft(ROTATE_SPEED);
                    if (turnAngle < -40) {
                        mode++;
                        resetClock();
                        startAngle = angles.firstAngle;
                        robot.MoveStop();
                    }
                    break;

                case 6:
                    robot.RotateRight(ROTATE_SPEED);
                    if (turnAngle > 40) {
                        mode++;
                        resetClock();
                        startAngle = angles.firstAngle;
                        robot.MoveStop();
                    }
                    break;

                case 7:
                    robot.RotateRight(ROTATE_SPEED);
                    if (turnAngle > 175) {
                        mode++;
                        resetClock();
                        startAngle = angles.firstAngle;
                        robot.MoveStop();
                    }
                    break;
                    
            }  // end of switch

            robot.Arm.Update();

        }
    }
}
