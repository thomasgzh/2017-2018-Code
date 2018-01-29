package org.firstinspires.ftc.teamcode;

/**
 * Created by Nicholas on 1/17/2018.
 */

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;


//naming the teleop thing
@Autonomous(name="AllAuto", group ="Drive")
public class AllAuto extends LinearOpMode {


    RobotConfig robot = new RobotConfig();
    private ElapsedTime runtime = new ElapsedTime();


    //clock reseter
    public void resetClock() {
        lastReset = runtime.seconds();
    }

    public boolean inputTeamColor(){
        //input the team color
        telemetry.addData("Input: ", "Select Team Color");
        telemetry.update();

        while (!gamepad1.x && !gamepad1.b){

        }
        if (gamepad1.x)
            return false;
        return true;
    }

    public boolean inputPosition(){
        //input the position
        telemetry.addData("Input: ", "Select Position");
        telemetry.update();

        while (!gamepad1.dpad_left && !gamepad1.dpad_right){

        }
        //delay selection
        if ((gamepad1.dpad_left && redteam) || (gamepad1.dpad_right && !redteam))
            return false;
        return true;
    }

    public void displaySelections(){
        telemetry.addData("Status", "Initialized");
        if (FI)
            telemetry.addData("Position", "FI");
        else
            telemetry.addData("Position", "BI");
        if (redteam)
            telemetry.addData("Team Color", "RED");
        else
            telemetry.addData("Team Color", "BLUE");
        telemetry.update();
    }

    //mode 'stuff'
    //modes lists which steps and in what order to accomplish them
    int mode = 0;
    int [] modes = {2, 3, 9, 22, 10, 21, 11, 12, 13, 20, 100};
    //1:  Check Vumark
    //2:  Close servos on preloaded glyph
    //3:  Lock arm in resting position
    //4-8 to be added if advance past state to score jewel
    //9:  Back off stone
    //10: Drive in front of/towards cryptobox (BI/FI)
    //11: Drive into cryptobox
    //12: Release glyph
    //13: Back up from cryptobox
    //14: Turn 135 degrees (left for red, right for blue)
    //15: Bonus glyphs! (if time)
    //20: Turn 180 degrees
    //21: Turn 90 degrees (right for red, left for blue)
    //22: Turn 90 degrees (left for red, right for blue)
    //31: Turn 90 degrees (right for red, left for blue) (BI)
    //32: Turn 90 degrees (left for red, right for blue) (BI)

    //start position variables
    boolean FI;
    boolean redteam;

    //time based variables
    double lastReset = 0;
    double now = 0;

    @Override
    public void runOpMode() throws InterruptedException {
        //declaring all my variables in one place for my sake
        final double MOVE_SPEED = 0.5;
        final double ROTATE_SPEED = 0.3;
        final double TEST_TIME = 2.0;

        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Wait for start");    //
        telemetry.update();

        //waits for that giant PLAY button to be pressed on RC
        waitForStart();

        //resets the clock after a case has been completed
        resetClock();

        //starting position input
        redteam = inputTeamColor();
        FI = inputPosition();

        // telling the code to run until you press that giant STOP button on RC
        // include opModeIsActive in all while loops so that STOP button terminates all actions
        while (opModeIsActive() && modes[mode] < 100) {


            //keeps now up to date
            now = runtime.seconds() - lastReset;

            switch (modes[mode]) {

                //MODE 1: Check Vumark
                /*case 1:
                    if (now > 1) {
                      mode++;
                      resetClock();
                    }

                    //Vuforia check
                    break;
                */

                //MODE 2: Close servos on preloaded glyph
                case 2:
                    robot.GGL.setPosition(robot.GRABBER_LEFT[1]);
                    robot.GGR.setPosition(robot.GRABBER_RIGHT[1]);
                    if (now > 0.5) {
                        mode++;
                        resetClock();
                    }
                    break;

                //MODE 3: Lock arm in resting position
                case 3:
                    if (now > 0.9) {
                        robot.Arm.MoveHome();
                        // wait until home until next step
                        if (robot.ArmSwitch.getState()==false) {
                            mode++;
                            resetClock();
                        }
                    } else {
                        robot.Arm.MoveToPosition(0.2);
                    }
                    break;

                //MODE 4-8 to be added if advance past state to score jewel

                //MODE 9: Back off stone
                case 9:
                    if (now > 0.5) {
                        mode++;
                        resetClock();
                    }
                    robot.MoveBackward(MOVE_SPEED);
                    break;

                //MODE 10: Drive in front of cryptobox
                case 10:
                    if (now > 1) {
                        mode++;
                        resetClock();
                    }
                    robot.MoveForward(MOVE_SPEED);
                    break;

                //MODE 11: Drive into cryptobox
                case 11:
                    if (now > 0.3) {
                        mode++;
                        resetClock();
                    }
                    robot.MoveForward(MOVE_SPEED);
                    break;

                //MODE 12: Release glyph
                case 12:
                    robot.GGL.setPosition(robot.GRABBER_LEFT[0]);
                    robot.GGR.setPosition(robot.GRABBER_RIGHT[0]);
                    if (now > 0.5) {
                        mode++;
                        resetClock();
                    }
                    break;

                //MODE 13: Back up from cryptobox
                case 13:
                    if (now > 0.3) {
                        mode++;
                        resetClock();
                    }
                    robot.MoveBackward(MOVE_SPEED);
                    break;

                //MODE 14: Turn 135 degrees (left for red, right for blue)

                //MODE 15: Bonus glyphs! (if time)

                //Standard turning modes follow

                //MODE 20: Turn 180 degrees
                case 20:
                    if (now > 2.4) {
                        mode++;
                        resetClock();
                    }
                    robot.RotateRight(ROTATE_SPEED);
                    break;

                //MODE 21: Turn 90 degrees (right for red, left for blue)
                case 21:
                    if (now > 1) {
                        mode++;
                        resetClock();
                    }

                    if (redteam) {
                        robot.RotateRight(MOVE_SPEED);
                    }
                    else {
                        robot.RotateLeft(MOVE_SPEED);
                    }
                    break;

                //MODE 22: Turn 90 degrees (left for red, right for blue)
                case 22:
                    if (now > 1) {
                        mode++;
                        resetClock();
                    }

                    if (redteam) {
                        robot.RotateLeft(MOVE_SPEED);
                    }
                    else {
                        robot.RotateRight(MOVE_SPEED);
                    }
                    break;
            }  // end of switch

            robot.Arm.Update(this);

        }
    }
}