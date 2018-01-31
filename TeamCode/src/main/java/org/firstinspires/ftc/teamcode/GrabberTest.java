package org.firstinspires.ftc.teamcode;
/* version history 2.0
     -10/21/17 (1.0) working and good
     -10/23/17 (1.3) adding speed changing by lbumper/ltrigger
     -10/30/17 (1.5) dpad control
 */


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.concurrent.TimeUnit;


//naming the teleop thing
@TeleOp(name="Grabber Test", group="Test")
public class GrabberTest extends LinearOpMode {

    RobotConfig robot = new RobotConfig();

    /* Declare extended gamepad */
    GamepadEdge egamepad1;
    GamepadEdge egamepad2;

    @Override
    public void runOpMode() throws InterruptedException {

        robot.init(hardwareMap);

        /* Instantiate extended gamepad */
        egamepad1 = new GamepadEdge(gamepad1);
        egamepad2 = new GamepadEdge(gamepad2);

        double increment = 0.005;
                waitForStart();

        //telling the code to run until you press that giant STOP button on RC
        while (opModeIsActive()) {
            telemetry.addData("GGR", robot.GGR.getPosition());
            telemetry.addData("GGL", robot.GGL.getPosition());
            telemetry.addData("Claw", robot.Claw.getPosition());
            telemetry.update();

            egamepad1.UpdateEdge();
            egamepad2.UpdateEdge();


            //increments -------------------------------------------------------------

            if (egamepad1.dpad_down.pressed) {
                robot.GGL.setPosition(robot.GGL.getPosition() - increment);
            }

            if (egamepad1.dpad_up.pressed) {
                robot.GGL.setPosition(robot.GGL.getPosition() + increment);
            }

            if (egamepad1.dpad_left.pressed) {
                robot.GGR.setPosition(robot.GGR.getPosition() - increment);
            }

            if (egamepad1.dpad_right.pressed) {
                robot.GGR.setPosition(robot.GGR.getPosition() + increment);
            }
            if (egamepad1.y.pressed) {
                robot.Claw.setPosition(robot.Claw.getPosition() + increment);

            }
            if (egamepad1.a.pressed) {
                robot.Claw.setPosition(robot.Claw.getPosition() - increment);
            }
            //prototype --------------------------------------------------------------
            if (egamepad1.x.released) {
                if (robot.Claw.getPosition() > 0.5) {
                    robot.Claw.setPosition(robot.CLAW[1]);
                }
                else {
                    robot.Claw.setPosition(robot.CLAW[0]);
                }
            }
            /*Left Trigger = Left Grabber Open
            Left Button = Left Grabber Close
            Right Trigger = Right Grabber Open
            Right Button = Right Grabber Close */

            //let the robot have a little rest, sleep is healthy
            sleep(40);
        }
    }
}