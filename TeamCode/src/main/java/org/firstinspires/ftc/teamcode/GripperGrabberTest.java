package org.firstinspires.ftc.teamcode;

/**
 * Created by Bear on 12/16/2017.
 */

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

@TeleOp(name="GripperGrabberTest", group="Drive")

public class GripperGrabberTest extends LinearOpMode {

    RobotConfig robot = new RobotConfig();

//    Gamepad gamepad1;
    Gamepad gamepad2;

    @Override
    public void runOpMode () throws InterruptedException {
        robot.init(hardwareMap);

        waitForStart();

        while (opModeIsActive()) {
            robot.init(hardwareMap);

            waitForStart();

            while (opModeIsActive()) {
                telemetry.addData("GGR", "robot.GGR.getPosition()");
                telemetry.addData("GGL", robot.GGL.getPosition());
                telemetry.update();

                double increment = 0.005;

                if (gamepad1.a) {
                    robot.GGL.setPosition(0.4);
                }
            }













        }





    }
}
