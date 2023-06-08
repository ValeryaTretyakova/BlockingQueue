import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static void numberLettersPerLine(String text, char symbol, AtomicInteger sum) {

        int frequency = (int) text.chars().filter(ch -> ch == symbol).count();
        if (sum.get() < frequency) {
            sum.set(frequency);
        }
    }

    public static BlockingQueue<String> queue1 = new ArrayBlockingQueue(100);
    public static BlockingQueue<String> queue2 = new ArrayBlockingQueue(100);
    public static BlockingQueue<String> queue3 = new ArrayBlockingQueue(100);

    public static AtomicInteger sumA = new AtomicInteger(0);
    public static AtomicInteger sumB = new AtomicInteger(0);
    public static AtomicInteger sumC = new AtomicInteger(0);

    public static void main(String[] args) {
        new Thread(() -> {
            for (int i = 0; i < 10_000; i++) {
                String text = generateText("abc", 100_000);
                try {
                    queue1.put(text);
                    queue2.put(text);
                    queue3.put(text);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();


        new Thread(() -> {
            for (int i = 0; i < 10_000; i++) {
                try {
                    String rout = queue1.take();
                    numberLettersPerLine(rout, 'a', sumA);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("Самое большое количество букв 'a' в строке " + sumA);
        }).start();

        new Thread(() -> {
            for (int i = 0; i < 10_000; i++) {
                try {
                    String rout = queue2.take();
                    numberLettersPerLine(rout, 'b', sumB);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("Самое большое количество букв 'b' в строке " + sumB);
        }).start();

        new Thread(() -> {
            for (int i = 0; i < 10_000; i++) {
                try {
                    String rout = queue3.take();
                    numberLettersPerLine(rout, 'c', sumC);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("Самое большое количество букв 'c' в строке " + sumC);
        }).start();

    }
}