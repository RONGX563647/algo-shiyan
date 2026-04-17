package com.rongx.exp2;

import java.util.*;

/**
 * ============================================================================
 * 实验二：棋子移动游戏
 * ============================================================================
 * 
 * 【题目描述】
 * 有2n（n≥4为整数）棋子，黑白棋子各n个。
 * 
 * 【初始状态】
 * 先白后黑，右边两空位
 * W W W ... W B B B ... B _ _
 * 
 * 【终止状态】
 * 白黑相间，左边两空位
 * _ _ W B W B W B ...
 * 
 * 【移动规则】
 * 1. 每次必须同时移动相邻两个棋子
 * 2. 移动方向不限
 * 3. 每次移动必须跳过若干棋子（不能移动到相邻空位）
 * 
 * 【算法策略】
 * 使用BFS（广度优先搜索）求解最短路径。
 * 将棋盘状态建模为状态空间，通过BFS逐层扩展找到最短解。
 * 
 * 【复杂度分析】
 * 时间复杂度：O(n³)
 * 空间复杂度：O(n³)
 * 
 * ============================================================================
 */
public class ChessMove {

    private int n;                    // 棋子数量（每种颜色）
    private String initialState;      // 初始状态字符串
    private String targetState;       // 目标状态字符串

    /**
     * 构造函数：初始化棋盘状态
     * 
     * @param n 每种颜色棋子的数量
     * 
     * 【状态表示】
     * 使用字符串表示棋盘状态：
     * - 'W'：白棋
     * - 'B'：黑棋
     * - '_'：空位
     * 
     * 【状态示例】 n=4
     * 初始状态："WWWWBBBB__"
     * 目标状态："__WBWBWBWB"
     */
    public ChessMove(int n) {
        this.n = n;

        // 构造初始状态：n个W + n个B + 2个空位
        // 例如n=4: "WWWWBBBB__"
        StringBuilder init = new StringBuilder();
        for (int i = 0; i < n; i++) {
            init.append('W');
        }
        for (int i = 0; i < n; i++) {
            init.append('B');
        }
        init.append("__");
        this.initialState = init.toString();

        // 构造目标状态：2个空位 + n组"WB"
        // 例如n=4: "__WBWBWBWB"
        StringBuilder target = new StringBuilder();
        target.append("__");
        for (int i = 0; i < n; i++) {
            target.append('W');
            target.append('B');
        }
        this.targetState = target.toString();
    }

    /**
     * 使用BFS求解棋子移动问题的最短路径
     * 
     * @return 解路径列表，如果无解返回null
     * 
     * 【BFS算法原理】
     * 
     * 广度优先搜索特点：
     * 1. 按层次遍历状态空间
     * 2. 先访问距离初始状态近的状态
     * 3. 首次到达目标状态时即为最短路径
     * 
     * 【算法流程】
     * 1. 初始化队列和visited集合
     * 2. 将初始状态加入队列
     * 3. 循环处理队列中的状态：
     *    a. 取出队首状态
     *    b. 如果是目标状态，重建路径返回
     *    c. 生成所有可能的下一状态
     *    d. 过滤已访问状态，加入队列
     * 4. 队列空仍未找到目标，返回null
     * 
     * 【数据结构】
     * - Queue：存储待扩展的状态
     * - Map<String, String>：记录状态的前驱，用于重建路径
     */
    public List<String> solve() {
        // BFS队列：存储待扩展的状态
        Queue<String> queue = new LinkedList<>();
        
        // parent映射：记录每个状态的前驱状态
        // key: 当前状态, value: 前驱状态
        // 用于重建从初始到目标的路径
        Map<String, String> parent = new HashMap<>();

        // 初始化：将初始状态加入队列
        queue.offer(initialState);
        parent.put(initialState, null);  // 初始状态无前驱

        // BFS主循环
        while (!queue.isEmpty()) {
            // 取出队首状态
            String current = queue.poll();

            // 检查是否到达目标状态
            if (current.equals(targetState)) {
                return reconstructPath(parent, current);
            }

            // 生成所有可能的下一状态
            List<String> nextStates = getNextStates(current);
            
            // 将未访问的状态加入队列
            for (String next : nextStates) {
                if (!parent.containsKey(next)) {
                    parent.put(next, current);  // 记录前驱
                    queue.offer(next);
                }
            }
        }

        return null;  // 无解
    }

