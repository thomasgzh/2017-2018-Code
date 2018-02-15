package org.firstinspires.ftc.teamcode;

/**
 * Created by Nicholas on 1/17/2018.
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
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;


//naming the teleop thing
@Autonomous(name="AllAuto", group ="Drive")
public class AllAuto extends LinearOpMode {
    OpenGLMatrix lastLocation = null;
    VuforiaLocalizer vuforia;
    RobotConfig robot = new RobotConfig();
    private ElapsedTime runtime = new ElapsedTime();

    public boolean inputTeamColor() {
        //input the team color
        telemetry.addData("Input: ", "Select Team Color");
        telemetry.update();

        while (!gamepad1.x && !gamepad1.b) {

        }
        if (gamepad1.x)
            return false;
        return true;
    }

    public boolean inputPosition() {
        //input the position
        telemetry.addData("Input: ", "Select Position");
        telemetry.update();

        while (!gamepad1.dpad_left && !gamepad1.dpad_right) {

        }
        //delay selection
        if ((gamepad1.dpad_left && redteam) || (gamepad1.dpad_right && !redteam))
            return true;
        return false;
    }

    public void displaySelections() {
        telemetry.addData("Status", "Initialized");
        if (FI)
            telemetry.addData("Position", "FI");
        else
            telemetry.addData("Position", "BI");
        if (redteam)
            telemetry.addData("Team Color", "RED");
        else
            telemetry.addData("Team Color", "BLUE");
        telemetry.update();
    }

    //mode 'stuff'
    //modes lists which steps and in what order to accomplish them
    int mode = 0;
    int [] modesRAFI = {-3, 0, -20, -1, 0, 1, 0, 20, 0, 30, 0, 40, 0, 50, 0, -21, 0, 6, 0, 7, 0, 80, 0, 100};
    int [] modesRABI = {-3, 0, -20, -1, 0, 1, 0, 20, 0, 31, 0, 41, 0, 51, 0, -21, 0, 6, 0, 7, 0, 81, 0, 100};
    int [] modesBAFI = {-3, 0, -20, -1, 0, 1, 0, 21, 0, 30, 0, 42, 0, 50, 0, -21, 0, 6, 0, 7, 0, 80, 0, 100};
    int [] modesBABI = {-3, 0, -20, -1, 0, 1, 0, 21, 0, 31, 0, 43, 0, 52, 0, -21, 0, 6, 0, 7, 0, 82, 0, 100};
    int[] modes = {};
    //-3 : Check Vumark
    //-20: Grab glyph
    //-21: Release glyph
    //-1 : Raise arm
    // 0 : Wait 1 sec
    // 1 : Back off balancing stone
    // 20: Turn left to -45 (red)
    // 21: Turn right to 45 (blue)
    // 30: Drive 1.5 diagonal tile (FI)
    // 31: Drive 1 diagonal tiles (BI)
    // 40: Turn right to 0
    // 41: Turn left to -90
    // 42: Turn left to 0
    // 43: Turn right to 90
    // 50: Turn to column FI
    // 51: Turn to column RABI
    // 52: Turn to column BABI
    // 6 : Drive into cryptobox
    // 7 : Back up from cryptobox
    // 80: Turn to 180 (FI)
    // 81: Turn left to 140 (RABI)
    // 82: Turn right to -140 (BABI)

    // 90: Drive into glyph pit
    // 91: Back up from glyph pit
    // 92: Turn 180
    // 93: Raise arm to glyph 2 pos
    // 94: Drive Forward
    // 95: Drive Back
    // 96: Lower Arm

    //100: End

    public void chooseModes() {
        if (FI && redteam)
            modes = modesRAFI;
        if (!FI && redteam)
            modes = modesRABI;
        if (FI && !redteam)
            modes = modesBAFI;
        if (!FI && !redteam)
            modes = modesBABI;
    }

    //start position variables
    boolean FI;
    boolean redteam;

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
        final double MOVE_SPEED = 0.5 + ((13.2-voltage)/8);
        final double STRAFFE_SPEED = 0.75 + ((13.2-voltage)/8);
        final double ROTATE_SPEED = 0.4 + ((13.2-voltage)/8);
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

        redteam = inputTeamColor();
        FI = inputPosition();
        displaySelections();
        chooseModes();

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
                case -3:
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

                /* grab glyph */
                case -20:
                    robot.GGL.setPosition(robot.GRABBER_LEFT[1]);
                    robot.GGR.setPosition(robot.GRABBER_RIGHT[1]);
                    if (now > 0.5) {
                        mode++;
                        resetClock();
                    }
                    break;

                /* release glyph */
                case -21:
                    robot.GGL.setPosition(robot.GRABBER_LEFT[0]);
                    robot.GGR.setPosition(robot.GRABBER_RIGHT[0]);
                    if (now > 0.5) {
                        mode++;
                        resetClock();
                    }
                    break;

                /* raise arm */
                case -1:
                    if (now > 0.9) {
                        robot.UpperArm.MoveHome();
                        // wait until home until next step
                        if (robot.UpperArm.Limit.getState()==false) {
                            mode++;
                            resetClock();
                        }
                    } else {
                        robot.UpperArm.MoveToPosition(0.2);
                    }
                    break;

                /* wait one second */
                case 0:
                    if (now > 1.0) {
                        mode++;
                        resetClock();
                        robot.MoveStop();
                    }
                    break;

                /* backup 24 inches */
                case 1:
                    robot.MoveBackward(MOVE_SPEED);
                    if (now > 0.92) {
                        mode++;
                        resetClock();
                        robot.MoveStop();
                    }
                    break;

                /* turn left to -45 (red) */
                case 20:
                    robot.RotateLeft(ROTATE_SPEED);
                    if (turnAngle < -40) {
                        mode++;
                        resetClock();
                        robot.MoveStop();
                    }
                    break;

                /* turn right to 45 (blue) */
                case 21:
                    robot.RotateRight(ROTATE_SPEED);
                    if (turnAngle > 40) {
                        mode++;
                        resetClock();
                        robot.MoveStop();
                    }
                    break;

                /* move forward 50.9 inches (FI) */
                case 30:
                    robot.MoveForward(MOVE_SPEED);
                    if (now > 1.46) {
                        mode++;
                        resetClock();
                        robot.MoveStop();
                    }
                    break;

                /* move forward 33.9 inches (BI) */
                case 31:
                    robot.MoveForward(MOVE_SPEED);
                    if (now > 1.12) {
                        mode++;
                        resetClock();
                        robot.MoveStop();
                    }
                    break;

                /* turn right to 0 (RAFI) */
                case 40:
                    robot.RotateRight(ROTATE_SPEED);
                    if (turnAngle > -5) {
                        mode++;
                        resetClock();
                        robot.MoveStop();
                    }
                    break;

                /* turn left to -90 (RABI) */
                case 41:
                    robot.RotateLeft(ROTATE_SPEED);
                    if (turnAngle < -85) {
                        mode++;
                        resetClock();
                        robot.MoveStop();
                    }
                    break;

                /* turn left to 0 (BAFI) */
                case 42:
                    robot.RotateLeft(ROTATE_SPEED);
                    if (turnAngle < 5) {
                        mode++;
                        resetClock();
                        robot.MoveStop();
                    }
                    break;

                /* turn right to 90 (BABI) */
                case 43:
                    robot.RotateRight(ROTATE_SPEED);
                    if (turnAngle > 85) {
                        mode++;
                        resetClock();
                        robot.MoveStop();
                    }
                    break;

                /* turn to column FI */
                case 50:
                    if (vuMark == RelicRecoveryVuMark.LEFT){
                        robot.RotateLeft(ROTATE_SPEED);
                        if (turnAngle < -25) {
                            mode++;
                            resetClock();
                            robot.MoveStop();
                        }
                    }
                    if (vuMark == RelicRecoveryVuMark.CENTER){
                        mode++;
                        resetClock();
                        robot.MoveStop();
                    }
                    if (vuMark == RelicRecoveryVuMark.RIGHT){
                        robot.RotateRight(ROTATE_SPEED);
                        if (turnAngle > 25) {
                            mode++;
                            resetClock();
                            robot.MoveStop();
                        }
                    }
                    break;

                /* turn to column RABI */
                case 51:
                    if (vuMark == RelicRecoveryVuMark.LEFT){
                        robot.RotateLeft(ROTATE_SPEED);
                        if (turnAngle < -115) {
                            mode++;
                            resetClock();
                            robot.MoveStop();
                        }
                    }
                    if (vuMark == RelicRecoveryVuMark.CENTER){
                        mode++;
                        resetClock();
                        robot.MoveStop();
                    }
                    if (vuMark == RelicRecoveryVuMark.RIGHT){
                        robot.RotateRight(ROTATE_SPEED);
                        if (turnAngle > -65) {
                            mode++;
                            resetClock();
                            robot.MoveStop();
                        }
                    }
                    break;

                /* turn to column BABI */
                case 52:
                    if (vuMark == RelicRecoveryVuMark.LEFT){
                        robot.RotateLeft(ROTATE_SPEED);
                        if (turnAngle < -65) {
                            mode++;
                            resetClock();
                            robot.MoveStop();
                        }
                    }
                    if (vuMark == RelicRecoveryVuMark.CENTER){
                        mode++;
                        resetClock();
                        robot.MoveStop();
                    }
                    if (vuMark == RelicRecoveryVuMark.RIGHT){
                        robot.RotateRight(ROTATE_SPEED);
                        if (turnAngle > 115) {
                            mode++;
                            resetClock();
                            robot.MoveStop();
                        }
                    }
                    break;

                /* move forward 12/8/12 inches into cryptobox */
                case 6:
                    robot.MoveForward(MOVE_SPEED);
                    if (vuMark == RelicRecoveryVuMark.CENTER){
                        if (now > 0.35) {
                            mode++;
                            resetClock();
                            robot.MoveStop();
                        }
                    }
                    else if (now > 0.45) {
                        mode++;
                        resetClock();
                        robot.MoveStop();
                    }
                    break;

                /* move backward 12/8/12 inches */
                case 7:
                    robot.MoveBackward(MOVE_SPEED);
                    if (vuMark == RelicRecoveryVuMark.CENTER){
                        if (now > 0.3) {
                            mode++;
                            resetClock();
                            robot.MoveStop();
                        }
                    }
                    else if (now > 0.45) {
                        mode++;
                        resetClock();
                        robot.MoveStop();
                    }
                    break;

                /* turn to 180 (FI) */
                case 80:
                    robot.RotateRight(ROTATE_SPEED);
                    if (turnAngle > 175) {
                        mode++;
                        resetClock();
                        robot.MoveStop();
                    }
                    break;

                /* turn left to 140 (RABI) */
                case 81:
                    robot.RotateRight(ROTATE_SPEED);
                    if (turnAngle > 135) {
                        mode++;
                        resetClock();
                        robot.MoveStop();
                    }
                    break;

                /* turn right to -140 (BABI) */
                case 82:
                    robot.RotateLeft(ROTATE_SPEED);
                    if (turnAngle < -135) {
                        mode++;
                        resetClock();
                        robot.MoveStop();
                    }
                    break;

            }  // end of switch

            robot.UpperArm.Update(this);
        }
    }

    String format(OpenGLMatrix transformationMatrix) {
        return (transformationMatrix != null) ? transformationMatrix.formatAsTransform() : "null";
    }
}