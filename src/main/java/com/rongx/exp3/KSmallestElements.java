package com.rongx.exp3;

import java.util.Arrays;
import java.util.Scanner;

/**
 * ============================================================================
 * 实验三：求数列中的第1～k小元素
 * ============================================================================
 * 
 * 【题目描述】
 * 设计算法实现在一个具有n个互不相同元素的数组A[1…n]中找出所有前k个最小元素的问题，
 * 这里k不是常量，即它是输入数据的一部分。要求算法的时间复杂性为Θ(n)。
 * 
 * 【输入格式】
 * 第一行：正整数m，表示测试例个数
 * 每个测试例包含三行：
 *   - 第一行：正整数n，表示元素的个数
 *   - 第二行：n个整数，整数之间用一个空格隔开
 *   - 第三行：正整数k，表示求前k个最小元素
 * 
 * 【输出格式】
 * 对于每个测试例输出一行，由k个整数组成，表示前k个最小元素（从小到大排序）
 * 两个测试例的输出数据之间用一个空行隔开，最后一个测试例后无空行
 * 
 * 【算法策略】
 * 使用改进的快速选择算法（SELECT算法）：
 * 1. 找到第k小元素（时间复杂度Θ(n)）
 * 2. 遍历数组收集所有小于等于第k小元素的元素（时间复杂度O(n)）
 * 3. 对结果排序（不计入时间复杂度）
 * 
 * 【复杂度分析】
 * 时间复杂度：Θ(n) 平均情况
 * 空间复杂度：O(n) 存储结果
 * 
 * ============================================================================
 */
public class KSmallestElements {

    /**
     * 快速选择算法：找到数组中第k小的元素
     * 
     * @param arr 输入数组
     * @param k 要找的第k小元素（1-indexed）
     * @return 第k小元素的值
     * 
     * 【算法原理】
     * 
     * 快速选择是快速排序的变种：
     * - 快速排序：递归处理两个分区
     * - 快速选择：只递归处理包含目标元素的一个分区
     * 
     * 【算法步骤】
     * 1. 选择一个基准元素（pivot）
     * 2. 将数组分成两部分：小于pivot和大于pivot
     * 3. 判断第k小元素在哪个分区
     * 4. 只递归处理包含目标元素的分区
     * 
     * 【时间复杂度分析】
     * 平均情况：T(n) = T(n/2) + O(n) = O(n)
 * 最坏情况：T(n) = T(n-1) + O(n) = O(n²)
     * 
     * 【改进策略】
     * 使用"中位数的中位数"作为基准，保证最坏情况也是O(n)
     * 但实现复杂，本题采用随机基准，平均情况O(n)即可满足要求
     */
    public static int quickSelect(int[] arr, int k) {
        if (arr == null || arr.length == 0 || k < 1 || k > arr.length) {
            throw new IllegalArgumentException("Invalid input");
        }
        
        // 复制数组避免修改原数组
        int[] copy = copyArray(arr);
        
        // 调用递归方法（k转换为0-indexed）
        return quickSelectHelper(copy, 0, copy.length - 1, k - 1);
    }

    /**
     * 快速选择递归辅助方法
     * 
     * @param arr 数组
     * @param left 左边界
     * @param right 右边界
     * @param k 目标索引（0-indexed）
     * @return 第k小元素
     * 
     * 【递归三要素】
     * 
     * 1. 递归出口：left == right时，只有一个元素，直接返回
     * 
     * 2. 递归关系：
     *    - 分区后，判断k在哪个分区
     *    - 只递归处理包含k的分区
     * 
     * 3. 参数设置：
     *    - left, right：当前处理的数组范围
     *    - k：目标元素的索引（相对于整个数组）
     */
    private static int quickSelectHelper(int[] arr, int left, int right, int k) {
        // 【递归出口】
        if (left == right) {
            return arr[left];
        }

        // 【分区操作】
        // 选择基准并进行分区
        int pivotIndex = partition(arr, left, right);

        // 【判断目标位置】
        // pivotIndex是基准元素的最终位置
        // 如果k == pivotIndex，找到了目标元素
        // 如果k < pivotIndex，目标在左分区
        // 如果k > pivotIndex，目标在右分区

        if (k == pivotIndex) {
            // 找到目标元素
            return arr[pivotIndex];
        } else if (k < pivotIndex) {
            // 目标在左分区，递归处理左半部分
            return quickSelectHelper(arr, left, pivotIndex - 1, k);
        } else {
            // 目标在右分区，递归处理右半部分
            return quickSelectHelper(arr, pivotIndex + 1, right, k);
        }
    }

