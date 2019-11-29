package com.example.newpolycom;

public interface IMainView {

    /**
     * 表示运动状态
     * 横线 ： 0 ：右向左 1：左向右 2：灰色
     * 竖线：0 ：下向上 1：上向下 2：灰色
     *
     * @param status
     */
    void setStatus(int status);

    /**
     * @param mPosition
     */
    void setmPosition(int mPosition);
}
