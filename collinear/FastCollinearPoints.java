/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class FastCollinearPoints {
    private LineSegment[] segments = null;

    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }

        Point[] pointsCopy = new Point[points.length];

        for (int i = 0; i < points.length; i++) {
            Point p = points[i];

            if (p == null) {
                throw new IllegalArgumentException();
            }

            pointsCopy[i] = p;
        }

        Arrays.sort(pointsCopy);

        List<LineSegment> lineSegments = new LinkedList<>();

        Point previousPoint = null;

        for (int i = 0; i < pointsCopy.length; i++) {
            Point p = pointsCopy[i];

            if (previousPoint != null && p.compareTo(previousPoint) == 0) {
                throw new IllegalArgumentException();
            }
            else {
                previousPoint = p;
            }

            Point[] slopeOrderedPoints = pointsCopy.clone();
            Arrays.sort(slopeOrderedPoints, p.slopeOrder());

            double lastSlope = Double.NEGATIVE_INFINITY;
            int slopeStartIndex = 0;

            for (int j = 1; j < slopeOrderedPoints.length; j++) {
                Point q = slopeOrderedPoints[j];
                double currentSlope = p.slopeTo(q);

                boolean lastPoint = j == slopeOrderedPoints.length - 1;

                if (Double.compare(currentSlope, lastSlope) != 0) {
                    if (j - slopeStartIndex >= 3) {
                        if (p.compareTo(slopeOrderedPoints[slopeStartIndex]) <= 0) {
                            LineSegment segment = new LineSegment(p, slopeOrderedPoints[j - 1]);
                            lineSegments.add(segment);
                        }
                    }

                    slopeStartIndex = j;
                }
                else if (lastPoint) {
                    if (j - slopeStartIndex >= 2) {
                        if (p.compareTo(slopeOrderedPoints[slopeStartIndex]) <= 0) {
                            LineSegment segment = new LineSegment(p, q);
                            lineSegments.add(segment);
                        }
                    }
                }

                lastSlope = currentSlope;
            }
        }

        segments = lineSegments.toArray(new LineSegment[lineSegments.size()]);
    }

    public int numberOfSegments() {
        return segments.length;
    }

    public LineSegment[] segments() {
        return segments.clone();
    }

    public static void main(String[] args) {

    }
}
