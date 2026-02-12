package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp
public class AllMotorTest extends OpMode {
    public DcMotor zero;
    public DcMotor one;
    public DcMotor two;
    public DcMotor three;
    public int motorNo =0;

    @Override
    public void init() {
        zero = hardwareMap.get(DcMotor.class,"0");
        one = hardwareMap.get(DcMotor.class,"1");
        two = hardwareMap.get(DcMotor.class,"2");
        three = hardwareMap.get(DcMotor.class,"3");
    }
    public void loop() {
        if (gamepad1.left_bumper) {
            motorNo -= 1;
        }
        if (gamepad1.right_bumper) {
            motorNo += 1;
        }
        telemetry.addData("Current Motor:",motorNo);
        telemetry.addData("Current 0 Power", zero.getPower() );
        telemetry.addData("Current 1 Power", one.getPower() );
        telemetry.addData("Current 2 Power", two.getPower() );
        telemetry.addData("Current 3 Power", three.getPower() );



        if (gamepad1.left_trigger > 0.1) {
            if (motorNo == 0) {
                zero.setPower(gamepad1.left_trigger);
            } else if (motorNo == 1) {
                one.setPower(gamepad1.left_trigger);
            } else if (motorNo == 2) {
                two.setPower(gamepad1.left_trigger);
            } else if (motorNo == 3) {
                three.setPower(gamepad1.left_trigger);
            }
        } else if (gamepad1.right_trigger > 0.1) {
            if (motorNo == 0) {
                zero.setPower(gamepad1.right_trigger * -1);
            } else if (motorNo == 1) {
                one.setPower(gamepad1.right_trigger * -1);
            } else if (motorNo == 2) {
                two.setPower(gamepad1.right_trigger * -1);
            } else if (motorNo == 3) {
                three.setPower(gamepad1.right_trigger * -1);
            }
        } else {
            zero.setPower(0);
            one.setPower(0);
            two.setPower(0);
            three.setPower(0);
        }
    }
}
