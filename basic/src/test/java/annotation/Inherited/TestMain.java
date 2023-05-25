package annotation.Inherited;

import java.util.Arrays;

/**
 * 2023/6/14
 */
class TestMain {
    public static void main(String[] args) {
        Class<Sub> clazz = Sub.class;
        System.out.println("============================AnnotatedElement===========================");
        System.out.println(Arrays.toString(clazz.getAnnotations()));    //获取自身和父亲的注解。如果@ATable未加@Inherited修饰，则获取的只是自身的注解而无法获取父亲的注解。
        System.out.println("------------------");

        //以下是对反射的拓展，与上文无关：
        System.out.println("============================Field===========================");
        System.out.println(Arrays.toString(clazz.getFields())); // 自身和父亲的公有字段
        System.out.println("------------------");
        System.out.println(Arrays.toString(clazz.getDeclaredFields()));  //自身所有字段
        System.out.println("============================Method===========================");
        System.out.println(Arrays.toString(clazz.getMethods()));   //自身和父亲的公有方法
        System.out.println("------------------");
        System.out.println(Arrays.toString(clazz.getDeclaredMethods()));// 自身所有方法
        System.out.println("============================Constructor===========================");
        System.out.println(Arrays.toString(clazz.getConstructors()));   //自身公有的构造方法
        System.out.println("------------------");
        System.out.println(Arrays.toString(clazz.getDeclaredConstructors()));   //自身的所有构造方法
        System.out.println("============================AnnotatedElement===========================");
        System.out.println(Arrays.toString(clazz.getAnnotations()));    //获取自身和父亲的注解
        System.out.println("------------------");
        System.out.println(Arrays.toString(clazz.getDeclaredAnnotations()));  //只获取自身的注解
        System.out.println("------------------");
    }
}

