package org.firstinspires.ftc.teamcode;
/* version history 2.0
     -10/21/17 (1.0) working and good
     -10/23/17 (1.3) adding speed changing by lbumper/ltrigger
     -10/30/17 (1.5) dpad control
 */


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

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

            //prototype --------------------------------------------------------------
            if (gamepad1.left_trigger > 0.7) {
                robot.GGL.setPosition(.34);
            }
                else if (egamepad1.left_bumper.released) {
                    robot.GGL.setPosition(.27944);
                }
                else if (gamepad1.right_trigger > 0.7) {
                    robot.GGR.setPosition(.89);
                }
                 else if (egamepad1.right_bumper.released) {
                    robot.GGR.setPosition(.995);
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