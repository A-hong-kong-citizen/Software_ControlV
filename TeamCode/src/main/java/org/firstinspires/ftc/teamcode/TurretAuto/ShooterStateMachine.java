package org.firstinspires.ftc.teamcode.TurretAuto;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.AprilTagDetection.AprilTagWebCam;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

public class ShooterStateMachine {
    private Servo hood;
    private Servo gate;
    private Servo push;
    private DcMotor intake;

    private ElapsedTime stateTimer = new ElapsedTime();
    private getHoodAngle hoodAngle = new getHoodAngle();
    private ShooterSpeedAuto shooterSpeed = new ShooterSpeedAuto();

    private TriggerControl canShoot = new TriggerControl();
    private turretTrack turret = new turretTrack();
    private AprilTagWebCam aprilTag = new AprilTagWebCam();
    private TurretAdjust turretAdjust = new TurretAdjust();
    private enum robotState {
        IDLE,
        SPIN_UP,
        HOOD_ANGLE,
        SHOOT,
        SPIN_INTAKE,
        SERVO_PUSH,
        RESET,
        INTAKE,
    }
    private robotState RobotState;

    private double gateCloseAngle = 0.4;
    private double gateOpenAngle = 0.8;
    private double gateOpenTime = 0.2;
    private double gateCloseTime = 0.2;
    private double pushUpAngle = -1;
    private double pushDownAngle = 0.7;
    private double pushTime = 1.0;

    private double flywheelSpinupTime = 2;

    private boolean NeedShoot;
    private boolean NeedIntake;
    public void init(HardwareMap haMap, Telemetry telemetry){
        hood = haMap.get(Servo.class, "hood");
        gate = haMap.get(Servo.class, "block");
        push = haMap.get(Servo.class, "push");
        intake = haMap.get(DcMotor.class,"intake");
        shooterSpeed.init(haMap);
        aprilTag.init(haMap,telemetry);
        turretAdjust.init(haMap);

        RobotState = robotState.IDLE;

        gate.setPosition(gateCloseAngle);
        push.setPosition(pushDownAngle);
    }

    public void update(Telemetry telemetry, Gamepad gamepad2) {
        int index;
        AprilTagDetection id20 = aprilTag.getTagBySpecificId(20);
        aprilTag.displayDetectionTelemetry(id20);
        aprilTag.update();
        index = hoodAngle.getInex(aprilTag.TagDistance);
        telemetry.addData("Index",hoodAngle.getInex(aprilTag.TagDistance));
        switch (RobotState) {
            case IDLE:
                gate.setPosition(gateCloseAngle);
                shooterSpeed.changeSpeed(telemetry, 1200);
                push.setPosition(pushDownAngle);
                if (NeedShoot) {
                        stateTimer.reset();
                        RobotState = robotState.SPIN_UP;
                }
                if (NeedIntake){
                    stateTimer.reset();
                    RobotState=robotState.INTAKE;
                }
                break;
            case SPIN_UP:
                aprilTag.displayDetectionTelemetry(id20);
                aprilTag.update();
                turretAdjust.loop(telemetry,gamepad2, aprilTag.TagXPos, aprilTag.TagDistance);
                shooterSpeed.changeSpeed(telemetry, canShoot.getFlywheelSpeed( aprilTag.TagDistance));
                if (stateTimer.seconds() > flywheelSpinupTime ) {
                    stateTimer.reset();
                    RobotState = robotState.HOOD_ANGLE;
                }
                break;
            case HOOD_ANGLE:
                turretAdjust.loop(telemetry,gamepad2, aprilTag.TagXPos, aprilTag.TagDistance);
                hood.setPosition(hoodAngle.hoodAngle(aprilTag.TagDistance));
                if (stateTimer.seconds() > 0.2) {
                    stateTimer.reset();
                    RobotState = robotState.SHOOT;
                }
                break;
            case SHOOT:
                aprilTag.displayDetectionTelemetry(id20);
                aprilTag.update();
                index = hoodAngle.getInex(aprilTag.TagDistance);
                turretAdjust.loop(telemetry,gamepad2, aprilTag.TagXPos, aprilTag.TagDistance);
                gate.setPosition(gateOpenAngle);
                hood.setPosition(hoodAngle.hoodAngle(aprilTag.TagDistance));
                if (stateTimer.seconds() > gateOpenTime) {
                    stateTimer.reset();
                    RobotState = robotState.SPIN_INTAKE;
                }
                break;
            case SPIN_INTAKE:
                aprilTag.displayDetectionTelemetry(id20);
                aprilTag.update();
                index = hoodAngle.getInex(aprilTag.TagDistance);
                turretAdjust.loop(telemetry,gamepad2, aprilTag.TagXPos, aprilTag.TagDistance);
                intake.setPower(0.8);
                hood.setPosition(hoodAngle.hoodAngle(aprilTag.TagDistance));
                if (stateTimer.seconds() > 1.5) {
                    stateTimer.reset();
                    RobotState = robotState.SERVO_PUSH;
                }
                break;
            case SERVO_PUSH:
                aprilTag.displayDetectionTelemetry(id20);
                aprilTag.update();
                index = hoodAngle.getInex(aprilTag.TagDistance);
                turretAdjust.loop(telemetry,gamepad2, aprilTag.TagXPos, aprilTag.TagDistance);
                push.setPosition(pushUpAngle);
                hood.setPosition(hoodAngle.hoodAngle(aprilTag.TagDistance));
                if (stateTimer.seconds() > pushTime) {
                    stateTimer.reset();
                    RobotState = robotState.RESET;
                }
                break;
            case RESET:
                push.setPosition(pushDownAngle);
                gate.setPosition(gateCloseAngle);
                NeedShoot = false;
                if(stateTimer.seconds()>1.5) {
                    stateTimer.reset();
                    shooterSpeed.changeSpeed(telemetry, 900);
                    RobotState = robotState.IDLE;
                }
                break;
            case INTAKE:
                intake.setPower(1);
                gate.setPosition(gateCloseAngle);
                push.setPosition(pushDownAngle);
                if (stateTimer.seconds() > 2) {
                    stateTimer.reset();
                    RobotState = robotState.IDLE;
                    NeedIntake = false;
                }
        }
    }

    public void fireShots(boolean shots) {
        if(RobotState == robotState.IDLE){
            NeedShoot = true;

        }

    }
    public void intakeArtifacts(){
        if(RobotState == robotState.IDLE){
            NeedIntake = true;
        }

    }
    public void stopIntake(){
        if(RobotState == robotState.INTAKE){
            NeedIntake = false;
            RobotState = robotState.IDLE;
        }

    }
    public boolean isBusy(){
        return RobotState != robotState.IDLE;
    }


}
