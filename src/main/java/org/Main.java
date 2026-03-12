package org;

import java.util.*;

class Point {
    int x, y;

    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

public class Main {

    // Hàm tính tích hữu hướng (cross product)
    static int cross(Point a, Point b, Point c) {
        return (b.x - a.x) * (c.y - a.y) - (b.y - a.y) * (c.x - a.x);
    }

    // Hàm tính khoảng cách giữa hai điểm
    static double distance(Point a, Point b) {
        return Math.sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y));
    }

    static List<Integer> best = new ArrayList<>();
    // Backtracking để tìm dãy con dài nhất có tổng bằng k
    static void backtrack(int[] a, int n, int k, int index,
                          List<Integer> current, int sum) {

        if (sum == k) {
            if (current.size() > best.size()) {
                best = new ArrayList<>(current);
            }
            return;
        }

        if (sum > k || index == n) return;

        // chọn
        current.add(a[index]);
        backtrack(a, n, k, index + 1, current, sum + a[index]);

        // không chọn
        current.remove(current.size() - 1);
        backtrack(a, n, k, index + 1, current, sum);
    }

    // Bai1 - Tính diện tích hình tròn bằng phương pháp Monte Carlo
    public static void Bai1() {
        System.out.println("Chuong trinh cho Bai 1");
        Scanner sc = new Scanner(System.in);
        System.out.print("Hay nhap gia tri cua ban kinh r: ");
        double r = sc.nextDouble();
        System.out.println("Ket qua cua bai tap 1:");
        int n = 100000;
        int count = 0;

        Random rand = new Random();
        for (int i = 0; i < n; i++) {
            double x = 2 * r * rand.nextDouble() - r;
            double y = 2 * r * rand.nextDouble() - r;
            if (x * x + y * y <= r * r) {
                count++;
            }
        }

        double dt = 4.0 * r * r * count / n;
        System.out.println("Dien tich xap xi cua hinh tron: " + dt);
    }

    // Bai2 - Tính diện tích hình tròn trong đơn vị bán kính 1
    public static void Bai2() {
        System.out.println("Chuong trinh cho Bai 2");
        System.out.println("Ket qua cua bai tap 2:");
        int n = 100000;
        int count = 0;
        double r = 1.0;

        Random rand = new Random();
        for (int i = 0; i < n; i++) {
            double x = 2 * r * rand.nextDouble() - r;
            double y = 2 * r * rand.nextDouble() - r;
            if (x * x + y * y <= r * r) {
                count++;
            }
        }

        double dt = 4.0 * r * r * count / n;
        System.out.println("Dien tich xap xi cua hinh tron: " + dt);
    }

    // Bai3 - Tìm các trạm cảnh báo (Convex Hull)
    public static void Bai3() {
        System.out.println("Chuong trinh cho Bai 3");
        System.out.println("Ket qua cua bai tap 3:");
        System.out.println("Nhập vào n số tọa độ");
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        ArrayList<Point> points = new ArrayList<>();

        // Nhập tọa độ các điểm
        for (int i = 0; i < n; i++) {
            System.out.print("Nhập vào tọa độ x: ");
            int x = sc.nextInt();
            System.out.print("Nhập vào tọa độ y: ");
            int y = sc.nextInt();
            points.add(new Point(x, y));
        }

        // Tìm điểm có tọa độ thấp nhất để làm điểm bắt đầu
        Point pivot = points.get(0);
        for (Point p : points) {
            if (p.y < pivot.y || (p.y == pivot.y && p.x < pivot.x)) {
                pivot = p;
            }
        }

        Point start_point = pivot;
        // Sắp xếp các điểm còn lại theo góc với điểm pivot
        points.sort((a, b) -> {
            int crossProd = cross(start_point, a, b);
            if (crossProd == 0) {
                return Double.compare(distance(start_point, a), distance(start_point, b));
            }
            return -crossProd;
        });

        // Sử dụng stack để tìm bao lồi
        Stack<Point> stack = new Stack<>();
        stack.push(pivot);
        stack.push(points.get(0));

        for (int i = 1; i < points.size(); i++) {
            Point top = stack.peek();
            while (stack.size() >= 2 && cross(stack.get(stack.size() - 2), stack.peek(), points.get(i)) <= 0) {
                stack.pop();
                top = stack.peek();
            }
            stack.push(points.get(i));
        }

        // In kết quả các trạm cảnh báo (convex hull)
        System.out.println("Các trạm cảnh báo (Convex Hull):");
        for (Point pt : stack) {
            System.out.println(pt.x + " " + pt.y);
        }
    }

    public static void Bai4() {
        System.out.println("Chuong trinh cho Bai 4");
        System.out.println("Tim day con dai nhat co tong = k");

        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt();   // số phần tử
        int k = sc.nextInt();   // tổng cần tìm

        int[] a = new int[n];

        for (int i = 0; i < n; i++) {
            a[i] = sc.nextInt();
        }

        best.clear(); // reset kết quả

        backtrack(a, n, k, 0, new ArrayList<>(), 0);

        if (best.isEmpty()) {
            System.out.println("Khong co day nao");
        } else {
            System.out.println("Day con dai nhat:");
            for (int x : best) {
                System.out.print(x + " ");
            }
        }
        System.out.println();
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n===== MENU =====");
            System.out.println("1. Bai 1 - Monte Carlo (nhap r)");
            System.out.println("2. Bai 2 - Monte Carlo (r = 1)");
            System.out.println("3. Bai 3 - Convex Hull");
            System.out.println("4. Bai 4 - Day con tong = k (dai nhat)");
            System.out.println("0. Thoat");
            System.out.print("Chon bai: ");

            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    Bai1();
                    break;
                case 2:
                    Bai2();
                    break;
                case 3:
                    Bai3();
                    break;
                case 4:
                    Bai4();
                    break;
                case 0:
                    System.out.println("Thoat chuong trinh...");
                    break;
                default:
                    System.out.println("Lua chon khong hop le!");
            }

        } while (choice != 0);

        sc.close();
    }
}