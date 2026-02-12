
// this method returns the target hood angle when the speed of flywheel and target distance is inputted.
// the code is current untested and written on 5/1
// also make sure that the current flywheel velocity is in between the upper and low bounds of the shooting speed
// this should be done before this method is called and before shooting proceeds.
package org.firstinspires.ftc.teamcode.TurretAuto;

public class getHoodAngle {

    // ax^4
    public final double[] a = {
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0
    };

    // bx^3
    private final double[] b = {
            0,
            -0.000002559458,
            0.0000000367235,
            0,
            -0.0000000319444,
            0,
            -0.0000001666667,
            0.000003212596,
            0.000000226422,
            0.0000000136091,
            0.0000000353558,
            -0.0000047049175,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0
    };

    // cx^2
    private final double[] c = {
            0.0000160714,
            0.004939826,
            -0.0000543466,
            -0.00000416667,
            0.000137167,
            0.0000263889,
            0.00071,
            -0.006784047,
            -0.000335699,
            -0.0000172352,
            -0.0000437869,
            0.010939158,
            0.000036363,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0

    };

    // dx
    private final double[] d = {
            -0.0403929,
            -4.22968,
            0.0273866,
            0.00898333,
            -0.196414,
            -0.0724722,
            -1.005933,
            6.367063,
            0.166228,
            0.00358857,
            0.0173945,
            -11.29304,
            -0.111273,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0

    };

    // e
    private final double[] e = {
            26.08,
            1356.75772,
            0,
            -3.91667,
            94.40833,
            50.45667,
            474.896,
            -2240.17523,
            0,
            4.42877,
            0,
            4368.31875,
            85.69091,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0
    };
    private double highestPos[] = {
            1,
            0.95,
            0.84,
            0.84,
            0.7,
            1,
            1,
            1,
            1,
            1,
            1,
            1,
            0.8,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0


    };
    private double lowestPos[] = {
            0.7,
            0.6,
            0.6,
            0.6,
            0.6,
            0.7,
            0.8,
            0.6,
            0.85,
            0.8,
            0.75,
            0.82,
            0.6,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0

    };
    public double calculatedAngle;

    public int getInex(double tagDistance){
        double calculatedAngle;
        tagDistance = Math.round(tagDistance);
        int index = (int) tagDistance - 80;
        //scale from 0,10,20,30 to 0,1,2,3
        index = index/10;
        return index;

    }
    public double hoodAngle(double AprilTagDistance){


        // calculate ax^4 + bx^3 + cx^2 + dx^1 + e = y
        // when x = current flywheel speed
        // when y = hood angle
        calculatedAngle =
                -0.0000000250583 * AprilTagDistance * AprilTagDistance * AprilTagDistance * AprilTagDistance +
                        0.000012028* AprilTagDistance * AprilTagDistance * AprilTagDistance -
                        0.00216597* AprilTagDistance * AprilTagDistance +
                        0.171177* AprilTagDistance -
                        4.18473;

                return calculatedAngle;

    }
}
