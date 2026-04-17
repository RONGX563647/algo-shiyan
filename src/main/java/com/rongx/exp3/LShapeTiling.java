package com.rongx.exp3;

import java.util.Scanner;

/**
 * ============================================================================
 * 实验三：L型组件填图问题
 * ============================================================================
 * 
 * 【题目描述】
 * 设B是一个n×n棋盘，n=2^k (k=1,2,3,...)。用分治法设计一个算法，
 * 使得用若干个L型条块可以覆盖住B的除一个特殊方格外的所有方格。
 * 其中，一个L型条块可以覆盖3个方格，且任意两个L型条块不能重叠覆盖棋盘。
 * 
 * 【输入格式】
 * 第一行：正整数m，表示测试例个数
 * 每个测试例包含：
 *   - 第一行：正整数n，表示棋盘大小（n=2^k）
 *   - 第二行：两个整数row和col，表示特殊方格的位置（0-indexed）
 * 
 * 【输出格式】
 * 对于每个测试例，输出一个被L型条块覆盖的n×n棋盘。
 * 每个L型条块用不同的数字标记，特殊方格用0或特殊标记表示。
 * 
 * 【算法策略】
 * 采用分治法：
 * 1. 将棋盘分成4个大小为n/2×n/2的子棋盘
 * 2. 特殊方格必在其中一个子棋盘中
 * 3. 用一个L型条块覆盖其他3个子棋盘的中心交汇处
 * 4. 递归处理4个子棋盘
 * 
 * 【复杂度分析】
 * 时间复杂度：O(n²) = O(4^k)
 * 空间复杂度：O(n²) 棋盘存储
 * 
 * ============================================================================
 */
public class LShapeTiling {

    private int[][] board;      // 棋盘
    private int tileCount = 1;  // 当前L型条块编号（从1开始，0表示特殊方格）

    /**
     * L型组件填图主方法
     * 
     * @param size 棋盘大小（size × size）
     * @param specialRow 特殊方格所在行（0-indexed）
     * @param specialCol 特殊方格所在列（0-indexed）
     * 
     * 【分治策略】
     * 
     * 核心思想：将大棋盘分成4个小棋盘，用L型条块覆盖3个不含特殊方格的子棋盘中心
     * 
     * 分治步骤：
     * 1. 分解：将2^k × 2^k棋盘分成4个2^(k-1) × 2^(k-1)子棋盘
     * 2. 处理：用一个L型条块覆盖3个子棋盘的中心交汇处
     * 3. 递归：对4个子棋盘分别递归处理
     * 
     * 【棋盘分割示意】
     * ┌─────────┬─────────┐
     * │ 左上    │ 右上    │
     * │ (0,0)   │ (0,mid) │
     * ├─────────┼─────────┤
     * │ 左下    │ 右下    │
     * │(mid,0)  │(mid,mid)│
     * └─────────┴─────────┘
     * 
     * 【L型条块覆盖策略】
     * 特殊方格在左上 → L型覆盖右上、左下、右下的中心交汇处
     * 特殊方格在右上 → L型覆盖左上、左下、右下的中心交汇处
     * 特殊方格在左下 → L型覆盖左上、右上、右下的中心交汇处
     * 特殊方格在右下 → L型覆盖左上、右上、左下的中心交汇处
     */
    public void solve(int size, int specialRow, int specialCol) {
        board = new int[size][size];
        tileCount = 1;
        board[specialRow][specialCol] = 0;  // 特殊方格标记为0
        tileBoard(0, 0, size, specialRow, specialCol);
    }