    /**
     * 获取当前状态所有可能的下一状态
     * 
     * @param state 当前状态字符串
     * @return 所有可能的下一状态列表
     * 
     * 【移动规则分析】
     * 
     * 合法移动条件：
     * 1. 选择相邻两个棋子（位置i和i+1）
     * 2. 目标位置必须是空位
     * 3. 必须跳过至少一个棋子（不能移动到相邻空位）
     * 
     * 【生成流程】
     * 1. 找到空位位置
     * 2. 遍历所有相邻两棋子位置
     * 3. 检查是否满足跳过条件
     * 4. 执行移动，生成新状态
     */
    private List<String> getNextStates(String state) {
        List<String> nextStates = new ArrayList<>();
        
        // 找到空位位置（第一个'_'的位置）
        int emptyPos = state.indexOf('_');

        // 遍历所有可能的相邻两棋子位置
        for (int i = 0; i < state.length() - 1; i++) {
            // 检查位置i和i+1是否都是棋子（不是空位）
            if (state.charAt(i) != '_' && state.charAt(i + 1) != '_') {
                
                // 计算棋子位置与空位位置的范围
                int minPos = Math.min(i, emptyPos);
                int maxPos = Math.max(i, emptyPos);

                // 检查是否跳过了至少一个棋子
                // 遍历棋子位置和空位之间的所有位置
                boolean hasJump = false;
                for (int j = minPos + 1; j < maxPos; j++) {
                    if (state.charAt(j) != '_') {
                        hasJump = true;  // 发现中间有棋子
                        break;
                    }
                }

                // 如果满足跳过条件，执行移动
                if (hasJump) {
                    String newState = move(state, i, emptyPos);
                    if (newState != null) {
                        nextStates.add(newState);
                    }
                }
            }
        }

        return nextStates;
    }

    /**
     * 执行移动操作
     * 
     * @param state 当前状态
     * @param from 移动起始位置（两个相邻棋子的起始位置）
     * @param to 目标空位起始位置
     * @return 移动后的新状态
     * 
     * 【移动操作】
     * 1. 记录要移动的两个棋子
     * 2. 将原位置设为空位
     * 3. 将棋子放到目标位置
     */
    private String move(String state, int from, int to) {
        char[] chars = state.toCharArray();

        // 记录要移动的两个棋子
        char c1 = chars[from];
        char c2 = chars[from + 1];

        // 将原位置设为空位
        chars[from] = '_';
        chars[from + 1] = '_';

        // 将棋子放到目标位置
        chars[to] = c1;
        chars[to + 1] = c2;

        return new String(chars);
    }

    /**
     * 从BFS结果重建路径
     * 
     * @param parent 状态前驱映射
     * @param target 目标状态
     * @return 从初始状态到目标状态的路径列表
     * 
     * 【路径重建】
     * 从目标状态开始，通过parent映射回溯到初始状态
     * 然后反转得到从初始到目标的路径
     */
    private List<String> reconstructPath(Map<String, String> parent, String target) {
        List<String> path = new ArrayList<>();
        String current = target;
        
        // 从目标回溯到初始状态
        while (current != null) {
            path.add(0, current);  // 在头部插入，实现反转
            current = parent.get(current);
        }
        
        return path;
    }

