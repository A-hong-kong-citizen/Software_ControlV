package org.firstinspires.ftc.teamcode.TurretAuto;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.GoBildaPinpointDriver;

public class turretTrack {
    private DcMotorEx turret;
    public double turretDegrees;
    private double targetX = 0;
    private double targetY = 300;
    GoBildaPinpointDriver odo;
    final double degreesPerTick = -0.102739726;
    public void init(HardwareMap hardwareMap){
        turret = hardwareMap.get(DcMotorEx.class, "turret");
        odo = hardwareMap.get(GoBildaPinpointDriver.class, "pinpoint");
        turret.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        turret.setTargetPosition(0);
        turret.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        odo.resetPosAndIMU();
    }
    public void trackTarget(Telemetry telemetry) {
        turretDegrees = turret.getCurrentPosition() * -0.102739726;
        odo.update();
        Pose2D pos = odo.getPosition();
        double robotX = pos.getX(DistanceUnit.MM);
        double robotY = pos.getY(DistanceUnit.MM);
        double robotHeading = -pos.getHeading(AngleUnit.DEGREES);
        double deltaX = targetX - robotX;
        double deltaY = targetY - robotY;
        double absoluteAngle = -Math.toDegrees(Math.atan2(deltaY, deltaX));
        double turretAngle = absoluteAngle - robotHeading;
        while (turretAngle > 180) turretAngle -= 360;
        while (turretAngle <= -180) turretAngle += 360;
        double limitedAngle = turretAngle;
        if (limitedAngle > 90) {
            limitedAngle = 90;
        } else if (limitedAngle < -90) {
            limitedAngle = -90;
        }
        int targetTicks = (int) Math.round(limitedAngle / degreesPerTick);
        turret.setTargetPosition(targetTicks);
        turret.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        turret.setPower(0.5);
        telemetry.addData("turret position: ", turret.getCurrentPosition());
        telemetry.addData("turret position in degrees: ", turretDegrees);
        telemetry.addData("RobotX: ", pos.getX(DistanceUnit.MM));
        telemetry.addData("Robot Y: ", pos.getY(DistanceUnit.MM));
        telemetry.addData("Robot Heading: ", robotHeading);
        telemetry.addData("Angle to Target: ", absoluteAngle);
        telemetry.addData("Safe Turret Angle: ", limitedAngle);
        telemetry.update();
    }
}
