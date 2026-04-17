package com.rongx.exp2;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * ============================================================================
 * 实验二：格雷码问题
 * ============================================================================
 * 
 * 【题目描述】
 * 对于给定的正整数n，格雷码为满足如下条件的一个编码序列：
 * 1. 序列由2^n个编码组成，每个编码都是长度为n的二进制位串
 * 2. 序列中无相同的编码
 * 3. 序列中位置相邻的两个编码恰有一位不同
 * 
 * 【输入格式】
 * 第一行：正整数m，表示测试例个数
 * 每个测试例包含一个正整数n
 * 
 * 【输出格式】
 * 对于每个测试例n，输出2^n个长度为n的格雷码
 * 每个格雷码内，两个位之间用一个空格隔开
 * 两个测试例之间用空行隔开，最后一个测试例后无空行
 * 
 * 【算法策略】
 * 使用递归算法生成格雷码。
 * n位格雷码可以由n-1位格雷码递归构造：
 * - 前半部分：在n-1位格雷码每个元素前加"0"
 * - 后半部分：将n-1位格雷码逆序，每个元素前加"1"
 * - 拼接两部分得到n位格雷码
 * 
 * 【复杂度分析】
 * 时间复杂度：O(n·2^n)
 * 空间复杂度：O(n·2^n)
 * 
 * ============================================================================
 */
public class GrayCode {

    /**
     * 递归生成n位格雷码
     * 
     * @param n 格雷码位数
     * @return 格雷码列表（包含2^n个编码）
     * 
     * 【递归构造原理】
     * 
     * 核心思想：n位格雷码由n-1位格雷码构造
     * 
     * 构造步骤：
     * 1. 获取n-1位格雷码序列G(n-1)
     * 2. 前半部分：G(n-1)每个元素前加"0"
     * 3. 后半部分：G(n-1)逆序后每个元素前加"1"
     * 4. 拼接两部分得到G(n)
     * 
     * 【构造示意】
     * n=1: ["0", "1"]
     * 
     * n=2:
     *   前半部分(加0): ["00", "01"]
     *   后半部分(逆序+1): ["11", "10"]
     *   结果: ["00", "01", "11", "10"]
     * 
     * n=3:
     *   前半部分(加0): ["000", "001", "011", "010"]
     *   后半部分(逆序+1): ["110", "111", "101", "100"]
     *   结果: ["000", "001", "011", "010", "110", "111", "101", "100"]
     */
    public static List<String> generateGrayCode(int n) {
        List<String> result = new ArrayList<>();

        // 【递归出口】
        // n=1时返回基础格雷码 ["0", "1"]
        if (n == 1) {
            result.add("0");
            result.add("1");
            return result;
        }

        // 【递归调用】
        // 获取n-1位格雷码
        List<String> prevGrayCode = generateGrayCode(n - 1);

        // 【构造前半部分】
        // 在每个n-1位格雷码前加"0"
        for (String code : prevGrayCode) {
            result.add("0" + code);
        }

        // 【构造后半部分】
        // 逆序遍历n-1位格雷码，每个元素前加"1"
        // 逆序是为了保证前后半部分交界处只差一位
        for (int i = prevGrayCode.size() - 1; i >= 0; i--) {
            result.add("1" + prevGrayCode.get(i));
        }

        return result;
    }

    /**
     * 格式化输出格雷码
     * 每个位之间用空格隔开
     * 
     * @param grayCode 格雷码列表
     * 
     * 【输出格式】
     * 输入: ["00", "01", "11", "10"]
     * 输出:
     *   0 0
     *   0 1
     *   1 1
     *   1 0
     */
    public static void printGrayCode(List<String> grayCode) {
        for (String code : grayCode) {
            StringBuilder sb = new StringBuilder();
            // 每个位之间用空格隔开
            for (int i = 0; i < code.length(); i++) {
                if (i > 0) {
                    sb.append(" ");
                }
                sb.append(code.charAt(i));
            }
            System.out.println(sb.toString());
        }
    }

