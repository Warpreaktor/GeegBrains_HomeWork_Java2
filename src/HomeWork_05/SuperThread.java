package HomeWork_05;

/**
 * 1. В данном классе было создано четыре метода, каждый из которых производит вычисления и за тем считает сумму этих вычислений.
 * Такого не было в ДЗ, однако в момент когда копируешь и сводишь обратно массивы вероятны ошибки и неправильная склейка.
 * Для лучше отладки я сделал вывод результат расчета, чтобы каждый из методов можно было точно сравнить по циферке,
 * любая разница от остальных методов будет ошибкой.
 *
 * 2. Изменил так же и саму формулу, она была завязана на размер проходимого массива, а при паралелльном просчете
 * размер массива становится короче и результат вычисления получается совсем иным. Теперь формула работает как и было
 * задумано как буд-то i каждый раз инкрементируется независимо от того на сколько частей мы разорвали массив.
 */
public class SuperThread extends Thread {
    static final int size = 10000000;
    static final int h = size / 2;

    public static void main(String[] args) {
        SuperThread superThread = new SuperThread();
        superThread.oneThread();
        superThread.twoThreads();
        superThread.threeThreads();
        superThread.fourThread();
    }

    //Первый просто бежит по массиву и вычисляет значения.
    public void oneThread(){
        float[] arr = new float[size];

        SuperThread thread = new SuperThread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < arr.length; i++) {
                    arr[i] = 1;
                }
                for (int i = 0; i < arr.length; i++) {
                    arr[i] = (float) (arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
                }
            }
        });
        //Запускаем вычисления
        long timerStart = System.currentTimeMillis();
        thread.start();
        //Ждем окончания вычислений
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //Считаем результат вычислений для проверки правильной работы
        double result = 0;
        for (int i = 0; i < arr.length; i++) {
            result += arr[i];
        }
        long timerEnd = System.currentTimeMillis();
        System.out.println("Результат вычислений " + result +" вычислен одним тредом за " + (timerEnd - timerStart) + "мс.");
    }

    //Второй разбивает массив на два массива, в двух потоках высчитывает новые значения
    // и потом склеивает эти массивы обратно в один.
    public void twoThreads() {
        float[] arr = new float[size];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = 1;
        }

        SuperThread thread1 = new SuperThread(new Runnable() {
            @Override
            public void run() {
                float[] leftPart = new float[h];
                System.arraycopy(arr, 0, leftPart, 0, h);
                for (int i = 0; i < leftPart.length; i++) {
                    leftPart[i] = (float) (leftPart[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
                }
                System.arraycopy(leftPart, 0, arr, 0, h);
            }
        });
        SuperThread thread2 = new SuperThread(new Runnable() {
            @Override
            public void run() {
                float[] rightPart = new float[h];
                System.arraycopy(arr, h, rightPart, 0, h);
                int tmp = h;
                for (int i = 0; i < rightPart.length; i++) {
                    //Изменил формулу и цикл чтобы результат получался всегда одинаковым независимо от размера массива
                    rightPart[i] = (float) (rightPart[i] * Math.sin(0.2f + tmp / 5) * Math.cos(0.2f + tmp / 5) * Math.cos(0.4f + tmp / 2));
                    tmp++;
                }
                System.arraycopy(rightPart, 0, arr, h, h);
            }
        });

        //Запускаем вычисление
        long timerStart = System.currentTimeMillis();
        thread1.start();
        thread2.start();
        //Ждем окончания вычислений
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Считаем результат вычислений для проверки правильной работы
        double result = 0;
        for (int i = 0; i < arr.length; i++) {
            result += arr[i];
        }

        long timerEnd = System.currentTimeMillis();
        System.out.println("Результат вычислений " + result +" вычислен двумя тредами за " + (timerEnd - timerStart) + "мс.");
    }

    //Третий в три треда
    public void threeThreads() {
        float[] arr = new float[size];
        int third = size / 3;
        for (int i = 0; i < arr.length; i++) {
            arr[i] = 1;
        }

        SuperThread thread1 = new SuperThread(new Runnable() {
            @Override
            public void run() {
                float[] firstPart = new float[third];
                System.arraycopy(arr, 0, firstPart, 0, third);
                for (int i = 0; i < firstPart.length; i++) {
                    firstPart[i] = (float) (firstPart[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
                }
                System.arraycopy(firstPart, 0, arr, 0, third);
            }
        });
        SuperThread thread2 = new SuperThread(new Runnable() {
            @Override
            public void run() {
                float[] secondPart = new float[third];
                System.arraycopy(arr, third, secondPart, 0, third);
                int tmp = third;
                for (int i = 0; i < secondPart.length; i++) {
                    secondPart[i] = (float) (secondPart[i] * Math.sin(0.2f + tmp / 5) * Math.cos(0.2f + tmp / 5) * Math.cos(0.4f + tmp / 2));
                    tmp++;
                }
                System.arraycopy(secondPart, 0, arr, third, third);
            }
        });

        SuperThread thread3 = new SuperThread(new Runnable() {
            int modulo = size % 3;
            @Override
            public void run() {
                float[] thirdPart = new float[third+modulo];
                System.arraycopy(arr, third*2, thirdPart, 0, third+modulo);
                int tmp = third*2;
                for (int i = 0; i < thirdPart.length; i++) {
                    thirdPart[i] = (float) (thirdPart[i] * Math.sin(0.2f + tmp / 5) * Math.cos(0.2f + tmp / 5) * Math.cos(0.4f + tmp / 2));
                    tmp++;
                }
                System.arraycopy(thirdPart, 0, arr, third*2, third+modulo);
            }
        });

        //Запускаем вычисление
        long timerStart = System.currentTimeMillis();
        thread1.start();
        thread2.start();
        thread3.start();

        //Ждем окончания вычислений
        try {
            thread1.join();
            thread2.join();
            thread3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Считаем результат вычислений для проверки правильной работы
        double result = 0;
        for (int i = 0; i < arr.length; i++) {
            result += arr[i];
        }

        long timerEnd = System.currentTimeMillis();
        System.out.println("Результат вычислений " + result +" вычислен тремя тредами за " + (timerEnd - timerStart) + "мс.");
    }

    //Четвертый в четыре треда соответственно
    public void fourThread(){
        float[] arr = new float[size];
        int fourth = size / 4;
        for (int i = 0; i < arr.length; i++) {
            arr[i] = 1;
        }

        SuperThread thread1 = new SuperThread(new Runnable() {
            @Override
            public void run() {
                float[] firstPart = new float[fourth];
                System.arraycopy(arr, 0, firstPart, 0, fourth);
                for (int i = 0; i < firstPart.length; i++) {
                    firstPart[i] = (float) (firstPart[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
                }
                System.arraycopy(firstPart, 0, arr, 0, fourth);
            }
        });
        SuperThread thread2 = new SuperThread(new Runnable() {
            @Override
            public void run() {
                float[] secondPart = new float[fourth];
                System.arraycopy(arr, fourth, secondPart, 0, fourth);
                int tmp = fourth;
                for (int i = 0; i < secondPart.length; i++) {
                    secondPart[i] = (float) (secondPart[i] * Math.sin(0.2f + tmp / 5) * Math.cos(0.2f + tmp / 5) * Math.cos(0.4f + tmp / 2));
                    tmp++;
                }
                System.arraycopy(secondPart, 0, arr, fourth, fourth);
            }
        });

        SuperThread thread3 = new SuperThread(new Runnable() {
            @Override
            public void run() {
                float[] thirdPart = new float[fourth];
                System.arraycopy(arr, fourth*2, thirdPart, 0, fourth);
                int tmp = fourth*2;
                for (int i = 0; i < thirdPart.length; i++) {
                    thirdPart[i] = (float) (thirdPart[i] * Math.sin(0.2f + tmp / 5) * Math.cos(0.2f + tmp / 5) * Math.cos(0.4f + tmp / 2));
                    tmp++;
                }
                System.arraycopy(thirdPart, 0, arr, fourth*2, fourth);
            }
        });
        SuperThread thread4 = new SuperThread(new Runnable() {
            int modulo = size % 4;
            @Override
            public void run() {
                float[] thirdPart = new float[fourth+modulo];
                System.arraycopy(arr, fourth*3, thirdPart, 0, fourth+modulo);
                int tmp = fourth*3;
                for (int i = 0; i < thirdPart.length; i++) {
                    thirdPart[i] = (float) (thirdPart[i] * Math.sin(0.2f + tmp / 5) * Math.cos(0.2f + tmp / 5) * Math.cos(0.4f + tmp / 2));
                    tmp++;
                }
                System.arraycopy(thirdPart, 0, arr, fourth*3, fourth+modulo);
            }
        });

        //Запускаем вычисление
        long timerStart = System.currentTimeMillis();
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();

        //Ждем окончания вычислений
        try {
            thread1.join();
            thread2.join();
            thread3.join();
            thread4.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Считаем результат вычислений для проверки правильной работы
        double result = 0;
        for (int i = 0; i < arr.length; i++) {
            result += arr[i];
        }

        long timerEnd = System.currentTimeMillis();
        System.out.println("Результат вычислений " + result +" вычислен тремя тредами за " + (timerEnd - timerStart) + "мс.");

    }

    public SuperThread(Runnable target) {
        super(target);
    }

    public SuperThread() {
    }
}
