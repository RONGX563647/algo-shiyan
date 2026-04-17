package com.rongx.exp1;

import java.util.Scanner;

/**
 * ============================================================================
 * 实验一：整数集合分解
 * ============================================================================
 * 
 * 【题目描述】
 * 将n个正整数的集合分成两个子集S1和S2，每个子集含有n/2个元素，
 * 使S1中所有元素的和与S2中所有元素的和的差最大。
 * 
 * 【输入格式】
 * 第一行：正整数m，表示测试例个数
 * 每个测试例包含两行：
 *   - 第一行：正整数n (n≤500)，表示原整数集合的长度
 *   - 第二行：n个整数序列，整数之间用一个空格隔开
 * 
 * 【输出格式】
 * 第一行：元素和较小的整数集合（从小到大排序）
 * 第二行：元素和较大的整数集合（从小到大排序）
 * 两个测试例之间用一个空行隔开
 * 
 * 【算法策略】
 * 贪心策略：排序后，前n/2个元素为较小集合，后n/2个元素为较大集合。
 * 使用自实现的快速排序算法对数组进行排序。
 * 
 * 【复杂度分析】
 * 时间复杂度：O(n log n) 快速排序
 * 空间复杂度：O(n) 存储分割后的数组
 * 
 * ============================================================================
 */
public class SetPartition {

    /**
     * 快速排序主方法
     * 
     * @param arr 待排序数组
     * 
     * 【快速排序原理】
     * 分治算法：
     * 1. 选择一个基准元素（pivot）
     * 2. 分区：小于pivot放左边，大于pivot放右边
     * 3. 递归处理左右两个子数组
     */
    public static void quickSort(int[] arr) {
        // 边界条件：空数组或单元素数组已有序
        if (arr == null || arr.length <= 1) {
            return;
        }
        // 调用递归排序方法
        quickSort(arr, 0, arr.length - 1);
    }

    /**
     * 快速排序递归方法
     * 
     * @param arr 待排序数组
     * @param low 起始索引
     * @param high 结束索引
     * 
     * 【递归流程】
     * 1. 分区操作，获取基准元素最终位置
     * 2. 递归排序左半部分（low 到 pivotIndex-1）
     * 3. 递归排序右半部分（pivotIndex+1 到 high）
     */
    private static void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            // 分区并获取基准元素位置
            int pivotIndex = partition(arr, low, high);
            
            // 递归排序左半部分（小于pivot的元素）
            quickSort(arr, low, pivotIndex - 1);
            
