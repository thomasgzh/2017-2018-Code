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
@TeleOp(name="ArmCode Test", group="Test")
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
            telemetry.addData("URpos","%d (%d)", robot.UR.getCurrentPosition(), robot.URArmHome);
            telemetry.addData("ULpos","%d (%d)", robot.UL.getCurrentPosition(), robot.ULArmHome);
            telemetry.addData("Switch", robot.ArmSwitch.getState());
            telemetry.update();

            /********** TeleOp code **********/
            upper_arm = 0.0;
            if (gamepad2.dpad_up) {
                upper_arm = 0.2;
            } else {
                if (gamepad2.dpad_down) {
                    upper_arm = -0.2;
                }
            }
            if (robot.ArmSwitch.getState()==false) {
                /* when switch is closed reset encoder positions */
                robot.URArmHome = robot.UR.getCurrentPosition();
                robot.ULArmHome = robot.UL.getCurrentPosition();
                if (upper_arm < 0.0) upper_arm = 0.0;
            }
            robot.UR.setPower(upper_arm);
            robot.UL.setPower(upper_arm);

            if (egamepad2.x.released) {
                robot.UR.setTargetPosition(robot.URArmHome + 200);
                int integral = 0;
                int error = 0;

                while (opModeIsActive() && (!egamepad2.x.pressed) ) {

                    // home made PI control loop
                    error = robot.UR.getTargetPosition()-robot.UR.getCurrentPosition();
                    if (Math.abs(error)>10)
                        integral += error;

                    upper_arm = integral/100000.0 + error/1000.0;

                    if (upper_arm>0.8) upper_arm = 0.8;
                    if (upper_arm<-0.8) upper_arm = -0.8;

                    robot.UR.setPower(upper_arm);
                    robot.UL.setPower(upper_arm);

                    // Display it for the driver.
                    telemetry.addData("Path", " %7d (%7d)",
                            robot.UR.getCurrentPosition(), robot.UR.getTargetPosition());
                    telemetry.addData("Motor", " %f", upper_arm);
                    telemetry.update();

                    //let the robot have a little rest, sleep is healthy
                    sleep(40);
                }
            }

            /* ****** setTargetPosition does not work
            does not get to target with one motor
            once it gets there it kills the power and drops
            doesn't hold position
             */
/*            if (egamepad2.x.released) {
                robot.UR.setPower(0.0);
                robot.UL.setPower(0.0);

                robot.UR.setTargetPosition(robot.URArmHome + 100);
                robot.UR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                robot.UR.setPower(0.8);
                while (opModeIsActive() && robot.UR.isBusy() ) {
                    // Display it for the driver.
                    telemetry.addData("Path",  "Running at %7d :%7d",
                            robot.UR.getTargetPosition(), robot.UR.getCurrentPosition());
                    telemetry.update();

                    //let the robot have a little rest, sleep is healthy
                    sleep(40);
                }
//                robot.UR.setPower(0);
//                robot.UR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            }
*/

            //let the robot have a little rest, sleep is healthy
            sleep(40);
        }
    }
}





