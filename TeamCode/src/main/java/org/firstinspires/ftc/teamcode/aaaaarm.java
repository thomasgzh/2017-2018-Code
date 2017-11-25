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
@TeleOp(name="Drive: Armtest", group="Drive")
public class aaaaarm extends LinearOpMode {

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

        //let the robot have a little rest, sleep is healthy
        sleep(40);

        double wantedpos = 0;
        double currentpos = 0;
        double movementtime = 0;
        double slowness1 = 0;

        waitForStart();

        //telling the code to run until you press that giant STOP button on RC
        while (opModeIsActive()) {
            slowness1 = ((wantedpos - currentpos) / movementtime);
            if (currentpos != wantedpos) {
                //every 1 sec
                robot.ball_servo.setPosition(currentpos + slowness1);
                TimeUnit.SECONDS.sleep(1);
                movementtime -= 1;

            }

            if (egamepad1.x.released){
                wantedpos = 142;
                movementtime = 5;
            }
            if (egamepad1.y.released){
                wantedpos = 100;
                movementtime = 10;
            }

        }
    }
}








