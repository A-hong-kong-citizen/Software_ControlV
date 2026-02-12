package org.firstinspires.ftc.teamcode.TRASHCAN;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.AprilTagDetection.AprilTagWebCam;
import org.firstinspires.ftc.teamcode.Pedropathing.FieldRelativeDriveFunction;
import org.firstinspires.ftc.teamcode.TurretAuto.ShooterSpeedAuto;
import org.firstinspires.ftc.teamcode.TurretAuto.TriggerControl;
import org.firstinspires.ftc.teamcode.TurretAuto.TurretAdjust;
import org.firstinspires.ftc.teamcode.TurretAuto.getHoodAngle;
import org.firstinspires.ftc.teamcode.TurretAuto.turretTrack;
import org.firstinspires.ftc.teamcode.TurretControl.turretGyro;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;


@TeleOp
public class Presentation extends OpMode{
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
        turret = hardwareMap.get(DcMotor.class,"turret");
        turret.setDirection(DcMotorSimple.Direction.FORWARD);
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



        //transfer.init(hardwareMap);
        //AprilTagRaw.init(hardwareMap, telemetry);
    }

    public void loop() {
        aprilTag.update();
        AprilTagDetection id20 = aprilTag.getTagBySpecificId(20);
        aprilTag.displayDetectionTelemetry(id20);
        telemetry.addData("CorX: ", adjustTurret.correctionX);
        telemetry.addData("seeTag: ", aprilTag.seeTag);
        driveCode.drive(telemetry, gamepad1);

        if (gamepad2.leftBumperWasPressed()){
            shooterControl.changeSpeed(telemetry,1900);
        }
        if(gamepad2.dpadUpWasPressed()){
            shooterControl.changeSpeed(telemetry,0);
        }

        if (gamepad2.left_trigger < 0.2 && gamepad2.right_trigger < 0.2) {
            intakeTrans.setPower(0);
        }
        if (gamepad2.left_trigger > 0.2) {
            intakeTrans.setPower(1);
            block.setPosition(0.4);

        }
        if (gamepad2.right_trigger > 0.2) {
            if (gamepad2.right_bumper) {
                push.setPosition(0.2);
            } else {
                push.setPosition(0.6);
            }
            block.setPosition(0.8);
            intakeTrans.setPower(1);
        } else {
            block.setPosition(0.4);
        }
        if (Math.abs(gamepad2.left_stick_x)<0.2) {
            adjustTurret.loop(telemetry, gamepad1, aprilTag.TagXPos, aprilTag.TagDistance);
        } else{
            turret.setPower(gamepad2.left_stick_x*0.1);

        }

    }

    @TeleOp
    public static class Shootertest extends OpMode {
        private DcMotor shooter1;
        private DcMotor shooter2;

        public void init(){
            shooter1 = hardwareMap.get(DcMotor.class,"shooterMotor");
            shooter2 = hardwareMap.get(DcMotor.class,"shooterMotor2");


        }
        public void loop(){
            if(gamepad1.left_trigger>0.2){
                shooter1.setPower(0.8);
                shooter2.setPower(0.8);
            }else {
                shooter2.setPower(0);
                shooter1.setPower(0);
            }
        }

    }

    @TeleOp
    public static class AutonomousMain extends OpMode{
        private AprilTagWebCam AprilTagRaw = new AprilTagWebCam();
        private TurretAdjust AdjustShoot = new TurretAdjust();

        private ShooterHLAdjustment AdjustHL = new ShooterHLAdjustment();

        private ShooterSpeedAuto ShooterSpeed = new ShooterSpeedAuto();
        @Override

        public void init(){
            AprilTagRaw.init(hardwareMap, telemetry);
            AdjustHL.init(hardwareMap);
            AdjustShoot.init(hardwareMap);
            ShooterSpeed.init(hardwareMap);

        }
        @Override
        public void loop() {
                AprilTagRaw.update();
                AprilTagDetection id20 = AprilTagRaw.getTagBySpecificId(20);
                AprilTagRaw.displayDetectionTelemetry(id20);
                AdjustHL.loop(telemetry,AprilTagRaw.TagDistance,ShooterSpeed.error);
                AdjustShoot.loop(telemetry,gamepad1,AprilTagRaw.TagXPos,AprilTagRaw.TagDistance);
                ShooterSpeed.changeSpeed(telemetry,1700);

        }
    }
}
