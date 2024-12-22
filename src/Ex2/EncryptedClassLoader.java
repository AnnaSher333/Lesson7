package Ex2;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class EncryptedClassLoader extends ClassLoader{
    private final String key;
    private final File dir;

    public EncryptedClassLoader(String key, File dir, ClassLoader parent) {
        super(parent);
        this.key = key;
        this.dir = dir;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        //задаем путь к файлу в заданной директории
        String path = dir.getAbsolutePath() + File.separator + name.replace('.', File.separatorChar) + ".class";
        File file = new File(path);
        //если файла не существует выбрасываем исключение
        if (!file.exists()) {
            throw new ClassNotFoundException("Класса: " + name + " нет в директории " + dir);
        }

        try {
            byte[] encryptedBytes = Files.readAllBytes(file.toPath());//сохраняем наш файл в массив байт
            byte[] decryptedBytes = decrypt(encryptedBytes);//расшифровываем
            //преобразовываем расшифрованный массив байтов в экземпляр класса Class
            return defineClass(name, decryptedBytes, 0, decryptedBytes.length);
        } catch (IOException e) {
            throw new ClassNotFoundException("Не удалось прочитать файл класса: " + path, e);
        }
    }
    //расашифровываем массив байт
    private byte[] decrypt(byte[] encryptedBytes) {
        byte[] decryptedBytes = new byte[encryptedBytes.length];
        for (int i = 0; i < encryptedBytes.length; i++) {
            //вычитаем значения символа из ключа
            decryptedBytes[i] = (byte) (encryptedBytes[i] - key.charAt(i % key.length()));
        }
        return decryptedBytes;
    }

}
