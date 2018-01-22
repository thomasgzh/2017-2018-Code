package org.firstinspires.ftc.teamcode;
/* version history 2.0
     -10/21/17 (1.0) working and good
     -10/23/17 (1.3) adding speed changing by lbumper/ltrigger
     -10/30/17 (1.5) dpad control
 */


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

//naming the teleop thing
@TeleOp(name="TeleOp", group="Drive")
public class teleop extends LinearOpMode {

    RobotConfig robot = new RobotConfig();

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
        double speed = 2.5;
        int grabber_left;
        int grabber_right;
        double upper_arm;

        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */

        robot.init(hardwareMap);

        /* Instantiate extended gamepad */
        egamepad1 = new GamepadEdge(gamepad1);
        egamepad2 = new GamepadEdge(gamepad2);

        boolean updpad;
        boolean downdpad;
        boolean leftdpad;
        boolean rightdpad;
        double reverse = 1;
        grabber_left = 0;
        grabber_right = 0;
        //waits for that giant PLAY button to be pressed on RC
        waitForStart();

        //telling the code to run until you press that giant STOP button on RC
        while (opModeIsActive()) {
            //and now, the fun stuff

                /* Update extended gamepad */
            egamepad1.UpdateEdge();
            egamepad2.UpdateEdge();

            //DpadDirection dpadDirection = GetDpadDirection(gamepad1);

            boolean abutton = egamepad1.a.released;

            //adds a lil' version thing to the telemetry so you know you're using the right version
            telemetry.addData("Version", "2.2");
            telemetry.addData("BRmotor", robot.BR.getPower());
            telemetry.addData("BLmotor", robot.BL.getPower());
            telemetry.addData("FLmotor", robot.FL.getPower());
            telemetry.addData("FRmotor", robot.FR.getPower());
            telemetry.addData("Speed", speed);
            telemetry.update();

            //when a button is just released, multiply the speed by -1 so it's reverse
            if (abutton) {
                reverse *= -1;
            }

            //change that speed by those bumpers
            if (egamepad1.right_bumper.released) {
                speed += 0.25;
            }
            if (egamepad1.left_bumper.released) {
                speed -= 0.25;
            }
            //if the speed is at the min/max value set it to NOT min/max so boom it cant go over
            if (speed < 0) {
                speed = 0;
            }
            if (speed > 3) {
                speed = 3;
            }

            // using the right joystick's x axis to rotate left and right
            front_right = -gamepad1.right_stick_x * 2;
            front_left = gamepad1.right_stick_x * 2;
            back_left = gamepad1.right_stick_x * 2;
            back_right = -gamepad1.right_stick_x * 2;

            // using the left joystick's y axis to move forward and backwards
            front_right -= gamepad1.left_stick_y;
            front_left -= gamepad1.left_stick_y;
            back_left -= gamepad1.left_stick_y;
            back_right -= gamepad1.left_stick_y;

            // using the left joystick's x axis to strafe left and right
            front_right += -gamepad1.left_stick_x * 2;
            front_left += gamepad1.left_stick_x * 2;
            back_left += -gamepad1.left_stick_x * 2;
            back_right += gamepad1.left_stick_x * 2;

            front_right = front_right / 3.414 * speed * reverse;
            front_left = front_left / 3.414 * speed * reverse;
            back_left = back_left / 3.414 * speed * reverse;
            back_right = back_right / 3.414 * speed * reverse;

            if (gamepad1.dpad_left){
                robot.MoveLeft(speed);
            } else if (gamepad1.dpad_right){
                robot.MoveRight(speed);
            } else if (gamepad1.dpad_up){
                robot.MoveForward(speed);
            } else if (gamepad1.dpad_down){
                robot.MoveBackward(speed);
            } else {
                //takes all those values, divides
                robot.FR.setPower(front_right);
                robot.FL.setPower(front_left);
                robot.BL.setPower(back_left);
                robot.BR.setPower(back_right);
            }

            /*for later- joysticks have a max input of 1 or -1. divide it by 3,
              which leaves us with a max input of 0.333333. motors have a max input
               of one. i'm not quite sure if this is perfectly true because i havent tested,
               but that should allow us to have a max speed var of 3. if you were to
               have max inputs on everything, you'd have 1 / 3 * 1 * 1, which
               equals 0.33. so the max speed should be set to 3, leaving us with
               1 / 3 * 3 * 1, equaling out to 1, our max value.
            */

            /********** GamePad2 controls **********/
            /********** Grabber code **********/
            if (egamepad2.left_bumper.released) {
                grabber_left = (grabber_left<2) ? grabber_left+1 : 0;
            }
            if (egamepad2.right_bumper.released) {
                grabber_right = (grabber_right<2) ? grabber_right+1 : 0;
            }
            if (egamepad2.b.released){
                grabber_left = 0;
                grabber_right = 0;
            }
            if (egamepad2.x.released) {
                grabber_left = 1;
                grabber_right = 1;
            }
            robot.GGL.setPosition(robot.GRABBER_LEFT[grabber_left]);
            robot.GGR.setPosition(robot.GRABBER_RIGHT[grabber_right]);

            /********** Arm code **********/
            upper_arm = 0.0;
            if (gamepad2.dpad_up) {
                upper_arm = 0.2;
            } else {
                if (gamepad2.dpad_down) {
                    upper_arm = -0.2;
                }
            }
            if (robot.ArmSwitch.getState()==false) {
                if (upper_arm < 0.0) upper_arm = 0.0;
            }
            robot.UR.setPower(upper_arm);
            robot.UL.setPower(upper_arm);

            //let the robot have a little rest, sleep is healthy
            sleep(40);
        }
    }
}



    // Movement code
    /*private void Move(DpadDirection buttonDirection, double speed) {
        switch (buttonDirection) {
            case Up:
                robot.FR.setPower(speed);
                robot.FL.setPower(speed);
                robot.BL.setPower(speed);
                robot.BR.setPower(speed);
                break;
            case Down:
                robot.FR.setPower(-speed);
                robot.FL.setPower(-speed);
                robot.BL.setPower(-speed);
                robot.BR.setPower(-speed);
                break;
            case Left:
                robot.FR.setPower(speed);
                robot.FL.setPower(-speed);
                robot.BL.setPower(speed);
                robot.BR.setPower(-speed);
                break;
            case Right:
                robot.FR.setPower(-speed);
                robot.FL.setPower(speed);
                robot.BL.setPower(-speed);
                robot.BR.setPower(speed);
                break;
            case None:
                // do nothing
                break;
        }
    }

    private DpadDirection GetDpadDirection(Gamepad gamepad) {
        if (gamepad.dpad_up) {
            return DpadDirection.Up;
        } else if (gamepad.dpad_down) {
            return DpadDirection.Down;
        } else if (gamepad.dpad_left) {
            return DpadDirection.Left;
        } else if (gamepad.dpad_right) {
            return DpadDirection.Right;
        } else {
            return DpadDirection.None;
        }
    }

    private enum DpadDirection {
        None,
        Up,
        Down,
        Left,
        Right
    }
} */


