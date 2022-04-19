import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


/**
 * 2021/6/26
 */

interface IA{
    String getHelloName();
}



public class proxyExam {

//    public class MyIA implements IA{
//        @Override
//        public String getHelloName() {
//            return null;
//        }
//    }

    public static void main(String[] args) throws Exception{
        IA ia = (IA) createObject(IA.class.getName() + "$getHelloName=Abc");
        System.out.println(ia.getHelloName());
        ia = (IA) createObject(IA.class.getName() + "$getTest=Bcd");
        System.out.println(ia.getHelloName());

    }

    public static Object createObject(String str) throws  Exception{

        String cmd[] = str.split("\\$");
        String className = cmd[0];
        String cmd2 [] = cmd[1].split("=");
        String mName = cmd2[0];
        String retrunValue = cmd2[1];

        Class target = Class.forName(className);
//        Class<?>[] interfaces = target.getClass().getInterfaces();
        Class<?>[] interfaces = new Class[]{IA.class};

        return  Proxy.newProxyInstance(target.getClassLoader(), interfaces, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if(method.getName().equals(mName)){
//                    method.invoke(proxy, args);
                    return retrunValue;
                }else{
                    return null;
                }
            }
        });

    }
}
