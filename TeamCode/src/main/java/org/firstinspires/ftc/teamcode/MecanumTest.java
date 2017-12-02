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
        double speed = 1.5;


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


        //waits for that giant PLAY button to be pressed on RC
        waitForStart();

        //telling the code to run until you press that giant STOP button on RC
        while (opModeIsActive()) {
            //and now, the fun stuff


            updpad = gamepad1.dpad_up;
            downdpad = gamepad1.dpad_down;
            leftdpad = gamepad1.dpad_left;
            rightdpad = gamepad1.dpad_right;
            boolean abutton = egamepad1.a.released;

            /* Update extended gamepad */
            egamepad1.UpdateEdge();
            egamepad2.UpdateEdge();

            //adds a lil' version thing to the telemetry so you know you're using the right version
            telemetry.addData("Version", "2.0, aaaaaaaaaa");
            telemetry.addData("Speed", speed);
            telemetry.addData("x", "d");
            telemetry.addData("ltrigger", gamepad1.left_trigger);
            telemetry.addData("rtrigger", gamepad1.right_trigger);
            telemetry.addLine("color |")
                    .addData("r", robot.color_sensor.red())
                    .addData("g", robot.color_sensor.green())
                    .addData("b", robot.color_sensor.blue());
            telemetry.update();

            //when a button is just released, multiply the speed by -1 so it's reverse
            if (abutton) {
                reverse *= -1;
            }


                if (egamepad1.x.pressed) {
                    if (robot.color_sensor.blue() > robot.color_sensor.red())
                        robot.ball_servo.setPosition(0.3);
                }
                if (egamepad1.x.released) {
                    robot.ball_servo.setPosition(0.0);
                }

                if (egamepad1.y.pressed) {
                    if (robot.color_sensor.red() > robot.color_sensor.blue())
                        robot.ball_servo.setPosition(0.3);
                }
                if (egamepad1.y.released) {
                    robot.ball_servo.setPosition(0.0);
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

            /*for later- joysticks have a max input of 1 or -1. divide it by 3,
              which leaves us with a max input of 0.333333. motors have a max input
               of one. i'm not quite sure if this is perfectly true because i havent tested,
               but that should allow us to have a max speed var of 3. if you were to
               have max inputs on everything, you'd have 1 / 3 * 1 * 1, which
               equals 0.33. so the max speed should be set to 3, leaving us with
               1 / 3 * 3 * 1, equaling out to 1, our max value.
            */


                //directional input with the dpad.
                if (updpad) {
                    robot.FR.setPower(speed);
                    robot.FL.setPower(speed);
                    robot.BL.setPower(speed);
                    robot.BR.setPower(speed);

                } else if (downdpad) {
                    robot.FR.setPower(-speed);
                    robot.FL.setPower(-speed);
                    robot.BL.setPower(-speed);
                    robot.BR.setPower(-speed);

                } else if (rightdpad) {
                    robot.FR.setPower(speed);
                    robot.FL.setPower(-speed);
                    robot.BL.setPower(speed);
                    robot.BR.setPower(-speed);

                } else if (leftdpad) {
                    robot.FR.setPower(-speed);
                    robot.FL.setPower(speed);
                    robot.BL.setPower(-speed);
                    robot.BR.setPower(speed);

                }
            }
            //change that speed by those bumpers


            if (egamepad1.right_bumper.pressed) {
                speed += 0.25;
            }
            if (egamepad1.left_bumper.pressed) {
                speed -= 0.25;
            }
            //if the speed is at the min/max value set it to NOT min/max so boom it cant go over
            if (speed == 0) {
                speed -= 0.25;
            }
            if (speed > 1) {
                speed = 1;
            }
            //let the robot have a little rest, sleep is healthy
            sleep(40);
        }
    }