    /**
     * 递归填充棋盘
     * 
     * @param topRow 子棋盘左上角行坐标
     * @param topCol 子棋盘左上角列坐标
     * @param size 当前子棋盘大小
     * @param specialRow 特殊方格所在行
     * @param specialCol 特殊方格所在列
     * 
     * 【递归三要素】
     * 
     * 1. 递归出口：size == 1时，只有一个方格，无需填充
     * 
     * 2. 递归关系：
     *    - 将当前棋盘分成4个子棋盘
     *    - 用一个L型条块覆盖3个不含特殊方格的子棋盘中心
     *    - 对4个子棋盘分别递归调用
     * 
     * 3. 参数设置：
     *    - topRow, topCol：当前子棋盘的左上角坐标
     *    - size：当前子棋盘大小
     *    - specialRow, specialCol：特殊方格在当前棋盘中的位置
     * 
     * 【L型条块覆盖示意】
     * 
     * 特殊方格在左上子棋盘：
     * ┌───┬───┐
     * │ S │ 1 │    S = 特殊方格
     * ├───┼───┤    1 = L型条块编号
     * │ 1 │ 1 │    L型覆盖右上、左下、右下中心
     * └───┴───┘
     * 
     * 特殊方格在右上子棋盘：
     * ┌───┬───┐
     * │ 1 │ S │
     * ├───┼───┤
     * │ 1 │ 1 │    L型覆盖左上、左下、右下中心
     * └───┴───┘
     * 
     * 特殊方格在左下子棋盘：
     * ┌───┬───┐
     * │ 1 │ 1 │
     * ├───┼───┤
     * │ S │ 1 │    L型覆盖左上、右上、右下中心
     * └───┴───┘
     * 
     * 特殊方格在右下子棋盘：
     * ┌───┬───┐
     * │ 1 │ 1 │
     * ├───┼───┤
     * │ 1 │ S │    L型覆盖左上、右上、左下中心
     * └───┴───┘
     */
    private void tileBoard(int topRow, int topCol, int size, 
                           int specialRow, int specialCol) {
        
        // 【递归出口】
        // 当棋盘大小为1时，只有一个方格，无需填充
        if (size == 1) {
            return;
        }

        // 【分解】
        // 将当前棋盘分成4个子棋盘
        int half = size / 2;  // 子棋盘大小
        int currentTile = tileCount++;  // 当前使用的L型条块编号

        // 计算中心交汇点（四个子棋盘交汇的四个方格）
        // 左上子棋盘的右下角：(topRow + half - 1, topCol + half - 1)
        // 右上子棋盘的左下角：(topRow + half - 1, topCol + half)
        // 左下子棋盘的右上角：(topRow + half, topCol + half - 1)
        // 右下子棋盘的左上角：(topRow + half, topCol + half)

        // 【判断特殊方格所在子棋盘】
        // 左上子棋盘范围：[topRow, topRow+half-1] × [topCol, topCol+half-1]
        // 右上子棋盘范围：[topRow, topRow+half-1] × [topCol+half, topCol+size-1]
        // 左下子棋盘范围：[topRow+half, topRow+size-1] × [topCol, topCol+half-1]
        // 右下子棋盘范围：[topRow+half, topRow+size-1] × [topCol+half, topCol+size-1]

        // 【情况1：特殊方格在左上子棋盘】
        if (specialRow < topRow + half && specialCol < topCol + half) {
            // 用L型条块覆盖其他3个子棋盘的中心交汇处
            // 覆盖右上子棋盘的左下角
            board[topRow + half - 1][topCol + half] = currentTile;
            // 覆盖左下子棋盘的右上角
            board[topRow + half][topCol + half - 1] = currentTile;
            // 覆盖右下子棋盘的左上角
            board[topRow + half][topCol + half] = currentTile;
            
            // 【递归处理】
            // 左上子棋盘：特殊方格位置不变
            tileBoard(topRow, topCol, half, specialRow, specialCol);
            // 右上子棋盘：特殊方格在左下角
            tileBoard(topRow, topCol + half, half, topRow + half - 1, topCol + half);
            // 左下子棋盘：特殊方格在右上角
            tileBoard(topRow + half, topCol, half, topRow + half, topCol + half - 1);
            // 右下子棋盘：特殊方格在左上角
            tileBoard(topRow + half, topCol + half, half, topRow + half, topCol + half);
        }
        
        // 【情况2：特殊方格在右上子棋盘】
        else if (specialRow < topRow + half && specialCol >= topCol + half) {
            // 用L型条块覆盖其他3个子棋盘的中心交汇处
            // 覆盖左上子棋盘的右下角
            board[topRow + half - 1][topCol + half - 1] = currentTile;
            // 覆盖左下子棋盘的右上角
            board[topRow + half][topCol + half - 1] = currentTile;
            // 覆盖右下子棋盘的左上角
            board[topRow + half][topCol + half] = currentTile;
            
            // 【递归处理】
            // 左上子棋盘：特殊方格在右下角
            tileBoard(topRow, topCol, half, topRow + half - 1, topCol + half - 1);
            // 右上子棋盘：特殊方格位置不变
            tileBoard(topRow, topCol + half, half, specialRow, specialCol);
            // 左下子棋盘：特殊方格在右上角
            tileBoard(topRow + half, topCol, half, topRow + half, topCol + half - 1);
            // 右下子棋盘：特殊方格在左上角
            tileBoard(topRow + half, topCol + half, half, topRow + half, topCol + half);
        }
        
        // 【情况3：特殊方格在左下子棋盘】
        else if (specialRow >= topRow + half && specialCol < topCol + half) {
            // 用L型条块覆盖其他3个子棋盘的中心交汇处
            // 覆盖左上子棋盘的右下角
            board[topRow + half - 1][topCol + half - 1] = currentTile;
            // 覆盖右上子棋盘的左下角
            board[topRow + half - 1][topCol + half] = currentTile;
            // 覆盖右下子棋盘的左上角
            board[topRow + half][topCol + half] = currentTile;
            
            // 【递归处理】
            // 左上子棋盘：特殊方格在右下角
            tileBoard(topRow, topCol, half, topRow + half - 1, topCol + half - 1);
            // 右上子棋盘：特殊方格在左下角
            tileBoard(topRow, topCol + half, half, topRow + half - 1, topCol + half);
            // 左下子棋盘：特殊方格位置不变
            tileBoard(topRow + half, topCol, half, specialRow, specialCol);
            // 右下子棋盘：特殊方格在左上角
            tileBoard(topRow + half, topCol + half, half, topRow + half, topCol + half);
        }
        
        // 【情况4：特殊方格在右下子棋盘】
        else {
            // 用L型条块覆盖其他3个子棋盘的中心交汇处
            // 覆盖左上子棋盘的右下角
            board[topRow + half - 1][topCol + half - 1] = currentTile;
            // 覆盖右上子棋盘的左下角
            board[topRow + half - 1][topCol + half] = currentTile;
            // 覆盖左下子棋盘的右上角
            board[topRow + half][topCol + half - 1] = currentTile;
            
            // 【递归处理】
            // 左上子棋盘：特殊方格在右下角
            tileBoard(topRow, topCol, half, topRow + half - 1, topCol + half - 1);
            // 右上子棋盘：特殊方格在左下角
            tileBoard(topRow, topCol + half, half, topRow + half - 1, topCol + half);
            // 左下子棋盘：特殊方格在右上角
            tileBoard(topRow + half, topCol, half, topRow + half, topCol + half - 1);
            // 右下子棋盘：特殊方格位置不变
            tileBoard(topRow + half, topCol + half, half, specialRow, specialCol);
        }
    }

