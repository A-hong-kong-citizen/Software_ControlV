package org.firstinspires.ftc.teamcode.AutoRoutines;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.pedropathing.util.Timer;

import org.firstinspires.ftc.teamcode.TurretAuto.turretTrack;
import org.firstinspires.ftc.teamcode.TurretAuto.ShooterStateMachine;
import org.firstinspires.ftc.teamcode.TurretAuto.ShooterSpeedAuto;

import org.firstinspires.ftc.teamcode.Pedropathing.Constants;

@Autonomous
public class sampleAutoPathing extends OpMode {
    private Follower follower;
    private Timer pathTimer, opModeTimer;

    //Init All Shooter Logic
    private turretTrack turretTrack = new turretTrack();
    private ShooterStateMachine shooter = new ShooterStateMachine();
    public boolean shotsTriggered = false;
    private int pickUpLoc = 1;
    private ShooterSpeedAuto shooterSpeedAuto = new ShooterSpeedAuto();

    public enum PathState {
        // START POSITION_END POSITION
        // DRIVE > MOVEMENT STATE
        // SHOOT > ATTEMPT TO SHOOT ARTIFACT
         DRIVE_STARTPOS_SHOOTPOS,
        SHOOT_PRELOAD,
        DRIVE_SHOOTPOS_PICKUP1,

        DRIVE_PICKUP1_PICKUP_END1,
        DRIVE_PICKUP_END1_SHOOTPOS,
        DRIVE_SHOOTPOS_PICKUP2,
        DRIVE_PICKUP2_PICKUP2END,
        DRIVE_PICKUP2END_SHOOTPOS
    }

    PathState pathState;

    private final Pose startPose = new Pose(21.695716395864103, 121.02806499261447, Math.toRadians(180));
    private final Pose shootPose = new Pose(55.4926764, 90.0865512, Math.toRadians(180));
    private final Pose pickup1 = new Pose(51.18375499, 84.8415446, Math.toRadians(180));
    private final Pose pickUp1End =new Pose(15.5539280,84.5086551,Math.toRadians(180));
    private final Pose pickUp2 = new Pose(43.52141802067946,59.88774002954208,Math.toRadians(180));
    private final Pose pickUp2End = new Pose(13.787296898079763,59.601181683899554,Math.toRadians(180));


    private PathChain driveStartPosShootPos, driveShootPosPickupPos1, drivePickUp1toEnd,drivePickUp1EndtoShootPos,driveShootPosPickUp2End, drivePickUp2toPickUp2End,drivePickUp2EndtoShootPos;

    public void buildPath() {
        // put in coords for start and end pose
        driveStartPosShootPos = follower.pathBuilder()
                .addPath(new BezierLine(startPose, shootPose))
                .setLinearHeadingInterpolation(startPose.getHeading(), shootPose.getHeading())
                .build();
        driveShootPosPickupPos1 = follower.pathBuilder()
                .addPath(new BezierLine(shootPose,pickup1))
                .setLinearHeadingInterpolation(shootPose.getHeading(), pickup1.getHeading())
                .addPath(new BezierLine(pickup1,pickUp1End) )
                .setLinearHeadingInterpolation(shootPose.getHeading(), pickUp1End.getHeading())
                .build();
        drivePickUp1EndtoShootPos = follower.pathBuilder()
                .addPath(new BezierLine(pickUp1End,shootPose))
                .setLinearHeadingInterpolation(pickUp1End.getHeading(), shootPose.getHeading())
                .build();
        driveShootPosPickUp2End = follower.pathBuilder()
                .addPath(new BezierLine(shootPose,pickUp2))
                .setLinearHeadingInterpolation(shootPose.getHeading(), pickUp2.getHeading())
                .addPath(new BezierLine(pickUp2,pickUp2End))
                .setLinearHeadingInterpolation(pickUp2.getHeading(),pickUp2End.getHeading())
                .build();
        drivePickUp2EndtoShootPos= follower.pathBuilder()
                .addPath(new BezierLine(pickUp2End,shootPose))
                .setLinearHeadingInterpolation(pickUp2End.getHeading(), shootPose.getHeading())
                .build();


    }

    public void statePathUpdate() {
        switch(pathState) {
            case DRIVE_STARTPOS_SHOOTPOS:
                follower.followPath(driveStartPosShootPos, true);
                setPathState(PathState.SHOOT_PRELOAD);
                break;
            case SHOOT_PRELOAD:
                if (!follower.isBusy()){
                    //shots requested?
                    if (!shotsTriggered){
                        shooter.fireShots(true);
                        shotsTriggered =true;
                    }
                    else if(shotsTriggered && !shooter.isBusy()){
                        if (pickUpLoc == 1){
                            follower.followPath(driveShootPosPickupPos1, true);
                            shotsTriggered =false;
                            setPathState(PathState.DRIVE_SHOOTPOS_PICKUP1);
                            shooter.intakeArtifacts();
                        } else if (pickUpLoc == 2){
                            follower.followPath(driveShootPosPickupPos1, true);
                            shotsTriggered =false;
                            setPathState(PathState.DRIVE_SHOOTPOS_PICKUP2);
                            shooter.intakeArtifacts();
                        } else if (pickUpLoc == 3){

                        }

                    }
                }
                //check is follower done its path?
            break;
            case DRIVE_SHOOTPOS_PICKUP1:
                if(!follower.isBusy()) {
                    follower.followPath(drivePickUp1EndtoShootPos,true);
                    setPathState(PathState.DRIVE_PICKUP_END1_SHOOTPOS);
                    shooter.stopIntake();
                }

            default:
                telemetry.addLine("no state commanded");
                break;
            case DRIVE_PICKUP_END1_SHOOTPOS:
                if(!follower.isBusy()){
                    pickUpLoc += 1;
                    setPathState(PathState.SHOOT_PRELOAD);
                }
            break;
            case DRIVE_SHOOTPOS_PICKUP2:
                if ((!follower.isBusy())){
                    follower.followPath(drivePickUp2EndtoShootPos,true);
                    shooter.stopIntake();
                    setPathState(PathState.DRIVE_PICKUP2END_SHOOTPOS);
                }
            break;
            case DRIVE_PICKUP2END_SHOOTPOS:
                if(!follower.isBusy()){
                    pickUpLoc += 1;
                    setPathState(PathState.SHOOT_PRELOAD);
                }

        }
    }

    public void setPathState(PathState newState) {
        pathState = newState;
        pathTimer.resetTimer();
        shotsTriggered = false;

    }

    @Override
    public void init() {
        pathState = PathState.DRIVE_STARTPOS_SHOOTPOS;
        pathTimer = new Timer();
        opModeTimer = new Timer();
        follower = Constants.createFollower(hardwareMap);
        shooter.init(hardwareMap, telemetry);
        shooterSpeedAuto.init(hardwareMap);
        //turretTrack.init(hardwareMap);

        buildPath();
        follower.setPose(startPose);
    }

    public void start() {
        opModeTimer.resetTimer();
        setPathState(pathState);
    }

    @Override
    public void loop() {
        follower.update();
        shooter.update(telemetry,gamepad2);
        statePathUpdate();
        double curVelocity = shooterSpeedAuto.curVelocity;



        telemetry.addData("path state", pathState.toString());
        telemetry.addData("Path time", pathTimer.getElapsedTimeSeconds());
        telemetry.addData("current flywheel velocity", curVelocity);
    }
}
