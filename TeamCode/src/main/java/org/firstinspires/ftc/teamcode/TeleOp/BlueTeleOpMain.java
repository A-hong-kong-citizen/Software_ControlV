package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.TRASHCAN.Turret;

import org.firstinspires.ftc.teamcode.AprilTagDetection.AprilTagWebCam;

import org.firstinspires.ftc.teamcode.TRASHCAN.Intake;
import org.firstinspires.ftc.teamcode.TurretAuto.ShooterSpeedAuto;
import org.firstinspires.ftc.teamcode.TurretAuto.TriggerControl;
import org.firstinspires.ftc.teamcode.Pedropathing.FieldRelativeDriveFunction;
import org.firstinspires.ftc.teamcode.TurretAuto.turretTrack;
import org.firstinspires.ftc.teamcode.TurretAuto.TurretAdjust;
import org.firstinspires.ftc.teamcode.TurretControl.turretGyro;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.teamcode.TurretAuto.getHoodAngle;


@TeleOp
public class BlueTeleOpMain extends OpMode{
    private FieldRelativeDriveFunction driveCode = new FieldRelativeDriveFunction();
    private ShooterSpeedAuto shooterControl = new ShooterSpeedAuto();
    private TriggerControl triggerControl = new TriggerControl();
    private Intake intake = new Intake();
    private Turret turretCon = new Turret();
    private turretGyro gyro = new turretGyro();
    private turretTrack turretGyro = new turretTrack();
    private TurretAdjust adjustTurret = new TurretAdjust();
    private AprilTagWebCam aprilTag = new AprilTagWebCam();
    private getHoodAngle backboardAngle = new getHoodAngle();

    public Servo  hood;
    public Servo block;
    public Servo push;
    private DcMotor intakeTrans;
    private DcMotor turret;
    double lastSpeed;


    //private Transfer transfer = new Transfer();


    //private AprilTagWebCam AprilTagRaw = new AprilTagWebCam();

    @Override
    public void init(){
        intakeTrans =hardwareMap.get(DcMotor.class,"intake");
        turret =hardwareMap.get(DcMotor.class,"turret");
        driveCode.init(hardwareMap);
        shooterControl.init(hardwareMap);
        intake.init(hardwareMap);
        turretCon.init(hardwareMap);
        hood = hardwareMap.get(Servo.class,"hood");
        block = hardwareMap.get(Servo.class,"block");
        push = hardwareMap.get(Servo.class,"push");
        gyro.init(hardwareMap);
        turretGyro.init(hardwareMap);
        adjustTurret.init(hardwareMap);
        aprilTag.init(hardwareMap,telemetry);
        hood.setPosition(0.6);



        //transfer.init(hardwareMap);
        //AprilTagRaw.init(hardwareMap, telemetry);
    }

    public void loop(){
        aprilTag.update();
        AprilTagDetection id20 = aprilTag.getTagBySpecificId(20);
        aprilTag.displayDetectionTelemetry(id20);

        driveCode.drive(telemetry,gamepad1);


        if (gamepad2.left_trigger<0.2 && gamepad2.right_trigger<0.2 ){
            intakeTrans.setPower(0);
        }
        if (gamepad2.left_trigger>0.2) {
            intakeTrans.setPower(1);
            push.setPosition(0.7);
            block.setPosition(0.4);

        }
        if(aprilTag.seeTag){

            int Index = backboardAngle.getInex(aprilTag.TagDistance);
            double Hood_angle = backboardAngle.hoodAngle(aprilTag.TagDistance);

            adjustTurret.loop(telemetry,gamepad1,aprilTag.TagXPos,aprilTag.TagDistance);
            hood.setPosition(backboardAngle.hoodAngle(aprilTag.TagDistance));
            shooterControl.changeSpeed(telemetry,triggerControl.getFlywheelSpeed(aprilTag.TagDistance));
            lastSpeed = triggerControl.getFlywheelSpeed(Index);


            telemetry.addData("Index: ", Index);
            telemetry.addData("calculated angle:", backboardAngle.calculatedAngle);
            if(gamepad2.right_trigger>0.2){
                    if(gamepad2.right_bumper){
                        push.setPosition(-1);
                    } else {
                        push.setPosition(0.9);
                    }
                    block.setPosition(0.8);
                    intakeTrans.setPower(0.6);
                } else{
                block.setPosition(0.4);
            }}


        else {
            turret.setPower(gamepad2.left_stick_x*-0.3);
            if (gamepad2.circleWasPressed()){
                shooterControl.changeSpeed(telemetry,lastSpeed);
            }
            if (gamepad2.crossWasPressed()){
                shooterControl.changeSpeed(telemetry, 0);
            }
        }



        //turretCon.turret(gamepad2);


        //transfer.loop(gamepad2);
        //AprilTagRaw.update();
        //AprilTagDetection id20 = AprilTagRaw.getTagBySpecificId(20);
        //AprilTagRaw.displayDetectionTelemetry(id20);
    }
}
