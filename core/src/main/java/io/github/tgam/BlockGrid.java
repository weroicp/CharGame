package io.github.tgam;

public class BlockGrid {
    private boolean[][] grid; // true 表示该位置有方块
    private final int rows;
    private final int columns;

    public BlockGrid(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        grid = new boolean[rows][columns];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                grid[row][col] = true;
            }
        }
    }

    /**
     * 更新当前数据结构，移除与给定矩形重叠的所有方块，
     * 并返回是否发生了任何更新。
     *
     * @param x,y LibGDX 坐标系下的矩形（原点在左下角）
     * @return 是否有任何方块被移除
     */
    public boolean updateWithRectangle(float x,float y) {
        int minRow = getRow(y);                  // 矩形底部 y
        int maxRow = getRow(y + Main.BLOCK_HEIGHT- 0.0001f);   // 矩形顶部 y
        int minCol = getColumn(x);              // 矩形左侧 x
        int maxCol = getColumn(x + Main.BLOCK_WIDTH- 0.0001f); // 矩形右侧 x

        // 防止越界
        minRow = Math.max(0, minRow);
        maxRow = Math.min(rows - 1, maxRow);
        minCol = Math.max(0, minCol);
        maxCol = Math.min(columns - 1, maxCol);

        boolean updated = false;

        // 遍历所有可能被覆盖的格子
        for (int row = minRow; row <= maxRow; row++) {
            for (int col = minCol; col <= maxCol; col++) {
                if (grid[row][col]) {
                    grid[row][col] = false; // 移除方块
                    updated = true;
                }
            }
        }

        return updated;
    }

    // 将世界坐标 y 映射到对应的行号
    public int getRow(float y) {
        return (int)(y / Main.BLOCK_HEIGHT);
    }

    // 将世界坐标 x 映射到对应的列号
    public int getColumn(float x) {
        return (int)(x / Main.BLOCK_WIDTH);
    }

    // 可选：获取原始数据（用于调试或绘制）
    public boolean[][] getRawData() {
        return grid;
    }

    public boolean hasBlockAt(int row, int col){
        return grid[row][col];
    }
}
