package org.firstinspires.ftc.teamcode.TurretAuto;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.firstinspires.ftc.robotcore.external.Telemetry;


public class ShooterSpeedAuto {
    public DcMotorEx shooterMotor;
    public DcMotorEx shooterMotor2;

    public double lowSpeed = 0;
    public double highSpeed = 1700;
    public double curTargetVelocity = highSpeed;
    public double kP = 15;
    public double kF = 15.51;
    public double error;
    public double curVelocity;


    double[] stepSize = {10.0,1.0,0.1,0.01,0.001,0.0001};
    int stepIndex = 1;


    public void init(HardwareMap hardwareMap){
        shooterMotor = hardwareMap.get(DcMotorEx.class,"shooterMotor");
        shooterMotor2 = hardwareMap.get(DcMotorEx.class,"shooterMotor2");
        shooterMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooterMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        shooterMotor2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooterMotor2.setDirection(DcMotorSimple.Direction.FORWARD);
        PIDFCoefficients shooterPIDF = new PIDFCoefficients(kP,0,0, kF);
        shooterMotor.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER,shooterPIDF);
        shooterMotor2.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER,shooterPIDF);

    }

    public void changeSpeed (Telemetry telemetry, double calculatedSpeed){
        curTargetVelocity = calculatedSpeed + 80;
        kF = 0.000000000002331*curTargetVelocity*curTargetVelocity*curTargetVelocity*curTargetVelocity
                -0.0000000142968*curTargetVelocity*curTargetVelocity*curTargetVelocity
                +0.00003331*curTargetVelocity*curTargetVelocity
                -0.0339852*curTargetVelocity
                +25.75103
        ;

        PIDFCoefficients PIDFTuning = new PIDFCoefficients(kP,0,0, kF);
        shooterMotor.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER,PIDFTuning);
        shooterMotor2.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER,PIDFTuning);

        shooterMotor.setVelocity(curTargetVelocity);
        shooterMotor2.setVelocity(curTargetVelocity);
        curVelocity = shooterMotor.getVelocity();
        error = curTargetVelocity - curVelocity;

        telemetry.addData("Target Velocity",calculatedSpeed);
        telemetry.addData("Current Velocity", "%.2f",curVelocity);
        telemetry.addData("Error", "%.2f", error);
    }
}
