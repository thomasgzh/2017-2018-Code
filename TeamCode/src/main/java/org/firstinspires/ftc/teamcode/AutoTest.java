package org.firstinspires.ftc.teamcode;
/* version history 2.0
     -11/06/17 created autonomous practice
               add methods for basic movement (forward/backward, left/right, rotate left/right)
 */


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;


//naming the teleop thing
@Autonomous(name="AutoTest", group ="Practice")
public class AutoTest extends LinearOpMode {

    RobotConfig robot = new RobotConfig();
    private ElapsedTime runtime = new ElapsedTime();


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

        //waits for that giant PLAY button to be pressed on RC
        waitForStart();

        // telling the code to run until you press that giant STOP button on RC
        // include opModeIsActive in all while loops so that STOP button terminates all actions
        while (opModeIsActive()) {
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

        }
    }
}