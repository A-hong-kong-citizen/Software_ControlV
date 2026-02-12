package org.firstinspires.ftc.teamcode.TRASHCAN;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Turret {
    public DcMotor turret;

    public void init(HardwareMap haMap){
        turret = haMap.get(DcMotor.class,"turret");
        turret.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        turret.setDirection(DcMotorSimple.Direction.FORWARD);
    }
    public void turret(Gamepad gamepad2){
        turret.setPower(gamepad2.left_stick_x*-0.5);
    }
}