    /**
     * 格式化输出状态
     * 
     * @param state 状态字符串
     * @return 格式化后的字符串（每个字符用空格隔开）
     */
    public static String formatState(String state) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < state.length(); i++) {
            if (i > 0) {
                sb.append(" ");
            }
            sb.append(state.charAt(i));
        }
        return sb.toString();
    }

    /**
     * 显示移动过程
     * 
     * @param path 解路径列表
     * 
     * 【输出格式】
     * 显示每一步的移动位置和结果状态
     */
    public static void showMoves(List<String> path) {
        if (path == null || path.isEmpty()) {
            System.out.println("No solution found");
            return;
        }

        System.out.println("Initial: " + formatState(path.get(0)));

        // 显示每一步移动
        for (int i = 1; i < path.size(); i++) {
            String prev = path.get(i - 1);
            String curr = path.get(i);

            // 找出移动的两个棋子的位置
            int fromPos = -1, toPos = -1;
            
            // 找原位置（棋子变成空位）
            for (int j = 0; j < prev.length(); j++) {
                if (prev.charAt(j) != '_' && curr.charAt(j) == '_') {
                    fromPos = j;
                    break;
                }
            }
            
            // 找目标位置（空位变成棋子）
            for (int j = 0; j < curr.length(); j++) {
                if (curr.charAt(j) != '_' && prev.charAt(j) == '_') {
                    toPos = j;
                    break;
                }
            }

            System.out.println("Step " + i + ": Move [" + fromPos + "," + (fromPos+1) + "] -> [" + toPos + "," + (toPos+1) + "]");
            System.out.println("         " + formatState(curr));
        }

        System.out.println("\nTotal: " + (path.size() - 1) + " steps");
    }

    /**
     * 获取初始状态
     */
    public String getInitialState() {
        return initialState;
    }

    /**
     * 获取目标状态
     */
    public String getTargetState() {
        return targetState;
    }

    /**
     * 主方法：交互式求解
     * 
     * 【交互流程】
     * 1. 输入棋子数量n
     * 2. 显示初始状态和目标状态
     * 3. 使用BFS求解
     * 4. 显示解路径和耗时
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Chess Move Game (BFS)");
        System.out.println("Rule: Move two adjacent pieces to empty positions, must jump over at least one piece");
        System.out.println();

        System.out.print("Enter n (n>=4): ");
        int n = scanner.nextInt();

        // 边界检查
        if (n < 4) {
            System.out.println("n must be >= 4");
            scanner.close();
            return;
        }

        // 创建游戏实例
        ChessMove game = new ChessMove(n);

        // 显示初始和目标状态
        System.out.println("\nInitial: " + formatState(game.getInitialState()));
        System.out.println("Target:  " + formatState(game.getTargetState()));
        System.out.println("\nSolving with BFS...");

        // 计时求解
        long startTime = System.currentTimeMillis();
        List<String> solution = game.solve();
        long endTime = System.currentTimeMillis();

        // 显示结果
        if (solution != null) {
            System.out.println("\nSolution found!\n");
            showMoves(solution);
            System.out.println("\nTime: " + (endTime - startTime) + "ms");
        } else {
            System.out.println("No solution found");
        }

        scanner.close();
    }
}

/**
 * ============================================================================
 * 【算法设计总结】
 * ============================================================================
 * 
 * 1. 【问题建模】
 *    状态空间搜索问题：
 *    - 状态：棋盘布局字符串
 *    - 初始状态：n个W + n个B + "__"
 *    - 目标状态："__" + n组"WB"
 *    - 操作：移动相邻两棋子到空位（需跳过棋子）
 *    - 目标：找到最短操作序列
 * 
 * 2. 【为什么选择BFS？】
 *    - BFS按层次遍历，保证找到最短路径
 *    - DFS可能找到很长路径（n=4时DFS找到96步）
 *    - BFS首次到达目标即为最优解（n=4时BFS找到5步）
 * 
 * 3. 【BFS核心思想】
 *    - 队列：存储待扩展状态
 *    - 层次遍历：先扩展距离近的状态
 *    - 去重：避免重复访问同一状态
 *    - 路径记录：通过parent映射重建路径
 * 
 * 4. 【状态空间分析】
 *    状态数量：O(n²)
 *    - 总位置数：2n+2
 *    - 空位位置选择：C(2n+2, 2) = O(n²)
 *    
 *    每状态转移数：O(n)
 *    - 最多有n种合法移动
 * 
 * 5. 【复杂度分析】
 *    时间复杂度：O(n³)
 *    - 状态数O(n²) × 每状态转移O(n)
 *    
 *    空间复杂度：O(n³)
 *    - 存储O(n²)个状态，每个状态长度O(n)
 * 
 * 6. 【最短步数规律】
 *    实验发现：
 *    - n=4: 5步
 *    - n=5: 6步
 *    - 推测：最短步数 = n+1
 * 
 * 7. 【优化方向】
 *    - 双向BFS：从起点和终点同时搜索
 *    - 状态压缩：用更紧凑的方式表示状态
 *    - 启发式搜索：A*算法，使用启发函数
 *    - 剪枝：提前终止无效分支
 * 
 * 8. 【BFS vs DFS对比】
 *    BFS：
 *    - 优点：保证最短路径
 *    - 缺点：空间开销大
 *    - 适用：找最短解
 *    
 *    DFS：
 *    - 优点：空间开销小
 *    - 缺点：可能不是最短
 *    - 适用：找任意解、空间受限
 * 
 * ============================================================================
 */