package org.firstinspires.ftc.teamcode;
//10/21/17 working and good

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcontroller.external.samples.HardwareK9bot;
import org.firstinspires.ftc.teamcode.RobotConfig;

//naming the teleop thing
@TeleOp(name="Drive: MecanumTest", group="Drive")
public class MecanumTest extends LinearOpMode {

    RobotConfig robot  = new RobotConfig();

    @Override
    public void runOpMode() throws InterruptedException {
        //declares double values for the variables the joysticks will change and the motors will use
        double front_right = 0.0;
        double front_left = 0.0;
        double back_left = 0.0;
        double back_right = 0.0;

        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */

        robot.init(hardwareMap);
        //waits for that giant PLAY button to be pressed on RC
        waitForStart();

        //telling the code to run until you press that giant STOP button on RC
        while (opModeIsActive()) {
            //and now, the fun stuff

            //adds a lil' version thing to the telemetry so you know you're using the right version
            telemetry.addData("Version","2");
            telemetry.update();

            // using the right joystick's x axis to rotate left and right
            front_right = -gamepad1.right_stick_x;
            front_left = gamepad1.right_stick_x;
            back_left = gamepad1.right_stick_x;
            back_right = -gamepad1.right_stick_x;

            // using the left joystick's y axis to move forward and backwards
            front_right += -gamepad1.left_stick_y;
            front_left  += -gamepad1.left_stick_y;
            back_left   += -gamepad1.left_stick_y;
            back_right  += -gamepad1.left_stick_y;

            // using the left joystick's x axis to strafe left and right
            front_right += gamepad1.left_stick_x;
            front_left  += -gamepad1.left_stick_x;
            back_left   += gamepad1.left_stick_x;
            back_right  += -gamepad1.left_stick_x;

            //takes all those values, divides by three, and tells the motors to use that power
            robot.FR.setPower( front_right /3);
            robot.FL.setPower( front_left /3);
            robot.BL.setPower( back_left /3);
            robot.BR.setPower( back_right /3);

            //let the robot have a little rest, sleep is healthy
            sleep(40);
        }
    }


}


