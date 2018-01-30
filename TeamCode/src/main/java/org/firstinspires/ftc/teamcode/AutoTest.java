package org.firstinspires.ftc.teamcode;
/* version history 1.0
     -1/29/18 created from blue autonomous
            make autonomous moves into methods
            get rid of loop with switch and change to linear steps
 */


import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

//naming the teleop thing
@Autonomous(name="Auto Test", group ="Test")
public class AutoTest extends LinearOpMode {
    VuforiaLocalizer vuforia;
    RobotConfig robot = new RobotConfig();
    private ElapsedTime runtime = new ElapsedTime();

    /* move speeds */
    final double    MOVE_SPEED = 0.5;
    final double    STRAFE_SPEED = 0.5;
    final double    ROTATE_SPEED = 0.5;

    /* VuMark variable */
    RelicRecoveryVuMark vuMark;

    /* IMU objects */
    BNO055IMU imu;
    Orientation angles;

    @Override
    public void runOpMode() throws InterruptedException {
        //declaring all my variables in one place for my sake

        // Send telemetry message to signify robot waiting;
        telemetry.addLine("Auto Test");    //

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

        AutoFindVuMark(5.0);                AutoDelaySec(1.0);
        AutoGlyphGrab(1.0);                 AutoDelaySec(1.0);
        AutoArmLift(2.0);                   AutoDelaySec(1.0);
        AutoArmHome(2.0);                   AutoDelaySec(1.0);
        AutoMoveBackward(MOVE_SPEED,2.0);   AutoDelaySec(1.0);
        AutoRotateAngle(ROTATE_SPEED,45);     AutoDelaySec(1.0);
        AutoMoveForward(MOVE_SPEED,2.0);    AutoDelaySec(1.0);
        AutoRotateAngle(ROTATE_SPEED,-45);     AutoDelaySec(1.0);

        if (vuMark == RelicRecoveryVuMark.LEFT){
            AutoMoveLeft(STRAFE_SPEED, 0.5);
        }
        if (vuMark == RelicRecoveryVuMark.RIGHT){
            AutoMoveRight(STRAFE_SPEED, 0.5);
        }
        AutoDelaySec(1.0);

        AutoMoveForward(MOVE_SPEED,0.5);    AutoDelaySec(1.0);
        AutoGlyphRelease(1.0);              AutoDelaySec(1.0);

    }

    void AutoFindVuMark(double time_sec) {
        if ( !opModeIsActive() ) return;

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters vuforia_parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        vuforia_parameters.vuforiaLicenseKey = "AQepDXf/////AAAAGcvzfI2nd0MHnzIGZ7JtquJk3Yx64l7jwu6XImRkNmBkhjVdVcI47QZ7xQq0PvugAb3+ppJxL4n+pNcnt1+PYpQHVETBEPk5WkofitFuYL8zzXEbs7uLY0dMUepnOiJcLSiVISKWWDyc8BJkKcK3a/KmB2sHaE1Lp2LJ+skW43+pYeqtgJE8o8xStowxPJB0OaSFXXw5dGUurK+ykmPam5oE+t+6hi9o/pO1EOHZFoqKl6tj/wsdu9+3I4lqGMsRutKH6s1rKLfip8s3MdlxqnlRKFmMDFewprELOwm+zpjmrJ1cqdlzzWQ6i/EMOzhcOzrPmH3JiH4CocA/Kcck12IuqvN4l6iAIjntb8b0G8zL";
        vuforia_parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK  ;
        this.vuforia = ClassFactory.createVuforiaLocalizer(vuforia_parameters);
        VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        VuforiaTrackable relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate"); // can help in debugging; otherwise not necessary
        relicTrackables.activate();

        vuMark = RelicRecoveryVuMark.from(relicTemplate);

        double timeStart = 0;
        double timeNow = 0;
        timeStart = runtime.seconds();
        do {
            AutoUpdate();
            timeNow = runtime.seconds() - timeStart;
        } while ( ((timeNow-timeStart) < time_sec) && opModeIsActive() && (vuMark==RelicRecoveryVuMark.UNKNOWN));

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
        telemetry.update();
    }