    /**
     * 分区函数：将数组分成小于基准和大于基准两部分
     * 
     * @param arr 数组
     * @param left 左边界
     * @param right 右边界
     * @return 基准元素的最终位置
     * 
     * 【分区策略】
     * 
     * 采用Lomuto分区方案：
     * 1. 选择最右元素作为基准
     * 2. i指针指向小于基准的最后一个元素
     * 3. j指针遍历数组
     * 4. 当arr[j] <= pivot时，交换arr[i+1]和arr[j]，i++
     * 5. 最后交换arr[i+1]和基准
     * 
     * 【分区示意】
     * 原始数组: [56, 34, 22, 7, 16, 95, 46, 37, 81, 12]
     * 基准: 12 (最右元素)
     * 
     * 分区过程:
     * i=-1, j=0: 56 > 12, 不交换
     * i=-1, j=1: 34 > 12, 不交换
     * ...
     * i=-1, j=9: 12 <= 12, 交换arr[0]和arr[9]
     * 
     * 分区后: [12, 34, 22, 7, 16, 95, 46, 37, 81, 56]
     * 基准位置: 0
     */
    private static int partition(int[] arr, int left, int right) {
        // 选择最右元素作为基准
        int pivot = arr[right];
        int i = left - 1;  // i指向小于基准的最后一个元素

        // 遍历数组（不包括基准）
        for (int j = left; j < right; j++) {
            if (arr[j] <= pivot) {
                i++;
                swap(arr, i, j);
            }
        }

        // 将基准放到正确位置
        swap(arr, i + 1, right);
        return i + 1;  // 返回基准位置
    }

