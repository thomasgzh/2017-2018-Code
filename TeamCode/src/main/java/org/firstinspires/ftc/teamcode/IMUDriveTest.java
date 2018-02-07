package org.firstinspires.ftc.teamcode;

/**
 * Created by Nicholas on 2/7/2018.
 */
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

//naming the teleop thing
@Autonomous(name="IMUDriveTest", group ="Drive")
public class IMUDriveTest extends LinearOpMode {
    OpenGLMatrix lastLocation = null;
    VuforiaLocalizer vuforia;
    RobotConfig robot = new RobotConfig();
    private ElapsedTime runtime = new ElapsedTime();

    //mode 'stuff'
    //modes lists which steps and in what order to accomplish them
    int mode = 0;
    int[] modes = {0, 1, 100};

    //time based variables
    double lastReset = 0;
    double now = 0;

    /* IMU objects */
    BNO055IMU imu;
    Orientation angles;
    Acceleration gravity;
    double startAngle;

    //clock reseter
    public void resetClock() {
        lastReset = runtime.seconds();
    }

    @Override
    public void runOpMode() throws InterruptedException {
        //declaring all my variables in one place for my sake
        final double MOVE_SPEED = 0.5;
        final double STRAFFE_SPEED = 0.75;
        final double ROTATE_SPEED = 0.5;
        double          turnAngle;
        double          currentAngle;
        double          currentDistance;

        double intStart = 0;
        double intEnd = 0;
        double intStartTime = 0;
        double timeInt = 0;

        // Send telemetry message to signify robot waiting;
        telemetry.addLine("RA_FI");    //

        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);

        /* initialize IMU */
        // Send telemetry message to signify robot waiting;
        telemetry.addLine("Init imu");    //
        BNO055IMU.Parameters imu_parameters = new BNO055IMU.Parameters();
        imu_parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        imu_parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        imu_parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        imu_parameters.loggingEnabled = true;
        imu_parameters.loggingTag = "IMU";
        imu_parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(imu_parameters);
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        telemetry.addData(">", "Press Play to start");
        telemetry.update();

        //waits for that giant PLAY button to be pressed on RC
        waitForStart();

        resetClock();
        startAngle = angles.firstAngle;

        // telling the code to run until you press that giant STOP button on RC
        // include opModeIsActive in all while loops so that STOP button terminates all actions
        while (opModeIsActive() && modes[mode] < 100) {

            /* IMU update code */
            angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            currentAngle = angles.firstAngle;
            turnAngle = startAngle-currentAngle;

            if (Math.abs(gravity.zAccel) < .1){
                intEnd = 0;
            }

            if (intStart != intEnd) {
                intStart = intEnd;
                intStartTime = runtime.seconds();
                intEnd = gravity.zAccel;
                timeInt = runtime.seconds() - intStartTime;
            }
            currentDistance =+ ((intStart + intEnd)*timeInt/2);

            telemetry.addData("current distance", currentDistance);
            telemetry.update();

            if (turnAngle > 180)
                turnAngle -= 360;
            if (turnAngle < -180)
                turnAngle += 360;

            //keeps now up to date
            now = runtime.seconds() - lastReset;

            switch (modes[mode]) {

                default:
                    telemetry.addLine("All done");
                    robot.MoveStop();
                    break;

                /* wait one second */
                case 0:
                    if (now > 1.0) {
                        mode++;
                        resetClock();
                        robot.MoveStop();
                    }
                    break;

                case 1:
                    robot.MoveForward(MOVE_SPEED);
                    if (currentDistance > 100) {
                        mode++;
                        resetClock();
                        robot.MoveStop();
                    }
                    break;

            }  // end of switch

            robot.Arm.Update(this);
        }
    }

    String format(OpenGLMatrix transformationMatrix) {
        return (transformationMatrix != null) ? transformationMatrix.formatAsTransform() : "null";
    }
}