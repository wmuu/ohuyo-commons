package org.ohuyo.common.classloader;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;

import net.sf.cglib.beans.BeanMap;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class TestPerformance
{
    public static void main(String[] args)
    {
        int times = 10000000;
         TestBean(times);//=15
         TestCglib(times);//=516
         TestBeanMap(times);//=256
        TestReflection(times);// =11359
    }

    public static void TestBean(int times)
    {
        MyBean bean = new MyBean();

        Date start = Calendar.getInstance().getTime();
        for (int i = 0; i < times; i++)
        {
            bean.setName("helloworld");
            Object v = bean.getName();
        }

        Date end = Calendar.getInstance().getTime();

        System.out.println(end.getTime() - start.getTime());
    }

    public static void TestCglib(int times)
    {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(MyBean.class);
        enhancer.setCallback(new TestMethodInterceptorImpl());
        MyBean my = (MyBean) enhancer.create();

        Date start = Calendar.getInstance().getTime();
        for (int i = 0; i < times; i++)
        {
            my.setName("helloworld");
            Object v = my.getName();
        }

        Date end = Calendar.getInstance().getTime();

        System.out.println(end.getTime() - start.getTime());
    }

    public static void TestBeanMap(int times)
    {
        MyBean bean = new MyBean();
        BeanMap map = BeanMap.create(bean);

        Date start = Calendar.getInstance().getTime();
        for (int i = 0; i < times; i++)
        {
            map.put(bean, "name", "helloworld");
            Object v = bean.getName();
        }
        Date end = Calendar.getInstance().getTime();

        System.out.println(end.getTime() - start.getTime());
    }

    public static void TestReflection(int times)
    {
        MyBean bean = new MyBean();
        Class c = MyBean.class;
        try
        {
            Method get = c.getDeclaredMethod("getName", null);
            Method set = c.getDeclaredMethod("setName", String.class);
            Date start = Calendar.getInstance().getTime();
            for (int i = 0; i < times; i++)
            {
                set.invoke(bean, "helloworld");
                Object v = get.invoke(bean, null);
            }
            Date end = Calendar.getInstance().getTime();

            System.out.println(end.getTime() - start.getTime());
        } catch (Exception ex)
        {

        }
    }
}

class TestMethodInterceptorImpl implements MethodInterceptor
{
    public Object intercept(Object obj, Method method, Object[] args,
            MethodProxy proxy) throws Throwable
    {
        return proxy.invokeSuper(obj, args);
    }
}

class MyBean
{
    private String name;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
