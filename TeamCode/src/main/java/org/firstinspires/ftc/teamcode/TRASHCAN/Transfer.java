package org.firstinspires.ftc.teamcode.TRASHCAN;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Transfer {
    public DcMotor TransferL;
    public DcMotor TransferR;


    public void init(HardwareMap haMap) {
        TransferL = haMap.get(DcMotor.class, "transferL");
        TransferR = haMap.get(DcMotor.class, "transferR");
        TransferL.setDirection(DcMotorSimple.Direction.FORWARD);
        TransferR.setDirection(DcMotorSimple.Direction.REVERSE);
        TransferL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        TransferR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void loop(Gamepad gamepad) {
        if (gamepad.right_trigger >= 0.2){
            TransferL.setPower(0.7);
            TransferR.setPower(0.7);
        } else {
            if (TransferL.getPower() == 0.7) {
                TransferL.setPower(0);
                TransferR.setPower(0);
            }
        }
    }

    public void TransferMotorReverse(){
        if (TransferL.getPower() == 0 || TransferL.getPower() == 0.7){
            TransferL.setPower(-0.7);
            TransferR.setPower(-0.7);
        }else if (TransferL.getPower() == -0.7){
            TransferL.setPower(0);
            TransferR.setPower(0);
        }

    }
}
