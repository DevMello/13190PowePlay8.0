package cf.devmello.rewrite.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import com.qualcomm.hardware.bosch.BNO055IMU;

/**
 * The only OpMode you will ever need
 *
 * @author  DevMello
 * @version 1.0
 * @since   2022-11-09
 */
@TeleOp
public class DevMelloGodOpMode extends LinearOpMode {

    /*
     * DC Motors
     */
    protected DcMotor motorFrontLeft, motorFrontRight, motorBackLeft, motorBackRight;
    /*
     * Create a List for all the DC Motors to iterate through them faster.
     */
    protected DcMotor[] motors = {motorFrontLeft, motorFrontRight, motorBackLeft, motorBackRight};
    /*
     * The IMU
     */
    protected BNO055IMU imu;
    /*
     * The Slide Motor
     */
    protected DcMotor slideMotor;
    /*
     * The Slide Class
     */
    Slides slide = new Slides(slideMotor);
    /*
     * The Clamp
     */
    protected Servo clamp;
    /*
     * The Clamp Class
     */
    Clamp clampServo = new Clamp(clamp);
    @Override
    public void runOpMode() throws InterruptedException {
        // Declare our motors
        // Make sure your ID's match your configuration
        motorFrontLeft = hardwareMap.dcMotor.get("lf");
        motorBackLeft = hardwareMap.dcMotor.get("lb");
        motorFrontRight = hardwareMap.dcMotor.get("rf");
        motorBackRight = hardwareMap.dcMotor.get("rb");

        imu = hardwareMap.get(BNO055IMU.class, "imu");
        // Reverse the right side motors
        // Reverse left motors if you are using NeveRests
        motorFrontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        motorBackLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        //iterate through all the motors and set their mode to RUN_USING_ENCODER and set their zero power behavior to BRAKE (so they don't move when the power is 0)
        for(DcMotor motor : motors) {
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
        //declare the slides motor
        slideMotor = hardwareMap.dcMotor.get("slideMotor");
        //set the slides motor to run using the given motor
        slide.setSlideMotor(slideMotor);
        //declare the clamp servo
        clamp = hardwareMap.servo.get("clamp");
        //set the clamp servo to run using the given servo
        clampServo.setClamp(clamp);

        // Wait for the game to start (driver presses PLAY)

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            double y = -gamepad1.left_stick_y; // Remember, this is reversed!
            double x = gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
            double rx = gamepad1.right_stick_x;

            // Denominator is the largest motor power (absolute value) or 1
            // This ensures all the powers maintain the same ratio, but only when
            // at least one is out of the range [-1, 1]
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double frontLeftPower = (y + x + rx) / denominator;
            double backLeftPower = (y - x + rx) / denominator;
            double frontRightPower = (y - x - rx) / denominator;
            double backRightPower = (y + x - rx) / denominator;

            motorFrontLeft.setPower(frontLeftPower);
            motorBackLeft.setPower(backLeftPower);
            motorFrontRight.setPower(frontRightPower);
            motorBackRight.setPower(backRightPower);
            //if driver clicks a on gamepad1, toggle the clampState
            if (gamepad1.a) {
                clampServo.setClampState();
            }
            //if driver clicks up on the dpad, move the slides up
            if (gamepad1.dpad_up) {
                slide.move(Slides.DIRECTION.UP);
            }
            //if driver clicks down on the dpad, move the slides down
            if (gamepad1.dpad_down) {
                slide.move(Slides.DIRECTION.DOWN);
            }


        }
    }
}

