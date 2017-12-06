package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by speer on 12/2/2017.
 */




import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

    //naming the teleop thing
    @TeleOp(name="telemetry", group="Drive")
    public class telemetry extends LinearOpMode {

        RobotConfig robot = new RobotConfig();
        GamepadEdge egamepad1;
        GamepadEdge egamepad2;
        @Override
        public void runOpMode() throws InterruptedException {
            /* Instantiate extended gamepad */
            egamepad1 = new GamepadEdge(gamepad1);
            egamepad2 = new GamepadEdge(gamepad2);

            robot.init(hardwareMap);
            //waits for that giant PLAY button to be pressed on RC
            waitForStart();

            //telling the code to run until you press that giant STOP button on RC
            while (opModeIsActive()) {
                telemetry.addData("GGR", robot.GGR.getPosition());
                telemetry.addData("GGL", robot.GGL.getPosition());
                telemetry.update();
                    /* Update extended gamepad */
                egamepad1.UpdateEdge();
                egamepad2.UpdateEdge();

                if (egamepad1.a.released){
                    robot.GGL.setPosition(.4);
                    robot.GGR.setPosition(.4);
                }
                if (egamepad1.b.released){
                    robot.GGL.setPosition(.112);
                    robot.GGR.setPosition(.078
                    );
                }

            }
        }
    }