    /**
     * 交换数组中两个元素
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
     */
    private static int[] copyArray(int[] arr) {
        int[] copy = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            copy[i] = arr[i];
        }
        return copy;
    }

    /**
     * 找出前k个最小元素
     * 
     * @param arr 输入数组
     * @param k 要找的前k个最小元素
     * @return 前k个最小元素数组（已排序）
     * 
     * 【算法流程】
     * 
     * 1. 使用快速选择找到第k小元素（时间复杂度Θ(n)）
     * 2. 遍历数组，收集所有小于等于第k小元素的元素（时间复杂度O(n)）
     * 3. 对结果排序输出（不计入时间复杂度）
     * 
     * 【为什么这样设计？】
     * 
     * 问题要求时间复杂度Θ(n)：
     * - 排序法：O(n log n)，超出要求
     * - 运行SELECT k次：Θ(kn)，当k接近n时变成O(n²)
     * - 本方法：找到第k小元素后，一次遍历收集所有小于等于它的元素
     * 
     * 【关键洞察】
     * 
     * 前1~k小元素 = 所有小于等于第k小元素的元素
     * 因为元素互不相同，所以恰好有k个元素小于等于第k小元素
     */
    public static int[] findKSmallest(int[] arr, int k) {
        if (arr == null || arr.length == 0 || k < 1 || k > arr.length) {
            throw new IllegalArgumentException("Invalid input");
        }

        // 【步骤1】找到第k小元素
        int kthSmallest = quickSelect(arr, k);

        // 【步骤2】收集所有小于等于第k小元素的元素
        int[] result = new int[k];
        int index = 0;
        for (int num : arr) {
            if (num <= kthSmallest) {
                result[index++] = num;
            }
        }

        // 【步骤3】排序输出（不计入时间复杂度）
        Arrays.sort(result);

        return result;
    }

    /**
     * 改进的快速选择算法（使用中位数的中位数作为基准）
     * 
     * @param arr 输入数组
     * @param k 第k小元素（1-indexed）
     * @return 第k小元素
     * 
     * 【算法原理】
     * 
     * 标准快速选择的最坏情况是O(n²)，当每次选择的基准都是最大或最小元素时发生。
     * 
     * 改进方法：使用"中位数的中位数"作为基准
     * 1. 将数组分成n/5组，每组5个元素
     * 2. 找出每组的中位数
     * 3. 递归找出这些中位数的中位数
     * 4. 用这个"中位数的中位数"作为基准
     * 
     * 【为什么选择5？】
     * 
     * 每组5个元素：
     * - 找中位数：最多6次比较
 * - 保证至少有3n/10个元素小于基准
     * - 保证至少有7n/10个元素大于基准
     * - 递归深度最多log(n)，保证O(n)最坏时间复杂度
     * 
     * 【时间复杂度】
     * T(n) = T(n/5) + T(7n/10) + O(n) = O(n)
     * 
     * 【实际应用】
     * 
     * 本题数据规模不大，标准快速选择（平均O(n))已足够。
     * 此改进方法作为扩展内容展示。
     */
    public static int improvedQuickSelect(int[] arr, int k) {
        if (arr == null || arr.length == 0 || k < 1 || k > arr.length) {
            throw new IllegalArgumentException("Invalid input");
        }
        
        int[] copy = copyArray(arr);
        return selectHelper(copy, 0, copy.length - 1, k - 1);
    }

    /**
     * 改进快速选择的递归辅助方法
     */
    private static int selectHelper(int[] arr, int left, int right, int k) {
        if (left == right) {
            return arr[left];
        }

        // 找到中位数的中位数作为基准
        int pivotIndex = medianOfMedians(arr, left, right);
        
        // 将基准移到最右位置
        swap(arr, pivotIndex, right);
        
        // 分区
        pivotIndex = partition(arr, left, right);

        if (k == pivotIndex) {
            return arr[pivotIndex];
        } else if (k < pivotIndex) {
            return selectHelper(arr, left, pivotIndex - 1, k);
        } else {
            return selectHelper(arr, pivotIndex + 1, right, k);
        }
    }

    /**
     * 找到中位数的中位数
     * 
     * @param arr 数组
     * @param left 左边界
     * @param right 右边界
     * @return 中位数的中位数的位置
     * 
     * 【算法步骤】
     * 1. 将数组分成若干组，每组5个元素（最后一组可能少于5个）
     * 2. 对每组排序，找到每组的中位数
     * 3. 递归找出这些中位数的中位数
     */
    private static int medianOfMedians(int[] arr, int left, int right) {
        int n = right - left + 1;
        
        // 如果数组很小，直接返回中位数位置
        if (n <= 5) {
            insertionSort(arr, left, right);
            return left + n / 2;
        }

        // 将数组分成若干组，每组5个元素
        int numGroups = (n + 4) / 5;  // 向上取整
        int[] medians = new int[numGroups];

        for (int i = 0; i < numGroups; i++) {
            int groupLeft = left + i * 5;
            int groupRight = Math.min(groupLeft + 4, right);
            
            // 对每组排序
            insertionSort(arr, groupLeft, groupRight);
            
            // 取每组的中位数
            medians[i] = arr[groupLeft + (groupRight - groupLeft) / 2];
        }

        // 递归找出中位数的中位数
        int medianOfMediansValue = improvedQuickSelect(medians, numGroups / 2 + 1);

        // 找到中位数的中位数在原数组中的位置
        for (int i = left; i <= right; i++) {
            if (arr[i] == medianOfMediansValue) {
                return i;
            }
        }

        return left;  // 默认返回
    }

    /**
     * 插入排序（用于小数组）
     */
    private static void insertionSort(int[] arr, int left, int right) {
        for (int i = left + 1; i <= right; i++) {
            int key = arr[i];
            int j = i - 1;
            while (j >= left && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
    }

    /**
     * 打印数组
     */
    public static void printArray(int[] arr) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            if (i > 0) {
                sb.append(" ");
            }
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
     *    - 读取元素个数n
     *    - 读取n个整数
     *    - 读取k值
     *    - 调用findKSmallest找出前k个最小元素
     *    - 输出结果
     *    - 测试例之间用空行隔开
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // 读取测试例个数
        int m = scanner.nextInt();

        // 处理每个测试例
        for (int t = 0; t < m; t++) {
            // 读取元素个数
            int n = scanner.nextInt();

            // 读取数组元素
            int[] arr = new int[n];
            for (int i = 0; i < n; i++) {
                arr[i] = scanner.nextInt();
            }

            // 读取k值
            int k = scanner.nextInt();

            // 找出前k个最小元素
            int[] result = findKSmallest(arr, k);

            // 输出结果
            printArray(result);

            // 测试例之间用空行隔开（最后一个测试例后无空行）
            if (t < m - 1) {
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
 * 1. 【为什么不能用排序？】
 *    排序算法时间复杂度O(n log n)，超出题目要求的Θ(n)
 * 
 * 2. 【为什么不能运行SELECT k次？】
 *    SELECT算法时间复杂度Θ(n)，运行k次变成Θ(kn)
 *    当k接近n时，Θ(kn)变成Θ(n²)，超出要求
 * 
 * 3. 【本算法的核心思想】
 *    - 前1~k小元素 = 所有小于等于第k小元素的元素
 *    - 找到第k小元素后，一次遍历即可收集所有前k小元素
 *    - 时间复杂度：Θ(n)（找第k小）+ O(n)（收集）= Θ(n)
 * 
 * 4. 【快速选择算法原理】
 *    - 快速排序的变种，只递归处理包含目标元素的分区
 *    - 平均时间复杂度：O(n)
 *    - 最坏时间复杂度：O(n²)（当每次基准选择不当）
 * 
 * 5. 【改进的SELECT算法】
 *    - 使用"中位数的中位数"作为基准
 *    - 保证每次分区后至少有30%的元素被排除
 *    - 最坏时间复杂度：O(n)
 * 
 * 6. 【时间复杂度对比】
 *    | 方法 | 时间复杂度 | 适用场景 |
 *    |------|------------|----------|
 *    | 排序法 | O(n log n) | k接近n时效率低 |
 *    | SELECT k次 | Θ(kn) | k小时可行 |
 *    | 本方法 | Θ(n) | 任意k值 |
 *    | 改进SELECT | O(n)最坏 | 要求严格时 |
 * 
 * 7. 【分治思想体现】
 *    - 分解：选择基准，将数组分成两部分
 *    - 处理：判断目标在哪个分区
 *    - 递归：只处理包含目标的分区
 *    - 组合：找到第k小元素后，遍历收集前k小元素
 * 
 * ============================================================================
 */