package cn.inkroom.study.virtual.classLoader;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

/**
 * @author inkbox
 * @date 2021/6/18
 */
public class UserClassLoader extends ClassLoader {

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {

        try {
            FileInputStream fileInputStream = new FileInputStream("src/main/java/cn/inkroom/study/virtual/classLoader/" + name+".class");
            byte[] bytes = fileInputStream.readAllBytes();
            return defineClass(name, bytes, 0, bytes.length);
        } catch (IOException e) {
            throw new ClassNotFoundException(e.getMessage());
        }

    }
}
