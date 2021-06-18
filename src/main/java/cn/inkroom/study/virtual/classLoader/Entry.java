package cn.inkroom.study.virtual.classLoader;

/**
 * @author inkbox
 * @date 2021/6/18
 */
public class Entry {

    public static void main(String[] args) {


        try {
            UserClassLoader classLoader = new UserClassLoader();
            Class<?> testB = classLoader.findClass("TestB");
            TestInterface testInterface = ((TestInterface) testB.newInstance());

            System.out.println(TestA.class.getClassLoader());
            System.out.println(testB.getClassLoader());

            new TestA(testInterface);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }
}
