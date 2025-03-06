package com.zeeyeh.nyt.factory;

/**
 * @author LeonKeiran
 * @description 颜色工厂
 * @date 2025/3/6 20:11
 */
public interface ColorFactory<O, T, U> {
    U apply(O origin, T text);
}
