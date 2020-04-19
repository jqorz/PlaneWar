package com.jqorz.planewar.tools;

import com.jqorz.planewar.base.BaseEntityImp;

/**
 * @author j1997
 * @since 2020/4/19
 */
public class CollisionCheck {
    private static float DEFAULT_OVERLAP_AREA = 0.2f;

    public static boolean isCollision(BaseEntityImp entity1, BaseEntityImp entity2) {//判断两个矩形是否碰撞
        int xd;//大的x
        int yd;//大的y
        int xx;//小的x
        int yx;//小的y
        int width;
        int height;
        boolean xFlag;//entity1 x是否在前
        boolean yFlag;//entity1 y是否在前
        if (entity1.getX() >= entity2.getX()) {
            xd = entity1.getX();
            xx = entity2.getX();
            xFlag = false;
        } else {
            xd = entity2.getX();
            xx = entity1.getX();
            xFlag = true;
        }
        if (entity1.getY() >= entity2.getY()) {
            yd = entity1.getY();
            yx = entity2.getY();
            yFlag = false;
        } else {
            yd = entity2.getY();
            yx = entity1.getY();
            yFlag = true;
        }
        if (xFlag) {
            width = entity1.getWidth();
        } else {
            width = entity2.getWidth();
        }
        if (yFlag) {
            height = entity1.getHeight();
        } else {
            height = entity2.getHeight();
        }
        if (xd >= xx && xd <= xx + width - 1 &&
                yd >= yx && yd <= yx + height - 1) {//首先判断两个矩形有否重叠
            double Dwidth = width - xd + xx;   //重叠区域宽度
            double Dheight = height - yd + yx; //重叠区域高度
            //重叠面积超20%则判定为碰撞
            return Dwidth * Dheight / (entity2.getHeight() * entity2.getWidth()) >= DEFAULT_OVERLAP_AREA;
        }
        return false;
    }
}
