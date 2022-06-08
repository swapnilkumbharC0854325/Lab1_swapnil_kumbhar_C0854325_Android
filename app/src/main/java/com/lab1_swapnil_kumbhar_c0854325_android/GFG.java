package com.lab1_swapnil_kumbhar_c0854325_android;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

// A Java program to check if a given point
// lies inside a given polygon
// Refer https://www.geeksforgeeks.org/check-if-two-given-line-segments-intersect/
// for explanation of functions onSegment(),
// orientation() and doIntersect()
class GFG {

    // Define Infinite (Using INT_MAX
    // caused overflow problems)
    static int INF = 10000;

    // Given three collinear points p, q, r,
    // the function checks if point q lies
    // on line segment 'pr'
    static boolean onSegment(LatLng p, LatLng q, LatLng r) {
        return q.latitude <= Math.max(p.latitude, r.latitude) &&
                q.latitude >= Math.min(p.latitude, r.latitude) &&
                q.longitude <= Math.max(p.longitude, r.longitude) &&
                q.longitude >= Math.min(p.longitude, r.longitude);
    }

    // To find orientation of ordered triplet (p, q, r).
    // The function returns following values
    // 0 --> p, q and r are collinear
    // 1 --> Clockwise
    // 2 --> Counterclockwise
    static int orientation(LatLng p, LatLng q, LatLng r) {
        double val = (q.longitude - p.longitude) * (r.latitude - q.latitude)
                - (q.latitude - p.latitude) * (r.longitude - q.longitude);

        if (val == 0f) {
            return 0; // collinear
        }
        return (val > 0) ? 1 : 2; // clock or counterclock wise
    }

    // The function that returns true if
    // line segment 'p1q1' and 'p2q2' intersect.
    static boolean doIntersect(LatLng p1, LatLng q1,
                               LatLng p2, LatLng q2) {
        // Find the four orientations needed for
        // general and special cases
        int o1 = orientation(p1, q1, p2);
        int o2 = orientation(p1, q1, q2);
        int o3 = orientation(p2, q2, p1);
        int o4 = orientation(p2, q2, q1);

        // General case
        if (o1 != o2 && o3 != o4) {
            return true;
        }

        // Special Cases
        // p1, q1 and p2 are collinear and
        // p2 lies on segment p1q1
        if (o1 == 0 && onSegment(p1, p2, q1)) {
            return true;
        }

        // p1, q1 and p2 are collinear and
        // q2 lies on segment p1q1
        if (o2 == 0 && onSegment(p1, q2, q1)) {
            return true;
        }

        // p2, q2 and p1 are collinear and
        // p1 lies on segment p2q2
        if (o3 == 0 && onSegment(p2, p1, q2)) {
            return true;
        }

        // p2, q2 and q1 are collinear and
        // q1 lies on segment p2q2
        return o4 == 0 && onSegment(p2, q1, q2);

        // Doesn't fall in any of the above cases
    }

    // Returns true if the point p lies
    // inside the polygon[] with n vertices
    static boolean isInside(List<LatLng> polygon, int n, LatLng p) {
        // There must be at least 3 vertices in polygon[]
        if (n < 3) {
            return false;
        }

        // Create a point for line segment from p to infinite
        LatLng extreme = new LatLng(INF, p.longitude);

        // Count intersections of the above line
        // with sides of polygon
        int count = 0, i = 0;
        do {
            int next = (i + 1) % n;

            // Check if the line segment from 'p' to
            // 'extreme' intersects with the line
            // segment from 'polygon[i]' to 'polygon[next]'
            if (doIntersect(polygon.get(i), polygon.get(next), p, extreme)) {
                // If the point 'p' is collinear with line
                // segment 'i-next', then check if it lies
                // on segment. If it lies, return true, otherwise false
                if (orientation(polygon.get(i), p, polygon.get(next)) == 0) {
                    return onSegment(polygon.get(i), p,
                            polygon.get(next));
                }

                count++;
            }
            i = next;
        } while (i != 0);

        // Return true if count is odd, false otherwise
        return (count % 2 == 1); // Same as (count%2 == 1)
    }

//    public static void main(String[] args) {
//        Point[] polygon1 = {new Point(0, 0),
//                new Point(10, 0),
//                new Point(10, 10),
//                new Point(0, 10)};
//        int n = polygon1.length;
//        Point p = new Point(20, 20);
//        if (isInside(polygon1, n, p)) {
//            System.out.println("Yes");
//        } else {
//            System.out.println("No");
//        }
//        p = new Point(5, 5);
//        if (isInside(polygon1, n, p)) {
//            System.out.println("Yes");
//        } else {
//            System.out.println("No");
//        }
//        Point[] polygon2 = {new Point(0, 0),
//                new Point(5, 5), new Point(5, 0)};
//        p = new Point(3, 3);
//        n = polygon2.length;
//        if (isInside(polygon2, n, p)) {
//            System.out.println("Yes");
//        } else {
//            System.out.println("No");
//        }
//        p = new Point(5, 1);
//        if (isInside(polygon2, n, p)) {
//            System.out.println("Yes");
//        } else {
//            System.out.println("No");
//        }
//        p = new Point(8, 1);
//        if (isInside(polygon2, n, p)) {
//            System.out.println("Yes");
//        } else {
//            System.out.println("No");
//        }
//        Point[] polygon3 = {new Point(0, 0),
//                new Point(10, 0),
//                new Point(10, 10),
//                new Point(0, 10)};
//        p = new Point(-1, 10);
//        n = polygon3.length;
//        if (isInside(polygon3, n, p)) {
//            System.out.println("Yes");
//        } else {
//            System.out.println("No");
//        }
//    }
}

// This code is contributed by 29AjayKumar
