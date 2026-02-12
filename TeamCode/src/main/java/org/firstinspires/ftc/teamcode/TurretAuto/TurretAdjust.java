package org.firstinspires.ftc.teamcode.TurretAuto;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class TurretAdjust {
    public DcMotorEx Turret;
    private double KpX = 1.72;
    private double KiX = 0;
    private double KdX = 0.1;
    private double KfX = 0.001;
    private double errorX = 0;
    private double PX;
    private double IX;
    private double DX;
    private double LastErrorX;
    private double totalError;
    public double correctionX;
    double[] stepSize = {10.0,1.0,0.1,0.01,0.001,0.0001};
    int stepIndex = 1;




    public void init(HardwareMap haMap){
        Turret = haMap.get(DcMotorEx.class,"turret");
        Turret.setDirection(DcMotorSimple.Direction.FORWARD);
        Turret.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Turret.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }
    public void loop(Telemetry telemetry, Gamepad gamepad1, double TagX, double TagDistance){
            Turret.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            errorX = (-5 - TagX);
            PX = errorX * KpX;
            totalError += errorX;
            IX = KiX * (totalError);
            DX = KdX * (errorX - LastErrorX);
            LastErrorX = errorX;
            correctionX = (PX + IX + DX)/TagDistance;
            if (correctionX >= 0) {
                correctionX += KfX;
            } else {
                correctionX -= KfX;
            }

            Turret.setPower(correctionX);

    }
}
