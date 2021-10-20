package com.wenxianm.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.core.Converter;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.*;

import static org.apache.logging.log4j.util.Strings.EMPTY;
import static org.springframework.util.StringUtils.isEmpty;

/**
 * @ClassName BeanUtil
 * @Author cwx
 * @Date 2021/10/12 16:48
 **/
public class BeanUtil {

    private static final Logger log = LoggerFactory.getLogger(BeanUtil.class);
    private static Map<String, BeanCopier> beanCopierMap = new HashMap();

    public BeanUtil() {
    }

    public static void copyProperties(Object target, Object source) {
        if (null != source && null != target) {
            String beanKey = generateKey(source.getClass(), target.getClass());
            BeanCopier copier = null;
            if (!beanCopierMap.containsKey(beanKey)) {
                copier = BeanCopier.create(source.getClass(), target.getClass(), false);
                beanCopierMap.put(beanKey, copier);
            } else {
                copier = (BeanCopier)beanCopierMap.get(beanKey);
            }

            copier.copy(source, target, (Converter)null);
        }
    }

    public static <T> T from(Object source, Class<T> clazz) {
        if (null == source) {
            return null;
        } else {
            try {
                T target = clazz.newInstance();
                copyProperties(target, source);
                return target;
            } catch (Exception var3) {
                throw new RuntimeException("copyProperties ERROR, obj: " + source.getClass() + " clz: " + clazz, var3);
            }
        }
    }

    public static <T> List<T> fromList(Collection<? extends Object> list, Class<T> clazz) {
        List<T> result = new ArrayList();
        if (null != list) {
            Iterator var3 = list.iterator();

            while(var3.hasNext()) {
                Object source = var3.next();
                result.add(from(source, clazz));
            }
        }

        return result;
    }

    public static void setProperty(Object bean, String name, Object value) {
        if (value != null) {
            Class<?> beanClass = bean.getClass();
            PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(beanClass);
            PropertyDescriptor[] var5 = propertyDescriptors;
            int var6 = propertyDescriptors.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                PropertyDescriptor property = var5[var7];
                String key = property.getName();
                if (key.equals(name)) {
                    Method writeMethod = property.getWriteMethod();

                    try {
                        writeMethod.invoke(bean, value);
                        break;
                    } catch (Exception var12) {
                        log.error("Could not copy property '" + property.getName() + "' to target", var12);
                    }
                }
            }

        }
    }

    public static Object getProperty(Object bean, String name) {
        if (bean == null) {
            return null;
        } else {
            PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(bean.getClass());
            PropertyDescriptor[] var3 = propertyDescriptors;
            int var4 = propertyDescriptors.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                PropertyDescriptor property = var3[var5];
                String key = property.getName();
                if (key.equals(name)) {
                    Method getter = property.getReadMethod();

                    try {
                        return getter.invoke(bean);
                    } catch (Exception var10) {
                        log.error("Could not copy property '" + property.getName() + "' from source to target", var10);
                    }
                }
            }

            return null;
        }
    }

    public static <T> Map<String, Object> beanToMap(T bean, boolean isToUnderlineCase, String[] properties, boolean exclude) {
        if (bean == null) {
            return null;
        } else {
            Map<String, Object> map = new HashMap<>();
            PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(bean.getClass());
            PropertyDescriptor[] var6 = propertyDescriptors;
            int var7 = propertyDescriptors.length;

            for(int var8 = 0; var8 < var7; ++var8) {
                PropertyDescriptor property = var6[var8];
                String key = property.getName();
                if (isCopy(key)) {
                    Method getter = property.getReadMethod();

                    try {
                        Object value = getter.invoke(bean);
                        if (null != value) {
                            key = isToUnderlineCase ? toUnderline(key, false) : key;
                            if (properties != null && properties.length > 0) {
                                if (exclude && !Arrays.asList(properties).contains(key)) {
                                    map.put(key, value);
                                } else if (!exclude && Arrays.asList(properties).contains(key)) {
                                    map.put(key, value);
                                }
                            } else {
                                map.put(key, value);
                            }
                        }
                    } catch (Exception var13) {
                        log.error("Could not copy property '" + property.getName() + "' from source to target", var13);
                    }
                }
            }

            return map;
        }
    }

    public static boolean isCopy(String key) {
        return !"serialVersionUID".equals(key) && !"class".equals(key) && !"instance".equals(key);
    }

    private static String generateKey(Class<?> class1, Class<?> class2) {
        return class1.toString() + class2.toString();
    }

    public static String toUnderline(String name, boolean upper) {
        if (isEmpty(name)) {
            return EMPTY;
        }

        StringBuilder builder = new StringBuilder(name.length());
        char[] chars = name.toCharArray();
        char ch;
        for (int i = 0, length = chars.length; i < length; i++) {
            ch = chars[i];
            if (Character.isUpperCase(ch)) {
                if (i != 0) {
                    builder.append("_");
                }
                builder.append(upper ? ch : Character.toLowerCase(ch));
            } else {
                builder.append(upper ? Character.toUpperCase(ch) : ch);
            }
        }
        return builder.toString();
    }
}
