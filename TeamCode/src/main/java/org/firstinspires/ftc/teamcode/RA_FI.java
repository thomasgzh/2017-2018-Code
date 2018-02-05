package org.firstinspires.ftc.teamcode;

/**
 * Created by Nicholas on 1/27/2018.
 */

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

//naming the teleop thing
@Autonomous(name="RA_FI", group ="Drive")
public class RA_FI extends LinearOpMode {
    OpenGLMatrix lastLocation = null;
    VuforiaLocalizer vuforia;
    RobotConfig robot = new RobotConfig();
    private ElapsedTime runtime = new ElapsedTime();

    //mode 'stuff'
    //modes lists which steps and in what order to accomplish them
    int mode = 0;
    int [] modes = {-1, 0, 21, 20, 0, 1, 0, 2, 0, 3, 0, 4, 0, 5, 0, 7, 0, 8, 0, 100};

    //time based variables
    double lastReset = 0;
    double now = 0;

    /* IMU objects */
    BNO055IMU imu;
    Orientation angles;
    double startAngle;

    //clock reseter
    public void resetClock() {
        lastReset = runtime.seconds();
    }




    @Override
    public void runOpMode() throws InterruptedException {
        VoltageSensor vs = hardwareMap.voltageSensor.get("Lower hub 2");
        double voltage = vs.getVoltage();
        telemetry.addData("Voltage", voltage);

        //declaring all my variables in one place for my sake
        final double    MOVE_SPEED = 4/(voltage-5.2);
        final double    STRAFFE_SPEED = 6/(voltage-5.2);
        final double    ROTATE_SPEED = 4/(voltage-5.2);
        double          turnAngle;
        double          currentAngle;

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


        // Send telemetry message to signify robot waiting;
        telemetry.addLine("Init VuForia");    //
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters vuforia_parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        vuforia_parameters.vuforiaLicenseKey = "AQepDXf/////AAAAGcvzfI2nd0MHnzIGZ7JtquJk3Yx64l7jwu6XImRkNmBkhjVdVcI47QZ7xQq0PvugAb3+ppJxL4n+pNcnt1+PYpQHVETBEPk5WkofitFuYL8zzXEbs7uLY0dMUepnOiJcLSiVISKWWDyc8BJkKcK3a/KmB2sHaE1Lp2LJ+skW43+pYeqtgJE8o8xStowxPJB0OaSFXXw5dGUurK+ykmPam5oE+t+6hi9o/pO1EOHZFoqKl6tj/wsdu9+3I4lqGMsRutKH6s1rKLfip8s3MdlxqnlRKFmMDFewprELOwm+zpjmrJ1cqdlzzWQ6i/EMOzhcOzrPmH3JiH4CocA/Kcck12IuqvN4l6iAIjntb8b0G8zL";
        vuforia_parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK  ;
        this.vuforia = ClassFactory.createVuforiaLocalizer(vuforia_parameters);
        VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        VuforiaTrackable relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate"); // can help in debugging; otherwise not necessary
        relicTrackables.activate();
        RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);

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

                /* wait for vuMark detection */
                case -1:
                    /* VuForia update code */
                    vuMark = RelicRecoveryVuMark.from(relicTemplate);

                    if (vuMark == RelicRecoveryVuMark.UNKNOWN) {
                        telemetry.addData("VuMark", "not visible");
                    }
                    if (vuMark == RelicRecoveryVuMark.LEFT){
                        telemetry.addData("VuMark", "left");
                    }
                    if (vuMark == RelicRecoveryVuMark.RIGHT){
                        telemetry.addData("VuMark", "right");
                    }
                    if (vuMark == RelicRecoveryVuMark.CENTER){
                        telemetry.addData("VuMark", "center");
                    }
                    if ((vuMark != RelicRecoveryVuMark.UNKNOWN) || (now > 5.0)){
                        mode++;
                        resetClock();
                        startAngle = angles.firstAngle;
                        robot.MoveStop();
                    }
                    break;

                /* wait one second */
                case 0:
                    if (now > 1.0) {
                        mode++;
                        resetClock();
                        startAngle = angles.firstAngle;
                        robot.MoveStop();
                    }
                    break;

