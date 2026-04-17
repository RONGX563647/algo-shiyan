package com.rongx.exp1;

import java.util.Scanner;

/**
 * ============================================================================
 * 实验一：相等元素问题
 * ============================================================================
 * 
 * 【题目描述】
 * 给出一个整数集合，假定这些整数存储在数组A[1…n]中，确定它们中是否存在两个相等的元素。
 * 
 * 【输入格式】
 * 第一行：正整数m，表示测试例个数
 * 每个测试例包含两行：
 *   - 第一行：正整数n (n≤500)，表示整数序列的长度
 *   - 第二行：整数序列，整数之间用一个空格隔开
 * 
 * 【输出格式】
 * 对于每个测试例输出一行，若存在两个相等的元素则输出Yes，否则输出No。
 * 
 * 【算法策略】
 * 使用自实现的HashSet（哈希表）来检测重复元素。
 * 哈希表通过哈希函数将元素映射到固定位置，实现O(1)的平均查找时间。
 * 采用链地址法（Separate Chaining）解决哈希冲突问题。
 * 
 * 【复杂度分析】
 * 时间复杂度：O(n) 平均情况
 * 空间复杂度：O(n) 哈希表存储
 * 
 * ============================================================================
 */
public class EqualElements {

    /**
     * 自定义HashSet实现
     * 
     * 【数据结构】
     * 采用链地址法解决哈希冲突：
     * - 哈希表数组：每个位置称为一个"桶"
     * - 链表节点：当多个元素哈希到同一桶时，用链表存储
     * 
     * 【结构示意】
     * table[0] -> Node(5) -> Node(21) -> null
     * table[1] -> null
     * table[2] -> Node(16) -> Node(2) -> null
     * ...
     */
    static class MyHashSet {
        
        /**
         * 链表节点类
         * 存储哈希冲突时落入同一桶的元素
         */
        private static class Node {
            int value;      // 存储的元素值
            Node next;      // 指向链表中下一个节点的指针

            Node(int value) {
                this.value = value;
                this.next = null;
            }
        }

        private Node[] table;   // 哈希表数组，每个元素是一个链表头节点
        private int capacity;   // 哈希表容量（桶的数量）

        /**
         * 构造函数：初始化哈希表
         * 
         * @param capacity 哈希表容量
         * 
         * 【设计考虑】
         * 容量设为元素数量的2倍，以减少哈希冲突概率
         * 冲突越少，链表越短，查找效率越高
         */
        public MyHashSet(int capacity) {
            this.capacity = capacity;
            this.table = new Node[capacity];
        }

        /**
         * 哈希函数：将元素值映射到桶索引
         * 
         * @param value 要哈希的元素值
         * @return 桶索引（0 到 capacity-1）
         * 
         * 【哈希函数设计】
         * 使用简单的取模运算：hash = abs(value) % capacity
         * - abs()确保负数也能正确映射
         * - 取模运算将任意整数映射到固定范围
         */
        private int hash(int value) {
            return Math.abs(value) % capacity;
        }

        /**
         * 添加元素到哈希表
         * 
         * @param value 要添加的元素值
         * @return 如果元素已存在返回false，否则添加成功返回true
         * 
         * 【添加流程】
         * 1. 计算哈希值，获取桶索引
         * 2. 遍历该桶的链表，检查元素是否已存在
         * 3. 如果存在，返回false（重复元素）
         * 4. 如果不存在，创建新节点插入链表头部，返回true
         * 
         * 【插入头部原因】
         * 插入链表头部只需O(1)时间
         * 插入尾部需要遍历整个链表
         */
        public boolean add(int value) {
            // 步骤1：计算哈希值，定位桶
            int index = hash(value);

            // 步骤2：遍历桶内链表，检查是否已存在
            Node current = table[index];
            while (current != null) {
                if (current.value == value) {
                    return false;  // 发现重复元素
                }
                current = current.next;
            }

            // 步骤3：元素不存在，创建新节点
            Node newNode = new Node(value);
            
            // 步骤4：插入链表头部（头插法）
            newNode.next = table[index];
            table[index] = newNode;
            
            return true;  // 成功添加
        }
    }

    /**
     * 检测数组中是否存在重复元素
     * 
     * @param arr 输入整数数组
     * @return 如果存在重复元素返回true，否则返回false
     * 
     * 【算法流程】
     * 1. 创建哈希表，容量设为数组长度的2倍
     * 2. 遍历数组每个元素
     * 3. 尝试将元素添加到哈希表
     * 4. 如果add返回false，说明元素已存在，发现重复
     * 5. 如果遍历完成未发现重复，返回false
     */
    public static boolean hasDuplicate(int[] arr) {
        // 创建哈希表，容量为2倍数组长度以减少冲突
        MyHashSet set = new MyHashSet(arr.length * 2);

        // 遍历数组，逐个添加到哈希表
        for (int num : arr) {
            // add返回false表示元素已存在（重复）
            if (!set.add(num)) {
                return true;  // 发现重复元素
            }
        }
        
        return false;  // 所有元素都唯一，无重复
    }

    /**
     * 主方法：处理多个测试用例
     * 
     * 【输入处理流程】
     * 1. 读取测试例个数m
     * 2. 对于每个测试例：
     *    - 读取数组长度n
     *    - 读取n个整数到数组
     *    - 调用hasDuplicate判断是否有重复
     *    - 输出结果Yes或No
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

            // 判断是否存在重复元素并输出结果
            if (hasDuplicate(arr)) {
                System.out.println("Yes");
            } else {
                System.out.println("No");
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
 * 1. 【为什么选择哈希表？】
 *    - 暴力法：两层循环比较，O(n²)时间，效率低
 *    - 排序法：排序后比较相邻，O(n log n)时间，中等效率
 *    - 哈希法：哈希表查找，O(n)平均时间，效率最高
 * 
 * 2. 【为什么自实现HashSet？】
 *    - 深入理解哈希表底层原理
 *    - 掌握链地址法解决冲突的方法
 *    - 不依赖Java标准库，增强代码可移植性
 * 
 * 3. 【链地址法原理】
 *    - 哈希函数：hash = abs(value) % capacity
 *    - 冲突处理：多个元素哈希到同一桶时，用链表存储
 *    - 查找过程：先定位桶，再遍历链表查找元素
 * 
 * 4. 【复杂度分析】
 *    - 理想情况：无冲突，每个操作O(1)
 *    - 平均情况：少量冲突，链表长度有限，O(1)
 *    - 最坏情况：所有元素冲突到同一桶，O(n)
 *    - 实际应用中，合理设置容量可避免最坏情况
 * 
 * 5. 【优化方向】
 *    - 动态扩容：当元素过多时，扩大哈希表容量
 *    - 更好的哈希函数：减少冲突概率
 *    - 负载因子监控：保持负载因子<0.75以保证效率
 * 
 * ============================================================================
 */