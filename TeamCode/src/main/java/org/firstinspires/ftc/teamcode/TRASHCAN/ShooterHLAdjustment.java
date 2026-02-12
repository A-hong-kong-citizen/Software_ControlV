package org.firstinspires.ftc.teamcode.TRASHCAN;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class ShooterHLAdjustment {
    public Servo Shooter;

    public double servoPos;
    public double flippedPos;

    public double Kp = 0.2;

    public double P;

    public void init(HardwareMap haMap){
        Shooter = haMap.get(Servo.class,"shooter");
        Shooter.setDirection(Servo.Direction.FORWARD);
        Shooter.setPosition(0.6);
    }

    public void loop(Telemetry telemetry,double TagDistance, double flywheelError){
        P = (flywheelError/2200) * Kp;
        if(TagDistance <= 330 || TagDistance>= 110) {
            flippedPos = (TagDistance - 110)/(330-110) * 0.4 + 0.6;
            servoPos = (flippedPos - 0.8)*-1 + 0.72 - P;
        }
        telemetry.addData("TagDistance:",TagDistance);
        telemetry.addData("ServoPos:",servoPos);

        if (servoPos < 0.6) {
                servoPos = 0.6;
        }
        if (servoPos > 1) {
                servoPos = 1;
        }
            Shooter.setPosition(servoPos);
    }
}

