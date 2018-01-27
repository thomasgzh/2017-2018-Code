package org.firstinspires.ftc.teamcode;
/* version history 2.0
     -11/06/17 created autonomous practice
               add methods for basic movement (forward/backward, left/right, rotate left/right)
 */


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
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
@Autonomous(name="Blue Auto", group ="Drive")
public class BlueAuto extends LinearOpMode {
    OpenGLMatrix lastLocation = null;
    VuforiaLocalizer vuforia;
    RobotConfig robot = new RobotConfig();
    private ElapsedTime runtime = new ElapsedTime();


    //clock reseter
    public void resetClock() {
        lastReset = runtime.seconds();
    }

    //mode 'stuff'
    //modes lists which steps and in what order to accomplish them
    int mode = 0;
    int [] modes = {2, 3, 7, 8, 9, 11, 12, 13, 100};

    //time based variables
    double lastReset = 0;
    double now = 0;

    //servo initialization
    //servo1
    //servo2

    @Override
    public void runOpMode() throws InterruptedException {
        //declaring all my variables in one place for my sake
        final double     MOVE_SPEED = 0.5;
        final double     ROTATE_SPEED = 0.3;
        final double     TEST_TIME = 2.0;

        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Wait for start");    //
        telemetry.update();
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

        parameters.vuforiaLicenseKey = "AQepDXf/////AAAAGcvzfI2nd0MHnzIGZ7JtquJk3Yx64l7jwu6XImRkNmBkhjVdVcI47QZ7xQq0PvugAb3+ppJxL4n+pNcnt1+PYpQHVETBEPk5WkofitFuYL8zzXEbs7uLY0dMUepnOiJcLSiVISKWWDyc8BJkKcK3a/KmB2sHaE1Lp2LJ+skW43+pYeqtgJE8o8xStowxPJB0OaSFXXw5dGUurK+ykmPam5oE+t+6hi9o/pO1EOHZFoqKl6tj/wsdu9+3I4lqGMsRutKH6s1rKLfip8s3MdlxqnlRKFmMDFewprELOwm+zpjmrJ1cqdlzzWQ6i/EMOzhcOzrPmH3JiH4CocA/Kcck12IuqvN4l6iAIjntb8b0G8zL";
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK  ;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);
        VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        VuforiaTrackable relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate"); // can help in debugging; otherwise not necessary

        telemetry.addData(">", "Press Play to start");
        telemetry.update();
        //waits for that giant PLAY button to be pressed on RC
        waitForStart();
        relicTrackables.activate();
        resetClock();

        // telling the code to run until you press that giant STOP button on RC
        // include opModeIsActive in all while loops so that STOP button terminates all actions
        while (opModeIsActive() && modes[mode] < 100) {
            RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);
            if (vuMark != RelicRecoveryVuMark.UNKNOWN) {

                /* Found an instance of the template. In the actual game, you will probably
                 * loop until this condition occurs, then move on to act accordingly depending
                 * on which VuMark was visible. */
                telemetry.addData("VuMark", "%s visible", vuMark);

                /* For fun, we also exhibit the navigational pose. In the Relic Recovery game,
                 * it is perhaps unlikely that you will actually need to act on this pose information, but
                 * we illustrate it nevertheless, for completeness. */
                OpenGLMatrix pose = ((VuforiaTrackableDefaultListener)relicTemplate.getListener()).getPose();
                telemetry.addData("Pose", format(pose));
                /* We further illustrate how to decompose the pose into useful rotational and
                 * translational components */
                if (pose != null) {
                    VectorF trans = pose.getTranslation();
                    Orientation rot = Orientation.getOrientation(pose, AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);

                    // Extract the X, Y, and Z components of the offset of the target relative to the robot
                    double tX = trans.get(0);
                    double tY = trans.get(1);
                    double tZ = trans.get(2);

                    // Extract the rotational components of the target relative to the robot
                    double rX = rot.firstAngle;
                    double rY = rot.secondAngle;
                    double rZ = rot.thirdAngle;
                }
            }
            else {
                telemetry.addData("VuMark", "not visible");
            }

            telemetry.update();

            //keeps now up to date
            now = runtime.seconds() - lastReset;

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

            /*
            //and now, the fun stuff

            // Test forwards movement
            robot.MoveForward(MOVE_SPEED);
            runtime.reset();
            while (opModeIsActive() && (runtime.seconds() < TEST_TIME)) {
                telemetry.addData("Move Forward", "Leg 1: %2.5f S Elapsed", runtime.seconds());
                telemetry.update();
            }
            robot.MoveStop();

            // Test backwards movement
            robot.MoveBackward(MOVE_SPEED);
            runtime.reset();
            while (opModeIsActive() && (runtime.seconds() < TEST_TIME)) {
                telemetry.addData("Move Backward", "Leg 1: %2.5f S Elapsed", runtime.seconds());
                telemetry.update();
            }
            robot.MoveStop();

            // Test left movement
            robot.MoveLeft(MOVE_SPEED);
            runtime.reset();
            while (opModeIsActive() && (runtime.seconds() < TEST_TIME)) {
                telemetry.addData("Move Left", "Leg 1: %2.5f S Elapsed", runtime.seconds());
                telemetry.update();
            }
            robot.MoveStop();

            // Test right movement
            robot.MoveRight(MOVE_SPEED);
            runtime.reset();
            while (opModeIsActive() && (runtime.seconds() < TEST_TIME)) {
                telemetry.addData("Move Right", "Leg 1: %2.5f S Elapsed", runtime.seconds());
                telemetry.update();
            }
            robot.MoveStop();

            // Test rotate left
            robot.RotateLeft(ROTATE_SPEED);
            runtime.reset();
            while (opModeIsActive() && (runtime.seconds() < TEST_TIME)) {
                telemetry.addData("Rotate Left", "Leg 1: %2.5f S Elapsed", runtime.seconds());
                telemetry.update();
            }
            robot.MoveStop();

            // Test rotate right
            robot.RotateRight(ROTATE_SPEED);
            runtime.reset();
            while (opModeIsActive() && (runtime.seconds() < TEST_TIME)) {
                telemetry.addData("Rotate Right", "Leg 1: %2.5f S Elapsed", runtime.seconds());
                telemetry.update();
            }
            robot.MoveStop();

            // Send telemetry message to signify robot waiting;
            telemetry.addData("Status", "Wait for end of autonomous");    //
            telemetry.update();
            while (opModeIsActive()) {
            }
*/





        }
    }

    String format(OpenGLMatrix transformationMatrix) {
        return (transformationMatrix != null) ? transformationMatrix.formatAsTransform() : "null";
    }
}