    void AutoRotateAngle(double speed, double target) {
        if ( !opModeIsActive() ) return;

        double startAngle;
        double turnAngle;

        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        startAngle = angles.firstAngle;

        if (target>0) {
            robot.RotateRight(speed);
            do {
                AutoUpdate();
                turnAngle = -(angles.firstAngle-startAngle);
                if (turnAngle>180) turnAngle -= 360;
                if (turnAngle<-180) turnAngle += 360;
            } while ( (turnAngle < target) && opModeIsActive() );
        }
        if (target<0) {
            robot.RotateLeft(speed);
            do {
                AutoUpdate();
                turnAngle = -(angles.firstAngle-startAngle);
                if (turnAngle>180) turnAngle -= 360;
                if (turnAngle<-180) turnAngle += 360;
            } while ( (turnAngle > target) && opModeIsActive() );
        }
        robot.MoveStop();
    }

    void AutoRotateRight(double speed, double time_sec) {
        if ( !opModeIsActive() ) return;
        robot.RotateRight(speed);
        AutoDelaySec(time_sec);
        robot.MoveStop();
    }

    void AutoRotateLeft(double speed, double time_sec) {
        if ( !opModeIsActive() ) return;
        robot.RotateLeft(speed);
        AutoDelaySec(time_sec);
        robot.MoveStop();
    }

    void AutoMoveForward(double speed, double time_sec) {
        if ( !opModeIsActive() ) return;
        robot.MoveForward(speed);
        AutoDelaySec(time_sec);
        robot.MoveStop();
    }

    void AutoMoveBackward(double speed, double time_sec) {
        if ( !opModeIsActive() ) return;
        robot.MoveBackward(speed);
        AutoDelaySec(time_sec);
        robot.MoveStop();
    }

    void AutoMoveLeft(double speed, double time_sec) {
        if ( !opModeIsActive() ) return;
        robot.MoveLeft(speed);
        AutoDelaySec(time_sec);
        robot.MoveStop();
    }

    void AutoMoveRight(double speed, double time_sec) {
        if ( !opModeIsActive() ) return;
        robot.MoveRight(speed);
        AutoDelaySec(time_sec);
        robot.MoveStop();
    }

    void AutoGlyphRelease(double time_sec) {
        if ( !opModeIsActive() ) return;
        robot.GGL.setPosition(robot.GRABBER_LEFT[0]);
        robot.GGR.setPosition(robot.GRABBER_RIGHT[0]);
        AutoDelaySec(time_sec);
        robot.MoveStop();
    }

    void AutoGlyphGrab(double time_sec) {
        if ( !opModeIsActive() ) return;
        robot.GGL.setPosition(robot.GRABBER_LEFT[1]);
        robot.GGR.setPosition(robot.GRABBER_RIGHT[1]);
        AutoDelaySec(time_sec);
    }

    void AutoArmLift(double time_sec) {
        if ( !opModeIsActive() ) return;
        robot.Arm.MoveToPosition(0.2);
        AutoDelaySec(time_sec);
    }

    void AutoArmHome(double time_sec) {
        if ( !opModeIsActive() ) return;
        robot.Arm.MoveHome();

        double timeStart = 0;
        double timeNow = 0;
        timeStart = runtime.seconds();
        do {
            AutoUpdate();
            timeNow = runtime.seconds() - timeStart;
        } while ( ((timeNow-timeStart) < time_sec) && opModeIsActive() && (robot.ArmSwitch.getState()==true));
    }

    void AutoDelaySec(double time_sec) {
        if ( !opModeIsActive() ) return;

        double timeStart = 0;
        double timeNow = 0;
        timeStart = runtime.seconds();
        do {
            AutoUpdate();
            timeNow = runtime.seconds() - timeStart;
        } while ( ((timeNow-timeStart) < time_sec) && opModeIsActive() );
    }

    void AutoUpdate() {
        if ( !opModeIsActive() ) return;

        robot.Arm.Update(this);
        sleep(40);
    }
}
