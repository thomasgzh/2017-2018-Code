package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import static java.lang.Math.atan;

@TeleOp(name="MecanumTest2", group= "Drive")



public class Equation extends LinearOpMode {

    RobotConfig robot = new RobotConfig();

    GamepadEdge egamepad1;
    GamepadEdge egamepad2;

    @Override
    public void runOpMode() throws InterruptedException {
        double front_left;
        double front_right;
        double back_left;
        double back_right;
        double speed = 1;

        robot.init(hardwareMap);

        double Vx; fred
        double Vy;
        Math W = atan (Vy / Vx);
        double O;

        front_left = 0;
        front_right = 0;
        back_left = 0;
        back_right = 0;

        Vx = 0;
        Vy = 0;
        W = 0;
        O = 0;

        egamepad1 = new GamepadEdge(gamepad1);
        egamepad2 = new GamepadEdge(gamepad2);

        boolean updpad;
        boolean downdpad;
        boolean leftdpad;
        boolean rightdpad;
        double reverse = 1;

        waitForStart();

        while (opModeIsActive()) {
            updpad = gamepad1.dpad_up;
            downdpad = gamepad1.dpad_down;
            leftdpad = gamepad1.dpad_left;
            rightdpad = gamepad1.dpad_right;
            boolean abutton = egamepad1.a.released;

            egamepad1.UpdateEdge();
            egamepad2.UpdateEdge();

            telemetry.addData("Version", "2.0, aaaaaaaaaa");
            telemetry.addData("Speed", speed);
            telemetry.addData("x", "d");
            telemetry.addData("ltrigger", egamepad1.left_bumper.pressed);
            telemetry.addData("rtrigger", egamepad1.right_bumper.pressed);
            telemetry.addData("BRmotor", robot.BR.getPower());
            telemetry.addData("BLmotor", robot.BL.getPower());
            telemetry.addData("FLmotor", robot.FL.getPower());
            telemetry.addData("FRmotor", robot.FR.getPower());
            telemetry.addData("lbumper", gamepad1.left_bumper);
            telemetry.addData("rbumper", gamepad1.right_bumper);
            telemetry.update();

            W = atan(Vy / Vx);

//gamepad1.x == Vx - W(8.49*gamepad1.y);
//gamepad1.y == Vy - W(8.49*gamepad1.x);

            if (updpad) {
                Vy = 1;
            }

            else if (downdpad) {
                Vy = -1;
            }

            else {
                Vy = 0;
            }

            if (rightdpad) {
                Vx = 1;
            }

            else if (leftdpad) {
                Vx = -1;
            }

            else {
                Vx = 0;
            }

            front_left = Vy + Vx - (12.0*O);
            front_right = Vy - Vx + (12.0*O);
            back_left = Vy - Vx - (12.0*O);
            back_right = Vy + Vx + (12.0*O);

            robot.FR.setPower(front_right);
            robot.FL.setPower(front_left);
            robot.BL.setPower(back_left);
            robot.BR.setPower(back_right);
        }
    }
}

