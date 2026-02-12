package org.firstinspires.ftc.teamcode.TurretControl;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Config
public class ShooterSpeedDirect {
    public DcMotorEx shooterMotor1;
    public DcMotorEx shooterMotor2;

    public double lowSpeed = 0;
    public double highSpeed = 700;
    public double curTargetVelocity = highSpeed;
    public double kP = 40;
    public double kF = 15.51;
    public double error;
    public double curVelocity;



    double[] stepSize = {10.0,1.0,0.1,0.01,0.001,0.0001};
    int stepIndex = 1;
    FtcDashboard dashboard = FtcDashboard.getInstance();
    Telemetry dashboardTelemetry = dashboard.getTelemetry();
    public static PIDFCoefficients shooterPIDF = new PIDFCoefficients(100,0.1,0, 14.3);


    public void init(HardwareMap hardwareMap){
        shooterMotor1 = hardwareMap.get(DcMotorEx.class,"shooterMotor");
        shooterMotor1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooterMotor1.setDirection(DcMotorSimple.Direction.FORWARD);
        shooterMotor2 = hardwareMap.get(DcMotorEx.class,"shooterMotor2");
        shooterMotor2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooterMotor2.setDirection(DcMotorSimple.Direction.FORWARD);



        shooterMotor1.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER,shooterPIDF);
        shooterMotor2.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER,shooterPIDF);
    }

    public void changeSpeed (Telemetry telemetry, Gamepad gamepad2){

        kF = 0.000000000002331*curTargetVelocity*curTargetVelocity*curTargetVelocity*curTargetVelocity
                -0.0000000142968*curTargetVelocity*curTargetVelocity*curTargetVelocity
                +0.00003331*curTargetVelocity*curTargetVelocity
                -0.0339852*curTargetVelocity
                +25.75103
        ;


        if(gamepad2.circleWasPressed()){
            curTargetVelocity += 20;
        }
        if(gamepad2.crossWasPressed()){
            curTargetVelocity -= 20;
        }



        PIDFCoefficients PIDFTuning = new PIDFCoefficients(kP,0,0, kF);
        shooterMotor1.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER,shooterPIDF);
        shooterMotor2.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER,shooterPIDF);

        shooterMotor1.setVelocity(curTargetVelocity);
        shooterMotor2.setVelocity(curTargetVelocity);
        curVelocity = shooterMotor1.getVelocity();
        error = curTargetVelocity - curVelocity;
        dashboardTelemetry.addData("Target Velocity",curTargetVelocity);
        dashboardTelemetry.addData("Current Velocity", "%.2f",curVelocity);
        dashboardTelemetry.addData("Error",error);
        dashboardTelemetry.update();
        telemetry.addData("Target Velocity",curTargetVelocity);
        telemetry.addData("Current Velocity", "%.2f",curVelocity);
        telemetry.addData("Error", "%.2f", error);
        telemetry.addData("Current kF", kF);
    }
}
