package org.firstinspires.ftc.teamcode;
/* version history
     -1/13/18 created file for testing arm control
     -1/23/18 added potentiometer feedback for upper arm
 */


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

//naming the teleop thing
@TeleOp(name="ArmCode Test", group="Test")
public class ArmCodeTest extends LinearOpMode {

    RobotConfig robot = new RobotConfig();

    /* Declare extended gamepad */
    GamepadEdge egamepad1;
    GamepadEdge egamepad2;

    @Override
    public void runOpMode() throws InterruptedException {

        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);

        /* Instantiate extended gamepad */
        egamepad1 = new GamepadEdge(gamepad1);
        egamepad2 = new GamepadEdge(gamepad2);

        //adds a lil' version thing to the telemetry so you know you're using the right version
        telemetry.addData("Version", "3.0");
        telemetry.addData("Lower arm potentiometer", robot.LowerArmPot);
        telemetry.update();

        //waits for that giant PLAY button to be pressed on RC
        waitForStart();

        //telling the code to run until you press that giant STOP button on RC
        while (opModeIsActive()) {
            //and now, the fun stuff

            /* Update extended gamepad */
            egamepad1.UpdateEdge();
            egamepad2.UpdateEdge();


            if (gamepad2.dpad_down) {
                robot.LR.setPower(-0.1);
                robot.LL.setPower(-0.1);
            }
            else if (gamepad2.dpad_up) {
                robot.LL.setPower(0.1);
                robot.LR.setPower(0.1);
            }
             else {
                robot.LR.setPower(0);
                robot.LL.setPower(0);
            }

            /* TeleOp code
            if (gamepad2.dpad_down) {
                robot.Arm.MoveHome();
            }
            if (gamepad2.dpad_left) {
                robot.Arm.MoveToPosition(0.20);
            }
            if (gamepad2.dpad_right) {
                robot.Arm.MoveToPosition(0.30);
            }
            if (gamepad2.dpad_up) {
                robot.Arm.MoveToPosition(0.40);
            }
            if (gamepad2.x) {
                robot.Arm.MoveUp();
            }
            if (gamepad2.y) {
                robot.Arm.MoveDown();
            } */

            robot.Arm.Update(this);

            telemetry.update();

            //let the robot have a little rest, sleep is healthy
            sleep(40);
        }
    }
}
