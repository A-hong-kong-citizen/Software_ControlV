package org.firstinspires.ftc.teamcode.TRASHCAN;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class TriggerControl {
    public Servo trigger;

    public Transfer transfer;

    double ServoTarPos = 0.575;


    public void init(HardwareMap hardwareMap){
        trigger = hardwareMap.get(Servo.class,"trigger");

    }



    public void triggerControl(Gamepad gamepad1){

        if (gamepad1.leftBumperWasPressed()){
            if (ServoTarPos == 0.575){
                ServoTarPos = 0.4;

            } else {

                ServoTarPos = 0.575;
            }
            trigger.setPosition(ServoTarPos);
        }

    }
}
