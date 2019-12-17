package com.github.liaochong.example.pojo;

import java.math.BigDecimal;

/**
 * <p>
 * 简述一下～
 * <p>
 *
 * @author 时前程 2019年12月11日
 * @see
 * @since 1.0
 */
@FunctionalInterface
public interface ToBigDecimalFunction<T> {
	BigDecimal applyAsBigDecimal(T value);
}
