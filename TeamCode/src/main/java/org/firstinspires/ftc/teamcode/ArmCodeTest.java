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
        telemetry.addData("Version", "Dual 1.0");
        telemetry.addLine("gamepad 1 - lower");
        telemetry.addLine("gamepad 2 - upper");
        telemetry.addLine("x - up, y - down");
        telemetry.addLine("dpad - set positions");
        telemetry.update();

        //waits for that giant PLAY button to be pressed on RC
        waitForStart();

        //telling the code to run until you press that giant STOP button on RC
        while (opModeIsActive()) {
            //and now, the fun stuff

            /* Update extended gamepad */
            egamepad1.UpdateEdge();
            egamepad2.UpdateEdge();

            /* TeleOp code */
            if (egamepad1.dpad_down.pressed) {
                robot.LowerArm.MoveHome();
            }
            if (egamepad1.dpad_left.pressed) {
                robot.LowerArm.MoveToPosition(0.20);
            }
            if (egamepad1.dpad_right.pressed) {
                robot.LowerArm.MoveToPosition(0.30);
            }
            if (egamepad1.dpad_up.pressed) {
                robot.LowerArm.MoveToPosition(0.40);
            }
            if (egamepad1.x.pressed) {
                robot.LowerArm.MoveUp();
            }
            if (egamepad1.y.pressed) {
                robot.LowerArm.MoveDown();
            }

            if (egamepad2.dpad_down.pressed) {
                robot.UpperArm.MoveHome();
            }
            if (egamepad2.dpad_left.pressed) {
                robot.UpperArm.MoveToPosition(0.20);
            }
            if (egamepad2.dpad_right.pressed) {
                robot.UpperArm.MoveToPosition(0.30);
            }
            if (egamepad2.dpad_up.pressed) {
                robot.UpperArm.MoveToPosition(0.40);
            }
            if (egamepad2.x.pressed) {
                robot.UpperArm.MoveUp();
            }
            if (egamepad2.y.pressed) {
                robot.UpperArm.MoveDown();
            }

            robot.LowerArm.Update(this);
            robot.UpperArm.Update(this);

            telemetry.update();

            //let the robot have a little rest, sleep is healthy
            sleep(40);
        }
    }
}
