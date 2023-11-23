package io.github.x.ray.agent.util;

import java.lang.reflect.Method;

/**
 * 反射工具类
 */
public class ReflectUtils {

	/**
	 * 按照方法名查找指定方法名的方法，只返回匹配到的第一个方法，如果找不到对应的方法则返回{@code null}
	 *
	 * <p>
	 * 此方法只检查方法名是否一致，并不检查参数的一致性。
	 * </p>
	 *
	 * @param clazz      类，如果为{@code null}返回{@code null}
	 * @param methodName 方法名，如果为空字符串返回{@code null}
	 * @return 方法
	 * @throws SecurityException 无权访问抛出异常
	 * @since 4.3.2
	 */
	public static Method getMethodByName(Class<?> clazz, String methodName) throws SecurityException {
		return getMethodByName(clazz, false, methodName);
	}

	/**
	 * 按照方法名查找指定方法名的方法，只返回匹配到的第一个方法，如果找不到对应的方法则返回{@code null}
	 *
	 * <p>
	 * 此方法只检查方法名是否一致（忽略大小写），并不检查参数的一致性。
	 * </p>
	 *
	 * @param clazz      类，如果为{@code null}返回{@code null}
	 * @param methodName 方法名，如果为空字符串返回{@code null}
	 * @return 方法
	 * @throws SecurityException 无权访问抛出异常
	 * @since 4.3.2
	 */
	public static Method getMethodByNameIgnoreCase(Class<?> clazz, String methodName) throws SecurityException {
		return getMethodByName(clazz, true, methodName);
	}

	/**
	 * 按照方法名查找指定方法名的方法，只返回匹配到的第一个方法，如果找不到对应的方法则返回{@code null}
	 *
	 * <p>
	 * 此方法只检查方法名是否一致，并不检查参数的一致性。
	 * </p>
	 *
	 * @param clazz      类，如果为{@code null}返回{@code null}
	 * @param ignoreCase 是否忽略大小写
	 * @param methodName 方法名，如果为空字符串返回{@code null}
	 * @return 方法
	 * @throws SecurityException 无权访问抛出异常
	 * @since 4.3.2
	 */
	public static Method getMethodByName(Class<?> clazz, boolean ignoreCase, String methodName) throws SecurityException {
		if (null == clazz || methodName == null || methodName.isEmpty()) {
			return null;
		}

		Method res = null;
		final Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (StringUtils.equals(methodName, method.getName(), ignoreCase)
                    && (res == null
                    || res.getReturnType().isAssignableFrom(method.getReturnType()))) {
                res = method;
            }
        }
        return res;
	}

	/**
	 * 是否为equals方法
	 *
	 * @param method 方法
	 * @return 是否为equals方法
	 */
	public static boolean isEqualsMethod(Method method) {
		if (method == null ||
			1 != method.getParameterCount() ||
			false == "equals".equals(method.getName())) {
			return false;
		}
		return (method.getParameterTypes()[0] == Object.class);
	}

	/**
	 * 是否为hashCode方法
	 *
	 * @param method 方法
	 * @return 是否为hashCode方法
	 */
	public static boolean isHashCodeMethod(Method method) {
		return method != null//
			&& "hashCode".equals(method.getName())//
			&& isEmptyParam(method);
	}

	/**
	 * 是否为toString方法
	 *
	 * @param method 方法
	 * @return 是否为toString方法
	 */
	public static boolean isToStringMethod(Method method) {
		return method != null//
			&& "toString".equals(method.getName())//
			&& isEmptyParam(method);
	}

	/**
	 * 是否为无参数方法
	 *
	 * @param method 方法
	 * @return 是否为无参数方法
	 * @since 5.1.1
	 */
	public static boolean isEmptyParam(Method method) {
		return method.getParameterCount() == 0;
	}
}