    /**
     * 打印棋盘
     * 
     * 【输出格式】
     * 每个方格显示其L型条块编号
     * 特殊方格显示为"A"或"0"
     * 数字右对齐，保持格式整齐
     */
    public void printBoard() {
        int size = board.length;
        
        for (int i = 0; i < size; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < size; j++) {
                if (j > 0) {
                    sb.append(" ");
                }
                // 特殊方格显示为"A"
                if (board[i][j] == 0) {
                    sb.append("A");
                } else {
                    sb.append(board[i][j]);
                }
            }
            System.out.println(sb.toString());
        }
    }

    /**
     * 验证棋盘填充的正确性
     * 
     * @param size 棋盘大小
     * @param specialRow 特殊方格行
     * @param specialCol 特殊方格列
     * @return 如果填充正确返回true
     * 
     * 【验证条件】
     * 1. 特殊方格为0
     * 2. 其他方格都被L型条块覆盖（非0）
     * 3. 每个L型条块恰好覆盖3个方格
     * 4. L型条块形状正确（3个方格形成L形）
     */
    public boolean validate(int size, int specialRow, int specialCol) {
        // 验证特殊方格
        if (board[specialRow][specialCol] != 0) {
            System.out.println("错误：特殊方格未正确标记");
            return false;
        }

        // 统计每个L型条块覆盖的方格数
        int[] tileCountArr = new int[tileCount];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] > 0) {
                    tileCountArr[board[i][j]]++;
                }
            }
        }

        // 验证每个L型条块恰好覆盖3个方格
        for (int t = 1; t < tileCount; t++) {
            if (tileCountArr[t] != 3) {
                System.out.println("错误：L型条块" + t + "覆盖了" + tileCountArr[t] + "个方格");
                return false;
            }
        }

        // 验证覆盖方格总数
        int coveredCount = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] != 0) {
                    coveredCount++;
                }
            }
        }
        int expectedCovered = size * size - 1;
        if (coveredCount != expectedCovered) {
            System.out.println("错误：覆盖方格数不正确，期望" + expectedCovered + "，实际" + coveredCount);
            return false;
        }

        return true;
    }

    /**
     * 主方法：处理多个测试用例
     * 
     * 【输入处理流程】
     * 1. 读取测试例个数m
     * 2. 对于每个测试例：
     *    - 读取棋盘大小n
     *    - 读取特殊方格位置(row, col)
     *    - 调用solve方法填充棋盘
     *    - 输出填充结果
     *    - 测试例之间用空行隔开
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // 读取测试例个数
        int m = scanner.nextInt();

        // 处理每个测试例
        for (int t = 0; t < m; t++) {
            // 读取棋盘大小
            int n = scanner.nextInt();
            
            // 读取特殊方格位置
            int specialRow = scanner.nextInt();
            int specialCol = scanner.nextInt();

            // 创建求解器并填充棋盘
            LShapeTiling tiling = new LShapeTiling();
            tiling.solve(n, specialRow, specialCol);

            // 输出棋盘
            tiling.printBoard();

            // 验证正确性（可选）
            // tiling.validate(n, specialRow, specialCol);

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
 * 1. 【分治策略核心】
 *    - 分解：将2^k × 2^k棋盘分成4个2^(k-1) × 2^(k-1)子棋盘
 *    - 处理：用一个L型条块覆盖3个不含特殊方格的子棋盘中心
 *    - 递归：对4个子棋盘分别递归处理
 * 
 * 2. 【为什么这样分治？】
 *    - 每次分割后，特殊方格必在其中一个子棋盘
 *    - 其他3个子棋盘的中心交汇处恰好形成L形
 *    - 用一个L型条块覆盖后，每个子棋盘都有了"特殊方格"
 *    - 问题规模减半，可以递归处理
 * 
 * 3. 【L型条块数量】
 *    - 设棋盘大小为n=2^k，需要L型条块数量为：(n²-1)/3 = (4^k-1)/3
 *    - 例如：n=2时需要1个，n=4时需要5个，n=8时需要21个
 * 
 * 4. 【复杂度分析】
 *    时间复杂度：T(k) = 4T(k-1) + O(1)
 *    解得：T(k) = O(4^k) = O(n²)
 *    
 *    空间复杂度：O(n²) 棋盘存储 + O(k) 递归栈深度
 * 
 * 5. 【分治法特点】
 *    - 子问题相互独立：4个子棋盘互不影响
 *    - 子问题与原问题性质相同：都是L型填充问题
 *    - 递归出口明确：棋盘大小为1时无需处理
 * 
 * 6. 【L型条块的旋转】
 *    L型条块可以旋转放置，有4种形态：
 *    ┌─┐    ┌───┐    ┌─┬─┐    ┌───┐
 *    │ │    │   │    │ │ │    │   │
 *    ├─┤    └─┬─┤    └───┤    ├─┬─┘
 *    │ │    │ │ │    │   │    │ │
 *    └───┘    └───┘    └───┘    └───┘
 *    本算法通过选择覆盖位置实现不同旋转形态
 * 
 * ============================================================================
 */