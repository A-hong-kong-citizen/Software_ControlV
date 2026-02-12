package org.firstinspires.ftc.teamcode.TRASHCAN;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake {
    public DcMotor Intake;

    public void init(HardwareMap haMap){
        Intake = haMap.get(DcMotor.class,"intake");
        Intake.setDirection(DcMotorSimple.Direction.FORWARD);
        Intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void loop(Gamepad gamepad){
        if (gamepad.left_trigger >= 0.2){
            Intake.setPower(1);
        } else{
            Intake.setPower(0);
        }

    }

}
