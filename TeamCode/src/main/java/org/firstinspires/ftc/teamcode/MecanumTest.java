package org.firstinspires.ftc.teamcode;
/* version history 2.0
     -10/21/17 (1.0) working and good
     -10/23/17 (1.3) adding speed changing by lbumper/ltrigger
     -10/30/17 (1.5) dpad control
 */


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


//naming the teleop thing
@TeleOp(name="Drive: org.firstinspires.ftc.teamcode.DriveCode.MecanumTest", group="Drive")
public class MecanumTest extends LinearOpMode {

    RobotConfig robot  = new RobotConfig();

    /* Declare extended gamepad */
    GamepadEdge egamepad1;
    GamepadEdge egamepad2;


    @Override
    public void runOpMode() throws InterruptedException {
        //declaring all my variables in one place for my sake
        double front_right;
        double front_left;
        double back_left;
        double back_right;
        double speed = 1.5;


        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */

        robot.init(hardwareMap);

        /* Instantiate extended gamepad */
        egamepad1 = new GamepadEdge(gamepad1);
        egamepad2 = new GamepadEdge(gamepad2);

        boolean ltrigger;
        boolean rtrigger;
        boolean updpad;
        boolean downdpad;
        boolean leftdpad;
        boolean rightdpad;
        boolean abutton;
        double reverse = 1;


        //waits for that giant PLAY button to be pressed on RC
        waitForStart();

        //telling the code to run until you press that giant STOP button on RC
        while (opModeIsActive()) {
            //and now, the fun stuff

            ltrigger = gamepad1.left_trigger > 0.7;
            rtrigger = gamepad1.right_trigger > 0.7;
            updpad = gamepad1.dpad_up;
            downdpad = gamepad1.dpad_down;
            leftdpad = gamepad1.dpad_left;
            rightdpad = gamepad1.dpad_right;
            abutton = egamepad1.a.released;

            /* Update extended gamepad */
            egamepad1.UpdateEdge();
            egamepad2.UpdateEdge();

            //adds a lil' version thing to the telemetry so you know you're using the right version
            telemetry.addData("Version", "1.5, The Dpad Update");
            telemetry.addData("Speed", speed);
            telemetry.addData("x", "d");
            telemetry.addData("ltriggervar", ltrigger);
            telemetry.addData("rtriggervar", rtrigger);
            telemetry.addData("ltrigger", gamepad1.left_trigger);
            telemetry.addData("rtrigger", gamepad1.right_trigger);
            telemetry.update();

            if (abutton){
             reverse *= -1;
            }


            // using the right joystick's x axis to rotate left and right
            front_right = -gamepad1.right_stick_x;
            front_left = gamepad1.right_stick_x;
            back_left = gamepad1.right_stick_x;
            back_right = -gamepad1.right_stick_x;

            // using the left joystick's y axis to move forward and backwards
            front_right += -gamepad1.left_stick_y;
            front_left += -gamepad1.left_stick_y;
            back_left += -gamepad1.left_stick_y;
            back_right += -gamepad1.left_stick_y;

            // using the left joystick's x axis to strafe left and right
            front_right += gamepad1.left_stick_x;
            front_left += -gamepad1.left_stick_x;
            back_left += gamepad1.left_stick_x;
            back_right += -gamepad1.left_stick_x;

            //takes all those values, divides by three, and tells the motors to use that power
            robot.FR.setPower(front_right / 3 * speed * reverse);
            robot.FL.setPower(front_left / 3 * speed * reverse);
            robot.BL.setPower(back_left / 3 * speed * reverse);
            robot.BR.setPower(back_right / 3 * speed * reverse);

            if (updpad) {
                robot.FR.setPower(speed);
                robot.FL.setPower(speed);
                robot.BL.setPower(speed);
                robot.BR.setPower(speed);

            }
            if (downdpad) {
                robot.FR.setPower(-speed);
                robot.FL.setPower(-speed);
                robot.BL.setPower(-speed);
                robot.BR.setPower(-speed);

            }
            if (rightdpad) {
                robot.FR.setPower(speed);
                robot.FL.setPower(-speed);
                robot.BL.setPower(speed);
                robot.BR.setPower(-speed);

            }
            if (leftdpad) {
                robot.FR.setPower(-speed);
                robot.FL.setPower(speed);
                robot.BL.setPower(-speed);
                robot.BR.setPower(speed);

            }
            //change that speed by those bumpers


            if (ltrigger) {
                speed += 0.25;
            }
            if (speed < 0) {
                speed = 0;
            }
            if (rtrigger) {
                speed -= 0.25;
            }
            if (speed > 5){
                speed = 5;
        }
            //let the robot have a little rest, sleep is healthy
            sleep(40);
        }
    }


}


