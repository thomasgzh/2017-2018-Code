package org.firstinspires.ftc.teamcode;
/* version history 2.0
     -10/21/17 (1.0) working and good
     -10/23/17 (1.3) adding speed changing by lbumper/ltrigger
     -10/30/17 (1.5) dpad control
 */


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

//naming the teleop thing
@TeleOp(name="ArmCode Test", group="Drive")
public class ArmCodeTest extends LinearOpMode {

    RobotConfig robot = new RobotConfig();

/* Declare extended gamepad */
GamepadEdge egamepad1;
GamepadEdge egamepad2;

@Override
public void runOpMode() throws InterruptedException {
    //declaring all my variables in one place for my sake
    double upper_arm;

    /* Initialize the hardware variables.
     * The init() method of the hardware class does all the work here
     */

    robot.init(hardwareMap);

    /* Instantiate extended gamepad */
    egamepad1 = new GamepadEdge(gamepad1);
    egamepad2 = new GamepadEdge(gamepad2);


    //waits for that giant PLAY button to be pressed on RC
    waitForStart();

    //telling the code to run until you press that giant STOP button on RC
    while (opModeIsActive()) {
        //and now, the fun stuff

        /* Update extended gamepad */
        egamepad1.UpdateEdge();
        egamepad2.UpdateEdge();

        //adds a lil' version thing to the telemetry so you know you're using the right version
        telemetry.addData("Version", "1.0");
        telemetry.addData("ULpos", robot.UL.getCurrentPosition());
        telemetry.addData("URpos", robot.UR.getCurrentPosition());
        telemetry.addData("Switch", robot.ArmSwitch.getState());

        /********** TeleOp code **********/
        if (gamepad2.dpad_up) {
            upper_arm = 0.2;
        } else {
            if (gamepad2.dpad_down) {
                upper_arm = -0.2;
            } else {
                upper_arm = 0.0;
            }
        }
        if (robot.ArmSwitch.getState()==false) {
            /* when switch is closed reset encoder positions */
            robot.UR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.UL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            if (upper_arm < 0.0) upper_arm = 0.0;
        }
        robot.UR.setPower(upper_arm);
        robot.UL.setPower(upper_arm);

//        if (gamepad1.dpad_right) {
//            telemetry.addData("Upper","pos");
//            robot.UR.setTargetPosition(robot.UR.getCurrentPosition());
//            robot.UL.setTargetPosition(robot.UL.getCurrentPosition());
 //       } else {
 //           if (gamepad1.dpad_left) {
 //               telemetry.addData("Upper", "neg");
 //               robot.UL.setPower(-0.5);
 //               robot.UR.setPower(-0.5);
 //           } else {
//                robot.UL.setPower(0.0);
//                robot.UR.setPower(0.0);
//            }
//        }

        telemetry.update();

        //let the robot have a little rest, sleep is healthy
            sleep(40);
        }
    }

}





