package element;

public class TimeCounter {

    public int minute = 3, scond = 0;
    public boolean over = false;
    private int oneSecond = 0;
    private final int OneSecond = 100;

    public void count() {
        if (!over) {
            oneSecond += 1;
            if (oneSecond == OneSecond) {
                oneSecond = 0;
                if (scond > 0) {
                    scond -= 1;
                    return;
                }
                if (scond == 0 && minute > 0) {
                    minute -= 1;
                    scond = 59;
                    return;
                }
                if (scond == 0 && minute == 0) {
                    over = true;
                }
            }
        }
    }
}
