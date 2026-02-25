package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.CRServo;

import org.firstinspires.ftc.teamcode.Pedropathing.FieldRelativeDriveFunction;
@TeleOp
public class ControlV extends OpMode {
    private DcMotor shooter;
    private FieldRelativeDriveFunction driveCode = new FieldRelativeDriveFunction();
    private CRServo transferL;
    private CRServo transferR;
    @Override
    public void init() {
        driveCode.init(hardwareMap);
        shooter = hardwareMap.get(DcMotor.class,"shooter");
        transferL = hardwareMap.get(CRServo.class,"L_transfer");
        transferR = hardwareMap.get(CRServo.class,"R_transfer");


    }
    public void loop(){
        driveCode.drive(telemetry,gamepad1,gamepad2);
        if (gamepad1.left_trigger >0.2){
            shooter.setPower(0.8);

        }else {
            shooter.setPower(0);
        }
        if (gamepad1.right_trigger>0.2){
            transferL.setPower(-1);
            transferR.setPower(1);
        } else {
            transferL.setPower(0);
            transferR.setPower(0);
        }
    }

}
