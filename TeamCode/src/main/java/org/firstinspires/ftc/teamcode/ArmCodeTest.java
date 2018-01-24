package org.firstinspires.ftc.teamcode;
/* version history
     -1/13/18 created file for testing arm control
     -1/23/18 added potentiometer feedback for upper arm
 */


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

//naming the teleop thing
@TeleOp(name="ArmCode Test", group="Test")
public class ArmCodeTest extends LinearOpMode {

    RobotConfig robot = new RobotConfig();
    ArmControl  Arm = new ArmControl();

    /* Declare extended gamepad */
    GamepadEdge egamepad1;
    GamepadEdge egamepad2;

    @Override
    public void runOpMode() throws InterruptedException {

        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);

        /* Instantiate extended gamepad */
        egamepad1 = new GamepadEdge(gamepad1);
        egamepad2 = new GamepadEdge(gamepad2);

        //adds a lil' version thing to the telemetry so you know you're using the right version
        telemetry.addData("Version", "2.0");
        telemetry.update();

        //waits for that giant PLAY button to be pressed on RC
        waitForStart();

        //telling the code to run until you press that giant STOP button on RC
        while (opModeIsActive()) {
            //and now, the fun stuff

            /* Update extended gamepad */
            egamepad1.UpdateEdge();
            egamepad2.UpdateEdge();

            Arm.Update();

            Arm.DoEverything();

            //let the robot have a little rest, sleep is healthy
            sleep(40);
        }
    }


    /********** Copy this code for OpMode usage **********/
    public class ArmControl {
        //declaring all my variables in one place for my sake
        private boolean ArmHomed = false;                /* has the arm been home */
        private double UpperArmHomePosition = 0;         /* position value at home */
        private double UpperArmPosition = 0;             /* current position relative to home */
        private double upper_arm;
        private final double UPPER_ARM_HOLD_POWER = 0.01;
        private final double UPPER_ARM_UP_POWER = 0.2;
        private final double UPPER_ARM_DOWN_POWER = 0.2;
        private double upper_arm_target = 0;
        private boolean upper_motion = false;

        /* Constructor */
        public ArmControl() {
        }

        /* Call this method when you want to update the arm motors */
        public void Update() {
            //adds a lil' version thing to the telemetry so you know you're using the right version
            telemetry.addData("ArmControl","%.2f", robot.UpperArmPot.getVoltage());
        }

        public void DoEverything() {
            /********** TeleOp code **********/
            if (robot.ArmSwitch.getState()==false) {
                /* arm in home position */
                UpperArmHomePosition = robot.UpperArmPot.getVoltage();
                ArmHomed = true;
            }
            UpperArmPosition = robot.UpperArmPot.getVoltage() - UpperArmHomePosition;

            //adds a lil' version thing to the telemetry so you know you're using the right version
            telemetry.addData("Upper pos","%.2f (%.2f)", UpperArmPosition, UpperArmHomePosition);
            telemetry.addData("Switch", robot.ArmSwitch.getState());

            /****** manual control *****/
            /* when switch is closed or if the arm has not been homed, do not try to hold position */
            upper_arm = UPPER_ARM_HOLD_POWER;
            if ((robot.ArmSwitch.getState()==false) || (ArmHomed==false)) {
                upper_arm = 0;
            }
            if (gamepad2.dpad_up) {
                upper_motion = false;
                upper_arm = UPPER_ARM_UP_POWER;
            } else if (egamepad2.dpad_up.released) {
                upper_motion = true;
                upper_arm_target = UpperArmPosition;
            }
            if (gamepad2.dpad_down) {
                upper_motion = false;
                upper_arm = -UPPER_ARM_DOWN_POWER;
            } else if (egamepad2.dpad_down.released) {
                upper_motion = true;
                upper_arm_target = UpperArmPosition;
            }

            /* automatically move to fixed positions */
            if (egamepad2.x.pressed) {
                upper_motion = true;
                upper_arm_target = 0.15;
            }
            if (egamepad2.y.pressed) {
                upper_motion = true;
                upper_arm_target = 0.0;
            }

            /*********** control code **********/
            /* check if fixed position motion active - overrides manual */
            if (upper_motion && ArmHomed) {
                double error = upper_arm_target - UpperArmPosition;
                if (error>0.5) error = 0.5;
                if (error<-0.5) error = -0.5;

                upper_arm = UPPER_ARM_HOLD_POWER;
                if ((robot.ArmSwitch.getState()==false) || (ArmHomed==false)) {
                    upper_arm = 0;
                }
                if (error>0.0) {
                    upper_arm = UPPER_ARM_UP_POWER * 2 * error;
                } else if (error<0.0) {
                    upper_arm = UPPER_ARM_DOWN_POWER * 2 * error;
                }
                upper_arm += UPPER_ARM_HOLD_POWER*(2.5-upper_arm_target)/2.5;
                if (upper_arm==0.0) upper_arm = UPPER_ARM_HOLD_POWER/4;

                telemetry.addData("Motion Upper","%.2f %.2f", upper_arm_target, error);
            }

            /* when switch is closed or if the arm has not been homed, prevent downward motion */
            if ((robot.ArmSwitch.getState()==false) || (ArmHomed==false)){
                if (upper_arm < 0.0) upper_arm = 0.0;
            }

            telemetry.addData("Power","%.2f", upper_arm);
            telemetry.update();

            robot.UR.setPower(upper_arm);
            robot.UL.setPower(upper_arm);
        }
    }
}
