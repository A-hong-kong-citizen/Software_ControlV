package org.firstinspires.ftc.teamcode.TurretControl;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.AprilTagDetection.AprilTagWebCam;
import org.firstinspires.ftc.teamcode.TurretAuto.ShooterSpeedAuto;
import org.firstinspires.ftc.teamcode.TurretAuto.TurretAdjust;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.teamcode.TurretAuto.getHoodAngle;
import org.firstinspires.ftc.teamcode.TurretAuto.TriggerControl;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;

import org.firstinspires.ftc.teamcode.TurretControl.ShooterSpeedDirect;

@TeleOp
public class TurrectDirectControl extends OpMode {
private Servo backboard;
private ShooterSpeedDirect shooterSpeed = new ShooterSpeedDirect();
    private AprilTagWebCam AprilTagRaw = new AprilTagWebCam();
    private TurretAdjust AdjustTurret = new TurretAdjust();
    private getHoodAngle AdjustHood = new getHoodAngle();
    private TriggerControl triggerControl = new TriggerControl();
    public DcMotor intake;
public double currentPos;

    public void init() {
    backboard = hardwareMap.get(Servo.class, "hood");
    shooterSpeed.init(hardwareMap);
    AprilTagRaw.init(hardwareMap, telemetry);
    AdjustTurret.init(hardwareMap);
    intake = hardwareMap.get(DcMotor.class,"intake");
    }
    public void loop() {
        telemetry.addData("Current Position: ", currentPos);
        telemetry.addData("Distance from Tag: ", AprilTagRaw.TagDistance);
        //currentPos = AdjustHood.hoodAngle(shooterSpeed.curVelocity, AdjustHood.getInex(AprilTagRaw.TagDistance));
        backboard.setPosition(currentPos);
        if (gamepad1.dpadDownWasPressed()) {
            currentPos -= 0.1;
        }
        if (gamepad1.dpadUpWasPressed()) {
            currentPos += 0.1;
        }
        if (gamepad1.dpadLeftWasPressed()) {
            currentPos -= 0.01;
        }
        if (gamepad1.dpadRightWasPressed()) {
            currentPos += 0.01;
        }
        if(currentPos>1){
            currentPos=1;

        }
        if (currentPos <0.6){
            currentPos = 0.6;
        }
        if(gamepad1.left_trigger>0.2){
            intake.setPower(0.5);
        }else if (gamepad1.right_trigger>0.2){
            intake.setPower(1);
        }else{
            intake.setPower(0);
        }

        shooterSpeed.changeSpeed(telemetry,gamepad1);
        AdjustTurret.loop(telemetry,gamepad1,AprilTagRaw.TagXPos,AprilTagRaw.TagDistance);
        AprilTagRaw.update();
        AprilTagDetection id20 = AprilTagRaw.getTagBySpecificId(20);
        AprilTagRaw.displayDetectionTelemetry(id20);


    }
}