                /* backup 24 inches */
                case 1:
                    robot.MoveBackward(MOVE_SPEED);
                    if (now > 0.7) {
                        mode++;
                        resetClock();
                        startAngle = angles.firstAngle;
                        robot.MoveStop();
                    }
                    break;

                /* turn left 90 degrees */
                case 2:
                    robot.RotateRight(ROTATE_SPEED);
                    if (turnAngle > 85) {
                        mode++;
                        resetClock();
                        startAngle = angles.firstAngle;
                        robot.MoveStop();
                    }
                    break;

                /* move forward 24 inches */
                case 3:
                    robot.MoveForward(MOVE_SPEED);
                    if (now > 0.8) {
                        mode++;
                        resetClock();
                        startAngle = angles.firstAngle;
                        robot.MoveStop();
                    }
                    break;

                /* straif right 3/9/15 inches */
                case 4:
                    robot.MoveLeft(STRAFFE_SPEED);
                    if (vuMark == RelicRecoveryVuMark.LEFT && now > 0.5){
                        mode++;
                        resetClock();
                        startAngle = angles.firstAngle;
                        robot.MoveStop();
                    }
                    if (vuMark == RelicRecoveryVuMark.CENTER && now > 1.0){
                        mode++;
                        resetClock();
                        startAngle = angles.firstAngle;
                        robot.MoveStop();
                    }
                    if (vuMark == RelicRecoveryVuMark.RIGHT && now > 1.5){
                        mode++;
                        resetClock();
                        startAngle = angles.firstAngle;
                        robot.MoveStop();
                    }
                    break;

                /* move forward 8 inches */
                case 5:
                    robot.MoveForward(MOVE_SPEED);
                    if (now > 0.3) {
                        mode++;
                        resetClock();
                        startAngle = angles.firstAngle;
                        robot.MoveStop();
                    }
                    break;

                /* release glyph */
                case 6:
                    robot.GGL.setPosition(robot.GRABBER_LEFT[0]);
                    robot.GGR.setPosition(robot.GRABBER_RIGHT[0]);
                    if (now > 0.5) {
                        mode++;
                        resetClock();
                    }
                    break;

                /* move backward 8 inches */
                case 7:
                    robot.MoveBackward(MOVE_SPEED);
                    if (now > 0.3) {
                        mode++;
                        resetClock();
                        startAngle = angles.firstAngle;
                        robot.MoveStop();
                    }
                    break;

                /* rotate left 135 degrees */
                case 8:
                    robot.RotateRight(ROTATE_SPEED);
                    if (turnAngle > 130) {
                        mode++;
                        resetClock();
                        startAngle = angles.firstAngle;
                        robot.MoveStop();
                    }
                    break;

                case 20:
                    if (now > 0.9) {
                        robot.Arm.MoveHome();
                        // wait until home until next step
                        if (robot.ArmSwitch.getState()==false) {
                            mode++;
                            resetClock();
                        }
                    } else {
                        robot.Arm.MoveToPosition(0.2);
                    }
                    break;

                case 21:
                    robot.GGL.setPosition(robot.GRABBER_LEFT[1]);
                    robot.GGR.setPosition(robot.GRABBER_RIGHT[1]);
                    if (now > 0.5) {
                        mode++;
                        resetClock();
                    }
                    break;

/* not used rn
                case 31:
                    if (now > 0.9) {
                        robot.UL.setPower(0);
                        robot.UR.setPower(0);
                        mode++;
                        resetClock();
                    } else {
                        robot.UL.setPower(0.2);
                        robot.UR.setPower(0.2);
                    }
                    break;

                //MODE 3-7 to be added if advance past state to score jewel

                //MODE 8: Grab preloaded glyph

                case 71:
                    if (now > 1) {
                        mode++;
                        resetClock();
                    }
                    robot.MoveForward(MOVE_SPEED);
                    break;

                case 81:
                    if (now > 1) {
                        mode++;
                        resetClock();
                    }
                    robot.RotateLeft(MOVE_SPEED);
                    break;

                case 91:
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
*/

            }  // end of switch

            robot.Arm.Update(this);
        }
    }

    String format(OpenGLMatrix transformationMatrix) {
        return (transformationMatrix != null) ? transformationMatrix.formatAsTransform() : "null";
    }
}