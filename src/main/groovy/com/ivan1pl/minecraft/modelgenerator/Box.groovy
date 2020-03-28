package com.ivan1pl.minecraft.modelgenerator

class Box {
    int fromX
    int fromY
    int fromZ
    int toX
    int toY
    int toZ

    static Box row(int fromX, int toX, int y) {
        new Box(fromX: fromX, toX: toX, fromY: y, toY: y+1, fromZ: 7.5, toZ: 8.5)
    }

    static Box column(int fromY, int toY, int x) {
        new Box(fromX: x, toX: x+1, fromY: fromY, toY: toY, fromZ: 7.5, toZ: 8.5)
    }
}
