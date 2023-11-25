package org.firstinspires.ftc.team22676;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "Mecanum22676")
public class Mecanum22676 extends LinearOpMode {

    private DcMotor arm;
    private Servo claw_right;
    private Servo claw_left;
    private DcMotor motor_backleft;
    private DcMotor motor_frontleft;
    private DcMotor motor_frontright;
    private DcMotor motor_backright;
    private Servo wrist;

    @Override
    public void runOpMode() {
        // initialize hardware fields
        motor_backleft = hardwareMap.get(DcMotor.class, "bl");
        motor_frontleft = hardwareMap.get(DcMotor.class, "fl");
        arm = hardwareMap.get(DcMotor.class, "arm");
        claw_right = hardwareMap.get(Servo.class, "clawright");
        claw_left = hardwareMap.get(Servo.class, "clawleft");
        motor_frontright = hardwareMap.get(DcMotor.class, "fr");
        motor_backright = hardwareMap.get(DcMotor.class, "br");
        wrist = hardwareMap.get(Servo.class, "wrist");

        waitForStart();

        if (opModeIsActive()) {
            setDirectionsAndPowerBehavior();

            while (opModeIsActive()) {
                handleGamepad1();
                handleGamepad2();
            }
        }
    }

    private void setDirectionsAndPowerBehavior() {
        motor_backleft.setDirection(DcMotor.Direction.REVERSE);
        motor_frontleft.setDirection(DcMotor.Direction.REVERSE);
        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        claw_right.setDirection(Servo.Direction.REVERSE);
        claw_left.setDirection(Servo.Direction.REVERSE);
    }

    private void handleGamepad1() {
        float dpad_down = 0.0F;
        float dpad_up = 0.0F;
        float horizontal;
        float vertical;
        float pivot = gamepad1.right_stick_x;

        if (gamepad1.dpad_down) {
            dpad_down += 0.1;
            vertical = -dpad_down;
        } else {
            vertical = -gamepad1.left_stick_y;
        }
        if (gamepad1.dpad_up) {
            dpad_up += 0.1;
            horizontal = dpad_up;
        } else {
            horizontal = gamepad1.left_stick_x;
        }

        updateMotorPower(horizontal, vertical, pivot);
    }

    private void handleGamepad2() {
        if (gamepad2.x) {
            closeClaw();
        }

        if (gamepad2.y) {
            moveClawRight(0.3);
        } else {
            moveClawRight(0);
        }

        if (gamepad2.a) {
            wrist.setPosition(0);
        }

        arm.setPower(gamepad2.right_stick_y);
    }

    private void updateMotorPower(float horizontal, float vertical, float pivot) {
        double sum = vertical + horizontal;
        double difference = vertical - horizontal;
        double halfPivot = pivot * 0.5;

        motor_frontright.setPower(halfPivot + difference);
        motor_frontleft.setPower(halfPivot + sum);

        motor_backright.setPower(-halfPivot + sum);
        motor_backleft.setPower(-halfPivot + difference);
    }

    private void closeClaw() {
        claw_left.setPosition(0);
        claw_right.setPosition(0);
    }

    private void moveClawRight(double pos) {
        claw_right.setPosition(pos);
    }
}