            // 递归排序右半部分（大于pivot的元素）
            quickSort(arr, pivotIndex + 1, high);
        }
    }

    /**
     * 分区函数：核心操作
     * 
     * @param arr 待排序数组
     * @param low 起始索引
     * @param high 结束索引（pivot所在位置）
     * @return 基准元素的最终位置
     * 
     * 【分区策略】
     * 选择最右元素arr[high]作为基准
     * 
     * 【分区过程】
     * 维护两个指针：
     * - i：指向小于pivot的最后一个元素位置
     * - j：遍历指针，扫描所有元素
     * 
     * 【分区示意】
     * 原始: [68, 25, 34, 16, 2, 37, 3, 95(pivot)]
     *       i=-1, j从0开始遍历
     * 
     * 遍历过程：
     * - j=0: 68<=95, i++, swap(0,0), i=0
     * - j=1: 25<=95, i++, swap(1,1), i=1
     * - ...继续遍历...
     * - 最后: swap(i+1, high), pivot放到正确位置
     * 
     * 结果: [2, 3, 16, 25, 34, 37, 68, 95]
     *       pivot位置=7
     */
    private static int partition(int[] arr, int low, int high) {
        // 选择最右元素作为基准
        int pivot = arr[high];
        
        // i指向小于pivot的最后一个元素位置
        // 初始为low-1，表示还没有找到小于pivot的元素
        int i = low - 1;

        // j遍历从low到high-1的所有元素
        for (int j = low; j < high; j++) {
            // 如果当前元素小于等于pivot
            if (arr[j] <= pivot) {
                // i向前移动一位
                i++;
                // 将小于pivot的元素交换到i位置
                swap(arr, i, j);
            }
        }

        // 将pivot放到正确位置（i+1）
        // 此时：arr[low..i] < pivot, arr[i+1] = pivot, arr[i+2..high] > pivot
        swap(arr, i + 1, high);
        
        // 返回pivot的最终位置
        return i + 1;
    }

    /**
     * 交换数组中两个元素
     * 
     * @param arr 数组
     * @param i 第一个元素索引
     * @param j 第二个元素索引
     * 
     * 【优化】
     * 只有i!=j时才交换，避免不必要的操作
     */
    private static void swap(int[] arr, int i, int j) {
        if (i != j) {
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
    }

    /**
     * 复制数组
     * 
     * @param arr 原数组
     * @return 复制后的新数组
     * 
     * 【目的】
     * 避免修改原数组，保持数据完整性
     */
    public static int[] copyArray(int[] arr) {
        int[] copy = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            copy[i] = arr[i];
        }
        return copy;
    }

    /**
     * 分割数组，使两个子集的和差最大
     * 
     * @param arr 输入数组
     * @return 二维数组，result[0]为较小集合，result[1]为较大集合
     * 
     * 【贪心策略】
     * 要使两个子集和差最大：
     * - 较小集合应包含最小的n/2个元素
     * - 较大集合应包含最大的n/2个元素
     * 
     * 【正确性证明】
     * 设排序后数组为 a[0] ≤ a[1] ≤ ... ≤ a[n-1]
     * 
     * S1 = {a[0], ..., a[n/2-1]}（最小的一半）
     * S2 = {a[n/2], ..., a[n-1]}（最大的一半）
     * 
     * 对于任意其他分割方式，如果交换S2中x与S1中y（y<x）：
     * 新差值 = 原差值 - 2(x-y) < 原差值
     * 因此贪心策略确实能获得最大差值
     */
    public static int[][] partition(int[] arr) {
        int n = arr.length;

        // 复制数组避免修改原数组
        int[] sorted = copyArray(arr);

        // 使用自实现的快速排序
        quickSort(sorted);

        // 分割数组：前半部分为较小元素，后半部分为较大元素
        int half = n / 2;
        int[] smaller = new int[half];  // 较小集合
        int[] larger = new int[half];   // 较大集合

        // 复制前半部分（较小元素）
        for (int i = 0; i < half; i++) {
            smaller[i] = sorted[i];
        }

        // 复制后半部分（较大元素）
        for (int i = 0; i < half; i++) {
            larger[i] = sorted[half + i];
        }

        return new int[][]{smaller, larger};
    }

    /**
     * 计算数组元素之和
     * 
     * @param arr 数组
     * @return 元素之和
     */
    public static int sum(int[] arr) {
        int total = 0;
        for (int num : arr) {
            total += num;
        }
        return total;
    }

    /**
     * 打印数组元素（空格分隔）
     * 
     * @param arr 数组
     */
    public static void printArray(int[] arr) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            if (i > 0) sb.append(" ");
            sb.append(arr[i]);
        }
        System.out.println(sb.toString());
    }

    /**
     * 主方法：处理多个测试用例
     * 
     * 【输入处理流程】
     * 1. 读取测试例个数m
     * 2. 对于每个测试例：
     *    - 读取数组长度n
     *    - 读取n个整数到数组
     *    - 调用partition分割数组
     *    - 输出两个子集
     *    - 测试例之间用空行隔开
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // 读取测试例个数
        int m = scanner.nextInt();

        // 处理每个测试例
        for (int i = 0; i < m; i++) {
            // 读取数组长度
            int n = scanner.nextInt();

            // 读取数组元素
            int[] arr = new int[n];
            for (int j = 0; j < n; j++) {
                arr[j] = scanner.nextInt();
            }

            // 分割数组
            int[][] result = partition(arr);
            int[] smaller = result[0];  // 较小集合
            int[] larger = result[1];   // 较大集合

            // 输出结果（已按从小到大排序）
            printArray(smaller);
            printArray(larger);

            // 两个测试例之间用空行隔开（最后一个测试例后不需要空行）
            if (i < m - 1) {
                System.out.println();
            }
        }

        scanner.close();
    }
}

/**
 * ============================================================================
 * 【算法设计总结】
 * ============================================================================
 * 
 * 1. 【贪心策略分析】
 *    问题目标：使两个子集和差最大
 *    贪心选择：最小元素放一组，最大元素放另一组
 *    正确性：任何交换都会减小差值，故贪心最优
 * 
 * 2. 【快速排序原理】
 *    分治思想：
 *    - 分解：选择pivot，分区操作
 *    - 解决：递归排序左右子数组
 *    - 合并：无需合并，原地排序
 * 
 * 3. 【分区操作详解】
 *    - 选择基准：arr[high]作为pivot
 *    - 双指针法：i指向小于pivot区域末尾，j遍历数组
 *    - 交换策略：遇到小于pivot的元素，i++并交换
 *    - 最终放置：pivot放到i+1位置
 * 
 * 4. 【复杂度分析】
 *    时间复杂度：
 *    - 最好情况：O(n log n)，每次均匀分割
 *    - 平均情况：O(n log n)，随机数据
 *    - 最坏情况：O(n²)，已排序或逆序数组
 *    
 *    空间复杂度：
 *    - O(log n)：递归调用栈深度
 *    - 原地排序：无需额外数组空间
 * 
 * 5. 【优化方向】
 *    - 随机化基准：避免最坏情况
 *    - 三数取中：更好的基准选择
 *    - 小数组优化：n<10时用插入排序
 *    - 三路分区：处理大量重复元素
 * 
 * 6. 【与其他排序对比】
 *    - 归并排序：稳定，但需要O(n)额外空间
 *    - 堆排序：稳定O(n log n)，但实际效率不如快排
 *    - 快速排序：平均最快，但不稳定
 * 
 * ============================================================================
 */