    /**
     * 验证格雷码的正确性
     * 
     * @param grayCode 格雷码列表
     * @param n 格雷码位数
     * @return 如果格雷码正确返回true，否则返回false
     * 
     * 【验证条件】
     * 1. 序列中有2^n个编码
     * 2. 每个编码长度为n
     * 3. 序列中无相同编码
     * 4. 相邻编码恰有一位不同
     */
    public static boolean validateGrayCode(List<String> grayCode, int n) {
        // 验证条件1：数量正确（应为2^n）
        int expectedSize = 1 << n;  // 2^n

        if (grayCode.size() != expectedSize) {
            System.out.println("错误：格雷码数量不正确，期望 " + expectedSize + "，实际 " + grayCode.size());
            return false;
        }

        // 验证条件2：每个编码长度为n
        for (String code : grayCode) {
            if (code.length() != n) {
                System.out.println("错误：编码长度不正确");
                return false;
            }
        }

        // 验证条件3：无重复编码
        for (int i = 0; i < grayCode.size(); i++) {
            for (int j = i + 1; j < grayCode.size(); j++) {
                if (grayCode.get(i).equals(grayCode.get(j))) {
                    System.out.println("错误：存在重复编码");
                    return false;
                }
            }
        }

        // 验证条件4：相邻编码恰有一位不同
        for (int i = 0; i < grayCode.size() - 1; i++) {
            int diff = countDifference(grayCode.get(i), grayCode.get(i + 1));
            if (diff != 1) {
                System.out.println("错误：相邻编码差异位数不等于1");
                return false;
            }
        }

        // 检查首尾编码（形成循环格雷码）
        int diff = countDifference(grayCode.get(0), grayCode.get(grayCode.size() - 1));
        if (diff != 1) {
            System.out.println("注意：首尾编码差异位数不等于1（非循环格雷码）");
        }

        return true;
    }

    /**
     * 计算两个二进制字符串的差异位数
     * 
     * @param s1 第一个字符串
     * @param s2 第二个字符串
     * @return 差异的位数
     */
    private static int countDifference(String s1, String s2) {
        int count = 0;
        for (int i = 0; i < s1.length(); i++) {
            if (s1.charAt(i) != s2.charAt(i)) {
                count++;
            }
        }
        return count;
    }

    /**
     * 主方法：处理多个测试用例
     * 
     * 【输入处理流程】
     * 1. 读取测试例个数m
     * 2. 对于每个测试例：
     *    - 读取格雷码位数n
     *    - 调用generateGrayCode生成格雷码
     *    - 输出格雷码
     *    - 测试例之间用空行隔开
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // 读取测试例个数
        int m = scanner.nextInt();

        // 处理每个测试例
        for (int t = 0; t < m; t++) {
            // 读取格雷码位数
            int n = scanner.nextInt();

            // 生成格雷码
            List<String> grayCode = generateGrayCode(n);

            // 输出格雷码
            printGrayCode(grayCode);

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
 * 1. 【格雷码性质】
 *    - 数量：2^n个编码
 *    - 长度：每个编码n位
 *    - 无重复：所有编码唯一
 *    - 相邻差一位：相邻编码仅一位不同
 *    - 循环性：首尾编码也只差一位（形成循环）
 * 
 * 2. 【递归构造原理】
 *    核心思想：利用n-1位格雷码构造n位格雷码
 *    
 *    为什么这样构造？
 *    - 前半部分以"0"开头，后半部分以"1"开头 → 两部分无交集
 *    - 前半部分内部：G(n-1)相邻差一位，加"0"后仍差一位
 *    - 后半部分内部：G(n-1)逆序后相邻关系保持，加"1"后仍差一位
 *    - 交界处：前半最后和后半第一都是G(n-1)最后元素加不同前缀
 *             → 只差首位（0 vs 1）
 * 
 * 3. 【递归三要素】
 *    - 递归出口：n=1时返回["0", "1"]
 *    - 递归关系：G(n) = [0+G(n-1)] + [1+reverse(G(n-1))]
 *    - 参数设置：参数n表示格雷码位数
 * 
 * 4. 【为什么逆序？】
 *    逆序拼接保证交界处只差一位：
 *    - 前半部分最后元素：0 + G(n-1)最后元素
 *    - 后半部分第一元素：1 + G(n-1)最后元素（逆序后第一个）
 *    - 两者只有首位不同（0 vs 1）
 * 
 * 5. 【复杂度分析】
 *    时间复杂度：O(n·2^n)
 *    - 递归深度：n
 *    - 第k层产生：2^k个格雷码，每个长度k
 *    - 总时间：Σ(k=1 to n) k·2^k = O(n·2^n)
 *    
 *    空间复杂度：O(n·2^n)
 *    - 存储2^n个格雷码，每个长度n
 *    - 递归栈深度：n
 * 
 * 6. 【其他生成方法】
 *    - 直接构造法：G(i) = i ^ (i >> 1)
 *    - 非递归方法：迭代生成，避免递归开销
 *    - 公式法：利用格雷码的数学性质直接计算
 * 
 * 7. 【格雷码应用】
 *    - 通信编码：减少传输错误
 *    - 位置编码：旋转编码器
 *    - 遗传算法：变异操作
 *    - 数字电路：状态转换
 * 
 * ============================================================================
 */